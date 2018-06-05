package com.wekebere.openapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.validator.routines.EmailValidator;

import com.wekebere.helpers.Singleton;
import com.wekebere.popout.CreatingPopOut;
public class RegisterForm extends AppCompatActivity implements View.OnClickListener {
//..................xml variables..........................
private EditText full_name,email_address,user_password,confirm_password;
private TextView submit_button;
//.................email validation...........................................................
private EmailValidator emailValidator = EmailValidator.getInstance();
//............................static variables.................................................
public static String pass_full_name,pass_email_address,pass_user_password;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.register_form);
//........................setting resources.........................................
submit_button = (TextView)findViewById(R.id.submit_button);
full_name = (EditText)findViewById(R.id.full_name);
email_address = (EditText)findViewById(R.id.email_address);
user_password = (EditText)findViewById(R.id.user_password);
confirm_password = (EditText)findViewById(R.id.confirm_password);
//........................validating values........................................
email_address.addTextChangedListener(new TextWatcher() {
@Override
public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
}
@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
 if(emailValidator.isValid(email_address.getText().toString())==true){
  submit_button.setEnabled(true);
 }else{
  submit_button.setEnabled(false);
 }
 }
@Override
public void afterTextChanged(Editable editable) {
if(emailValidator.isValid(email_address.getText().toString())==false){
email_address.setError("Invalid Email Address");
}
}});

user_password.addTextChangedListener(new TextWatcher() {
@Override
public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
}
@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {

}
@Override
public void afterTextChanged(Editable editable) {
if(user_password.getText().length() < 5){
user_password.setError("Weak Password");
}
}});
//.............................setting listeners....................................
submit_button.setOnClickListener(this);

}
@Override
public void onClick(View view) {
switch(view.getId()){
case R.id.submit_button:
if(TextUtils.isEmpty(full_name.getText().toString()) || TextUtils.isEmpty(email_address.getText().toString()) || TextUtils.isEmpty(user_password.getText().toString()) || TextUtils.isEmpty(confirm_password.getText().toString())){
Toast.makeText(getApplicationContext(), "Validate all fields",Toast.LENGTH_LONG).show();
}else {
if(user_password.getText().toString().equals(confirm_password.getText().toString())){
if(user_password.getText().length() > 4) {

  //........................setting static variables.....................................

  pass_full_name = full_name.getText().toString();
  pass_email_address = email_address.getText().toString();
  pass_user_password = user_password.getText().toString();
  //......switching intent..............................................................
 Singleton.getInstance().setTitleCode("QUERY_SIGNUP");
 Intent intent = new Intent(getApplicationContext(), CreatingPopOut.class);
 startActivity(intent);
}else{
user_password.setError("Weak Password atleast(5)chars");
}}else{
confirm_password.setError("Password is not matching");
}}
break;
}
}
}
