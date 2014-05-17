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
