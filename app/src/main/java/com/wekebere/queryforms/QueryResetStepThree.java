package com.wekebere.queryforms;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.wekebere.helpers.KeyControl;
import com.wekebere.helpers.MysqlConnector;
import com.wekebere.helpers.Singleton;
import com.wekebere.openapp.ForgottenStepThree;
import com.wekebere.popout.MessagePopOut;
import com.wekebere.popout.NetworkPopOut;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class QueryResetStepThree extends AsyncTask<Void, Void, Boolean>{
    //..................loading variables..........
    Connection conn=null;
    PreparedStatement pstmt=null;
    ResultSet query_value =null;
    int check_user;
    TextView create_button;
    Context context;
    Exception error;
    public QueryResetStepThree( TextView create_button,Context context){
        this.create_button=create_button;
        this.context=context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
// this might take a while ...
        create_button.setText("Resetting Password");
        try{
//...............SERVER DATABASE CONNECTION....................
            conn=MysqlConnector.getConnection();
            pstmt = conn.prepareStatement("UPDATE MST_USER_INFO SET VC_PASSWORD='"+new KeyControl().encrypt(ForgottenStepThree.pass_user_password)+"' WHERE VC_EMAIL='"+Singleton.getInstance().getEmail()+"'");
            check_user=pstmt.executeUpdate();
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
//..............................password reset success.....................
                Singleton.getInstance().setTitleCode("STEPTHREE-SUCCESS");
                Intent intent = new Intent(context,MessagePopOut.class);
                View view = new View(context);
                view.getContext().startActivity(intent);
            }else{
//..............................failed to reset password ..................
                Singleton.getInstance().setTitleCode("STEPTHREE-FAILED");
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