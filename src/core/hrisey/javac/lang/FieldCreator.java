package hrisey.javac.lang;

import java.util.Arrays;
import java.util.Collections;

public class FieldCreator {
	
	public static Field createField(Modifier modifier, String typeName, String varName) {
		return new Field(Collections.singletonList(modifier), typeName, varName);
	}
	
	public static Field createField(Modifier modifier1, Modifier modifier2, String typeName, String varName) {
		return new Field(Arrays.asList(modifier1, modifier2), typeName, varName);
	}
}
