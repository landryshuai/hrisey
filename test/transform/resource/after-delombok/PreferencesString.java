class PrefsClass {
	private final android.content.SharedPreferences __prefs;
	private String myString;
	
	@java.lang.SuppressWarnings("all")
	public PrefsClass(android.content.SharedPreferences prefs) {
		this.__prefs = prefs;
	}
	
	@java.lang.SuppressWarnings("all")
	public java.lang.String getMyString() {
		return this.__prefs.getString("myString", this.myString);
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyString(java.lang.String myString) {
		this.__prefs.edit().putString("myString", myString).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyString() {
		return this.__prefs.contains("myString");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyString() {
		this.__prefs.edit().remove("myString").apply();
	}
}
