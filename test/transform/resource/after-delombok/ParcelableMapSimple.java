import java.util.Map;

class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	Map<String, String> myMap;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		if (this.myMap == null) {
			dest.writeInt(-1);
		} else {
			dest.writeInt(this.myMap.size());
			for (java.util.Map.Entry<java.lang.String, java.lang.String> __loopVar1 : this.myMap.entrySet()) {
				java.lang.String __keyVar1 = __loopVar1.getKey();
				java.lang.String __valueVar1 = __loopVar1.getValue();
				dest.writeString(__keyVar1);
				dest.writeString(__valueVar1);
			}
		}
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		{
			int __sizeVar1 = source.readInt();
			if (__sizeVar1 == -1) {
				this.myMap = null;
			} else {
				this.myMap = new java.util.HashMap<java.lang.String, java.lang.String>();
				while (__sizeVar1 != 0) {
					java.lang.String __keyVar1;
					java.lang.String __valueVar1;
					__keyVar1 = source.readString();
					__valueVar1 = source.readString();
					this.myMap.put(__keyVar1, __valueVar1);
					--__sizeVar1;
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