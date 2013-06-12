package edu.ustb.mirror;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class FeedbackActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
	}

	public void start_activity(View view) {

		if (view.getId() == R.id.header_back_btn) {
			this.onBackPressed();
			return;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		this.overridePendingTransition(R.anim.slide_in_from_left,
				R.anim.slide_out_to_right);
	}
}
