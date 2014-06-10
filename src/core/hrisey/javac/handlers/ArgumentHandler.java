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
import static hrisey.javac.lang.ConstructorCreator.createConstructor;
import static hrisey.javac.lang.ClassCreator.createClass;
import static hrisey.javac.lang.ExpressionCreator.*;
import static hrisey.javac.lang.FieldCreator.createField;
import static hrisey.javac.lang.MethodCreator.createMethod;
import static hrisey.javac.lang.Modifier.*;
import static hrisey.javac.lang.ParameterCreator.createParam;
import static hrisey.javac.lang.Primitive.*;
import static hrisey.javac.lang.StatementCreator.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import hrisey.Argument;
import hrisey.javac.handlers.util.FieldInfo;
import hrisey.javac.lang.DottedExpression;
import hrisey.javac.lang.Statement;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;

import org.mangosdk.spi.ProviderFor;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

@ProviderFor(JavacAnnotationHandler.class)
public class ArgumentHandler extends JavacAnnotationHandler<Argument> {
	
	@Override
	public void handle(AnnotationValues<Argument> annotation, JCAnnotation ast, JavacNode annotationNode) {
		deleteAnnotationIfNeccessary(annotationNode, Argument.class);
		deleteImportFromCompilationUnit(annotationNode, Argument.class.getName());

		JavacNode classNode = annotationNode.up().up();
		FieldInfo fieldInfo = new FieldInfo(annotationNode.up());
		JCMethodDecl onCreate = InstanceStateHandler.getOnCreateMethod(classNode);
		if (onCreate == null) {
			addOnCreateMethod(classNode, fieldInfo);
		}
		addBuilderStaticMethod(classNode);
		JavacNode builderNode = addBuilderClass(classNode);
		addFields(builderNode);
		addPrivateConstructor(builderNode);
		addFluentSetters(builderNode);
		addBuildMethod(builderNode);
	}

	private void addOnCreateMethod(JavacNode classNode, FieldInfo f) {
		Statement assignment = createAssignment("this." + f.getName(), createCall("args.getInt", createLiteral(f.getName())));
		createMethod(PUBLIC, VOID, "onCreate", createParam("android.os.Bundle", "savedInstanceState"),
				createBlock(
						createBlock(
								createVariable("android.os.Bundle", "args", createCall("getArguments")),
								assignment
						),
						createExec(createCall("super.onCreate", "savedInstanceState"))
				)
		).inject(classNode);
	}
	
	private void addBuilderStaticMethod(JavacNode classNode) {
		createMethod(PUBLIC, STATIC, "Builder", "builder",
				createBlock(
						createReturn(createNewInstance("Builder"))
				)
		).inject(classNode);
	}
	
	private JavacNode addBuilderClass(JavacNode classNode) {
		return createClass(PUBLIC, STATIC, FINAL, "Builder")
				.inject(classNode);
	}
	
	private void addFields(JavacNode builderNode) {
		createField(PRIVATE, INT, "myInt").inject(builderNode);
		createField(PRIVATE, BOOLEAN, "myIntCalled").inject(builderNode);
	}
	
	private void addPrivateConstructor(JavacNode builderNode) {
		createConstructor(
				createBlock()
		).inject(builderNode);
	}
	
	private void addFluentSetters(JavacNode builderNode) {
		createMethod(PUBLIC, "Builder", "myInt", createParam(INT, "myInt"),
				createBlock(
						createAssignment("this.myInt", "myInt"),
						createAssignment("this.myIntCalled", createLiteral(true)),
						createReturn(new DottedExpression("this"))
				)
		).inject(builderNode);
	}
	
	private void addBuildMethod(JavacNode builderNode) {
		Statement throwStatement = createThrow(createNewInstance("java.lang.IllegalStateException", createLiteral("myInt is required")));
		createMethod(PUBLIC, "ArgumentIntFragment", "build",
				createBlock(
						createIf(createNot("this.myIntCalled"), createBlock(throwStatement)),
						createVariable("ArgumentIntFragment", "fragment", createNewInstance("ArgumentIntFragment")),
						createVariable("android.os.Bundle", "args", createNewInstance("android.os.Bundle")),
						createExec(createCall("args.putInt", createLiteral("myInt"), "this.myInt")),
						createExec(createCall("fragment.setArguments", "args")),
						createReturn("fragment")
				)
		).inject(builderNode);
	}
}
