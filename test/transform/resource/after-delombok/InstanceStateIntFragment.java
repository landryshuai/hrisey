import android.app.Fragment;

public class InstanceStateIntFragment extends Fragment {
	
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myInt = savedInstanceState.getInt("myInt");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putInt("myInt", this.myInt);
		super.onSaveInstanceState(outState);
	}
}
