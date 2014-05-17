package hrisey.javac.lang;

import java.util.Collections;

public class BodyCreator {
	
	public static Body createBody(Statement statement) {
		return new Body(Collections.singletonList(statement));
	}
}
