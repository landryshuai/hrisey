class PrefsClass {
	private final android.content.SharedPreferences __prefs;
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	public PrefsClass(android.content.SharedPreferences prefs) {
		this.__prefs = prefs;
	}
	
	@java.lang.SuppressWarnings("all")
	public int getMyInt() {
		return this.__prefs.getInt("myInt", this.myInt);
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyInt(int myInt) {
		this.__prefs.edit().putInt("myInt", myInt).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyInt() {
		return this.__prefs.contains("myInt");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyInt() {
		this.__prefs.edit().remove("myInt").apply();
	}
}
