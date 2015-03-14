//CONF: lombok.addGeneratedAnnotation = false
import android.app.Fragment;
import android.os.Bundle;

public class InstanceStateStringAndCharSequenceArraysFragment extends Fragment {
	
	@hrisey.InstanceState
	private String[] myStrings;
	@hrisey.InstanceState
	private CharSequence[] mySequences;
}
