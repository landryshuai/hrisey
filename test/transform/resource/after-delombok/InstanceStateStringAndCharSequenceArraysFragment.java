import android.app.Fragment;
import android.os.Bundle;

public class InstanceStateStringAndCharSequenceArraysFragment extends Fragment {
	
	private String[] myStrings;
	private CharSequence[] mySequences;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.mySequences = savedInstanceState.getCharSequenceArray("mySequences");
		}
		if (savedInstanceState != null) {
			this.myStrings = savedInstanceState.getStringArray("myStrings");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putCharSequenceArray("mySequences", this.mySequences);
		outState.putStringArray("myStrings", this.myStrings);
		super.onSaveInstanceState(outState);
	}
}
