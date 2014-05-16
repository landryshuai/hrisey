class PrefsClass {
	private double myDouble;
	private android.content.SharedPreferences __prefs;
	
	@java.lang.SuppressWarnings("all")
	public PrefsClass(android.content.SharedPreferences prefs) {
		this.__prefs = prefs;
	}
	
	@java.lang.SuppressWarnings("all")
	public double getMyDouble() {
		return Double.longBitsToDouble(this.__prefs.getLong("myDouble", Double.doubleToLongBits(this.myDouble)));
	}
	
	@java.lang.SuppressWarnings("all")
	public void setMyDouble(double myDouble) {
		this.__prefs.edit().putLong("myDouble", Double.doubleToLongBits(myDouble)).apply();
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean containsMyDouble() {
		return this.__prefs.contains("myDouble");
	}
	
	@java.lang.SuppressWarnings("all")
	public void removeMyDouble() {
		this.__prefs.edit().remove("myDouble").apply();
	}
}
