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
public class ForgottenStepTwo extends AppCompatActivity implements View.OnClickListener {
    //..................variables..........................
    private EditText enter_received;
    private TextView submit_button;
    //.................static variables...................
    public static String pass_enter_received;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotten_steptwo);
        submit_button = (TextView)findViewById(R.id.submit_button);
        enter_received= (EditText) findViewById(R.id.enter_received);
//........................setting listeners..............
        submit_button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
      if(view.getId()==R.id.submit_button){
       if(TextUtils.isEmpty(enter_received.getText().toString())){
       enter_received.setError("Empty Received Code");
       }else{
//.................................getting variables......
       pass_enter_received=enter_received.getText().toString();
       Singleton.getInstance().setTitleCode("QUERY_STEPTWO");
       Intent intent = new Intent(getApplicationContext(), CreatingPopOut.class);
       startActivity(intent);
       startActivity(intent);
       }
      }
    }
@Override
public void onBackPressed() {
startActivity(new Intent(getApplicationContext(),ForgottenStepOne.class));
finish();
}
}