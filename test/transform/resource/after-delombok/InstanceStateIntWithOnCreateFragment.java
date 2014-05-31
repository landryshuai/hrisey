import android.app.Fragment;
import android.os.Bundle;

public class InstanceStateIntWithOnCreateFragment extends Fragment {
	
	private int myInt;
	
	@Override
	public void onCreate(Bundle state) {
		if (state != null) {
			this.myInt = state.getInt("myInt");
		}
		super.onCreate(state);
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		state.putInt("myInt", this.myInt);
		super.onSaveInstanceState(state);
	}
}
