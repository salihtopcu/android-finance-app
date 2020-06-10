package com.gelistirmen.finance.util;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.DatePicker;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class Method {
    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isNullOrEmpty(String text) {
        if (text == null || text.isEmpty())
            return true;
        return false;
    }

    public static boolean isNotNullOrEmpty(String text) {
        return !isNullOrEmpty(text);
    }

    public static String getSdkVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return MyApplication.instance.getResources().getColor(id, null);
        else
            return MyApplication.instance.getResources().getColor(id);
    }

    public static Date createDate(String dateText, String formatText) {
        if (isNotNullOrEmpty(dateText)) {
            DateFormat format = new SimpleDateFormat(formatText);
            try {
                return format.parse(dateText);
            } catch (ParseException ex) {
                Log.e("Method / createdDate", ex.toString());
                return null;
            }
        } else {
            return null;
        }
    }

    public static String getFormatedDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getAndroidVersionName() {
        try {
            return MyApplication.instance.getPackageManager().getPackageInfo(MyApplication.instance.getPackageName(), 0).versionName;
        } catch (Exception e) {
            Log.e("Method/getAndVerNam", e.toString());
            return null;
        }
    }

    public static int getAndroidVersionCode() {
        try {
            return MyApplication.instance.getPackageManager().getPackageInfo(MyApplication.instance.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            Log.e("Method/getAndVerCod", e.toString());
            return 0;
        }
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(MyApplication.instance.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static void setApplicationLanguage(Context context, String code) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        if (code.toLowerCase().equals(Constants.LanguageCode.Turkish))
            conf.locale = new Locale("tr","TR");
        else
            conf.locale = Locale.ENGLISH;

        Locale.setDefault(conf.locale);
        res.updateConfiguration(conf, dm);
    }

    /**
     * Converts dp to pixel
     */
    public static int dpToPx(int dp, AppCompatActivity activity) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, activity.getResources().getDisplayMetrics()));
    }

    public static String formatDoubleAsString(Double value, int decimalLength) {
        String formatString = "#0";
        for (int i = 1; i <= decimalLength; i++) {
            if (i == 1)
                formatString += ".";
            formatString += "0";
        }
        return (new DecimalFormat(formatString)).format(value);
    }

    public static Double formatDoubleAsDouble(Double value, int decimalLength) {
        return Double.parseDouble(Method.formatDoubleAsString(value, decimalLength));
    }

    public static String formatDoubleAsAmount(Double value, int currency, Locale locale) {
        String format = "###,###,###,###" + (value > value.intValue() ? ".00" : "");
        return (new DecimalFormat(format, new DecimalFormatSymbols(locale))).format(value) + " " + Constants.Currency.getSymbolFor(currency);
    }

    public static Boolean exists(JSONObject jsonObject, String key) {
        if (jsonObject == null)
            return false;
        else if (!jsonObject.has(key))
            return false;
        else
            try {
                return (!jsonObject.getString(key).equals("null"));
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
    }

    public static int getInt(JSONObject jsonObject, String key, int defaultValue) throws JSONException {
        if (exists(jsonObject, key))
            return jsonObject.getInt(key);
        else
            return defaultValue;
    }

    public static int getInt(JSONObject jsonObject, String key) throws JSONException {
        return getInt(jsonObject, key, 0);
    }

    @Nullable
    public static String getString(JSONObject jsonObject, String key, String defaultValue) throws JSONException {
        if (exists(jsonObject, key))
            return jsonObject.getString(key);
        else
            return defaultValue;
    }

    @Nullable
    public static String getString(JSONObject jsonObject, String key) throws JSONException {
        return getString(jsonObject, key, "");
    }

    public static Double getDouble(JSONObject jsonObject, String key, Double defaultValue) throws JSONException {
        if (exists(jsonObject, key))
            return jsonObject.getDouble(key);
        else
            return defaultValue;
    }

    public static Double getDouble(JSONObject jsonObject, String key) throws JSONException {
        return getDouble(jsonObject, key, 0.0);
    }

    public static Boolean getBoolean(JSONObject jsonObject, String key, Boolean defaultValue) throws JSONException {
        if (exists(jsonObject, key))
            return jsonObject.getBoolean(key);
        else
            return defaultValue;
    }

    public static Boolean getBoolean(JSONObject jsonObject, String key) throws JSONException {
        return getBoolean(jsonObject, key, false);
    }

    @Nullable
    public static JSONObject getJsonObject(JSONObject jsonObject, String key) throws JSONException {
        if (exists(jsonObject, key))
            return jsonObject.getJSONObject(key);
        else
            return null;
    }

    @Nullable
    public static JSONArray getJsonArray(JSONObject jsonObject, String key) throws JSONException {
        if (exists(jsonObject, key))
            return jsonObject.getJSONArray(key);
        else
            return null;
    }

    @Nullable
    public static Date getDate(JSONObject jsonObject, String key, String format) throws JSONException {
        return createDate(getString(jsonObject, key), format);
    }

    public static String getString(Cursor cursor, String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }

    public static int getInt(Cursor cursor, String key) {
        return cursor.getInt(cursor.getColumnIndex(key));
    }

    public static double getDouble(Cursor cursor, String key) {
        return cursor.getDouble(cursor.getColumnIndex(key));
    }

    public static Date getDateForLong(Cursor cursor, String key) {
        return new Date(cursor.getLong(cursor.getColumnIndex(key)));
    }

    public static void fadeOut(final View view, long duration) {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
            }

            public void onAnimationRepeat(Animation animation) { }

            public void onAnimationStart(Animation animation) { }
        });
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void fadeOut(final View view) {
        fadeOut(view, 300);
    }

    public static void fadeIn(final View view, long duration) {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            public void onAnimationRepeat(Animation animation) { }

            public void onAnimationStart(Animation animation) { }
        });
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animation);
    }

    public static void fadeIn(final View view) {
        fadeIn(view, 300);
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar.getTime();
    }

    public static void setDateOfDatePicker(DatePicker datePicker, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1)
            byteBuffer.write(buffer, 0, len);
        return byteBuffer.toByteArray();
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static File createImageFile( Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

}
