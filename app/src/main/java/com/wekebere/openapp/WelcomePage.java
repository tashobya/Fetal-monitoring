package com.wekebere.openapp;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.wekebere.afterhome.MainActivity;
import com.wekebere.helpers.FileConfig;
import com.wekebere.helpers.FileManager;
import com.wekebere.helpers.getUserDetails;
import java.io.File;
public class WelcomePage extends AppCompatActivity implements View.OnClickListener {
 Context context;
 private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
 private static final int REQUEST_PERMISSION_SETTING = 101;
 private SharedPreferences permissionStatus;
 private boolean sentToSettings = false;
 @Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.welcome_page);
  TextView start_button = (TextView)findViewById(R.id.start_button);
  //.....................setting listener.......................
  start_button.setOnClickListener(this);
  permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
  if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
  //Show Information about why you need the permission
  AlertDialog.Builder builder = new AlertDialog.Builder(WelcomePage.this);
  builder.setTitle("Need Storage Permission");
  builder.setMessage("This app needs storage permission.");
  builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
  @Override
  public void onClick(DialogInterface dialog, int which) {
  dialog.cancel();
  ActivityCompat.requestPermissions(WelcomePage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
  }});
  builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
  @Override
  public void onClick(DialogInterface dialog, int which) {
  dialog.cancel();
  }});
  builder.show();
  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
  } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
  //Previously Permission Request was cancelled with 'Dont Ask Again',
  // Redirect to Settings after showing Information about why you need the permission
  AlertDialog.Builder builder = new AlertDialog.Builder(this);
  builder.setTitle("Need Storage Permission");
  builder.setMessage("This app needs storage permission.");
  builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
  @Override
  public void onClick(DialogInterface dialog, int which) {
  dialog.cancel();
  sentToSettings = true;
  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
  Uri uri = Uri.fromParts("package", getPackageName(), null);
  intent.setData(uri);
  startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
  Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
  }});
  builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
  @Override
   public void onClick(DialogInterface dialog, int which) {
   dialog.cancel();
   }});
   builder.show();
   } else {
   //just request the permission
   ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
   }
   SharedPreferences.Editor editor = permissionStatus.edit();
   editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
   editor.commit();
  } else {
   new FileManager().getFile("wekebere");
   File db_path = new File(Environment.getExternalStorageDirectory(), "wekebere");
   File list_db = new File(db_path, "wekebereconfig.txt");
   if (list_db.exists()) {
   } else {
    new FileConfig().WriteText("@useruser@user@id00@id@sess0@sess@emailexample@xample.com@email", context);
   }
//........................LOADING DEFAULT PANEL..................
   if (new getUserDetails().main("@sess", context).equals("1")) {
   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
   startActivity(intent);
   }}
}
 // Implement the OnClickListener callback
@Override
public void onClick(View v) {
if(v.getId()==R.id.start_button){
 if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
 sentToSettings = true;
 Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
 Uri uri = Uri.fromParts("package", getPackageName(), null);
 intent.setData(uri);
 startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
 Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
 }else {
 Intent intent = new Intent(getApplicationContext(), LoginForm.class);
 startActivity(intent);
 }
}}
}
