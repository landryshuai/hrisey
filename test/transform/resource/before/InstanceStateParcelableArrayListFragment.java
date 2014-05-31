import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class InstanceStateParcelableArrayListFragment extends Fragment {
	
	@hrisey.InstanceState
	private ArrayList<MyParcelable> myParcelableList;
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
