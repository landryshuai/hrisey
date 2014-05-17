package hrisey.javac.lang;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.util.List;

public class EmptyList {
	
	public static List<JCExpression> emptyExpressions() {
		return List.<JCExpression>nil();
	}
	
	public static List<JCTypeParameter> emptyTypeParameters() {
		return List.<JCTypeParameter>nil();
	}
}
