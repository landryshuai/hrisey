class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	Integer myInt;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		if (this.myInt == null) {
			dest.writeInt(0);
		} else {
			dest.writeInt(1);
			dest.writeInt(this.myInt);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		if (source.readInt() == 0) {
			this.myInt = null;
		} else {
			this.myInt = source.readInt();
		}
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