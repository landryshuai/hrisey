package hrisey.javac.lang;

import lombok.javac.JavacNode;

import com.sun.tools.javac.tree.JCTree.JCStatement;

public abstract class Statement {
	
	public abstract JCStatement create(JavacNode node);
}
