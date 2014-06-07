import android.app.Fragment;
import android.os.Bundle;

public class ArgumentIntFragment extends Fragment {
	
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(Bundle savedInstanceState) {
		{
			android.os.Bundle args = getArguments();
			this.myInt = args.getInt("myInt");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private int myInt;
		private boolean myIntCalled;
		
		@java.lang.SuppressWarnings("all")
		Builder() {
		}
		
		@java.lang.SuppressWarnings("all")
		public Builder myInt(int myInt) {
			this.myInt = myInt;
			this.myIntCalled = true;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public ArgumentIntFragment build() {
			if (!myIntCalled) {
				throw new java.lang.IllegalStateException("myInt is required");
			}
			ArgumentIntFragment fragment = new ArgumentIntFragment();
			android.os.Bundle args = new android.os.Bundle();
			args.putInt("myInt", this.myInt);
			fragment.setArguments(args);
			return fragment;
		}
	}
}
