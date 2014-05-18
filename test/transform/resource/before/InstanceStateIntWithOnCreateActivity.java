// ignore

import android.app.Activity;
import android.os.Bundle;

public class InstanceStateIntWithOnCreateActivity extends Activity {
	
	@hrisey.InstanceState
	private int myInt;
	
	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
	}
}
