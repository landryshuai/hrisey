import java.util.List;

class PrefsClass {
	private final android.content.SharedPreferences __prefs;
	private final com.google.gson.Gson __gson;
	private List<String> myList;
	
	@java.lang.SuppressWarnings("all")
	public PrefsClass(android.content.SharedPreferences prefs, com.google.gson.Gson gson) {
		this.__prefs = prefs;
		this.__gson = gson;
	}
	
	@java.lang.SuppressWarnings("all")
	public java.util.List<java.lang.String> getMyList() {
		java.lang.String myListString = this.__prefs.getString("myList", "DEFAULT");
		if (myListString == null) {
			return null;
		} else if (myListString.equals("DEFAULT")) {
			return this.myList;
		} else {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.List<java.lang.String>>(){
			}.getType();
			return this.__gson.fromJson(myListString, type);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyList(java.util.List<java.lang.String> myList) {
		String myListString;
		if (myList == null) {
			myListString = null;
		} else {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.List<java.lang.String>>(){
			}.getType();
			myListString = __gson.toJson(myList, type);
		}
		this.__prefs.edit().putString("myList", myListString).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyList() {
		return this.__prefs.contains("myList");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyList() {
		this.__prefs.edit().remove("myList").apply();
	}
}
