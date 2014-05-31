/*
 * Copyright (C) 2014 Maciej Gorski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hrisey.javac.lang;

import java.util.List;

import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCModifiers;

public enum Modifier {
	
	PUBLIC(Flags.PUBLIC),
	PROTECTED(Flags.PROTECTED),
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
