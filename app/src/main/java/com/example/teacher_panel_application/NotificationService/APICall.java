package com.example.teacher_panel_application.NotificationService;

import androidx.annotation.NonNull;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APICall {
    public static void callApi(JSONObject jsonObject){
        MediaType json = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAA7reCDoQ:APA91bHKOMdHeoUOtVUznGapVkZe27TYREQXUNAcON5FUepw3w_GOAer_ODZhNCmYC-ihXlagEojfs8dvj3DcWgeJqQpvYbxmRz8FMXWrb_b1keKYtRsv1agAC1fKnyiyJ1WWWKCO65F")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}
