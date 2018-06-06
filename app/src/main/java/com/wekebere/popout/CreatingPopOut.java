package com.wekebere.popout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.wekebere.helpers.Singleton;
import com.wekebere.openapp.R;
import com.wekebere.queryforms.QueryLogin;
import com.wekebere.queryforms.QueryRegister;
import com.wekebere.queryforms.QueryResetStepOne;
import com.wekebere.queryforms.QueryResetStepThree;
import com.wekebere.queryforms.QueryResetStepTwo;
public class CreatingPopOut extends AppCompatActivity{
 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
  setContentView(R.layout.creating_popout);
  TextView dialog_info = (TextView)findViewById(R.id.dialog_info);

  //...................switching................................
  switch(Singleton.getInstance().getTitleCode()) {
   case "QUERY_SIGNUP":
   new QueryRegister(dialog_info, this).execute();
   break;
   case "QUERY_STEPONE":
    new QueryResetStepOne(dialog_info, this).execute();
    break;
   case "QUERY_STEPTWO":
    new QueryResetStepTwo(dialog_info, this).execute();
    break;
   case "QUERY_STEPTHREE":
    new QueryResetStepThree(dialog_info, this).execute();
    break;
   case "QUERY_LOGIN":
   new QueryLogin(dialog_info, this).execute();
   break;
  }
 }


}

