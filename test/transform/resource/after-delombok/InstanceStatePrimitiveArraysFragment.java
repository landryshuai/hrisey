import android.app.Fragment;

public class InstanceStatePrimitiveArraysFragment extends Fragment {
	
	private boolean[] myBools;
	private byte[] myBytes;
	private char[] myChars;
	private double[] myDoubles;
	private float[] myFloats;
	private int[] myInts;
	private long[] myLongs;
	private short[] myShorts;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myShorts = savedInstanceState.getShortArray("myShorts");
		}
		if (savedInstanceState != null) {
			this.myLongs = savedInstanceState.getLongArray("myLongs");
		}
		if (savedInstanceState != null) {
			this.myInts = savedInstanceState.getIntArray("myInts");
		}
		if (savedInstanceState != null) {
			this.myFloats = savedInstanceState.getFloatArray("myFloats");
		}
		if (savedInstanceState != null) {
			this.myDoubles = savedInstanceState.getDoubleArray("myDoubles");
		}
		if (savedInstanceState != null) {
			this.myChars = savedInstanceState.getCharArray("myChars");
		}
		if (savedInstanceState != null) {
			this.myBytes = savedInstanceState.getByteArray("myBytes");
		}
		if (savedInstanceState != null) {
			this.myBools = savedInstanceState.getBooleanArray("myBools");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putShortArray("myShorts", this.myShorts);
		outState.putLongArray("myLongs", this.myLongs);
		outState.putIntArray("myInts", this.myInts);
		outState.putFloatArray("myFloats", this.myFloats);
		outState.putDoubleArray("myDoubles", this.myDoubles);
		outState.putCharArray("myChars", this.myChars);
		outState.putByteArray("myBytes", this.myBytes);
		outState.putBooleanArray("myBools", this.myBools);
		super.onSaveInstanceState(outState);
	}
}
