package hrisey.javac.lang;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCLiteral;

public class Literal extends Expression {
	
	private Object value;
	
	public Literal(String stringValue) {
		this.value = stringValue;
	}

	@Override
	public JCLiteral create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		return maker.Literal(value);
	}
}
