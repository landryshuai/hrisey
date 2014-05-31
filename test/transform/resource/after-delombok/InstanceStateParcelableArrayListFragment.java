import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class InstanceStateParcelableArrayListFragment extends Fragment {
	
	private ArrayList<MyParcelable> myParcelableList;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myParcelableList = savedInstanceState.getParcelableArrayList("myParcelableList");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putParcelableArrayList("myParcelableList", this.myParcelableList);
		super.onSaveInstanceState(outState);
	}
}

class MyParcelable implements Parcelable {
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
}
