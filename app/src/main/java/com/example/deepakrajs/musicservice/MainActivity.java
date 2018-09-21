package com.example.deepakrajs.musicservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.greetings_text_view);
    }

    public void openActivity(View view) {
        String button_text;
        button_text = ((Button)view).getText().toString();

        if (button_text.equals("Register")) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class); // "MainActivity.this" and "this" both are same
            startActivity(intent);
        }
        else if (button_text.equals("Login") || button_text.equals("Submit")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
