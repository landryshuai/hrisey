class PrefsClass {
	private final com.google.gson.Gson __gson;
	private final android.content.SharedPreferences __prefs;
	private static final char[] myArray = {'A', 'B', 'C'};
	
	@java.lang.SuppressWarnings("all")
	public PrefsClass(android.content.SharedPreferences prefs, com.google.gson.Gson gson) {
		this.__prefs = prefs;
		this.__gson = gson;
	}
	
	@java.lang.SuppressWarnings("all")
	public char[] getMyArray() {
		java.lang.String myArrayString = this.__prefs.getString("myArray", "DEFAULT");
		if (myArrayString == null) {
			return null;
		} else if (myArrayString.equals("DEFAULT")) {
			return this.myArray;
		} else {
			return this.__gson.fromJson(myArrayString, char[].class);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyArray(char[] myArray) {
		java.lang.String myArrayString;
		if (myArray == null) {
			myArrayString = null;
		} else {
			myArrayString = this.__gson.toJson(myArray);
		}
		this.__prefs.edit().putString("myArray", myArrayString).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyArray() {
		return this.__prefs.contains("myArray");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyArray() {
		this.__prefs.edit().remove("myArray").apply();
	}
}
