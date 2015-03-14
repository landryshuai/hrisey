//CONF: lombok.addGeneratedAnnotation = false
import android.os.Parcel;
import android.os.Parcelable;

@hrisey.Parcelable
class ParcelableClass extends ParcelableBaseClass {
}

class ParcelableBaseClass extends ParcelableBaseBaseClass {
	
	protected ParcelableBaseClass(Parcel source) {
		super(source);
	}
	
	public static final Parcelable.Creator<ParcelableBaseClass> CREATOR = new Parcelable.Creator<ParcelableBaseClass>(){
		
		public ParcelableBaseClass createFromParcel(Parcel source) {
			return new ParcelableBaseClass(source);
		}
		
		public ParcelableBaseClass[] newArray(int size) {
			return new ParcelableBaseClass[size];
		}
	};
}

class ParcelableBaseBaseClass implements Parcelable {
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
	}
	
	protected ParcelableBaseBaseClass(Parcel source) {
	}
	
	public static final Parcelable.Creator<ParcelableBaseBaseClass> CREATOR = new Parcelable.Creator<ParcelableBaseBaseClass>(){
		
		public ParcelableBaseBaseClass createFromParcel(Parcel source) {
			return new ParcelableBaseBaseClass(source);
		}
		
		public ParcelableBaseBaseClass[] newArray(int size) {
			return new ParcelableBaseBaseClass[size];
		}
	};
}
