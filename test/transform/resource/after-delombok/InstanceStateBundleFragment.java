import android.app.Fragment;
import android.os.Bundle;

public class InstanceStateBundleFragment extends Fragment {
	
	private Bundle myBundle;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myBundle = savedInstanceState.getBundle("myBundle");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putBundle("myBundle", this.myBundle);
		super.onSaveInstanceState(outState);
	}
}
