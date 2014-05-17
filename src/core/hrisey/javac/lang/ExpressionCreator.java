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
