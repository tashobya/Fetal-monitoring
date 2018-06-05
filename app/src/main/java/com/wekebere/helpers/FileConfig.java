package com.wekebere.helpers;
import java.io.File;
import java.io.FileOutputStream;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
public class FileConfig {
public void WriteText(String write_text,Context context){
try{
new FileManager().getFile("wekebere");
File db_path = new File(Environment.getExternalStorageDirectory(),"wekebere");
//........................creating session file
File list_db = new File (db_path,"wekebereconfig.txt");
FileOutputStream stream = new FileOutputStream(list_db);
stream.write(write_text.getBytes());
stream.close();
}catch(Exception out){
Toast.makeText(context, "Session connection error",Toast.LENGTH_LONG).show();
}
	
}
}
