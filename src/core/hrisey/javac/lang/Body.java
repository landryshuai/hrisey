package hrisey.javac.lang;

import java.util.List;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.ListBuffer;

public class Body {
	
	private final List<Statement> statements;

	public Body(List<Statement> statements) {
		this.statements = statements;
	}

	public JCBlock create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		ListBuffer<JCStatement> list = new ListBuffer<JCStatement>();
		for (Statement statement : statements) {
			list.add(statement.create(node));
		}
		return maker.Block(0, list.toList());
	}
}
