//CONF: lombok.addGeneratedAnnotation = false
import hrisey.InstanceState;
import android.app.Fragment;

public class InstanceStatePrimitiveArraysFragment extends Fragment {
	
	@InstanceState
	private boolean[] myBools;
	@InstanceState
	private byte[] myBytes;
	@InstanceState
	private char[] myChars;
	@InstanceState
	private double[] myDoubles;
	@InstanceState
	private float[] myFloats;
	@InstanceState
	private int[] myInts;
	@InstanceState
	private long[] myLongs;
	@InstanceState
	private short[] myShorts;
}
