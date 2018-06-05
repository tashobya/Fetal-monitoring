package com.wekebere.openapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.wekebere.helpers.Singleton;
import com.wekebere.popout.CreatingPopOut;
public class ForgottenStepOne extends AppCompatActivity implements View.OnClickListener {
//..................variables..........................
 private EditText email_address;
 private TextView submit_button;
 //.................static variables...................
 public static String pass_email_address;
 @Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.forgotten_stepone);
submit_button = (TextView)findViewById(R.id.submit_button);
email_address = (EditText) findViewById(R.id.email_address);
//........................setting default values........
email_address.setText(Singleton.getInstance().getEmail());
//........................setting listeners..............
submit_button.setOnClickListener(this);
}
@Override
public void onClick(View view) {
if(view.getId()==R.id.submit_button){
if(TextUtils.isEmpty(email_address.getText().toString())){
 email_address.setError("Empty Email Address");
}else{
//.................................getting variables......
 pass_email_address=email_address.getText().toString();
 Singleton.getInstance().setTitleCode("QUERY_STEPONE");
 Intent intent = new Intent(getApplicationContext(), CreatingPopOut.class);
 startActivity(intent);
 startActivity(intent);
}
}
}
 @Override
 public void onBackPressed() {
 startActivity(new Intent(getApplicationContext(),LoginForm.class));
finish();
 }
}