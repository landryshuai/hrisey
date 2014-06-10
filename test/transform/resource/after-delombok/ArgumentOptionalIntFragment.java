import android.app.Fragment;

public class ArgumentOptionalIntFragment extends Fragment {
	
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	public void onCreate(android.os.Bundle savedInstanceState) {
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
		
		@java.lang.SuppressWarnings("all")
		Builder() {
		}
		
		@java.lang.SuppressWarnings("all")
		public Builder myInt(int myInt) {
			this.myInt = myInt;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public ArgumentOptionalIntFragment build() {
			ArgumentOptionalIntFragment fragment = new ArgumentOptionalIntFragment();
			android.os.Bundle args = new android.os.Bundle();
			args.putInt("myInt", this.myInt);
			fragment.setArguments(args);
			return fragment;
		}
	}
}
