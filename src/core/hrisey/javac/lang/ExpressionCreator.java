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

import java.util.Arrays;

public class ExpressionCreator {
	
	public static Literal createLiteral(String stringValue) {
		return new Literal(stringValue);
	}
	
	public static Call createCall(Expression method, Expression... arguments) {
		return new Call(method, Arrays.asList(arguments));
	}
	
	public static Call createCall(String method) {
		return createCall(new DottedExpression(method));
	}
	
	public static Call createCall(String method, Expression argument1) {
		return createCall(new DottedExpression(method), argument1);
	}
	
	public static Call createCall(String method, Expression argument1, String argument2) {
		return createCall(new DottedExpression(method), argument1, new DottedExpression(argument2));
	}
	
	public static Call createCall(Expression method, Expression argument1, String argument2) {
		return createCall(method, argument1, new DottedExpression(argument2));
	}
	
	public static Select createSelect(Expression expression, String selector) {
		return new Select(expression, selector);
	}
}
