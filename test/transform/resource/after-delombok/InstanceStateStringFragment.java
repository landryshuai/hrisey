import android.app.Fragment;

public class InstanceStateStringFragment extends Fragment {
	
	private String myString;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myString = savedInstanceState.getString("myString");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putString("myString", this.myString);
		super.onSaveInstanceState(outState);
	}
}
