class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	Byte myByte;
	Double myDouble;
	Float myFloat;
	Integer myInt;
	Long myLong;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		if (this.myByte == null) {
			dest.writeInt(0);
		} else {
			dest.writeInt(1);
			dest.writeByte(this.myByte);
		}
		if (this.myDouble == null) {
			dest.writeInt(0);
		} else {
			dest.writeInt(1);
			dest.writeDouble(this.myDouble);
		}
		if (this.myFloat == null) {
			dest.writeInt(0);
		} else {
			dest.writeInt(1);
			dest.writeFloat(this.myFloat);
		}
		if (this.myInt == null) {
			dest.writeInt(0);
		} else {
			dest.writeInt(1);
			dest.writeInt(this.myInt);
		}
		if (this.myLong == null) {
			dest.writeInt(0);
		} else {
			dest.writeInt(1);
			dest.writeLong(this.myLong);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		if (source.readInt() == 0) {
			this.myByte = null;
		} else {
			this.myByte = source.readByte();
		}
		if (source.readInt() == 0) {
			this.myDouble = null;
		} else {
			this.myDouble = source.readDouble();
		}
		if (source.readInt() == 0) {
			this.myFloat = null;
		} else {
			this.myFloat = source.readFloat();
		}
		if (source.readInt() == 0) {
			this.myInt = null;
		} else {
			this.myInt = source.readInt();
		}
		if (source.readInt() == 0) {
			this.myLong = null;
		} else {
			this.myLong = source.readLong();
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
