package hrisey.javac.lang;

public class StatementCreator {
	
	public static Assignment createAssignment(String left, String right) {
		return new Assignment(left, right);
	}
	
	public static Return createReturn(Expression expression) {
		return new Return(expression);
	}
	
	public static Execution createExec(Expression expression) {
		return new Execution(expression);
	}
}
