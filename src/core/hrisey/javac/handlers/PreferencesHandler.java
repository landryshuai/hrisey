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

import static hrisey.javac.lang.BlockCreator.*;
import static hrisey.javac.lang.ConstructorCreator.*;
import static hrisey.javac.lang.ExpressionCreator.*;
import static hrisey.javac.lang.FieldCreator.*;
import static hrisey.javac.lang.MethodCreator.*;
import static hrisey.javac.lang.Modifier.*;
import static hrisey.javac.lang.ParameterCreator.*;
import static hrisey.javac.lang.Primitive.*;
import static hrisey.javac.lang.StatementCreator.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;

import java.util.List;

import hrisey.Preferences;
import hrisey.javac.handlers.util.FieldFinder;
import hrisey.javac.handlers.util.FieldInfo;
import hrisey.javac.lang.Call;
import hrisey.javac.lang.Expression;
import hrisey.javac.lang.NewInstance;
import hrisey.javac.lang.Statement;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;

import org.mangosdk.spi.ProviderFor;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

@ProviderFor(JavacAnnotationHandler.class)
public class PreferencesHandler extends JavacAnnotationHandler<Preferences> {
	
	@Override
	public void handle(AnnotationValues<Preferences> annotation, JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, Preferences.class);
		deleteImportFromCompilationUnit(annotationNode, Preferences.class.getName());
		
		JavacNode classNode = annotationNode.up();
		if (!(classNode.get() instanceof JCClassDecl)) {
			annotationNode.addError("@Preferences is only supported on classes.");
			return;
		}
		List<FieldInfo> infos = FieldFinder.findAllFields(classNode);
		addPrefsField(classNode);
		if (hasComplexType(infos)) {
			addGsonField(classNode);
			addPrefsAndGsonConstructor(classNode);
		} else {
			addPrefsConstructor(classNode);
		}
		for (FieldInfo fieldInfo : infos) {
			if (isComplexType(fieldInfo)) {
				addComplexGetMethod(classNode, fieldInfo);
				addComplexSetMethod(classNode, fieldInfo);
			} else {
				addGetMethod(classNode, fieldInfo);
				addSetMethod(classNode, fieldInfo);
			}
			addContainsMethod(classNode, fieldInfo);
			addRemoveMethod(classNode, fieldInfo);
		}
	}
	
	private static boolean hasComplexType(List<FieldInfo> infos) {
		for (FieldInfo fieldInfo : infos) {
			if (isComplexType(fieldInfo)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isComplexType(FieldInfo fieldInfo) {
		int tag = fieldInfo.getType().tag;
		if (tag == TypeTags.ARRAY) {
			return true;
		}
		if (tag == TypeTags.CLASS) {
			if (!"java.lang.String".equals(fieldInfo.getType().tsym.toString())) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isGenericType(FieldInfo fieldInfo) {
		Type type = fieldInfo.getType();
		while (type instanceof ArrayType) {
			type = ((ArrayType) type).elemtype;
		}
		return type.getTypeArguments().nonEmpty();
	}
	
	private static void addPrefsField(JavacNode classNode) {
		createField(PRIVATE, FINAL, "android.content.SharedPreferences", "__prefs")
				.inject(classNode);
	}

	private static void addGsonField(JavacNode classNode) {
		createField(PRIVATE, FINAL, "com.google.gson.Gson", "__gson")
				.inject(classNode);
	}
	
	private static void addPrefsConstructor(JavacNode classNode) {
		createConstructor(PUBLIC, createParam("android.content.SharedPreferences", "prefs"),
				createBlock(
						createAssignment("this.__prefs", "prefs")
				)
		).inject(classNode);
	}
	
	private static void addPrefsAndGsonConstructor(JavacNode classNode) {
		createConstructor(PUBLIC,
				createParam("android.content.SharedPreferences", "prefs"),
				createParam("com.google.gson.Gson", "gson"),
				createBlock(
						createAssignment("this.__prefs", "prefs"),
						createAssignment("this.__gson", "gson")
				)
		).inject(classNode);
	}
	
	private static String getSuffixForField(FieldInfo fieldInfo) {
		int tag = fieldInfo.getType().tag;
		if (tag == TypeTags.BOOLEAN) {
			return "Boolean";
		} else if (tag == TypeTags.FLOAT) {
			return "Float";
		} else if (tag == TypeTags.INT) {
			return "Int";
		} else if (tag == TypeTags.LONG || tag == TypeTags.DOUBLE) {
			return "Long";
		} else {
			return "String";
		}
	}
	
	private static void addGetMethod(JavacNode classNode, FieldInfo fieldInfo) {
		String methodSuffix = getSuffixForField(fieldInfo);
		Expression defaultValue;
		if (fieldInfo.getType().tag == TypeTags.DOUBLE) {
			defaultValue = createCall("Double.doubleToLongBits", "this." + fieldInfo.getName());
		} else {
			defaultValue = createIdent("this." + fieldInfo.getName());
		}
		Expression returnedExpression = createCall("this.__prefs.get" + methodSuffix, createLiteral(fieldInfo.getName()), defaultValue);
		if (fieldInfo.getType().tag == TypeTags.DOUBLE) {
			returnedExpression = createCall("Double.longBitsToDouble", returnedExpression);
		}
		createMethod(PUBLIC, createType(fieldInfo.getType()), "get" + fieldInfo.getNamePascal(),
				createBlock(
						createReturn(returnedExpression)
				)
		).inject(classNode);
	}
	
	private static void addSetMethod(JavacNode classNode, FieldInfo fieldInfo) {
		String methodSuffix = getSuffixForField(fieldInfo);
		Call edit = createCall("this.__prefs.edit");
		Expression value;
		if (fieldInfo.getType().tag == TypeTags.DOUBLE) {
			value = createCall("Double.doubleToLongBits", fieldInfo.getName());
		} else {
			value = createIdent(fieldInfo.getName());
		}
		Call putType = createCall(createSelect(edit, "put" + methodSuffix), createLiteral(fieldInfo.getName()), value);
		Call apply = createCall(createSelect(putType, "apply"));
		createMethod(PUBLIC, VOID, "set" + fieldInfo.getNamePascal(), createParam(createType(fieldInfo.getType()), fieldInfo.getName()),
				createBlock(
						createExec(apply)
				)
		).inject(classNode);
	}
	
	private static void addComplexGetMethod(JavacNode classNode, FieldInfo fieldInfo) {
		String localVar = fieldInfo.getName() + "String";
		Statement happyPathBlock;
		if (isGenericType(fieldInfo)) {
			String typeVar = fieldInfo.getName() + "Type";
			NewInstance instance = createNewAnonymousClassInstance(createType("com.google.gson.reflect.TypeToken", fieldInfo.getType()));
			happyPathBlock = createBlock(
					createVariable("java.lang.reflect.Type", typeVar, createCall(createSelect(instance, "getType"))),
					createReturn(createCall("this.__gson.fromJson", localVar, typeVar))
			);
		} else {
			happyPathBlock = createBlock(createReturn(createCall("this.__gson.fromJson", localVar, createSelect(createType(fieldInfo.getType()), "class"))));
		}
		Expression getString = createCall("this.__prefs.getString", createLiteral(fieldInfo.getName()), createLiteral("DEFAULT"));
		createMethod(PUBLIC, createType(fieldInfo.getType()), "get" + fieldInfo.getNamePascal(),
				createBlock(
						createVariable("java.lang.String", localVar, getString),
						createIf(createEquals(localVar, createNull()),
								createBlock(createReturn(createNull())),
								createIf(createCall(localVar + ".equals", createLiteral("DEFAULT")),
										createBlock(createReturn(createIdent("this." + fieldInfo.getName()))),
										happyPathBlock
								)
						)
				)
		).inject(classNode);
	}
	
	private static void addComplexSetMethod(JavacNode classNode, FieldInfo fieldInfo) {
		String localVar = fieldInfo.getName() + "String";
		Statement happyPathBlock;
		if (isGenericType(fieldInfo)) {
			String typeVar = fieldInfo.getName() + "Type";
			NewInstance instance = createNewAnonymousClassInstance(createType("com.google.gson.reflect.TypeToken", fieldInfo.getType()));
			happyPathBlock = createBlock(
					createVariable("java.lang.reflect.Type", typeVar, createCall(createSelect(instance, "getType"))),
					createAssignment(localVar, createCall("this.__gson.toJson", fieldInfo.getName(), typeVar))
			);
		} else {
			happyPathBlock = createBlock(createAssignment(localVar, createCall("this.__gson.toJson", fieldInfo.getName())));
		}
		Call edit = createCall("this.__prefs.edit");
		Call putType = createCall(createSelect(edit, "putString"), createLiteral(fieldInfo.getName()), localVar);
		Call apply = createCall(createSelect(putType, "apply"));
		createMethod(PUBLIC, VOID, "set" + fieldInfo.getNamePascal(), createParam(createType(fieldInfo.getType()), fieldInfo.getName()),
				createBlock(
						createVariable("java.lang.String", localVar),
						createIf(createEquals(fieldInfo.getName(), createNull()),
								createBlock(createAssignment(localVar, createNull())),
								happyPathBlock
						),
						createExec(apply)
				)
		).inject(classNode);
	}
	
	private static void addContainsMethod(JavacNode classNode, FieldInfo fieldInfo) {
		createMethod(PUBLIC, BOOLEAN, "contains" + fieldInfo.getNamePascal(),
				createBlock(
						createReturn(
								createCall("this.__prefs.contains", createLiteral(fieldInfo.getName()))
						)
				)
		).inject(classNode);
	}
	
	private static void addRemoveMethod(JavacNode classNode, FieldInfo fieldInfo) {
		Call edit = createCall("this.__prefs.edit");
		Call remove = createCall(createSelect(edit, "remove"), createLiteral(fieldInfo.getName()));
		Call apply = createCall(createSelect(remove, "apply"));
		createMethod(PUBLIC, VOID, "remove" + fieldInfo.getNamePascal(),
				createBlock(
						createExec(apply)
				)
		).inject(classNode);
	}
}
