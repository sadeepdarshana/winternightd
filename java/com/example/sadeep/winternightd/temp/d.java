package com.example.sadeep.winternightd.temp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Random;

/**
 * Created by Sadeep on 10/20/2016.
 */
public class d {
    public static void wow(Context c){
        Random rnd=new Random();

        ((AppCompatActivity)c).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(rnd.nextInt()%255,rnd.nextInt()%255,rnd.nextInt()%255)));
    }

    public static void p(Object ... objects){
        for (Object object:objects) {
            Log.i("mylog",object.toString());
        }
    }
}
