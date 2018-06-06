package com.wekebere.helpers;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
public class SqliteDb {
public static Connection con = null;
public static Connection ConnecrDb(Context context){
try{
Class.forName("org.sqldroid.SQLDroidDriver");
File db_path = new File(Environment.getExternalStorageDirectory(),"wekebere");
File list_db = new File (db_path,"weke.db");
con = DriverManager.getConnection("jdbc:sqlite:"+list_db.getAbsolutePath()+"");
return con;
}catch(ClassNotFoundException | SQLException e){
Toast.makeText(context, "Session connection error",Toast.LENGTH_LONG).show();
return null;
}}
}