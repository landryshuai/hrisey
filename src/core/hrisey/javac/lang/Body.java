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
