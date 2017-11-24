package com.example.supertec.php;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText txt_username , txt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_username = (EditText)findViewById(R.id.username);
        txt_password = (EditText)findViewById(R.id.password);
    }

    public void onLogin(View view){
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();
        String type = "POST";
        BackgroundWorker backgroundWorker = new BackgroundWorker(MainActivity.this);
        backgroundWorker.execute(type,username,password);

    }

}
