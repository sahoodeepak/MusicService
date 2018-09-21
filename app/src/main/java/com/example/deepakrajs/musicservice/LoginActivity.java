package com.example.deepakrajs.musicservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText uid;
    EditText upassword;

    Context context = this;
    UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uid = (EditText) findViewById(R.id.et_userid);
        upassword = (EditText) findViewById(R.id.et_userpw);
    }

    public void loginUser(View view) {
        userHelper = new UserHelper(context);
        String id = uid.getText().toString();
        String password = upassword.getText().toString();

        if (id.equals("") || password.equals("")) {
        } else {
            Boolean checkIdPassword = userHelper.checkIdPassword(id, password);
            if (checkIdPassword == false) {
                Toast.makeText(getBaseContext(), "Incorrect Id or Password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Login Successfully", Toast.LENGTH_SHORT).show();

                String button_text;
                button_text = ((Button) view).getText().toString();
                if (button_text.equals("Open")) {
                    Intent intent = new Intent(this, MusicActivity.class); // "MainActivity.this" and "this" both are same
                    startActivity(intent);
                }


            }
        }

    }
}
