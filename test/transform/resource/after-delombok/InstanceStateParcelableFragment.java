import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

public class InstanceStateParcelableFragment extends Fragment {
	
	private MyParcelable myParcelable;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.myParcelable = savedInstanceState.getParcelable("myParcelable");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public void onSaveInstanceState(android.os.Bundle outState) {
		outState.putParcelable("myParcelable", this.myParcelable);
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
