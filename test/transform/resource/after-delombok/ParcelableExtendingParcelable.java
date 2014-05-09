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

class ParcelableBaseClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableBaseClass> CREATOR = new CreatorImpl();
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableBaseClass(android.os.Parcel source) {
	}
	
	@java.lang.SuppressWarnings("all")
	private static class CreatorImpl implements android.os.Parcelable.Creator<ParcelableBaseClass> {
		
		@java.lang.SuppressWarnings("all")
		public ParcelableBaseClass createFromParcel(android.os.Parcel source) {
			return new ParcelableBaseClass(source);
		}
		
		@java.lang.SuppressWarnings("all")
		public ParcelableBaseClass[] newArray(int size) {
			return new ParcelableBaseClass[size];
		}
	}
}
