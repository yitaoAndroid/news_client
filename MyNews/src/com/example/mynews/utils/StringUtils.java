package com.example.mynews.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class StringUtils {

	@SuppressLint("SimpleDateFormat")
	public static String parseDate_phoneLocation_api(long timeMinis) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = dateFormat.format(timeMinis);
		return date;
	}
}
