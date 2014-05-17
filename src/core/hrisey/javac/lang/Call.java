package hrisey.javac.lang;

import java.util.List;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.ListBuffer;

public class Call extends Expression {

	private final Expression method;
	private final List<Expression> expressions;

	public Call(Expression method, List<Expression> expressions) {
		this.method = method;
		this.expressions = expressions;
	}
	
	@Override
	public JCExpression create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		ListBuffer<JCExpression> list = new ListBuffer<JCExpression>();
		for (Expression expression : expressions) {
			list.add(expression.create(node));
		}
		return maker.Apply(null, method.create(node), list.toList());
	}
}
