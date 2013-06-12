package edu.ustb.mirror;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MoreActivity extends Activity {
	private String TAG = "SETTING";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
	}

	public void start_activity(View view) {

		if (view.getId() == R.id.feedback_btn) { // 建议反馈
			this.startActivity(new Intent(this, FeedbackActivity.class));
			this.overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);
		} else if (view.getId() == R.id.contact_btn) { // 联系我们
			this.startActivity(new Intent(this, AboutActivity.class));
			this.overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);
		} else if (view.getId() == R.id.user_guide_btn) { // 新手引导
			Intent intent = new Intent(this, GuideActivity.class);
			intent.putExtra("from", "setting");
			this.startActivity(intent);
			this.overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);
		} else if (view.getId() == R.id.personal_center_btn) { // 收藏

			Intent intent = new Intent(this, LikeActivity.class);
			this.startActivity(intent);
			this.overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);

		} else if (view.getId() == R.id.invite_friend_btn) { // 邀请好友
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_TEXT, "Share");
			startActivity(Intent.createChooser(intent, getTitle()));
		} else if (view.getId() == R.id.logout_btn) { // 退出
			Intent intent = new Intent(this, DayActivity.class);
			intent.putExtra("exit", true);
			this.setResult(RESULT_OK, intent);
			this.onBackPressed();
		} else if (view.getId() == R.id.header_back_btn) {
			this.onBackPressed();
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
