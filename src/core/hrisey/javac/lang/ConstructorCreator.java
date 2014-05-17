package hrisey.javac.lang;

import java.util.Collections;

public class ConstructorCreator {
	
	public static Method createConstructor(Modifier modifier, Parameter parameter, Body body) {
		return new Method(Collections.singletonList(modifier), Collections.singletonList(parameter), body);
	}
}
