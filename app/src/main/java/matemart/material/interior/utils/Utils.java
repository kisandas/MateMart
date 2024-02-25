package matemart.material.interior.utils;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import matemart.material.interior.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

public static String LOGIN_MESSAGE ="Please do logIn First!";
    public static String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again after some time!";
    public static String ERROR_TITLE = "Something went wrong!";

    public static String ORDER_STATUS_PLACED = "Order Placed";
    public static String ORDER_STATUS_PLACED_TEXT = "We have received your order on ";

    public static String ORDER_STATUS_PROCESSED = "Order Processed";
    public static String ORDER_STATUS_PROCESSED_TEXT = "We are prepared your order on ";

    public static String ORDER_STATUS_CONFIRMED = "Order Confirmed";
    public static String ORDER_STATUS_CONFIRMED_TEXT = "We have been confirmed your order on ";

    public static String ORDER_STATUS_OUT_FOR_DELIVERY = "Out For Delivery";
    public static String ORDER_STATUS_OUT_FOR_DELIVERY_1_TEXT = "Your order will be delivered soon";
    public static String ORDER_STATUS_OUT_FOR_DELIVERY_2_TEXT = "Your order is out for Delivery";

    public static String ORDER_STATUS_DELIVERED = "Delivered";
    public static String ORDER_STATUS_DELIVERED_TEXT = "Your order is Delivered";

    public static String ORDER_STATUS_RETURN = "Return Pending";
    public static String ORDER_STATUS_RETURN_TEXT = "Your order will be returned";

    public static String ORDER_STATUS_RETURNED = "Returned";
    public static String ORDER_STATUS_RETURNED_TEXT = "Your order is Returned";

    public static String ORDER_STATUS_CANCELLED = "Order Cancelled";
    public static String ORDER_STATUS_CANCELLED_TEXT = "Order Cancelled On ";


    public static String OrderPlaced = "Order Placed";
    public static String OrderProcessed = "Order Processed";
    public static String OrderConfirmed = "Order Confirmed";
    public static String OrderOutForDelivery = "Out For Delivery";
    public static String OrderDelivered = "Delivered";
    public static String OrderReturn = "Order return";
    public static String OrderReturned = "Order Returned";
    public static String OrderCancelled = "Order Cancelled";


    public static int Placed = 0;
    public static int Processing = 1;
    public static int Confirmed = 2;
    public static int OutForDelivery = 3;
    public static int Delivered = 4;
    public static int ReturnPending = 5;
    public static int Returned = 6;
    public static int Cancelled = -1;


    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {

        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.layout_news_badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.END | Gravity.TOP; // Position badge at the top end of the view

        int pixelsTop = (int) (context.getResources().getDisplayMetrics().density * 6); // Adjust the value for desired margin
        int pixelsRight = (int) (context.getResources().getDisplayMetrics().density * 18); // Adjust the value for desired margin
        layoutParams.setMargins(0, pixelsTop, pixelsRight, 0); // Set margins (left, top, right, bottom)
        badge.setLayoutParams(layoutParams);
        badge.setTag("badge_" + itemId);
        itemView.addView(badge);
    }


    public static void hideBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);

        // Find badge view by tag
        View badgeToRemove = itemView.findViewWithTag("badge_" + itemId);

        // If the badge view exists, remove it
        if (badgeToRemove != null) {
            ((ViewGroup) itemView).removeView(badgeToRemove);
        }
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
