class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	int[][] myArray;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		if (this.myArray == null) {
			dest.writeInt(-1);
		} else {
			dest.writeInt(this.myArray.length);
			for (int[] __loopVar1 : this.myArray) {
				dest.writeIntArray(__loopVar1);
			}
		}
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		{
			int __sizeVar1 = source.readInt();
			if (__sizeVar1 == -1) {
				this.myArray = null;
			} else {
				this.myArray = new int[__sizeVar1][];
				for (int __iterVar1 = 0; __iterVar1 != __sizeVar1; ++__iterVar1) {
					this.myArray[__iterVar1] = source.createIntArray();
				}
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