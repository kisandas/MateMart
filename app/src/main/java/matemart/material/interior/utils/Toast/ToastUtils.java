package matemart.material.interior.utils.Toast;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import matemart.material.interior.R;

public class ToastUtils {

    Toaster.Status status;
    Context context;

    public ToastUtils(Toaster.Status status, Context context) {
        this.status = status;
        this.context = context;
    }

    protected Drawable setStatusBackground() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_round_corner_radius_8_dp_warning_toast);

        switch (status) {
            case INFO:
            case WARNING:
                drawable = ContextCompat.getDrawable(context, R.drawable.bg_round_corner_radius_8_dp_warning_toast);
                break;
            case SUCCESS:
                drawable = ContextCompat.getDrawable(context, R.drawable.bg_round_corner_radius_8_dp_success_toast);
                break;
            case ERROR:
                drawable = ContextCompat.getDrawable(context, R.drawable.bg_round_corner_radius_8_dp_error_toast);
                break;
        }
        return drawable;
    }

    protected Drawable setStatusIcon() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_warning);

        switch (status) {
            case INFO:
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_warning);
                break;
            case SUCCESS:
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_success);
                break;
            case WARNING:
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_warning);
                break;
            case ERROR:
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_error);
                break;
        }
        return drawable;
    }

}