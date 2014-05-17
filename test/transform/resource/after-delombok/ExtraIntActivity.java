import android.app.Activity;
import android.os.Bundle;

public class ExtraIntActivity extends Activity {
	
	private int myInt;
	
	@java.lang.SuppressWarnings("all")
	protected void onCreate(Bundle savedInstanceState) {
		{
			android.os.Bundle extras = getIntent().getExtras();
			this.myInt = extras.getInt("myInt");
		}
		super.onCreate(savedInstanceState);
	}
	
	@java.lang.SuppressWarnings("all")
	public static IntentBuilder intent(android.content.Context context) {
		return new IntentBuilder(context);
	}
	
	public static final class IntentBuilder {
		
		private android.content.Context __context;
		private int myInt;
		private boolean myIntCalled;
		
		@java.lang.SuppressWarnings("all")
		IntentBuilder(android.content.Context context) {
			this.__context = context;
		}
		
		@java.lang.SuppressWarnings("all")
		public IntentBuilder myInt(int myInt) {
			this.myInt = myInt;
			this.myIntCalled = true;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public android.content.Intent get() {
			if (!myIntCalled) {
				throw new java.lang.IllegalStateException("myInt is required");
			}
			android.content.Intent intent = new android.content.Intent(this.__context, ExtraIntActivity.class);
			android.os.Bundle extras = new android.os.Bundle();
			extras.putInt("myInt", this.myInt);
			intent.putExtras(extras);
			return intent;
		}
		
		@java.lang.SuppressWarnings("all")
		public void start() {
			android.content.Intent intent = get();
			this.__context.startActivity(intent);
		}
	}
}
