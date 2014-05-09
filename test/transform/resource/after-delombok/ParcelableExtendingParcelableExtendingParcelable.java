import android.os.Parcel;
import android.os.Parcelable;

class ParcelableClass extends ParcelableBaseClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		super(source);
	}
	
	@java.lang.SuppressWarnings("all")
	private static class CreatorImpl implements android.os.Parcelable.Creator<ParcelableClass> {
		
		@java.lang.SuppressWarnings("all")
		public ParcelableClass createFromParcel(android.os.Parcel source) {
			return new ParcelableClass(source);
		}
		
		@java.lang.SuppressWarnings("all")
		public ParcelableClass[] newArray(int size) {
			return new ParcelableClass[size];
		}
	}
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
