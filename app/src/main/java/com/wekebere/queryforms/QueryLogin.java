package com.wekebere.queryforms;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.wekebere.afterhome.MainActivity;
import com.wekebere.helpers.FileConfig;
import com.wekebere.helpers.KeyControl;
import com.wekebere.helpers.MysqlConnector;
import com.wekebere.helpers.Singleton;
import com.wekebere.openapp.LoginForm;
import com.wekebere.popout.MessagePopOut;
import com.wekebere.popout.NetworkPopOut;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class QueryLogin extends AsyncTask<Void, Void, Boolean>{
    Connection conn=null;
    PreparedStatement pstmt=null;
    ResultSet query_value =null;
    int check_user;
    TextView create_button;
    Context context;
    Exception error;
    public QueryLogin( TextView create_button,Context context){
        this.create_button=create_button;
        this.context=context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
// this might take a while ...
        create_button.setText("Logging In User");
        try{
            //...............SERVER DATABASE CONNECTION....................
            conn=MysqlConnector.getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM MST_USER_INFO I,confirm_user C  WHERE I.VC_EMAIL=C.VC_EMAIL AND I.VC_EMAIL='"+ LoginForm.pass_email_address+"' AND I.VC_PASSWORD='"+new KeyControl().encrypt(LoginForm.pass_user_password)+"' AND C.VC_STATUS ='ACTIVE'");
            query_value = pstmt.executeQuery();
            while(query_value.next()){
            check_user=query_value.getInt("COUNT");
            }
            query_value.close();
            pstmt.close();
            conn.close();
            if(check_user > 0){
            conn=MysqlConnector.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM MST_USER_INFO I,confirm_user C WHERE I.VC_EMAIL=C.VC_EMAIL AND I.VC_EMAIL='"+LoginForm.pass_email_address+"' AND I.VC_PASSWORD='"+new KeyControl().encrypt(LoginForm.pass_user_password)+"' AND C.VC_STATUS ='ACTIVE'");
            query_value = pstmt.executeQuery();
            while(query_value.next()){
            //.........................writing sessions...............................
            new FileConfig().WriteText("@user"+query_value.getString("VC_FULL_NAME")+"@user@id"+query_value.getString("NU_USER_ID")+"@id@sess1@sess@email"+query_value.getString("VC_EMAIL")+"@email@contact"+query_value.getString("VC_CONTACT")+"@contact",context);
            }
            query_value.close();
            pstmt.close();
            conn.close();
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
//..............................user logged in successfully..........
                Singleton.getInstance().setEmail(LoginForm.pass_email_address);
                Intent intent = new Intent(context,MainActivity.class);
                View view = new View(context);
                view.getContext().startActivity(intent);
            }else{
//..............................falied to login .........
                Singleton.getInstance().setTitleCode("LOGIN-FAILED");
                Singleton.getInstance().setEmail(LoginForm.pass_email_address);
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