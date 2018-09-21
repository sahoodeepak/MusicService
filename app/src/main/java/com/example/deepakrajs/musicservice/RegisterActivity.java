package com.example.deepakrajs.musicservice;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText uid;
    EditText uname;
    EditText upassword;

    Context context = this;
    UserHelper userHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uid = (EditText) findViewById(R.id.et_id);
        uname = (EditText) findViewById(R.id.et_name);
        upassword = (EditText) findViewById(R.id.et_password);
    }

    public void addUser(View view) {
        userHelper = new UserHelper(context);
        String id = uid.getText().toString();
        String name = uname.getText().toString();
        String password = upassword.getText().toString();

        if (id.equals("") || name.equals("") || password.equals("")) {
            Toast.makeText(getBaseContext(), "Fields Are Empty", Toast.LENGTH_SHORT).show();
        } else {
            Boolean checkId = userHelper.checkId(id);
            if (checkId == true) {
                sqLiteDatabase = userHelper.getWritableDatabase();
                userHelper.addInformations(id, name, password, sqLiteDatabase);

                Toast.makeText(getBaseContext(), "Data Saved", Toast.LENGTH_SHORT).show();

                String button_text;
                button_text = ((Button) view).getText().toString();
                if (button_text.equals("Save")) {
                    Intent intent = new Intent(this, LoginActivity.class); // "MainActivity.this" and "this" both are same
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getBaseContext(), "Id Already Exists", Toast.LENGTH_SHORT).show();
            }
//            sqLiteDatabase = userHelper.getWritableDatabase();
//            userHelper.addInformations(id, name, password, sqLiteDatabase);
//            Toast.makeText(getBaseContext(), "Data Saved", Toast.LENGTH_SHORT).show();
        }
//        String button_text;
//        button_text = ((Button)view).getText().toString();
//        if (button_text.equals("Save")) {
//            Intent intent = new Intent(this, LoginActivity.class); // "MainActivity.this" and "this" both are same
//            startActivity(intent);
//        }
//        userHelper.close();
    }
}
