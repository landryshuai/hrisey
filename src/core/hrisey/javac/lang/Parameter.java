package hrisey.javac.lang;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class Parameter {
	
	private final Expression typeName;
	private final String varName;
	
	Parameter(Expression typeName, String varName) {
		this.typeName = typeName;
		this.varName = varName;
	}

	public JCVariableDecl create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		JCVariableDecl parameter = maker.VarDef(maker.Modifiers(0), node.toName(varName), typeName.create(node), null);
		return parameter;
	}
}
