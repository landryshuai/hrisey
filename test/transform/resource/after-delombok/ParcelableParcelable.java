class ParcelableParentClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableParentClass> CREATOR = new CreatorImpl();
	
	ParcelableClass myParcelable;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		dest.writeParcelable(this.myParcelable, flags);
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableParentClass(android.os.Parcel source) {
		this.myParcelable = source.readParcelable(ParcelableClass.class.getClassLoader());
	}
	
	@java.lang.SuppressWarnings("all")
	private static class CreatorImpl implements android.os.Parcelable.Creator<ParcelableParentClass> {
		
		@java.lang.SuppressWarnings("all")
		public ParcelableParentClass createFromParcel(android.os.Parcel source) {
			return new ParcelableParentClass(source);
		}
		
		@java.lang.SuppressWarnings("all")
		public ParcelableParentClass[] newArray(int size) {
			return new ParcelableParentClass[size];
		}
	}
}

class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
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
