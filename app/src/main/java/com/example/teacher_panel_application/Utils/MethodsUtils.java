package com.example.teacher_panel_application.Utils;

import android.content.Context;
import android.content.Intent;

public class MethodsUtils {
    public static void shareOnWhatsapp(Context context,String name, String loc, String dura, String dep, String sub, String top, String start){
        String teacherName = "Teacher: " + name + "\n";
        String location = "Location: " + loc + "\n";
        String duration = "Duration: " + dura + " minutes\n";
        String depart = "Major: " + dep + "\n";
        String subject = "Subject: " + sub + "\n";
        String topic = "Today's Topic: " + top + "\n";
        String started = "Started At: " + start;

        String message = teacherName + location + duration + depart + subject + topic + started;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setPackage("com.whatsapp");
        context.startActivity(shareIntent);
    }
}
