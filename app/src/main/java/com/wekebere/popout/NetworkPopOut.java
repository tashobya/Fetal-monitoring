package com.wekebere.popout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.wekebere.openapp.R;
public class NetworkPopOut extends AppCompatActivity implements View.OnClickListener {
TextView dialog_info,try_button;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.network_popout);
dialog_info = (TextView) findViewById(R.id.dialog_info);
try_button = (TextView) findViewById(R.id.try_button);
//.............................setting listeners..................
try_button.setOnClickListener(this);
}
@Override
public void onClick(View view) {
switch(view.getId()){
case R.id.try_button:
Intent intent = new Intent(getApplicationContext(),CreatingPopOut.class);
startActivity(intent);
break;
}
}
}
