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

import static hrisey.javac.lang.BodyCreator.*;
import static hrisey.javac.lang.ConstructorCreator.*;
import static hrisey.javac.lang.ExpressionCreator.*;
import static hrisey.javac.lang.FieldCreator.*;
import static hrisey.javac.lang.MethodCreator.*;
import static hrisey.javac.lang.Modifier.*;
import static hrisey.javac.lang.ParameterCreator.*;
import static hrisey.javac.lang.Primitive.*;
import static hrisey.javac.lang.StatementCreator.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import hrisey.Preferences;
import hrisey.javac.handlers.util.FieldFinder;
import hrisey.javac.handlers.util.FieldInfo;
import hrisey.javac.lang.Call;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;

import org.mangosdk.spi.ProviderFor;

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
		addPrefsConstructor(classNode);
		for (FieldInfo fieldInfo : FieldFinder.findAllFields(classNode)) {
			addGetMethod(classNode, fieldInfo);
			addSetMethod(classNode, fieldInfo);
			addContainsMethod(classNode, fieldInfo);
			addRemoveMethod(classNode, fieldInfo);
		}
		addPrefsField(classNode);
	}

	private static void addPrefsField(JavacNode classNode) {
		createField(PRIVATE, FINAL, "android.content.SharedPreferences", "__prefs")
				.inject(classNode);
	}
	
	private static void addPrefsConstructor(JavacNode classNode) {
		createConstructor(PUBLIC, createParam("android.content.SharedPreferences", "prefs"),
				createBody(
						createAssignment("this.__prefs", "prefs")
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
		} else if (tag == TypeTags.LONG) {
			return "Long";
		} else {
			return "String";
		}
	}
	
	private static void addGetMethod(JavacNode classNode, FieldInfo fieldInfo) {
		String methodSuffix = getSuffixForField(fieldInfo);
		createMethod(PUBLIC, createType(fieldInfo.getType()), "get" + fieldInfo.getNamePascal(),
				createBody(
						createReturn(
								createCall("this.__prefs.get" + methodSuffix, createLiteral(fieldInfo.getName()), "this." + fieldInfo.getName())
						)
				)
		).inject(classNode);
	}
	
	private static void addSetMethod(JavacNode classNode, FieldInfo fieldInfo) {
		String methodSuffix = getSuffixForField(fieldInfo);
		Call edit = createCall("this.__prefs.edit");
		Call putInt = createCall(createSelect(edit, "put" + methodSuffix), createLiteral(fieldInfo.getName()), fieldInfo.getName());
		Call apply = createCall(createSelect(putInt, "apply"));
		createMethod(PUBLIC, VOID, "set" + fieldInfo.getNamePascal(), createParam(createType(fieldInfo.getType()), fieldInfo.getName()),
				createBody(
						createExec(apply)
				)
		).inject(classNode);
	}
	
	private static void addContainsMethod(JavacNode classNode, FieldInfo fieldInfo) {
		createMethod(PUBLIC, BOOLEAN, "contains" + fieldInfo.getNamePascal(),
				createBody(
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
				createBody(
						createExec(apply)
				)
		).inject(classNode);
	}
}
