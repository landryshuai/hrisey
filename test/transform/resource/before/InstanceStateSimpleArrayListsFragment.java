//CONF: lombok.addGeneratedAnnotation = false
import android.app.Fragment;
import android.os.Bundle;
import java.util.ArrayList;

public class InstanceStateSimpleArrayListsFragment extends Fragment {
	
	@hrisey.InstanceState
	private ArrayList<String> myStringList;
	@hrisey.InstanceState
	private ArrayList<CharSequence> mySequenceList;
	@hrisey.InstanceState
	private ArrayList<Integer> myIntegerList;
}
