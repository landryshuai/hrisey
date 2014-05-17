package hrisey.javac.lang;

import java.util.List;

import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCModifiers;

public enum Modifier {
	
	PUBLIC(Flags.PUBLIC),
	PRIVATE(Flags.PRIVATE),
	FINAL(Flags.FINAL);
	
	public static JCModifiers toJavac(JavacTreeMaker maker, List<Modifier> modifiers) {
		long mods = 0;
		for (Modifier modifier : modifiers) {
			mods |= modifier.javac;
		}
		return maker.Modifiers(mods);
	}
	
	private long javac;
	
	private Modifier(long javac) {
		this.javac = javac;
	}
}
