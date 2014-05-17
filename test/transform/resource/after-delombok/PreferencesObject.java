import java.util.List;

class PrefsClass {
	private final android.content.SharedPreferences __prefs;
	private final com.google.gson.Gson __gson;
	private MyObj myObj;
	
	@java.lang.SuppressWarnings("all")
	public PrefsClass(android.content.SharedPreferences prefs, com.google.gson.Gson gson) {
		this.__prefs = prefs;
		this.__gson = gson;
	}
	
	@java.lang.SuppressWarnings("all")
	public MyObj getMyObj() {
		String myObjString = this.__prefs.getString("myObj", "DEFAULT");
		if (myObjString == null) {
			return null;
		} else if (myObjString.equals("DEFAULT")) {
			return this.myObj;
		} else {
			return this.__gson.fromJson(myObjString, MyObj.class);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyObj(MyObj myObj) {
		String myObjString = __gson.toJson(myObj);
		this.__prefs.edit().putString("myObj", myObjString).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyObj() {
		return this.__prefs.contains("myObj");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyObj() {
		this.__prefs.edit().remove("myObj").apply();
	}
}

final class MyObj {
	int myInt;
	List<String> myStrings;
}
