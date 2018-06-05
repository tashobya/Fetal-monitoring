package com.wekebere.queryforms;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.wekebere.helpers.MailHost;
import com.wekebere.helpers.MysqlConnector;
import com.wekebere.helpers.Singleton;
import com.wekebere.openapp.ForgottenStepOne;
import com.wekebere.openapp.ForgottenStepTwo;
import com.wekebere.popout.MessagePopOut;
import com.wekebere.popout.NetworkPopOut;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class QueryResetStepOne extends AsyncTask<Void, Void, Boolean>{
GregorianCalendar calendar = new GregorianCalendar();
SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
//..................loading variables..........
Connection conn=null;
PreparedStatement pstmt=null;
ResultSet query_value =null;
int check_user;
TextView create_button;
 Context context;
    Exception error;
    public QueryResetStepOne( TextView create_button,Context context){
        this.create_button=create_button;
        this.context=context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
// this might take a while ...
        create_button.setText("Searching User Account");
        try{
//...............SERVER DATABASE CONNECTION....................
            conn=MysqlConnector.getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM MST_USER_INFO I,confirm_user C  WHERE I.VC_EMAIL=C.VC_EMAIL AND I.VC_EMAIL='"+ForgottenStepOne.pass_email_address+"' AND C.VC_STATUS ='ACTIVE'");
            query_value = pstmt.executeQuery();
            while(query_value.next()){
            check_user=query_value.getInt("COUNT");
            }
            query_value.close();
            pstmt.close();
            conn.close();
            if(check_user > 0){
             conn=MysqlConnector.getConnection();
             pstmt = conn.prepareStatement("INSERT INTO DT_PASS_RESET (NU_ID,VC_EMAIL,NU_PASSCODE) VALUES (0,'"+ForgottenStepOne.pass_email_address+"',"+df.format(Calendar.getInstance().getTime()).replace("-","")+")");
             pstmt.executeUpdate();
             pstmt.close();
             conn.close();
             MailHost.send("wekebereapp@gmail.com","weke@2018",""+ForgottenStepOne.pass_email_address+"","Wekebere App Password Reset Message","Reset Code : "+df.format(Calendar.getInstance().getTime()).replace("-","")+"");
            }
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
//..............................success email captured..........
                Singleton.getInstance().setEmail(ForgottenStepOne.pass_email_address);
                Intent intent = new Intent(context,ForgottenStepTwo.class);
                View view = new View(context);
                view.getContext().startActivity(intent);
            }else{
//..............................failed email does exists.........
                Singleton.getInstance().setTitleCode("STEPONE-FAILED");
                Singleton.getInstance().setEmail(ForgottenStepOne.pass_email_address);
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