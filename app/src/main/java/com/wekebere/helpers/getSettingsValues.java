package com.wekebere.helpers;
import android.content.Context;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class getSettingsValues {
Context main_view;
Connection sqlite=null;
PreparedStatement pstmt=null;
ResultSet query_value =null;
private int lower_limtit_cont,upper_limtit_cont,lower_fhr,upper_fhr,diag_timer;
String RunDate;
float samp_freq;
//....................................settings constructor.................
public getSettingsValues(){
try{
sqlite = SqliteDb.ConnecrDb(main_view);
pstmt =sqlite.prepareStatement("SELECT * FROM WEKE_SETTINGS");
query_value = pstmt.executeQuery();
while(query_value.next()){
lower_limtit_cont=query_value.getInt("NU_LOWER_CONT");
upper_limtit_cont=query_value.getInt("NU_UPPER_CONT");
lower_fhr=query_value.getInt("NU_LOWER_FHR");
upper_fhr=query_value.getInt("NU_UPPER_FHR");
samp_freq=query_value.getFloat("NU_SAMP_FREQ");
diag_timer=query_value.getInt("NU_DIA_TIMER");
RunDate=query_value.getString("DT_RUN_DATE");
}
query_value.close();
pstmt.close();
sqlite.close();
}catch(Exception out){
Toast.makeText(main_view, "Settings update filtering error",Toast.LENGTH_LONG).show();
}
}
//..................................calling methods........................................
public int getlowerCont(){
return lower_limtit_cont;
}
public int getUpperCont(){
return upper_limtit_cont;
}
public int getUpperFhr(){
return upper_fhr;
}
public int getLowerFhr(){
return lower_fhr;
}
public float getSampleFreg(){
return samp_freq;
}
public int getDiagTimer(){
return diag_timer;
}
public String getRunDate(){
        return RunDate;
    }
}
