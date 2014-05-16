import android.app.Fragment;
import android.os.Bundle;

public class InstanceStateIntFragment extends Fragment {
	
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myInt = savedInstanceState.getInt("myInt");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt("myInt", this.myInt);
		super.onSaveInstanceState(outState);
	}
}
