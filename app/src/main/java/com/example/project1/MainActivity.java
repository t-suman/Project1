package com.example.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private ImageView image;
    private EditText userName;
    private EditText password;
    private Button button;
    private TextView textView;
    private RelativeLayout parent;
    private Boolean b1=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        image=findViewById(R.id.imageView);
        userName =findViewById(R.id.user_name);
        password=findViewById(R.id.password);
        button=findViewById(R.id.button2);
        textView=findViewById(R.id.textView);
        parent=findViewById(R.id.parent);



        textView.setOnClickListener(this);
        password.setOnKeyListener(this);
        parent.setOnClickListener(this);
        image.setOnClickListener(this);


        if(ParseUser.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,MyProfile.class));
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    public void fun(View view){
        if(b1)signUp();
        else login();
    }
    private void signUp(){
        ParseUser user=new ParseUser();
        if(userName.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this,"User Name and Password are required",Toast.LENGTH_SHORT).show();
        }
        else {
            user.setUsername(userName.getText().toString());
            user.setPassword(password.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null) {
                        Toast.makeText(MainActivity.this, "Account Successfully created", Toast.LENGTH_SHORT).show();
                        //showUserList();
                        startActivity(new Intent(MainActivity.this,MyProfile.class));
                    }
                    else
                        Toast.makeText(MainActivity.this,"Sign Up failed :"+e.toString(),Toast.LENGTH_SHORT ).show();
                }
            });
        }
    }
    private void login(){
        ParseUser user=new ParseUser();
        if(userName.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this,"User Name and Password are required",Toast.LENGTH_SHORT).show();
        }
        else {
            ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user!=null){
                        Toast.makeText(MainActivity.this,"Login Sussessful",Toast.LENGTH_SHORT).show();
                        //showUserList();
                        startActivity(new Intent(MainActivity.this,MyProfile.class));

                    }
                    else{
                        Toast.makeText(MainActivity.this,"Login failed :"+e.toString(),Toast.LENGTH_SHORT ).show();

                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==textView.getId()){
            if(b1==true) {
                button.setText("LOGIN");
                textView.setText("or, SIGN UP");
                b1 = false;
            }
            else {
                button.setText("SIGN UP");
                textView.setText("or, LOGIN");
                b1 = true;
            }
        }
        else if(v.getId()==parent.getId()||v.getId()==image.getId()){
            InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_ENTER&& event.getAction()==KeyEvent.ACTION_DOWN){
            fun(button);
        }
        return false;
    }
}
