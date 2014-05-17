package hrisey.javac.lang;

public class ParameterCreator {
	
	public static Parameter createParam(String typeName, String varName) {
		return new Parameter(new DottedExpression(typeName), varName);
	}
	
	public static Parameter createParam(Primitive typeName, String varName) {
		return new Parameter(typeName, varName);
	}
}
