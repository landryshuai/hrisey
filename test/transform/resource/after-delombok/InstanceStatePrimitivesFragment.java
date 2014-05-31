import android.app.Fragment;

public class InstanceStatePrimitivesFragment extends Fragment {
	
	private boolean myBool;
	private byte myByte;
	private char myChar;
	private double myDouble;
	private float myFloat;
	private int myInt;
	private long myLong;
	private short myShort;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myShort = savedInstanceState.getShort("myShort");
		}
		if (savedInstanceState != null) {
			this.myLong = savedInstanceState.getLong("myLong");
		}
		if (savedInstanceState != null) {
			this.myInt = savedInstanceState.getInt("myInt");
		}
		if (savedInstanceState != null) {
			this.myFloat = savedInstanceState.getFloat("myFloat");
		}
		if (savedInstanceState != null) {
			this.myDouble = savedInstanceState.getDouble("myDouble");
		}
		if (savedInstanceState != null) {
			this.myChar = savedInstanceState.getChar("myChar");
		}
		if (savedInstanceState != null) {
			this.myByte = savedInstanceState.getByte("myByte");
		}
		if (savedInstanceState != null) {
			this.myBool = savedInstanceState.getBoolean("myBool");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putShort("myShort", this.myShort);
		outState.putLong("myLong", this.myLong);
		outState.putInt("myInt", this.myInt);
		outState.putFloat("myFloat", this.myFloat);
		outState.putDouble("myDouble", this.myDouble);
		outState.putChar("myChar", this.myChar);
		outState.putByte("myByte", this.myByte);
		outState.putBoolean("myBool", this.myBool);
		super.onSaveInstanceState(outState);
	}
}
