package com.example.mynews.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {
	private static BitmapUtils bitmapUtils;

	private BitmapUtils() {

	}

	public static BitmapUtils getInstance() {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils();
		}
		return bitmapUtils;
	}

	public Bitmap decodeByteArray(byte[] data) {
		Bitmap bitmap = null;
		Options options = new Options();
		options.inSampleSize = 1;
		try {
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		} catch (OutOfMemoryError e) {
			options.inSampleSize += 2;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		}
		options = null;
		return bitmap;
	}

	public Bitmap decodeFile(String pathName) {
		Bitmap bitmap = null;
		Options options = new Options();
		options.inSampleSize = 1;
		try {
			bitmap = BitmapFactory.decodeFile(pathName, options);
		} catch (OutOfMemoryError e) {
			options.inSampleSize += 2;
			bitmap = BitmapFactory.decodeFile(pathName, options);
		}
		options = null;
		return bitmap;
	}

	public Bitmap decodeResource(Context context, int resId) {
		Bitmap bitmap = null;
		Options options = new Options();
		Resources res = context.getResources();
		options.inSampleSize = 1;
		try {
			bitmap = BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			options.inSampleSize += 2;
			bitmap = BitmapFactory.decodeResource(res, resId, options);
		}
		options = null;
		res = null;
		return bitmap;
	}

}
