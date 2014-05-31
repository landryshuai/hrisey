import android.app.Fragment;
import android.os.Bundle;

public class InstanceStateCharSequenceFragment extends Fragment {
	
	private CharSequence mySeq;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.mySeq = savedInstanceState.getCharSequence("mySeq");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putCharSequence("mySeq", this.mySeq);
		super.onSaveInstanceState(outState);
	}
}
