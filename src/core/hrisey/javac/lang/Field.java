package hrisey.javac.lang;

import static lombok.javac.handlers.JavacHandlerUtil.*;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

public class Field {
	
	private final List<Modifier> modifiers;
	private final String typeName;
	private final String varName;
	
	Field(List<Modifier> modifiers, String typeName, String varName) {
		this.modifiers = modifiers;
		this.typeName = typeName;
		this.varName = varName;
	}

	public void inject(JavacNode classNode) {
		JavacTreeMaker maker = classNode.getTreeMaker();
		JCVariableDecl field = maker.VarDef(Modifier.toJavac(maker, modifiers), classNode.toName(varName), chainDotsString(classNode, typeName), null);
		injectField(classNode, field);
	}
}
