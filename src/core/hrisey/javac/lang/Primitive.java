package hrisey.javac.lang;

import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;

import lombok.javac.Javac;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.JavacTreeMaker.TypeTag;

public class Primitive extends Expression {
	
	public static final Primitive VOID = new Primitive(Javac.CTC_VOID);
	public static final Primitive BOOLEAN = new Primitive(Javac.CTC_BOOLEAN);
	public static final Primitive INT = new Primitive(Javac.CTC_INT);
	
	private final TypeTag javac;
	
	private Primitive(TypeTag javac) {
		this.javac = javac;
	}
	
	@Override
	public JCPrimitiveTypeTree create(JavacNode node) {
		JavacTreeMaker maker = node.getTreeMaker();
		return maker.TypeIdent(javac);
	}
}
