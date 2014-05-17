package hrisey.javac.lang;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCStatement;

public class Execution extends Statement {
	
	private final Expression expression;

	public Execution(Expression expression) {
		this.expression = expression;
	}

	@Override
	public JCStatement create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		return maker.Exec(expression.create(node));
	}
}
