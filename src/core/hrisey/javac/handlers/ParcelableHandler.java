/*
 * Copyright (C) 2014 Maciej Gorski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hrisey.javac.handlers;

import static lombok.javac.Javac.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import hrisey.Parcelable;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;

import lombok.core.AnnotationValues;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacResolution;
import lombok.javac.JavacResolution.TypeNotConvertibleException;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.HandleConstructor;

import org.mangosdk.spi.ProviderFor;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

@ProviderFor(JavacAnnotationHandler.class)
public class ParcelableHandler extends JavacAnnotationHandler<Parcelable> {
	
	private static final Map<TypeKind, String> readWriteIdents = new HashMap<TypeKind, String>();
	static {
		readWriteIdents.put(TypeKind.BYTE, "Byte");
		readWriteIdents.put(TypeKind.DOUBLE, "Double");
		readWriteIdents.put(TypeKind.FLOAT, "Float");
		readWriteIdents.put(TypeKind.INT, "Int");
		readWriteIdents.put(TypeKind.LONG, "Long");
	}
	
	private static final Map<TypeKind, String> readWriteArrayIdents = new HashMap<TypeKind, String>();
	static {
		readWriteArrayIdents.put(TypeKind.BOOLEAN, "BooleanArray");
		readWriteArrayIdents.put(TypeKind.BYTE, "ByteArray");
		readWriteArrayIdents.put(TypeKind.CHAR, "CharArray");
		readWriteArrayIdents.put(TypeKind.DOUBLE, "DoubleArray");
		readWriteArrayIdents.put(TypeKind.FLOAT, "FloatArray");
		readWriteArrayIdents.put(TypeKind.INT, "IntArray");
		readWriteArrayIdents.put(TypeKind.LONG, "LongArray");
	}
	
	private static final Map<String, String> readWriteSymbolMap = new HashMap<String, String>();
	static {
		readWriteSymbolMap.put("java.lang.String", "String");
		readWriteSymbolMap.put("android.os.Bundle", "Bundle");
		readWriteSymbolMap.put("android.os.IBinder", "StrongBinder");
		readWriteSymbolMap.put("android.util.SparseBooleanArray", "SparseBooleanArray");
	}
	
	private static final Map<String, String> readWriteArraySymbolMap = new HashMap<String, String>();
	static {
		readWriteArraySymbolMap.put("java.lang.String", "StringArray");
	}
	
	private static final Map<String, String> readWriteListSymbolMap = new HashMap<String, String>();
	static {
		readWriteListSymbolMap.put("java.lang.String", "String");
	}
	
	private static final Map<String, String> boxedPrimitivesMap = new HashMap<String, String>();
	static {
		boxedPrimitivesMap.put("java.lang.Byte", "Byte");
		boxedPrimitivesMap.put("java.lang.Double", "Double");
		boxedPrimitivesMap.put("java.lang.Float", "Float");
		boxedPrimitivesMap.put("java.lang.Integer", "Int");
		boxedPrimitivesMap.put("java.lang.Long", "Long");
	}
	
	@Override
	public void handle(AnnotationValues<Parcelable> annotation, JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, Parcelable.class);
		deleteImportFromCompilationUnit(annotationNode, Parcelable.class.getName());
		
		JavacNode type = annotationNode.up();
		if (!(type.get() instanceof JCClassDecl)) {
			annotationNode.addError("@Parcelable is only supported on types.");
			return;
		}
		
		addParcelableInterface(type);

		generateDescribeContentsMethod(type);
		generateWriteToParcelMethod(type);
		
		createConstructorWithParcelParam(type);
		
		JavacNode creatorType = createCreatorType(type);
		
		addCreatorInterface(creatorType, type);
		
		generateCreateFromParcelMethod(creatorType, type);
		generateNewArrayMethod(creatorType, type);
		
		addCreatorStaticField(type);
	}
	
	private void addParcelableInterface(JavacNode type) {
		JCClassDecl clazz = (JCClassDecl) type.get();
		if (!implementsParcelable(clazz)) {
			ListBuffer<JCExpression> newImpl = new ListBuffer<JCExpression>();
			newImpl.appendList(clazz.implementing);
			JCExpression androidOsParcelable = toExpression(type, "android.os.Parcelable");
			newImpl.append(androidOsParcelable);
			clazz.implementing = newImpl.toList();
		}
	}
	
	private boolean implementsParcelable(JCClassDecl classDecl) {
		List<JCExpression> implList = classDecl.implementing;
		for (JCExpression impl : implList) {
			if ("android.os.Parcelable".equals(impl.type.tsym.toString())) {
				return true;
			}
		}
		return false;
	}
	
	private JCExpression toExpression(JavacNode type, String name) {
		return chainDotsString(type, name);
	}
	
	private JCExpression toExpression(JavacNode classNode, Type type) {
		try {
			return JavacResolution.typeToJCTree(type, classNode.getAst(), false);
		} catch (TypeNotConvertibleException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void generateDescribeContentsMethod(JavacNode type) {
		JavacTreeMaker maker = type.getTreeMaker();
		
		JCStatement statement = maker.Return(maker.Literal(0));
		
		JCBlock body = block(maker, statement);
		
		JCMethodDecl md = maker.MethodDef(maker.Modifiers(Flags.PUBLIC), type.toName("describeContents"), maker.TypeIdent(CTC_INT), List.<JCTypeParameter>nil(), List.<JCVariableDecl>nil(), List.<JCExpression>nil(), body, null);
		injectMethod(type, md);
	}
	
	private void generateWriteToParcelMethod(JavacNode type) {
		
		JavacTreeMaker maker = type.getTreeMaker();
		
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>();
		if (hasParcelableBaseClass(type)) {
			statements.add(createCallSuperWriteToParcelStatement(type));
		}
		statements.addAll(createWrites(type));
		JCBlock body = maker.Block(0, statements.toList());
		
		JCVariableDecl parcelParam = maker.VarDef(maker.Modifiers(Flags.PARAMETER), type.toName("dest"), toExpression(type, "android.os.Parcel"), null);
		JCVariableDecl flagsParam = maker.VarDef(maker.Modifiers(Flags.PARAMETER), type.toName("flags"), maker.TypeIdent(CTC_INT), null);
		List<JCVariableDecl> parameters = List.<JCVariableDecl>of(parcelParam, flagsParam);
		
		JCMethodDecl md = maker.MethodDef(maker.Modifiers(Flags.PUBLIC), type.toName("writeToParcel"), maker.TypeIdent(CTC_VOID), List.<JCTypeParameter>nil(), parameters, List.<JCExpression>nil(), body, null);
		injectMethod(type, md);
	}
	
	private boolean hasParcelableBaseClass(JavacNode classNode) {
		JCClassDecl classDecl = (JCClassDecl) classNode.get();
		if (Javac.getExtendsClause(classDecl) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	private JCStatement createCallSuperWriteToParcelStatement(JavacNode classNode) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		JCExpression superWriteToParcel = toExpression(classNode, "super.writeToParcel");
		JCExpression dest = toExpression(classNode, "dest");
		JCExpression flags = toExpression(classNode, "flags");
		JCExpression callSuperWriteToParcel = maker.Apply(null, superWriteToParcel, List.<JCExpression>of(dest, flags));
		return maker.Exec(callSuperWriteToParcel);
	}
	
	private void createConstructorWithParcelParam(JavacNode type) {
		JCClassDecl clazz = (JCClassDecl) type.get();
		boolean isFinal = clazz.mods.getFlags().contains(Modifier.FINAL);
		JavacTreeMaker maker = type.getTreeMaker();
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>();
		if (hasParcelableBaseClass(type)) {
			statements.add(createCallSuperConstructorStatement(type));
		}
		statements.addAll(createConstructorAssignments(type));
		JCBlock body = maker.Block(0, statements.toList());
		
		JCVariableDecl parcelParam = maker.VarDef(maker.Modifiers(Flags.PARAMETER), type.toName("source"), toExpression(type, "android.os.Parcel"), null);
		List<JCVariableDecl> parameters = List.<JCVariableDecl>of(parcelParam);
		
		JCMethodDecl md = maker.MethodDef(maker.Modifiers(isFinal ? 0 : Flags.PROTECTED), type.toName("<init>"), null, List.<JCTypeParameter>nil(), parameters, List.<JCExpression>nil(), body, null);
		injectMethod(type, md);
	}
	
	private JCStatement createCallSuperConstructorStatement(JavacNode classNode) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		JCExpression superConstructor = toExpression(classNode, "super");
		JCExpression source = toExpression(classNode, "source");
		JCExpression callSuperConstructor = maker.Apply(null, superConstructor, List.<JCExpression>of(source));
		return maker.Exec(callSuperConstructor);
	}
	
	private List<JCStatement> createWrites(JavacNode type) {
		JavacTreeMaker maker = type.getTreeMaker();
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>();
		for (JavacNode fieldNode : HandleConstructor.findAllFields(type)) {
			JCVariableDecl field = (JCVariableDecl) fieldNode.get();
			Name rawName = field.name;
			JCExpression thisX = maker.Select(maker.Ident(fieldNode.toName("this")), rawName);
			if (field.type == null) {
				new JavacResolution(type.getContext()).resolveClassMember(fieldNode);
			}
			JCStatement statement = createWriteStatement(type, thisX, field.type, 1);
			statements.add(statement);
		}
		return statements.toList();
	}
	
	private JCStatement createWriteStatement(JavacNode classNode, JCExpression fromValue, Type fromValueType, int level) {
		JCStatement statement = createSimpleWriteStatement(classNode, fromValue, fromValueType);
		if (statement != null) {
			return statement;
		}
		statement = createParcelablesWriteStatement(classNode, fromValue, fromValueType);
		if (statement != null) {
			return statement;
		}
		statement = createArrayWriteStatement(classNode, fromValue, fromValueType, level);
		if (statement != null) {
			return statement;
		}
		statement = createListWriteStatement(classNode, fromValue, fromValueType, level);
		if (statement != null) {
			return statement;
		}
		statement = createMapWriteStatement(classNode, fromValue, fromValueType, level);
		if (statement != null) {
			return statement;
		}
		statement = createOtherWriteStatement(classNode, fromValue, fromValueType);
		if (statement != null) {
			return statement;
		}
		return createValueWriteStatement(classNode, fromValue);
	}
	
	private JCStatement createSimpleWriteStatement(JavacNode classNode, JCExpression fromValue, Type fromValueType) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		JCExpression writeExpression = null;
		if (fromValueType.getKind() == TypeKind.BOOLEAN) {
			// dest.writeInt(this.boolField ? 1 : 0);
			JCConditional boolToInt = maker.Conditional(fromValue, maker.Literal(CTC_INT, 1), maker.Literal(CTC_INT, 0));
			JCExpression writeIntExpression = toExpression(classNode, "dest.writeInt");
			JCMethodInvocation writeIntInvocation = maker.Apply(null, writeIntExpression, List.<JCExpression>of(boolToInt));
			return maker.Exec(writeIntInvocation);
		} else if (readWriteIdents.get(fromValueType.getKind()) != null) {
			// dest.writeDouble(this.primitiveField);
			writeExpression = toExpression(classNode, "dest.write" + readWriteIdents.get(fromValueType.getKind()));
		} else if (fromValueType instanceof ArrayType) {
			ArrayType arrayType = (ArrayType) fromValueType;
			if (readWriteArrayIdents.get(arrayType.elemtype.getKind()) != null) {
				// dest.writeCharArray(this.primitiveArray);
				writeExpression = toExpression(classNode, "dest.write" + readWriteArrayIdents.get(arrayType.elemtype.getKind()));
			} else if (readWriteArraySymbolMap.get(arrayType.elemtype.tsym.toString()) != null) {
				writeExpression = toExpression(classNode, "dest.write" + readWriteArraySymbolMap.get(arrayType.elemtype.tsym.toString()));
			}
		} else if (fromValueType instanceof ClassType) {
			ClassType classType = (ClassType) fromValueType;
			if (readWriteSymbolMap.get(fromValueType.tsym.toString()) != null) {
				writeExpression = toExpression(classNode, "dest.write" + readWriteSymbolMap.get(fromValueType.tsym.toString()));
			} else if ("java.util.List".equals(fromValueType.tsym.toString()) || "java.util.ArrayList".equals(fromValueType.tsym.toString())) {
				if (classType.allparams_field != null && classType.allparams_field.size() > 0) {
					Type paramType = classType.allparams_field.get(0);
					if (readWriteListSymbolMap.get(paramType.tsym.toString()) != null) {
						writeExpression = toExpression(classNode, "dest.write" + readWriteListSymbolMap.get(paramType.tsym.toString()) + "List");
					}
				}
			}
		}
		if (writeExpression != null) {
			JCMethodInvocation writeInvocation = maker.Apply(null, writeExpression, List.<JCExpression>of(fromValue));
			return maker.Exec(writeInvocation);
		}
		return null;
	}
	
	private JCStatement createParcelablesWriteStatement(JavacNode classNode, JCExpression fromValue, Type fromValueType) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		if (fromValueType instanceof ClassType) {
			ClassType classType = (ClassType) fromValueType;
			if (implementsParcelable(classType)) {
				if (isFinal(classType)) {
					JCBinary fromValueEqualNull = maker.Binary(CTC_EQUAL, fromValue, maker.Literal(CTC_BOT, null));
					JCStatement writeZero = createWriteNumberStatement(classNode, 0);
					JCStatement writeOne = createWriteNumberStatement(classNode, 1);
					JCExpression writeExpression = maker.Select(fromValue, classNode.toName("writeToParcel"));
					JCMethodInvocation writeInvocation = maker.Apply(null, writeExpression, List.<JCExpression>of(toExpression(classNode, "dest"), toExpression(classNode, "flags")));
					JCStatement writeStatement = maker.Exec(writeInvocation);
					JCBlock writeOneAndParcelable = maker.Block(0, List.<JCStatement>of(writeOne, writeStatement));
					return maker.If(fromValueEqualNull, block(maker, writeZero), writeOneAndParcelable);
				} else {
					JCExpression writeExpression = toExpression(classNode, "dest.writeParcelable");
					JCMethodInvocation writeInvocation = maker.Apply(null, writeExpression, List.<JCExpression>of(fromValue, toExpression(classNode, "flags")));
					return maker.Exec(writeInvocation);
				}
			}
		}
		return null;
	}
	
	private JCStatement createArrayWriteStatement(JavacNode classNode, JCExpression fromValue, Type fromValueType, int level) {
		if (fromValueType instanceof ArrayType) {
			JavacTreeMaker maker = classNode.getTreeMaker();
			JCExpression fromValueEqualsNull = maker.Binary(CTC_EQUAL, fromValue, maker.Literal(CTC_BOT, null));
			JCExpression writeInt = toExpression(classNode, "dest.writeInt");
			JCExpression writeMinusOne = maker.Apply(null, writeInt, List.<JCExpression>of(maker.Literal(CTC_INT, -1)));
			JCStatement writeMinusOneStatement = maker.Exec(writeMinusOne);
			JCExpression size = maker.Select(fromValue, classNode.toName("length"));
			JCExpression writeSize = maker.Apply(null, writeInt, List.<JCExpression>of(size));
			JCStatement writeSizeStatement = maker.Exec(writeSize);
			
			Name varName = classNode.toName("__loopVar" + level);
			Type childFromValueType = ((ArrayType) fromValueType).elemtype;
			JCExpression varType = toExpression(classNode, childFromValueType);
			JCVariableDecl loopVar = maker.VarDef(maker.Modifiers(0), varName, varType, null);
			JCExpression childFromValue = maker.Ident(varName);
			
			JCStatement childStatement = createWriteStatement(classNode, childFromValue, childFromValueType, level + 1);
			
			JCEnhancedForLoop loop = maker.ForeachLoop(loopVar, fromValue, block(maker, childStatement));
			JCBlock writeSizeAndLoop = maker.Block(0, List.<JCStatement>of(writeSizeStatement, loop));
			return maker.If(fromValueEqualsNull, block(maker, writeMinusOneStatement), writeSizeAndLoop);
		}
		return null;
	}
	
	private JCStatement createListWriteStatement(JavacNode classNode, JCExpression fromValue, Type fromValueType, int level) {
		if (fromValueType instanceof ClassType) {
			String symbol = fromValueType.tsym.toString();
			if ("java.util.List".equals(symbol)
					|| "java.util.ArrayList".equals(symbol)
					|| "java.util.LinkedList".equals(symbol)
					|| "java.util.Set".equals(symbol)
					|| "java.util.HashSet".equals(symbol)
					|| "java.util.LinkedHashSet".equals(symbol)
					|| "java.util.TreeSet".equals(symbol)) {
				JavacTreeMaker maker = classNode.getTreeMaker();
				JCExpression fromValueEqualsNull = maker.Binary(CTC_EQUAL, fromValue, maker.Literal(CTC_BOT, null));
				JCExpression writeInt = toExpression(classNode, "dest.writeInt");
				JCExpression writeMinusOne = maker.Apply(null, writeInt, List.<JCExpression>of(maker.Literal(CTC_INT, -1)));
				JCStatement writeMinusOneStatement = maker.Exec(writeMinusOne);
				JCExpression size = maker.Select(fromValue, classNode.toName("size"));
				JCExpression sizeCall = maker.Apply(null, size, List.<JCExpression>nil());
				JCExpression writeSize = maker.Apply(null, writeInt, List.<JCExpression>of(sizeCall));
				JCStatement writeSizeStatement = maker.Exec(writeSize);
				
				Name varName = classNode.toName("__loopVar" + level);
				Type childFromValueType = ((ClassType) fromValueType).allparams_field.head;
				JCExpression varType = toExpression(classNode, childFromValueType);
				JCVariableDecl loopVar = maker.VarDef(maker.Modifiers(0), varName, varType, null);
				JCExpression childFromValue = maker.Ident(varName);
				
				JCStatement childStatement = createWriteStatement(classNode, childFromValue, childFromValueType, level + 1);
				
				JCEnhancedForLoop loop = maker.ForeachLoop(loopVar, fromValue, block(maker, childStatement));
				JCBlock writeSizeAndLoop = maker.Block(0, List.<JCStatement>of(writeSizeStatement, loop));
				return maker.If(fromValueEqualsNull, block(maker, writeMinusOneStatement), writeSizeAndLoop);
			}
		}
		return null;
	}
	
	private JCStatement createMapWriteStatement(JavacNode classNode, JCExpression fromValue, Type fromValueType, int level) {
		if (fromValueType instanceof ClassType) {
			String symbol = fromValueType.tsym.toString();
			if ("java.util.Map".equals(symbol)
					|| "java.util.HashMap".equals(symbol)
					|| "java.util.LinkedHashMap".equals(symbol)
					|| "java.util.TreeMap".equals(symbol)) {
				JavacTreeMaker maker = classNode.getTreeMaker();
				JCExpression fromValueEqualsNull = maker.Binary(CTC_EQUAL, fromValue, maker.Literal(CTC_BOT, null));
				JCExpression writeInt = toExpression(classNode, "dest.writeInt");
				JCExpression writeMinusOne = maker.Apply(null, writeInt, List.<JCExpression>of(maker.Literal(CTC_INT, -1)));
				JCStatement writeMinusOneStatement = maker.Exec(writeMinusOne);
				JCExpression size = maker.Select(fromValue, classNode.toName("size"));
				JCExpression sizeCall = maker.Apply(null, size, List.<JCExpression>nil());
				JCExpression writeSize = maker.Apply(null, writeInt, List.<JCExpression>of(sizeCall));
				JCStatement writeSizeStatement = maker.Exec(writeSize);
				
				Name varName = classNode.toName("__loopVar" + level);
				Type keyChildFromValueType = ((ClassType) fromValueType).allparams_field.get(0);
				Type valueChildFromValueType = ((ClassType) fromValueType).allparams_field.get(1);
				JCExpression varType = maker.TypeApply(toExpression(classNode, "java.util.Map.Entry"), List.<JCExpression>of(toExpression(classNode, keyChildFromValueType), toExpression(classNode, valueChildFromValueType)));
				JCVariableDecl loopVar = maker.VarDef(maker.Modifiers(0), varName, varType, null);
				
				JCStatement keyDef = maker.VarDef(maker.Modifiers(0), classNode.toName("__keyVar" + level), toExpression(classNode, keyChildFromValueType), maker.Apply(null, maker.Select(maker.Ident(varName), classNode.toName("getKey")), List.<JCExpression>nil()));
				JCExpression keyChildFromValue = toExpression(classNode, "__keyVar" + level);
				JCStatement valueDef = maker.VarDef(maker.Modifiers(0), classNode.toName("__valueVar" + level), toExpression(classNode, valueChildFromValueType), maker.Apply(null, maker.Select(maker.Ident(varName), classNode.toName("getValue")), List.<JCExpression>nil()));
				JCExpression valueChildFromValue = toExpression(classNode, "__valueVar" + level);
				
				JCStatement keyChildStatement = createWriteStatement(classNode, keyChildFromValue, keyChildFromValueType, level + 1);
				JCStatement valueChildStatement = createWriteStatement(classNode, valueChildFromValue, valueChildFromValueType, level + 1);
				
				JCBlock childStatements = maker.Block(0, List.<JCStatement>of(keyDef, valueDef, keyChildStatement, valueChildStatement));
				JCMethodInvocation fromValueEntries = maker.Apply(null, maker.Select(fromValue, classNode.toName("entrySet")), List.<JCExpression>nil());
				JCEnhancedForLoop loop = maker.ForeachLoop(loopVar, fromValueEntries, childStatements);
				JCBlock writeSizeAndLoop = maker.Block(0, List.<JCStatement>of(writeSizeStatement, loop));
				return maker.If(fromValueEqualsNull, block(maker, writeMinusOneStatement), writeSizeAndLoop);
			}
		}
		return null;
	}

	private JCStatement createOtherWriteStatement(JavacNode classNode, JCExpression fromValue, Type fromValueType) {
		if (fromValueType instanceof ClassType) {
			String symbol = fromValueType.tsym.toString();
			if (boxedPrimitivesMap.containsKey(symbol)) {
				JavacTreeMaker maker = classNode.getTreeMaker();
				JCBinary fromValueEqualNull = maker.Binary(CTC_EQUAL, fromValue, maker.Literal(CTC_BOT, null));
				JCStatement writeZero = createWriteNumberStatement(classNode, 0);
				JCStatement writeOne = createWriteNumberStatement(classNode, 1);
				JCExpression writeSomething = toExpression(classNode, "dest.write" + boxedPrimitivesMap.get(symbol));
				JCMethodInvocation writeInvocation = maker.Apply(null, writeSomething, List.<JCExpression>of(fromValue));
				JCStatement writeStatement = maker.Exec(writeInvocation);
				JCBlock writeOneAndParcelable = maker.Block(0, List.<JCStatement>of(writeOne, writeStatement));
				return maker.If(fromValueEqualNull, block(maker, writeZero), writeOneAndParcelable);
			} else if ("java.lang.Boolean".equals(symbol)) {
				JavacTreeMaker maker = classNode.getTreeMaker();
				JCBinary fromValueEqualNull = maker.Binary(CTC_EQUAL, fromValue, maker.Literal(CTC_BOT, null));
				JCExpression booleanFalseEquals = toExpression(classNode, "Boolean.FALSE.equals");
				JCExpression equalsInvocation = maker.Apply(null, booleanFalseEquals, List.<JCExpression>of(fromValue));
				JCStatement writeMinusOne = createWriteNumberStatement(classNode, -1);
				JCStatement writeZero = createWriteNumberStatement(classNode, 0);
				JCStatement writeOne = createWriteNumberStatement(classNode, 1);
				return maker.If(fromValueEqualNull, block(maker, writeMinusOne), maker.If(equalsInvocation, block(maker, writeZero), block(maker, writeOne)));
			}
		}
		return null;
	}
	
	private JCStatement createValueWriteStatement(JavacNode classNode, JCExpression fromValue) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		JCExpression writeValue = toExpression(classNode, "dest.writeValue");
		JCMethodInvocation writeInvocation = maker.Apply(null, writeValue, List.<JCExpression>of(fromValue));
		return maker.Exec(writeInvocation);
	}
	
	private JCStatement createWriteNumberStatement(JavacNode classNode, int value) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		return maker.Exec(maker.Apply(null, toExpression(classNode, "dest.writeInt"), List.<JCExpression>of(maker.Literal(value))));
	}
	
	private boolean isFinal(ClassType classType) {
		return classType.tsym.getModifiers().contains(Modifier.FINAL);
	}
	
	private boolean implementsParcelable(ClassType classType) {
		if (classType.interfaces_field != null) {
			for (Type interfaceType : classType.interfaces_field) {
				if ("android.os.Parcelable".equals(interfaceType.tsym.toString())) {
					return true;
				}
			}
		}
		return classType.tsym.getAnnotation(Parcelable.class) != null;
	}
	
	private List<JCStatement> createConstructorAssignments(JavacNode type) {
		JavacTreeMaker maker = type.getTreeMaker();
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>();
		for (JavacNode fieldNode : HandleConstructor.findAllFields(type)) {
			JCVariableDecl field = (JCVariableDecl) fieldNode.get();
			Name rawName = field.name;
			JCExpression thisX = maker.Select(maker.Ident(type.toName("this")), rawName);
			if (field.type == null) {
				new JavacResolution(type.getContext()).resolveClassMember(fieldNode);
			}
			JCStatement statement = createReadStatement(type, thisX, field.type, 1);
			statements.append(statement);
		}
		return statements.toList();
	}
	
	private JCStatement createReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType, int level) {
		JCStatement statement = createSimpleReadStatement(classNode, assignTo, assignToType);
		if (statement != null) {
			return statement;
		}
		statement = createParcelablesReadStatement(classNode, assignTo, assignToType);
		if (statement != null) {
			return statement;
		}
		statement = createArrayReadStatement(classNode, assignTo, assignToType, level);
		if (statement != null) {
			return statement;
		}
		statement = createListReadStatement(classNode, assignTo, assignToType, level);
		if (statement != null) {
			return statement;
		}
		statement = createMapReadStatement(classNode, assignTo, assignToType, level);
		if (statement != null) {
			return statement;
		}
		statement = createOtherReadStatement(classNode, assignTo, assignToType);
		if (statement != null) {
			return statement;
		}
		return createValueReadStatement(classNode, assignTo, assignToType);
	}
	
	private JCStatement createSimpleReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		JCExpression readExpression = null;
		if (assignToType.getKind() == TypeKind.BOOLEAN) {
			JCExpression readIntExpression = toExpression(classNode, "source.readInt");
			JCMethodInvocation readIntInvocation = maker.Apply(null, readIntExpression, List.<JCExpression>nil());
			JCBinary notEqualTo0 = maker.Binary(CTC_NOT_EQUAL, readIntInvocation, maker.Literal(CTC_INT, 0));
			JCAssign assign = maker.Assign(assignTo, notEqualTo0);
			return maker.Exec(assign);
		} else if (readWriteIdents.get(assignToType.getKind()) != null) {
			readExpression = toExpression(classNode, "source.read" + readWriteIdents.get(assignToType.getKind()));
		} else if (assignToType instanceof ArrayType) {
			ArrayType arrayType = (ArrayType) assignToType;
			if (readWriteArrayIdents.get(arrayType.elemtype.getKind()) != null) {
				readExpression = toExpression(classNode, "source.create" + readWriteArrayIdents.get(arrayType.elemtype.getKind()));
			} else if (readWriteArraySymbolMap.get(arrayType.elemtype.tsym.toString()) != null) {
				readExpression = toExpression(classNode, "source.create" + readWriteArraySymbolMap.get(arrayType.elemtype.tsym.toString()));
			}
		} else if (assignToType instanceof ClassType) {
			ClassType classType = (ClassType) assignToType;
			if (readWriteSymbolMap.get(assignToType.tsym.toString()) != null) {
				readExpression = toExpression(classNode, "source.read" + readWriteSymbolMap.get(assignToType.tsym.toString()));
			} else if ("java.util.List".equals(assignToType.tsym.toString()) || "java.util.ArrayList".equals(assignToType.tsym.toString())) {
				if (classType.allparams_field != null && classType.allparams_field.size() > 0) {
					Type paramType = classType.allparams_field.get(0);
					if (readWriteListSymbolMap.get(paramType.tsym.toString()) != null) {
						readExpression = toExpression(classNode, "source.create" + readWriteListSymbolMap.get(paramType.tsym.toString()) + "ArrayList");
					}
				}
			}
		}
		if (readExpression != null) {
			JCMethodInvocation readInvocation = maker.Apply(null, readExpression, List.<JCExpression>nil());
			JCAssign assign = maker.Assign(assignTo, readInvocation);
			return maker.Exec(assign);
		}
		return null;
	}
	
	private JCStatement createParcelablesReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		if (assignToType instanceof ClassType) {
			ClassType classType = (ClassType) assignToType;
			if (implementsParcelable(classType)) {
				if (isFinal(classType)) {
					JCExpression readInt = toExpression(classNode, "source.readInt");
					JCMethodInvocation readIntInvocation = maker.Apply(null, readInt, List.<JCExpression>nil());
					JCBinary readIntEqualsZero = maker.Binary(CTC_EQUAL, readIntInvocation, maker.Literal(CTC_INT, 0));
					JCStatement assignNull = maker.Exec(maker.Assign(assignTo, maker.Literal(CTC_BOT, null)));
					JCExpression createFromParcel = toExpression(classNode, classType.tsym + ".CREATOR.createFromParcel");
					JCMethodInvocation createFromParcelInvocation = maker.Apply(null, createFromParcel, List.<JCExpression>of(toExpression(classNode, "source")));
					JCStatement assignCreateFromParcel = maker.Exec(maker.Assign(assignTo, createFromParcelInvocation));
					return maker.If(readIntEqualsZero, block(maker, assignNull), block(maker, assignCreateFromParcel));
				} else {
					JCExpression classloaderExpression = toExpression(classNode, classType.tsym + ".class.getClassLoader");
					JCMethodInvocation classloaderInvocation = maker.Apply(null, classloaderExpression, List.<JCExpression>nil());
					JCExpression readExpression = toExpression(classNode, "source.readParcelable");
					JCMethodInvocation readInvocation = maker.Apply(null, readExpression, List.<JCExpression>of(classloaderInvocation));
					JCAssign assign = maker.Assign(assignTo, readInvocation);
					return maker.Exec(assign);
				}
			}
		}
		return null;
	}
	
	private JCStatement createArrayReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType, int level) {
		if (assignToType instanceof ArrayType) {
			Type childAssignToType = ((ArrayType) assignToType).elemtype;
			JavacTreeMaker maker = classNode.getTreeMaker();
			JCExpression readInt = toExpression(classNode, "source.readInt");
			JCExpression readIntCall = maker.Apply(null, readInt, List.<JCExpression>nil());
			Name size = classNode.toName("__sizeVar" + level);
			JCVariableDecl sizeAssignment = maker.VarDef(maker.Modifiers(0), size, maker.TypeIdent(CTC_INT), readIntCall);
			
			JCExpression sizeSmallerThanZero = maker.Binary(CTC_EQUAL, maker.Ident(size), maker.Literal(CTC_INT, -1));
			
			JCStatement assignNullStatement = maker.Exec(maker.Assign(assignTo, maker.Literal(CTC_BOT, null)));
			
			JCNewArray newArray = maker.NewArray(toExpression(classNode, childAssignToType), List.<JCExpression>of(maker.Ident(size)), null);
			JCStatement assignArray = maker.Exec(maker.Assign(assignTo, newArray));
			
			Name iterVar = classNode.toName("__iterVar" + level);
			JCVariableDecl loopInit = maker.VarDef(maker.Modifiers(0), iterVar, maker.TypeIdent(CTC_INT), maker.Literal(CTC_INT, 0));
			JCExpression iterVarNotEqualSize = maker.Binary(CTC_NOT_EQUAL, maker.Ident(iterVar), maker.Ident(size));
			JCExpressionStatement iterVarIncrement = maker.Exec(maker.Unary(CTC_PREINC, maker.Ident(iterVar)));
			
			JCArrayAccess childAssignTo = maker.Indexed(assignTo, maker.Ident(iterVar));
			JCStatement childAssignToStatement = createReadStatement(classNode, childAssignTo, childAssignToType, level + 1);
			
			JCForLoop loop = maker.ForLoop(List.<JCStatement>of(loopInit), iterVarNotEqualSize, List.<JCExpressionStatement>of(iterVarIncrement), blockIfNot(maker, childAssignToStatement));
			JCBlock assignArrayAndLoop = maker.Block(0, List.<JCStatement>of(assignArray, loop));
			
			JCIf ifStatement = maker.If(sizeSmallerThanZero, block(maker, assignNullStatement), assignArrayAndLoop);
			return maker.Block(0, List.<JCStatement>of(sizeAssignment, ifStatement));
		}
		return null;
	}
	
	private JCBlock block(JavacTreeMaker maker, JCStatement statement) {
		return maker.Block(0, List.<JCStatement>of(statement));
	}
	
	private JCBlock blockIfNot(JavacTreeMaker maker, JCStatement statement) {
		if (statement instanceof JCBlock) {
			return (JCBlock) statement;
		} else {
			return block(maker, statement);
		}
	}
	
	private JCStatement createListReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType, int level) {
		if (assignToType instanceof ClassType) {
			String symbol = assignToType.tsym.toString();
			if ("java.util.List".equals(symbol)
					|| "java.util.ArrayList".equals(symbol)
					|| "java.util.LinkedList".equals(symbol)
					|| "java.util.Set".equals(symbol)
					|| "java.util.HashSet".equals(symbol)
					|| "java.util.LinkedHashSet".equals(symbol)
					|| "java.util.TreeSet".equals(symbol)) {
				
				JavacTreeMaker maker = classNode.getTreeMaker();
				Type childAssignToType = ((ClassType) assignToType).allparams_field.head;
				JCExpression clazz;
				if ("java.util.List".equals(symbol)) {
					clazz = toExpression(classNode, "java.util.ArrayList");
					clazz = maker.TypeApply(clazz, List.<JCExpression>of(toExpression(classNode, childAssignToType)));
				} else if ("java.util.Set".equals(symbol)) {
					clazz = toExpression(classNode, "java.util.HashSet");
					clazz = maker.TypeApply(clazz, List.<JCExpression>of(toExpression(classNode, childAssignToType)));
				} else {
					clazz = toExpression(classNode, assignToType);
				}
				JCExpression readInt = toExpression(classNode, "source.readInt");
				JCExpression readIntCall = maker.Apply(null, readInt, List.<JCExpression>nil());
				Name size = classNode.toName("__sizeVar" + level);
				JCVariableDecl sizeAssignment = maker.VarDef(maker.Modifiers(0), size, maker.TypeIdent(CTC_INT), readIntCall);
				
				JCExpression sizeSmallerThanZero = maker.Binary(CTC_EQUAL, maker.Ident(size), maker.Literal(CTC_INT, -1));
				
				JCStatement assignNullStatement = maker.Exec(maker.Assign(assignTo, maker.Literal(CTC_BOT, null)));
				
				JCNewClass newList = maker.NewClass(null, List.<JCExpression>nil(), clazz, List.<JCExpression>nil(), null);
				JCStatement assignNewList = maker.Exec(maker.Assign(assignTo, newList));
				
				JCExpression sizeNotEqualZero = maker.Binary(CTC_NOT_EQUAL, maker.Ident(size), maker.Literal(CTC_INT, 0));
				
				Name elem = classNode.toName("__childVar" + level);
				JCExpression elemIdent = maker.Ident(elem);
				JCVariableDecl elemVar = maker.VarDef(maker.Modifiers(0), elem, toExpression(classNode, childAssignToType), null);
				JCStatement childAssignToStatement = createReadStatement(classNode, elemIdent, childAssignToType, level + 1);
				JCExpressionStatement addElem = maker.Exec(maker.Apply(null, maker.Select(assignTo, classNode.toName("add")), List.<JCExpression>of(elemIdent)));
				JCExpressionStatement sizeVarDecrement = maker.Exec(maker.Unary(CTC_PREDEC, maker.Ident(size)));
				
				JCBlock block = maker.Block(0, List.<JCStatement>of(
						elemVar,
						childAssignToStatement,
						addElem,
						sizeVarDecrement));
				
				JCWhileLoop loop = maker.WhileLoop(sizeNotEqualZero, block);
				
				JCBlock assignArrayAndLoop = maker.Block(0, List.<JCStatement>of(assignNewList, loop));
				
				JCIf ifStatement = maker.If(sizeSmallerThanZero, block(maker, assignNullStatement), assignArrayAndLoop);
				return maker.Block(0, List.<JCStatement>of(sizeAssignment, ifStatement));
			}
		}
		return null;
	}
	
	private JCStatement createMapReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType, int level) {
		if (assignToType instanceof ClassType) {
			String symbol = assignToType.tsym.toString();
			if ("java.util.Map".equals(symbol)
					|| "java.util.HashMap".equals(symbol)
					|| "java.util.LinkedHashMap".equals(symbol)
					|| "java.util.TreeMap".equals(symbol)) {
				
				Type keyChildAssignToType = ((ClassType) assignToType).allparams_field.get(0);
				Type valueChildAssignToType = ((ClassType) assignToType).allparams_field.get(1);
				JavacTreeMaker maker = classNode.getTreeMaker();
				JCExpression clazz;
				if ("java.util.Map".equals(symbol)) {
					clazz = toExpression(classNode, "java.util.HashMap");
					clazz = maker.TypeApply(clazz, List.<JCExpression>of(toExpression(classNode, keyChildAssignToType), toExpression(classNode, valueChildAssignToType)));
				} else {
					clazz = toExpression(classNode, assignToType);
				}
				JCExpression readInt = toExpression(classNode, "source.readInt");
				JCExpression readIntCall = maker.Apply(null, readInt, List.<JCExpression>nil());
				Name size = classNode.toName("__sizeVar" + level);
				JCVariableDecl sizeAssignment = maker.VarDef(maker.Modifiers(0), size, maker.TypeIdent(CTC_INT), readIntCall);
				
				JCExpression sizeSmallerThanZero = maker.Binary(CTC_EQUAL, maker.Ident(size), maker.Literal(CTC_INT, -1));
				
				JCStatement assignNullStatement = maker.Exec(maker.Assign(assignTo, maker.Literal(CTC_BOT, null)));
				
				JCNewClass newList = maker.NewClass(null, List.<JCExpression>nil(), clazz, List.<JCExpression>nil(), null);
				JCStatement assignNewList = maker.Exec(maker.Assign(assignTo, newList));
				
				JCExpression sizeNotEqualZero = maker.Binary(CTC_NOT_EQUAL, maker.Ident(size), maker.Literal(CTC_INT, 0));
				
				Name keyElem = classNode.toName("__keyVar" + level);
				JCExpression keyElemIdent = maker.Ident(keyElem);
				JCVariableDecl keyElemVar = maker.VarDef(maker.Modifiers(0), keyElem, toExpression(classNode, keyChildAssignToType), null);
				JCStatement keyChildAssignToStatement = createReadStatement(classNode, keyElemIdent, keyChildAssignToType, level + 1);
				
				Name valueElem = classNode.toName("__valueVar" + level);
				JCExpression valueElemIdent = maker.Ident(valueElem);
				JCVariableDecl valueElemVar = maker.VarDef(maker.Modifiers(0), valueElem, toExpression(classNode, valueChildAssignToType), null);
				JCStatement valueChildAssignToStatement = createReadStatement(classNode, valueElemIdent, valueChildAssignToType, level + 1);
				
				JCExpressionStatement putElem = maker.Exec(maker.Apply(null, maker.Select(assignTo, classNode.toName("put")), List.<JCExpression>of(keyElemIdent, valueElemIdent)));
				JCExpressionStatement sizeVarDecrement = maker.Exec(maker.Unary(CTC_PREDEC, maker.Ident(size)));
				
				JCBlock block = maker.Block(0, List.<JCStatement>of(
						keyElemVar,
						valueElemVar,
						keyChildAssignToStatement,
						valueChildAssignToStatement,
						putElem,
						sizeVarDecrement));
				
				JCWhileLoop loop = maker.WhileLoop(sizeNotEqualZero, block);
				
				JCBlock assignArrayAndLoop = maker.Block(0, List.<JCStatement>of(assignNewList, loop));
				
				JCIf ifStatement = maker.If(sizeSmallerThanZero, block(maker, assignNullStatement), assignArrayAndLoop);
				return maker.Block(0, List.<JCStatement>of(sizeAssignment, ifStatement));
			}
		}
		return null;
	}
	
	private JCStatement createOtherReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType) {
		if (assignToType instanceof ClassType) {
			String symbol = assignToType.tsym.toString();
			if (boxedPrimitivesMap.containsKey(symbol)) {
				JavacTreeMaker maker = classNode.getTreeMaker();
				JCExpression readInt = toExpression(classNode, "source.readInt");
				JCMethodInvocation readIntInvocation = maker.Apply(null, readInt, List.<JCExpression>nil());
				JCBinary readIntEqualsZero = maker.Binary(CTC_EQUAL, readIntInvocation, maker.Literal(CTC_INT, 0));
				JCStatement assignNull = maker.Exec(maker.Assign(assignTo, maker.Literal(CTC_BOT, null)));
				JCExpression readSomething = toExpression(classNode, "source.read" + boxedPrimitivesMap.get(symbol));
				JCMethodInvocation readSomethingInvocation = maker.Apply(null, readSomething, List.<JCExpression>nil());
				JCStatement assignSomething = maker.Exec(maker.Assign(assignTo, readSomethingInvocation));
				return maker.If(readIntEqualsZero, block(maker, assignNull), block(maker, assignSomething));
			} else if ("java.lang.Boolean".equals(symbol)) {
				JavacTreeMaker maker = classNode.getTreeMaker();
				JCExpression readInt = toExpression(classNode, "source.readInt");
				JCMethodInvocation readIntInvocation = maker.Apply(null, readInt, List.<JCExpression>nil());
				Name boolValue = classNode.toName("__boolValue");
				JCStatement boolValueAssignReadInt = maker.VarDef(maker.Modifiers(0), boolValue, maker.TypeIdent(CTC_INT), readIntInvocation);
				JCBinary boolValueEqualsMinusOne = maker.Binary(CTC_EQUAL, maker.Ident(boolValue), maker.Literal(CTC_INT, -1));
				JCBinary boolValueEqualsZero = maker.Binary(CTC_EQUAL, maker.Ident(boolValue), maker.Literal(CTC_INT, 0));
				JCStatement assignNull = maker.Exec(maker.Assign(assignTo, maker.Literal(CTC_BOT, null)));
				JCStatement assignFalse = maker.Exec(maker.Assign(assignTo, toExpression(classNode, "Boolean.FALSE")));
				JCStatement assignTrue = maker.Exec(maker.Assign(assignTo, toExpression(classNode, "Boolean.TRUE")));
				JCIf ifStatement = maker.If(boolValueEqualsMinusOne, block(maker, assignNull), maker.If(boolValueEqualsZero, block(maker, assignFalse), block(maker, assignTrue)));
				return maker.Block(0, List.<JCStatement>of(boolValueAssignReadInt, ifStatement));
			}
		}
		return null;
	}
	
	private JCStatement createValueReadStatement(JavacNode classNode, JCExpression assignTo, Type assignToType) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		JCExpression classLoaderParam;
		if (assignToType instanceof ClassType) {
			classLoaderParam = toExpression(classNode, assignToType.tsym.toString() + ".class.getClassLoader");
			classLoaderParam = maker.Apply(null, classLoaderParam, List.<JCExpression>nil());
		} else {
			classLoaderParam = maker.Literal(CTC_BOT, null);
		}
		JCExpression readValue = toExpression(classNode, "source.readValue");
		JCMethodInvocation readValueInvocation = maker.Apply(null, readValue, List.<JCExpression>of(classLoaderParam));
		JCTypeCast castReadValue = maker.TypeCast(toExpression(classNode, assignToType), readValueInvocation);
		JCStatement assignReadValue = maker.Exec(maker.Assign(assignTo, castReadValue));
		return assignReadValue;
	}

	private JavacNode createCreatorType(JavacNode type) {
		JavacTreeMaker maker = type.getTreeMaker();
		JCModifiers mods = maker.Modifiers(Flags.PRIVATE | Flags.STATIC);
		JCClassDecl creator = maker.ClassDef(mods, type.toName("CreatorImpl"), List.<JCTypeParameter>nil(), null, List.<JCExpression>nil(), List.<JCTree>nil());
		return injectType(type, creator);
	}
	
	private void addCreatorInterface(JavacNode type, JavacNode parentType) {
		JavacTreeMaker maker = type.getTreeMaker();
		JCClassDecl clazz = (JCClassDecl) type.get();
		ListBuffer<JCExpression> newImpl = new ListBuffer<JCExpression>();
		List<JCExpression> impl = clazz.implementing;
		newImpl.appendList(impl);
		JCExpression androidOsParcelableCreator = toExpression(type, "android.os.Parcelable.Creator");
		JCClassDecl parentTypeDecl = (JCClassDecl) parentType.get();
		JCTypeApply androidOsParcelableCreatorOfClass = maker.TypeApply(androidOsParcelableCreator, List.<JCExpression>of(maker.Ident(parentTypeDecl.name)));
		newImpl.append(androidOsParcelableCreatorOfClass);
		clazz.implementing = newImpl.toList();
	}
	
	private void generateCreateFromParcelMethod(JavacNode type, JavacNode parentType) {
		JCClassDecl parentTypeDecl = (JCClassDecl) parentType.get();
		JavacTreeMaker maker = type.getTreeMaker();
		
		JCVariableDecl parcelParam = maker.VarDef(maker.Modifiers(Flags.PARAMETER), type.toName("source"), toExpression(type, "android.os.Parcel"), null);
		List<JCVariableDecl> parameters = List.<JCVariableDecl>of(parcelParam);
		
		JCExpression parentClassExpression = maker.Ident(parentTypeDecl.name);
		JCExpression parcelParamExpression = toExpression(type, "source");
		JCStatement statement = maker.Return(maker.NewClass(null, List.<JCExpression>nil(), parentClassExpression, List.<JCExpression>of(parcelParamExpression), null));
		
		JCBlock body = block(maker, statement);
		
		JCMethodDecl md = maker.MethodDef(maker.Modifiers(Flags.PUBLIC), type.toName("createFromParcel"), maker.Ident(parentTypeDecl.name), List.<JCTypeParameter>nil(), parameters, List.<JCExpression>nil(), body, null);
		injectMethod(type, md);
	}
	
	private void generateNewArrayMethod(JavacNode type, JavacNode parentType) {
		JavacTreeMaker maker = type.getTreeMaker();
		JCVariableDecl sizeParam = maker.VarDef(maker.Modifiers(Flags.PARAMETER), type.toName("size"), maker.TypeIdent(CTC_INT), null);
		
		JCClassDecl parentTypeDecl = (JCClassDecl) parentType.get();
		JCExpression newArray = maker.NewArray(maker.Ident(parentTypeDecl.name), List.<JCExpression>of(maker.Ident(type.toName("size"))), null);
		JCStatement statement = maker.Return(newArray);
		
		JCBlock body = block(maker, statement);
		
		JCMethodDecl md = maker.MethodDef(maker.Modifiers(Flags.PUBLIC), type.toName("newArray"), maker.TypeArray(maker.Ident(parentTypeDecl.name)), List.<JCTypeParameter>nil(), List.<JCVariableDecl>of(sizeParam), List.<JCExpression>nil(), body, null);
		injectMethod(type, md);

	}
	
	private void addCreatorStaticField(JavacNode type) {
		JavacTreeMaker maker = type.getTreeMaker();
		JCExpression androidOsParcelableCreator = toExpression(type, "android.os.Parcelable.Creator");
		JCClassDecl parentTypeDecl = (JCClassDecl) type.get();
		JCTypeApply androidOsParcelableCreatorOfClass = maker.TypeApply(androidOsParcelableCreator, List.<JCExpression>of(maker.Ident(parentTypeDecl.name)));
		JCNewClass newCreatorImpl = maker.NewClass(null, List.<JCExpression>nil(), toExpression(type, "CreatorImpl"), List.<JCExpression>nil(), null);
		JCVariableDecl creator = maker.VarDef(maker.Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL), type.toName("CREATOR"), androidOsParcelableCreatorOfClass, newCreatorImpl);
		injectField(type, creator);
	}
}
