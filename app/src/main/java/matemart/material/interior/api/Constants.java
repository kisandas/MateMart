package matemart.material.interior.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import matemart.material.interior.model.StateAndCityModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class Constants {

    public static String BASE_URL = "https://www.matemart.org/api/";
public static long NETWORK_TIMEOUT = 5000;

    public static String GET_STATE = "/get-states";
    public static String SEND_OTP = "/send-otp";
    public static String VERIFY_OTP = "/verify-otp";
    public static String CHECK_PINCODE = "check-pincode";


    public static ArrayList<StateAndCityModel> statList = new ArrayList<>();


    public static String getErrorMessage(Context context, ResponseBody responseBody) {
        Gson gson = new Gson();

        JsonObject jsonObject = null;
        try {
            jsonObject = gson.fromJson(responseBody.charStream(), JsonObject.class);
            String string = null;
            string = jsonObject.get("message").getAsString();
            return string;

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
