class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	byte myByte;
	double myDouble;
	float myFloat;
	int myInt;
	long myLong;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		dest.writeByte(this.myByte);
		dest.writeDouble(this.myDouble);
		dest.writeFloat(this.myFloat);
		dest.writeInt(this.myInt);
		dest.writeLong(this.myLong);
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		this.myByte = source.readByte();
		this.myDouble = source.readDouble();
		this.myFloat = source.readFloat();
		this.myInt = source.readInt();
		this.myLong = source.readLong();
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