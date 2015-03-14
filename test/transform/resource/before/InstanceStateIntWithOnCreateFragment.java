//CONF: lombok.addGeneratedAnnotation = false
import android.app.Fragment;
import android.os.Bundle;

public class InstanceStateIntWithOnCreateFragment extends Fragment {
	
	@hrisey.InstanceState
	private int myInt;
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
	}
}
