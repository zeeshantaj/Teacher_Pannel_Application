package com.example.teacher_panel_application.Access;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification {

    private final String userFCMToken;
    private final String title;
    private final String body;
    private final Context context;
    private final String postUrl = "https://fcm.googleapis.com/v1/projects/teacherpanelapp/messages:send";


    public SendNotification(String userFCMToken, String title, String body, Context context) {
        this.userFCMToken = userFCMToken;
        this.title = title;
        this.body = body;
        this.context = context;
    }
    public void sendNotification(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try {
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title",title);
            notificationObject.put("body",body);
            messageObject.put("token",userFCMToken);
            messageObject.put("notification",notificationObject);

            mainObj.put("message",messageObject);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("MyApp","response success");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError.networkResponse != null) {
                        int statusCode = volleyError.networkResponse.statusCode;
                        Log.d("MyApp", "Error: " + statusCode + " " + new String(volleyError.networkResponse.data));
                    } else {
                        Log.d("MyApp", "Volley error " + volleyError.getMessage());
                        Log.d("MyApp", "Volley error " + volleyError.getLocalizedMessage());
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    AccessToken accessToken = new AccessToken();
                    Log.d("MyApp","access token "+accessToken.getAccessToken());
                    String accessKey = accessToken.getAccessToken();
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","Bearer "+ accessKey);
                    return header;
                }
            };
            requestQueue.add(jsonObjectRequest);

        }catch (JSONException e){
            Log.d("MyApp","json exception "+e.getMessage());
        }
    }
}
