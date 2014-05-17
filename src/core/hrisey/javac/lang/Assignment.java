package hrisey.javac.lang;

import static lombok.javac.handlers.JavacHandlerUtil.chainDotsString;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCStatement;

public class Assignment extends Statement {

	private final String left;
	private final String right;
	
	public Assignment(String left, String right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public JCStatement create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		JCStatement assignment = maker.Exec(maker.Assign(chainDotsString(node, left), chainDotsString(node, right)));
		return assignment;
	}
}
