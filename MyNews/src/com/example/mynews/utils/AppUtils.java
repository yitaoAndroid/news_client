package com.example.mynews.utils;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AppUtils {
	public static void toAnotherApp(Context context,String pakage) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent=new Intent();
		intent =packageManager.getLaunchIntentForPackage(pakage);
		context.startActivity(intent);
	}
}
