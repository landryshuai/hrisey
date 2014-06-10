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

import static hrisey.javac.lang.EmptyList.*;
import static lombok.javac.handlers.JavacHandlerUtil.injectType;

import java.util.List;

import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;

public class Classs {
	
	private final List<Modifier> modifiers;
	private final String name;
	
	public Classs(List<Modifier> modifiers, String name) {
		this.modifiers = modifiers;
		this.name = name;
	}
	
	public JavacNode inject(JavacNode parentClass) {
		JavacTreeMaker maker = parentClass.getTreeMaker();
		JCClassDecl classDecl = maker.ClassDef(
				Modifier.toJavac(maker, modifiers),
				parentClass.toName(name),
				emptyTypeParameters(),
				null,
				emptyExpressions(),
				emptyTrees());
		return injectType(parentClass, classDecl);
	}
}
