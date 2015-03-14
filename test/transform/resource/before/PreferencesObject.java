//CONF: lombok.addGeneratedAnnotation = false
import java.util.List;

@hrisey.Preferences
class PrefsClass {
	private MyObj myObj;
}

final class MyObj {
	int myInt;
	List<String> myStrings;
}
