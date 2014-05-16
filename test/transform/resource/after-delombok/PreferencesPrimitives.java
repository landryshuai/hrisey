class PrefsClass {
	private final boolean myBool = true;
	private final float myFloat = 0.5F;
	private final int myInt = 666;
	private final long myLong = 100000000000000000L;
	private final String myString = "mg6";
	private android.content.SharedPreferences __prefs;
	
	@java.lang.SuppressWarnings("all")
	public PrefsClass(android.content.SharedPreferences prefs) {
		this.__prefs = prefs;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean getMyBool() {
		return this.__prefs.getBoolean("myBool", this.myBool);
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyBool(boolean myBool) {
		this.__prefs.edit().putBoolean("myBool", myBool).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyBool() {
		return this.__prefs.contains("myBool");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyBool() {
		this.__prefs.edit().remove("myBool").apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public float getMyFloat() {
		return this.__prefs.getFloat("myFloat", this.myFloat);
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyFloat(float myFloat) {
		this.__prefs.edit().putFloat("myFloat", myFloat).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyFloat() {
		return this.__prefs.contains("myFloat");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyFloat() {
		this.__prefs.edit().remove("myFloat").apply();
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
	
	@java.lang.SuppressWarnings("all")
	public long getMyLong() {
		return this.__prefs.getLong("myLong", this.myLong);
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyLong(long myLong) {
		this.__prefs.edit().putLong("myLong", myLong).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyLong() {
		return this.__prefs.contains("myLong");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyLong() {
		this.__prefs.edit().remove("myLong").apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public String getMyString() {
		return this.__prefs.getString("myString", this.myString);
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyString(String myString) {
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
