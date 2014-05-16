import android.app.Activity;
import android.os.Bundle;

public class InstanceStateIntWithOnCreateActivity extends Activity {
	
	private int myInt;
	
	@Override
	protected void onCreate(Bundle state) {
		if (state != null) {
			this.myInt = state.getInt("myInt");
		}
		super.onCreate(state);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
		state.putInt("myInt", this.myInt);
		super.onSaveInstanceState(state);
	}
}
