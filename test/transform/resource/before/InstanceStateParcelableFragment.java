//CONF: lombok.addGeneratedAnnotation = false
import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

public class InstanceStateParcelableFragment extends Fragment {
	
	@hrisey.InstanceState
	private MyParcelable myParcelable;
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
