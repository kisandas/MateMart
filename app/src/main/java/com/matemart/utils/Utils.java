package com.matemart.utils;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.matemart.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {

        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.layout_news_badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        boolean hasAllPermissions = true;

        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                hasAllPermissions = false;
            }
        }

        return hasAllPermissions;

    }
    public static boolean hasPermission(Context context, String permission) {

        int res = context.checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED;

    }
    public static String getDateTimeStampName() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        final Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isAboveQ() {
        return SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static String IMG_FOLDER = "MyImages";
    public static File createImageFile(Context context) throws IOException {
        String timeStamp = Utils.getDateTimeStampName();
        String imageFileName = "PIC_" + timeStamp;
        File image, storageDir;

        if (Utils.isAboveQ()) {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + IMG_FOLDER);
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
//            String currentPhotoPath = image.getAbsolutePath();
        } else {
            storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + IMG_FOLDER);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            image = new File(storageDir, imageFileName + ".jpg");
            image.createNewFile();
        }
        return image;
    }

    public static Uri getUriForFileProvider(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getString(R.string.authority), file);
    }
}
