package com.wekebere.popout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.wekebere.helpers.Singleton;
import com.wekebere.openapp.ForgottenStepOne;
import com.wekebere.openapp.ForgottenStepThree;
import com.wekebere.openapp.ForgottenStepTwo;
import com.wekebere.openapp.LoginForm;
import com.wekebere.openapp.R;
import com.wekebere.openapp.RegisterForm;
public class MessagePopOut extends AppCompatActivity implements View.OnClickListener {
TextView dialog_info,try_button;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.message_popout);
dialog_info = (TextView) findViewById(R.id.dialog_info);
try_button = (TextView) findViewById(R.id.try_button);
//.....................creating switch............................
switch(Singleton.getInstance().getTitleCode()){
case "REG-EXISTS":
dialog_info.setText("Email address already taken");
try_button.setText("Try Again");
break;
case "REG-CREATED":
dialog_info.setText("Check Your Email");
try_button.setText("Login");
break;
case "STEPONE-FAILED":
dialog_info.setText("Entered email doesnot exists");
try_button.setText("Try Again");
break;
case "STEPTWO-FAILED":
dialog_info.setText("Entered code doesnot exists");
try_button.setText("Try Again");
break;
case "STEPTHREE-FAILED":
dialog_info.setText("Failed to reset password");
try_button.setText("Try Again");
break;
case "STEPTHREE-SUCCESS":
dialog_info.setText("Password updated successfully");
try_button.setText("Login");
break;
case "LOGIN-FAILED":
dialog_info.setText("Wrong Email ID or Password");
try_button.setText("Try Again");
break;
}
//.............................setting listeners..................
try_button.setOnClickListener(this);
}

@Override
public void onClick(View view) {
switch(view.getId()){
case R.id.try_button:
if(Singleton.getInstance().getTitleCode().equals("REG-CREATED")) {
Intent intent = new Intent(getApplicationContext(),LoginForm.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("REG-EXISTS")){
Intent intent = new Intent(getApplicationContext(),RegisterForm.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPONE-FAILED")){
Intent intent = new Intent(getApplicationContext(),ForgottenStepOne.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPTWO-FAILED")){
Intent intent = new Intent(getApplicationContext(),ForgottenStepTwo.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPTHREE-FAILED")){
Intent intent = new Intent(getApplicationContext(),ForgottenStepOne.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPTHREE-SUCCESS")){
Intent intent = new Intent(getApplicationContext(),LoginForm.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("LOGIN-FAILED")){
Intent intent = new Intent(getApplicationContext(),LoginForm.class);
startActivity(intent);
}
break;
}
}
@Override
public void onBackPressed() {
if(Singleton.getInstance().getTitleCode().equals("REG-CREATED")) {
Intent intent = new Intent(getApplicationContext(),RegisterForm.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("REG-EXISTS")){
Intent intent = new Intent(getApplicationContext(),RegisterForm.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPONE-FAILED")){
Intent intent = new Intent(getApplicationContext(),ForgottenStepOne.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPTWO-FAILED")){
Intent intent = new Intent(getApplicationContext(),ForgottenStepTwo.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPTHREE-FAILED")){
Intent intent = new Intent(getApplicationContext(),ForgottenStepOne.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("STEPTHREE-SUCCESS")){
Intent intent = new Intent(getApplicationContext(),ForgottenStepThree.class);
startActivity(intent);
}else if(Singleton.getInstance().getTitleCode().equals("LOGIN-FAILED")){
Intent intent = new Intent(getApplicationContext(),LoginForm.class);
startActivity(intent);
}
}
}