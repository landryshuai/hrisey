import android.app.Activity;

public class InstanceStateIntActivity extends Activity {
	
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	protected void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myInt = savedInstanceState.getInt("myInt");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	protected void onSaveInstanceState(android.os.Bundle outState) {
		outState.putInt("myInt", this.myInt);
		super.onSaveInstanceState(outState);
	}
}
