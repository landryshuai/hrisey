class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	Boolean myBool;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		if (this.myBool == null) {
			dest.writeInt(-1);
		} else if (Boolean.FALSE.equals(this.myBool)) {
			dest.writeInt(0);
		} else {
			dest.writeInt(1);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		{
			int __boolValue = source.readInt();
			if (__boolValue == -1) {
				this.myBool = null;
			} else if (__boolValue == 0) {
				this.myBool = Boolean.FALSE;
			} else {
				this.myBool = Boolean.TRUE;
			}
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