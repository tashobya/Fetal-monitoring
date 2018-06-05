package com.wekebere.openapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wekebere.helpers.Singleton;
import com.wekebere.popout.CreatingPopOut;

public class LoginForm extends AppCompatActivity implements View.OnClickListener{
    //..................variables..........................
    private TextView login_button,register_button,forgotten_password;
    private EditText email_address,user_password;
    //............................static variables.................................................
    public static String pass_email_address,pass_user_password;
    Intent intent =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
     setContentView(R.layout.login_form);
 //.................Resource File.......................................
     login_button = (TextView)findViewById(R.id.login_button);
     register_button = (TextView)findViewById(R.id.register_button);
     email_address = (EditText)findViewById(R.id.email_address);
     user_password = (EditText)findViewById(R.id.user_password);
     forgotten_password = (TextView) findViewById(R.id.forgotten_password);
     //.............setting listener....................................
     register_button.setOnClickListener(this);
     login_button.setOnClickListener(this);
     forgotten_password.setOnClickListener(this);
     //.................setting default values..........................
     email_address.setText(Singleton.getInstance().getEmail());
    }
    @Override
    public void onClick(View view) {
    switch(view.getId()){
    case R.id.register_button:
    intent = new Intent(getApplicationContext(),RegisterForm.class);
    startActivity(intent);
    break;
    case R.id.forgotten_password:
    intent = new Intent(getApplicationContext(),ForgottenStepOne.class);
    startActivity(intent);
    break;
    case R.id.login_button:
    if(TextUtils.isEmpty(email_address.getText().toString()) || TextUtils.isEmpty(user_password.getText().toString())){
    Toast.makeText(getApplicationContext(), "Validate all fields",Toast.LENGTH_LONG).show();
    }else {
    //........................setting static variables.....................................
    pass_email_address = email_address.getText().toString();
    pass_user_password = user_password.getText().toString();
    //......switching intent..............................................................
    Singleton.getInstance().setTitleCode("QUERY_LOGIN");
    Intent intent = new Intent(getApplicationContext(), CreatingPopOut.class);
    startActivity(intent);
    }
    break;
    }
    }
}
