package edu.ustb.mirror;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import edu.ustb.mirror.common.Global;

public class WelcomeActivity extends Activity {
	private String DEBUG_TAG = "WelcomeActivity";
	private SharedPreferences preferences;
	private boolean isGuest;
	private boolean isFirstLaunch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.preferences = this.getSharedPreferences(Global.PREF_ACCOUNT,
				MODE_PRIVATE);

		// 无title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_welcome);
	}

	@Override
	public void onResume() {
		super.onResume();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated method stub

				// 未登录
				Intent intent = null;

				// 进入介绍页
				intent = new Intent(WelcomeActivity.this, GuideActivity.class);
				intent.putExtra("from", "welcome");

				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
			}

		}).start();

	}
}
