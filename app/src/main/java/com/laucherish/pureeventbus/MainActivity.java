package com.laucherish.pureeventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.laucherish.pureeventbus.lib.EventBus;
import com.laucherish.pureeventbus.lib.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getInstance().register(this);

        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }

    @Subscriber
    public void onEvent(Intent intent) {
        Log.d("MainActivity", "onEvent: " + intent.getAction());
    }

    @Subscriber
    public void onEvent2(Intent intent) {
        Log.d("MainActivity", "onEvent2: " + intent.getAction());
    }
}
