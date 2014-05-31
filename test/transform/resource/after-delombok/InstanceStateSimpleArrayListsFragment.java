import android.app.Fragment;
import android.os.Bundle;
import java.util.ArrayList;

public class InstanceStateSimpleArrayListsFragment extends Fragment {
	
	private ArrayList<String> myStringList;
	private ArrayList<CharSequence> mySequenceList;
	private ArrayList<Integer> myIntegerList;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myIntegerList = savedInstanceState.getIntegerArrayList("myIntegerList");
		}
		if (savedInstanceState != null) {
			this.mySequenceList = savedInstanceState.getCharSequenceArrayList("mySequenceList");
		}
		if (savedInstanceState != null) {
			this.myStringList = savedInstanceState.getStringArrayList("myStringList");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putIntegerArrayList("myIntegerList", this.myIntegerList);
		outState.putCharSequenceArrayList("mySequenceList", this.mySequenceList);
		outState.putStringArrayList("myStringList", this.myStringList);
		super.onSaveInstanceState(outState);
	}
}
