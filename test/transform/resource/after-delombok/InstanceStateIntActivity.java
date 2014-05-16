import android.app.Activity;
import android.os.Bundle;

public class InstanceStateIntActivity extends Activity {
	
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	protected void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myInt = savedInstanceState.getInt("myInt");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("myInt", this.myInt);
		super.onSaveInstanceState(outState);
	}
}
