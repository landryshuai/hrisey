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

import static hrisey.javac.lang.BlockCreator.createBlock;
import static hrisey.javac.lang.ExpressionCreator.*;
import static hrisey.javac.lang.MethodCreator.*;
import static hrisey.javac.lang.Modifier.*;
import static hrisey.javac.lang.ParameterCreator.createParam;
import static hrisey.javac.lang.Primitive.VOID;
import static hrisey.javac.lang.StatementCreator.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import hrisey.InstanceState;
import hrisey.Parcelable;
import hrisey.javac.handlers.util.FieldInfo;
import hrisey.javac.lang.Modifier;
import hrisey.javac.lang.Statement;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;

import org.mangosdk.spi.ProviderFor;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.ListBuffer;

@ProviderFor(JavacAnnotationHandler.class)
public class InstanceStateHandler extends JavacAnnotationHandler<InstanceState> {
	
	private static final String[] primitivesMap = new String[TypeTags.TypeTagCount];
	static {
		primitivesMap[TypeTags.BOOLEAN] = "Boolean";
		primitivesMap[TypeTags.BYTE] = "Byte";
		primitivesMap[TypeTags.CHAR] = "Char";
		primitivesMap[TypeTags.DOUBLE] = "Double";
		primitivesMap[TypeTags.FLOAT] = "Float";
		primitivesMap[TypeTags.INT] = "Int";
		primitivesMap[TypeTags.LONG] = "Long";
		primitivesMap[TypeTags.SHORT] = "Short";
	}
	
	@Override
	public void handle(AnnotationValues<InstanceState> annotation, JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, InstanceState.class);
		deleteImportFromCompilationUnit(annotationNode, InstanceState.class.getName());

		JavacNode classNode = annotationNode.up().up();
		FieldInfo fieldInfo = new FieldInfo(annotationNode.up());
		JCMethodDecl onCreate = getOnCreateMethod(classNode);
		if (onCreate == null) {
			addOnCreateMethod(classNode, fieldInfo);
		} else {
			prependAssignmentStatement(classNode, onCreate, fieldInfo);
		}
		JCMethodDecl onSaveInstanceState = getOnSaveInstanceStateMethod(classNode);
		if (onSaveInstanceState == null) {
			addOnSaveInstanceStateMethod(classNode, fieldInfo);
		} else {
			prependStoreStatement(classNode, onSaveInstanceState, fieldInfo);
		}
	}
	
	private JCMethodDecl getOnCreateMethod(JavacNode classNode) {
		return getMethod("onCreate", classNode);
	}
	
	private JCMethodDecl getMethod(String name, JavacNode classNode) {
		for (JavacNode node : classNode.down()) {
			if (node.get() instanceof JCMethodDecl) {
				JCMethodDecl methodDecl = (JCMethodDecl) node.get();
				if (name.equalsIgnoreCase(methodDecl.name.toString())) {
					return methodDecl;
				}
			}
		}
		return null;
	}

	private void addOnCreateMethod(JavacNode classNode, FieldInfo f) {
		Statement assignment = createAssignment("this." + f.getName(), createCall("savedInstanceState.get" + functionNameForField(f), createLiteral(f.getName())));
		createMethod(getVisibilityForClass(classNode), VOID, "onCreate", createParam("android.os.Bundle", "savedInstanceState"),
				createBlock(
						createIf(createNotEquals("savedInstanceState", createNull()),
								createBlock(assignment)
						),
						createExec(createCall("super.onCreate", "savedInstanceState"))
				)
		).inject(classNode);
	}
	
	private Modifier getVisibilityForClass(JavacNode classNode) {
		if (isActivity(classNode)) {
			return PROTECTED;
		}
		return PUBLIC;
	}
	
	private boolean isActivity(JavacNode classNode) {
		JCClassDecl classDecl = (JCClassDecl) classNode.get();
		return classDecl.getExtendsClause().type.tsym.toString().contains("Activity");
	}
	
	private void prependAssignmentStatement(JavacNode classNode, JCMethodDecl onCreate, FieldInfo f) {
		String savedInstanceState = onCreate.params.head.name.toString();
		Statement assignment = createAssignment("this." + f.getName(), createCall(savedInstanceState + ".get" + functionNameForField(f), createLiteral(f.getName())));
		Statement conditionalAssignment = createIf(createNotEquals(savedInstanceState, createNull()),
				createBlock(assignment)
		);
		prependStatement(classNode, onCreate, conditionalAssignment);
	}
	
	private void prependStatement(JavacNode classNode, JCMethodDecl method, Statement statement) {
		ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>();
		statements.append(statement.create(classNode));
		statements.appendList(method.body.stats);
		method.body.stats = statements.toList();
	}
	
	private JCMethodDecl getOnSaveInstanceStateMethod(JavacNode classNode) {
		return getMethod("onSaveInstanceState", classNode);
	}

	private void addOnSaveInstanceStateMethod(JavacNode classNode, FieldInfo f) {
		Statement store = createExec(createCall("outState.put" + functionNameForField(f), createLiteral(f.getName()), "this." + f.getName()));
		Statement superCall = createExec(createCall("super.onSaveInstanceState", "outState"));
		createMethod(getVisibilityForClass(classNode), VOID, "onSaveInstanceState", createParam("android.os.Bundle", "outState"),
				createBlock(store, superCall)
		).inject(classNode);
	}
	
	private void prependStoreStatement(JavacNode classNode, JCMethodDecl onSaveInstanceState, FieldInfo f) {
		String outState = onSaveInstanceState.params.head.name.toString();
		Statement store = createExec(createCall(outState + ".put" + functionNameForField(f), createLiteral(f.getName()), "this." + f.getName()));
		prependStatement(classNode, onSaveInstanceState, store);
	}
	
	private String functionNameForField(FieldInfo field) {
		if (field.getType() instanceof ArrayType) {
			ArrayType arrayType = (ArrayType) field.getType();
			Type elemType = arrayType.elemtype;
			if (elemType instanceof ClassType) {
				String className = elemType.tsym.toString();
				if ("java.lang.String".equals(className)) {
					return "StringArray";
				} else if ("java.lang.CharSequence".equals(className)) {
					return "CharSequenceArray";
				}
			}
			return primitivesMap[elemType.tag] + "Array";
		} else if (field.getType() instanceof ClassType) {
			String className = field.getType().tsym.toString();
			if ("java.lang.String".equals(className)) {
				return "String";
			} else if ("android.os.Bundle".equals(className)) {
				return "Bundle";
			} else if ("java.lang.CharSequence".equals(className)) {
				return "CharSequence";
			} else if ("java.util.ArrayList".equals(className)) {
				ClassType classType = (ClassType) field.getType();
				if (classType.allparams_field != null && classType.allparams_field.size() == 1) {
					if (classType.allparams_field.head instanceof ClassType) {
						ClassType paramType = (ClassType) classType.allparams_field.head;
						String paramName = paramType.tsym.toString();
						if ("java.lang.String".equals(paramName)) {
							return "StringArrayList";
						} else if ("java.lang.CharSequence".equals(paramName)) {
							return "CharSequenceArrayList";
						} else if ("java.lang.Integer".equals(paramName)) {
							return "IntegerArrayList";
						} else if (implementsParcelable(paramType)) {
							return "ParcelableArrayList";
						}
					}
				}
			} else if (implementsParcelable((ClassType) field.getType())) {
				return "Parcelable";
			}
		}
		return primitivesMap[field.getType().tag];
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
}
