package com.wekebere.queryforms;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.wekebere.helpers.MysqlConnector;
import com.wekebere.helpers.Singleton;
import com.wekebere.openapp.ForgottenStepThree;
import com.wekebere.openapp.ForgottenStepTwo;
import com.wekebere.popout.MessagePopOut;
import com.wekebere.popout.NetworkPopOut;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class QueryResetStepTwo extends AsyncTask<Void, Void, Boolean>{
    //..................loading variables..........
    Connection conn=null;
    PreparedStatement pstmt=null;
    ResultSet query_value =null;
    int check_user;
    TextView create_button;
    Context context;
    Exception error;
    public QueryResetStepTwo( TextView create_button,Context context){
        this.create_button=create_button;
        this.context=context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
// this might take a while ...
        create_button.setText("Searching Entered Code");
        try{
//...............SERVER DATABASE CONNECTION....................
            conn=MysqlConnector.getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM DT_PASS_RESET  WHERE NU_PASSCODE='"+ForgottenStepTwo.pass_enter_received+"' AND VC_EMAIL ='"+Singleton.getInstance().getEmail()+"'");
            query_value = pstmt.executeQuery();
            while(query_value.next()){
            check_user=query_value.getInt("COUNT");
            }
            query_value.close();
            pstmt.close();
            conn.close();
            return true;
        }catch(Exception out){
            error =out;
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //.................after work......................................
        if (result) {
            if(check_user > 0){
//..............................success passcode matching..........
                Intent intent = new Intent(context,ForgottenStepThree.class);
                View view = new View(context);
                view.getContext().startActivity(intent);
            }else{
//..............................failed passcode not matching .........
                Singleton.getInstance().setTitleCode("STEPTWO-FAILED");
                Intent intent = new Intent(context,MessagePopOut.class);
                View view = new View(context);
                view.getContext().startActivity(intent);
            }
        } else {
            if (error != null) {
//..............................failed................... .........
                Intent intent = new Intent(context,NetworkPopOut.class);
                View view = new View(context);
                view.getContext().startActivity(intent);
            }}
    }
}