import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

public class InstanceStateParcelableSparseArrayFragment extends Fragment {
	
	private SparseArray<MyParcelable> myParcelableSparseArray;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myParcelableSparseArray = savedInstanceState.getSparseParcelableArray("myParcelableSparseArray");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putSparseParcelableArray("myParcelableSparseArray", this.myParcelableSparseArray);
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
