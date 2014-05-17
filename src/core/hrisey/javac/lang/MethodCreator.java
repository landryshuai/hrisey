package hrisey.javac.lang;

import java.util.Collections;

public class MethodCreator {

	public static Method createMethod(Modifier modifier, Primitive returnType, String name, Body body) {
		return new Method(Collections.singletonList(modifier), returnType, name, Collections.<Parameter>emptyList(), body);
	}

	public static Method createMethod(Modifier modifier, Primitive returnType, String name, Parameter parameter, Body body) {
		return new Method(Collections.singletonList(modifier), returnType, name, Collections.singletonList(parameter), body);
	}
}
