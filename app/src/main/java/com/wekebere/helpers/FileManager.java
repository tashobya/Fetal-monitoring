package com.wekebere.helpers;
import android.os.Environment;
import java.io.File;
public class FileManager {
public void getFile(String path){
File db_path = new File(Environment.getExternalStorageDirectory(),path);
if (!db_path.exists()) {
db_path.mkdirs();
}
}
}
