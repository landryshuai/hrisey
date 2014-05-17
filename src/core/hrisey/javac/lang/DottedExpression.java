package hrisey.javac.lang;

import static lombok.javac.handlers.JavacHandlerUtil.chainDotsString;
import lombok.javac.JavacNode;

import com.sun.tools.javac.tree.JCTree.JCExpression;

public class DottedExpression extends Expression {
	
	private final String dotted;

	public DottedExpression(String dotted) {
		this.dotted = dotted;
	}

	@Override
	public JCExpression create(JavacNode node) {
		return chainDotsString(node, dotted);
	}
}
