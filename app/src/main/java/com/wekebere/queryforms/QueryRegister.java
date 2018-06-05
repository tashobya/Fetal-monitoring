package com.wekebere.queryforms;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.wekebere.helpers.KeyControl;
import com.wekebere.helpers.MailHost;
import com.wekebere.helpers.MysqlConnector;
import com.wekebere.helpers.Singleton;
import com.wekebere.openapp.RegisterForm;
import com.wekebere.popout.MessagePopOut;
import com.wekebere.popout.NetworkPopOut;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
public class QueryRegister extends AsyncTask<Void, Void, Boolean>{
 GregorianCalendar calendar = new GregorianCalendar();
 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 //..................loading variables..........
 Connection conn=null;
 PreparedStatement pstmt=null;
 ResultSet query_value =null;
 int check_user;
 TextView create_button;
 Context context;
 Exception error;
 public QueryRegister( TextView create_button,Context context){
this.create_button=create_button;
this.context=context;
 }
@Override
 protected Boolean doInBackground(Void... params) {
// this might take a while ...
 create_button.setText("Creating User Account");
 try{
//...............SERVER DATABASE CONNECTION....................
  conn=MysqlConnector.getConnection();
  pstmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM MST_USER_INFO WHERE UPPER(VC_EMAIL)='"+RegisterForm.pass_email_address+"'");
  query_value = pstmt.executeQuery();
  while(query_value.next()){
  check_user=query_value.getInt("COUNT");
  }
  query_value.close();
  pstmt.close();
  conn.close();
 if(check_user == 0){
 conn=MysqlConnector.getConnection();
 pstmt = conn.prepareStatement("INSERT INTO MST_USER_INFO (NU_USER_ID,VC_FULL_NAME,VC_EMAIL,VC_PASSWORD,DT_REG_DATE)  VALUES (0,'"+RegisterForm.pass_full_name+"','"+ RegisterForm.pass_email_address+"','"+new KeyControl().encrypt(RegisterForm.pass_user_password)+"','"+format.format(calendar.getTime())+"')");
 pstmt.executeUpdate();
 pstmt.close();
 conn.close();
 MailHost.send("wekebereapp@gmail.com","weke@2018",""+RegisterForm.pass_email_address+"","Wekebere App Account Signup Email Confirmation","Confirmation link : http://wekebere.com/WekebereApp/verify_email.php?action&user_id="+RegisterForm.pass_email_address+"");
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
//..............................user account already exists..........
  Singleton.getInstance().setTitleCode("REG-EXISTS");
  Intent intent = new Intent(context,MessagePopOut.class);
  View view = new View(context);
  view.getContext().startActivity(intent);
 }else{
//..............................success account is created .........
 Singleton.getInstance().setEmail(RegisterForm.pass_email_address);
 Singleton.getInstance().setTitleCode("REG-CREATED");
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