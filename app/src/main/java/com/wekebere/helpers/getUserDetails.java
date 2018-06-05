package com.wekebere.helpers;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
public class getUserDetails {
String UserQuery;
public String main(String decide_text,Context context){
try{
//.........................GETTING USER LOGGED IN.......................................
File db_path = new File(Environment.getExternalStorageDirectory(),"wekebere");	
File list_db = new File (db_path,"wekebereconfig.txt");
FileInputStream inputStream = new FileInputStream(list_db);  
String user_text = IOUtils.toString(inputStream).replace(System.getProperty("line.separator"), "");
String str = user_text;
String[] myStrings = str.split(decide_text);
UserQuery= myStrings[1];
}catch(Exception out){
Toast.makeText(context, "Session connection error",Toast.LENGTH_LONG).show();
}
return UserQuery;
}
}
