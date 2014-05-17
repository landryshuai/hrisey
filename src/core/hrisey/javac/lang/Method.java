package hrisey.javac.lang;

import static hrisey.javac.lang.EmptyList.*;
import static lombok.javac.handlers.JavacHandlerUtil.injectMethod;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.ListBuffer;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

public class Method {
	
	private final List<Modifier> modifiers;
	private final Primitive returnType;
	private final String name;
	private final List<Parameter> parameters;
	private final Body body;

	public Method(List<Modifier> modifiers, List<Parameter> parameters, Body body) {
		this(modifiers, null, "<init>", parameters, body);
	}

	public Method(List<Modifier> modifiers, Primitive returnType, String name, List<Parameter> parameters, Body body) {
		this.modifiers = modifiers;
		this.returnType = returnType;
		this.name = name;
		this.parameters = parameters;
		this.body = body;
	}
	
	public void inject(JavacNode classNode) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		ListBuffer<JCVariableDecl> list = new ListBuffer<JCVariableDecl>();
		for (Parameter parameter : parameters) {
			list.add(parameter.create(classNode));
		}
		JCMethodDecl method = maker.MethodDef(
				Modifier.toJavac(maker, modifiers),
				classNode.toName(name),
				returnType != null ? returnType.create(classNode) : null,
				emptyTypeParameters(),
				list.toList(),
				emptyExpressions(),
				body.create(classNode),
				null);
		injectMethod(classNode, method);
	}
}
