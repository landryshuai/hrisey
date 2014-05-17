package hrisey.javac.lang;

import lombok.javac.JavacNode;

import com.sun.tools.javac.tree.JCTree.JCExpression;

public abstract class Expression {
	
	public abstract JCExpression create(JavacNode node);
}
