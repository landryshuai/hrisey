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

public class StatementCreator {
	
	public static Assignment createAssignment(String left, String right) {
		return new Assignment(new DottedExpression(left), new DottedExpression(right));
	}
	
	public static Assignment createAssignment(String left, Expression right) {
		return new Assignment(new DottedExpression(left), right);
	}
	
	public static Return createReturn(Expression expression) {
		return new Return(expression);
	}
	
	public static Execution createExec(Expression expression) {
		return new Execution(expression);
	}
	
	public static Variable createVariable(String typeName, String varName, Expression assignment) {
		return new Variable(typeName, varName, assignment);
	}
	
	public static Variable createVariable(String typeName, String varName) {
		return new Variable(typeName, varName, null);
	}
	
	public static If createIf(Expression booleanExpression, Statement ifTrue, Statement ifFalse) {
		return new If(booleanExpression, ifTrue, ifFalse);
	}
}
