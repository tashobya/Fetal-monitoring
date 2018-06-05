package com.wekebere.helpers;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
public class CreateDb {
public void WriteText(Context context){
try{
new FileManager().getFile("wekebere");
File db_path = new File(Environment.getExternalStorageDirectory(),"wekebere");
//..................creating db file..........................................
File create_db = new File(db_path, "weke.db");
create_db.createNewFile();
}catch(Exception out){
Toast.makeText(context, "Session connection error",Toast.LENGTH_LONG).show();
}}
}
