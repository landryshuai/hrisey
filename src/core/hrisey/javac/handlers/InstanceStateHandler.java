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

import java.util.List;

import hrisey.InstanceState;
import hrisey.javac.handlers.util.FieldFinder;
import hrisey.javac.handlers.util.FieldInfo;
import hrisey.javac.lang.Statement;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;

import org.mangosdk.spi.ProviderFor;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;

@ProviderFor(JavacAnnotationHandler.class)
public class InstanceStateHandler extends JavacAnnotationHandler<InstanceState> {
	
	@Override
	public void handle(AnnotationValues<InstanceState> annotation, JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, InstanceState.class);
		deleteImportFromCompilationUnit(annotationNode, InstanceState.class.getName());
		
		JavacNode classNode = annotationNode.up().up();
		List<FieldInfo> fields = FieldFinder.findAnnotatedFields(classNode, annotationNode);
		addOnCreateMethod(classNode, fields);
		addOnSaveInstanceStateMethod(classNode, fields);
	}

	private void addOnCreateMethod(JavacNode classNode, List<FieldInfo> fields) {
		Statement[] assignments = new Statement[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			FieldInfo f = fields.get(i);
			assignments[i] = createAssignment("this." + f.getName(), createCall("savedInstanceState.get" + functionNameForField(f), createLiteral(f.getName())));
		}
		createMethod(PUBLIC, VOID, "onCreate", createParam("android.os.Bundle", "savedInstanceState"),
				createBlock(
						createIf(createNotEquals("savedInstanceState", createNull()),
								createBlock(assignments)
						),
						createExec(createCall("super.onCreate", "savedInstanceState"))
				)
		).inject(classNode);
	}

	private void addOnSaveInstanceStateMethod(JavacNode classNode, List<FieldInfo> fields) {
		Statement[] stores = new Statement[fields.size() + 1];
		for (int i = 0; i < fields.size(); i++) {
			FieldInfo f = fields.get(i);
			stores[i] = createExec(createCall("outState.put" + functionNameForField(f), createLiteral(f.getName()), "this." + f.getName()));
		}
		stores[fields.size()] = createExec(createCall("super.onSaveInstanceState", "outState"));
		createMethod(PUBLIC, VOID, "onSaveInstanceState", createParam("android.os.Bundle", "outState"),
				createBlock(stores)
		).inject(classNode);
	}
	
	private String functionNameForField(FieldInfo field) {
		if ("java.lang.String".equals(field.getType().tsym.toString())) {
			return "String";
		}
		return "Int";
	}
}
