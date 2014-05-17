package hrisey.javac.lang;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCFieldAccess;

public class Select extends Expression {
	
	private final Expression expression;
	private final String selector;

	public Select(Expression expression, String selector) {
		this.expression = expression;
		this.selector = selector;
	}

	@Override
	public JCFieldAccess create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		return maker.Select(expression.create(node), node.toName(selector));
	}
}
