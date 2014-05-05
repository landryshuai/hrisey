import java.util.LinkedList;

class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	LinkedList<String> myList;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		if (this.myList == null) {
			dest.writeInt(-1);
		} else {
			dest.writeInt(this.myList.size());
			for (java.lang.String __loopVar1 : this.myList) {
				dest.writeString(__loopVar1);
			}
		}
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		{
			int __sizeVar1 = source.readInt();
			if (__sizeVar1 == -1) {
				this.myList = null;
			} else {
				this.myList = new java.util.LinkedList<java.lang.String>();
				while (__sizeVar1 != 0) {
					java.lang.String __childVar1;
					__childVar1 = source.readString();
					this.myList.add(__childVar1);
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