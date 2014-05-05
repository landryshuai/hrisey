import android.os.Bundle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

class ParcelableClass implements android.os.Parcelable {
	public static final android.os.Parcelable.Creator<ParcelableClass> CREATOR = new CreatorImpl();
	
	LinkedHashMap<char[][], List<TreeSet<Bundle>>> uberComplex;
	
	@java.lang.SuppressWarnings("all")
	public int describeContents() {
		return 0;
	}
	
	@java.lang.SuppressWarnings("all")
	public void writeToParcel(android.os.Parcel dest, int flags) {
		if (this.uberComplex == null) {
			dest.writeInt(-1);
		} else {
			dest.writeInt(this.uberComplex.size());
			for (java.util.Map.Entry<char[][], java.util.List<java.util.TreeSet<android.os.Bundle>>> __loopVar1 : this.uberComplex.entrySet()) {
				if (__loopVar1.getKey() == null) {
					dest.writeInt(-1);
				} else {
					dest.writeInt(__loopVar1.getKey().length);
					for (char[] __loopVar2 : __loopVar1.getKey()) {
						dest.writeCharArray(__loopVar2);
					}
				}
				if (__loopVar1.getValue() == null) {
					dest.writeInt(-1);
				} else {
					dest.writeInt(__loopVar1.getValue().size());
					for (java.util.TreeSet<android.os.Bundle> __loopVar2 : __loopVar1.getValue()) {
						if (__loopVar2 == null) {
							dest.writeInt(-1);
						} else {
							dest.writeInt(__loopVar2.size());
							for (android.os.Bundle __loopVar3 : __loopVar2) {
								dest.writeBundle(__loopVar3);
							}
						}
					}
				}
			}
		}
	}
	
	@java.lang.SuppressWarnings("all")
	protected ParcelableClass(android.os.Parcel source) {
		{
			int __sizeVar1 = source.readInt();
			if (__sizeVar1 == -1) {
				this.uberComplex = null;
			} else {
				this.uberComplex = new java.util.LinkedHashMap<char[][], java.util.List<java.util.TreeSet<android.os.Bundle>>>();
				while (__sizeVar1 != 0) {
					char[][] __keyVar1;
					java.util.List<java.util.TreeSet<android.os.Bundle>> __valueVar1;
					{
						int __sizeVar2 = source.readInt();
						if (__sizeVar2 == -1) {
							__keyVar1 = null;
						} else {
							__keyVar1 = new char[__sizeVar2][];
							for (int __iterVar2 = 0; __iterVar2 != __sizeVar2; ++__iterVar2) {
								__keyVar1[__iterVar2] = source.createCharArray();
							}
						}
					}
					{
						int __sizeVar2 = source.readInt();
						if (__sizeVar2 == -1) {
							__valueVar1 = null;
						} else {
							__valueVar1 = new java.util.ArrayList<java.util.TreeSet<android.os.Bundle>>();
							while (__sizeVar2 != 0) {
								java.util.TreeSet<android.os.Bundle> __childVar2;
								{
									int __sizeVar3 = source.readInt();
									if (__sizeVar3 == -1) {
										__childVar2 = null;
									} else {
										__childVar2 = new java.util.TreeSet<android.os.Bundle>();
										while (__sizeVar3 != 0) {
											android.os.Bundle __childVar3;
											__childVar3 = source.readBundle();
											__childVar2.add(__childVar3);
											--__sizeVar3;
										}
									}
								}
								__valueVar1.add(__childVar2);
								--__sizeVar2;
							}
						}
					}
					this.uberComplex.put(__keyVar1, __valueVar1);
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