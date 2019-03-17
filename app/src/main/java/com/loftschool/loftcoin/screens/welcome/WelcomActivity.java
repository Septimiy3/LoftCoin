package com.loftschool.loftcoin.screens.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.loftschool.loftcoin.R;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomActivity extends AppCompatActivity {


    public static void start(Context context) {
        Intent starter = new Intent(context, WelcomActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
    }
}
