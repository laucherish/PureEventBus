package com.laucherish.pureeventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.laucherish.pureeventbus.lib.EventBus;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("Main2Activity post event");
                EventBus.getInstance().post(intent);
                Log.d("Main2Activity", "post: " + intent.getAction());
            }
        });
    }
}
