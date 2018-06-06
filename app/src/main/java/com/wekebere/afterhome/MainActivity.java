package com.wekebere.afterhome;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.wekebere.helpers.CreateDb;
import com.wekebere.helpers.FileConfig;
import com.wekebere.helpers.FileManager;
import com.wekebere.helpers.SettingsTab;
import com.wekebere.helpers.Singleton;
import com.wekebere.helpers.SqliteDb;
import com.wekebere.helpers.getUserDetails;
import com.wekebere.openapp.HistoryFragments;
import com.wekebere.openapp.HomeFragment;
import com.wekebere.openapp.R;
import com.wekebere.openapp.WelcomePage;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
Context context;
Connection sqlite=null;
PreparedStatement pstmt=null;
Menu menu;
MenuItem connect_belt;
public static MenuItem pass_menuitem;
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
GregorianCalendar calendar = new GregorianCalendar();
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//..............................main activity...................................................................
setContentView(R.layout.activity_main);
Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
//.................................default fragments............................................................
 this.setTitle(R.string.connect_belt);
 HomeFragment f1 = new HomeFragment();
 FragmentTransaction ft = getFragmentManager().beginTransaction();
 ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
 ft.addToBackStack(null);
 ft.commit();

//......................checking session status....................................
 new FileManager().getFile("wekebere");
 File db_path = new File(Environment.getExternalStorageDirectory(), "wekebere");
 File list_db = new File(db_path, "weke.db");
 if (list_db.exists()) {
 LightUpdateTable();
 }else {
  new CreateDb().WriteText(context);
 CreateTable();
 LightUpdateTable();
 }

DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
drawer.addDrawerListener(toggle);
toggle.syncState();

NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
navigationView.setNavigationItemSelectedListener(this);
// get menu from navigationView
 menu = navigationView.getMenu();
 connect_belt = menu.findItem(R.id.connect_belt);
 pass_menuitem= connect_belt;
 if(Singleton.getInstance().getBeltSign() > 0){
 connect_belt.setTitle("Disonnect wekebere belt");
 }else{
 connect_belt.setTitle("Connect wekebere belt");
 }
//......................................calling header_text..........................................
 View header = navigationView.getHeaderView(0);
 TextView full_name = (TextView)header.findViewById(R.id.full_name);
 full_name.setText(new getUserDetails().main("@user",context));
 TextView email_address = (TextView)header.findViewById(R.id.email_address);
 email_address.setText(new getUserDetails().main("@email",context));
}

@Override
public void onBackPressed() {
DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
if (drawer.isDrawerOpen(GravityCompat.START)) {
drawer.closeDrawer(GravityCompat.START);
} else {
 super.onBackPressed();
}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.main, menu);
return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
int id = item.getItemId();
//noinspection SimplifiableIfStatement
if (id == R.id.action_settings) {
 this.setTitle("Settings");
 SettingsTab f1 = new SettingsTab();
 FragmentTransaction ft = getFragmentManager().beginTransaction();
 ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
 ft.addToBackStack(null);
 ft.commit();
 return true;
 }
return super.onOptionsItemSelected(item);
}

@SuppressWarnings("StatementWithEmptyBody")
@Override
public boolean onNavigationItemSelected(MenuItem item) {
// Handle navigation view item clicks here.
  int id = item.getItemId();
  if(id == R.id.home_tab){
  HomeFragment f1 = new HomeFragment();
  FragmentTransaction ft = getFragmentManager().beginTransaction();
  ft.replace(R.id.frame, f1); // f1_container is your FrameLayout container
  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
  ft.addToBackStack(null);
  ft.commit();
 }else if (id == R.id.connect_belt) {
 //...................Home Fragments...............................
  if(Singleton.getInstance().getBeltSign() > 0){
  connect_belt.setTitle("Connect wekebere belt");
  HomeFragment f1 = new HomeFragment();
  FragmentTransaction ft = getFragmentManager().beginTransaction();
  ft.replace(R.id.frame, f1); // f1_container is your FrameLayout container
  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
  ft.addToBackStack(null);
  ft.commit();
  this.setTitle(R.string.connect_belt);
  //.......................disconnecting devices...........................
  Singleton.getInstance().setBeltSign(0);
 }else {
  HomeFragment f1 = new HomeFragment();
  FragmentTransaction ft = getFragmentManager().beginTransaction();
  ft.replace(R.id.frame, f1); // f1_container is your FrameLayout container
  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
  ft.addToBackStack(null);
  ft.commit();
  this.setTitle(R.string.connect_belt);
 }
} else if (id == R.id.view_realtime) {
 if(Singleton.getInstance().getBeltSign() > 0){
  this.setTitle("Realtime results");
  View_realtime f1 = new View_realtime();
  FragmentTransaction ft = getFragmentManager().beginTransaction();
  ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
  ft.addToBackStack(null);
  ft.commit();
 }else{
 Toast.makeText(getApplicationContext(), "(0) Device connected",Toast.LENGTH_LONG).show();
}
} else if (id == R.id.track_progress) {
 this.setTitle("Track progress");
 HistoryFragments f1 = new  HistoryFragments();
 FragmentTransaction ft = getFragmentManager().beginTransaction();
 ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
 ft.addToBackStack(null);
 ft.commit();
} else if (id == R.id.settngs) {
 this.setTitle("Settings");
 SettingsTab f1 = new SettingsTab();
 FragmentTransaction ft = getFragmentManager().beginTransaction();
 ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
 ft.addToBackStack(null);
 ft.commit();
} else if (id == R.id.nav_share) {

} else if (id == R.id.logout_user) {
//.....................logout our user................................
 AlertDialog.Builder builder = new AlertDialog.Builder(this);
 builder.setTitle("Are you sure ?");
 builder.setMessage("Are you sure you want to log out ?");
 builder.setPositiveButton("Yes",
 new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int id) {
 new FileConfig().WriteText("@user"+new getUserDetails().main("@user",context)+"@user@id"+new getUserDetails().main("@id",context)+"@id@sess0@sess@email"+new getUserDetails().main("@email",context)+"@email",context);
 dialog.cancel();
 Intent intent = new Intent(getApplicationContext(),WelcomePage.class);
 startActivity(intent);
}});
 builder.setNegativeButton("No",
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
dialog.cancel();
}});
builder.show();
//....................cicking listener................................
}
 DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
 drawer.closeDrawer(GravityCompat.START);
return true;
}
 //..........................creating tables........................................................
  private void CreateTable(){
  try {
  //.......................CREATING TABLE...........................................................
   sqlite = SqliteDb.ConnecrDb(context);
   pstmt=sqlite.prepareStatement("CREATE TABLE IF NOT EXISTS WEKE_SETTINGS (NU_LOWER_CONT INTEGER,NU_UPPER_CONT INTEGER,NU_LOWER_FHR INTEGER,NU_UPPER_FHR INTEGER,NU_SAMP_FREQ INTEGER,NU_DIA_TIMER INTEGER,NU_ID INTEGER,DT_RUN_DATE DATETIME)");
   pstmt.executeUpdate();
   pstmt.close();
   sqlite.close();
   //.......................INSERTING DEFAULT VALUES..................................................
   sqlite = SqliteDb.ConnecrDb(context);
   pstmt=sqlite.prepareStatement("INSERT INTO WEKE_SETTINGS (NU_LOWER_CONT,NU_UPPER_CONT,NU_LOWER_FHR,NU_UPPER_FHR,NU_SAMP_FREQ,NU_DIA_TIMER,NU_ID,DT_RUN_DATE) VALUES (2,5,110,160,1000,120,1,'30-01-2018')");
   pstmt.executeUpdate();
   pstmt.close();
   sqlite.close();
   //.......................creating contraction table...............................................
   sqlite = SqliteDb.ConnecrDb(context);
   pstmt=sqlite.prepareStatement("CREATE TABLE IF NOT EXISTS CONTRACT_TBL (TRANS_ID INTEGER PRIMARY KEY AUTOINCREMENT,NU_SENSE INTEGER,NU_SEC INTEGER,DT_RUN_DATE DATETIME,DT_TIMEIN DATETIME,NU_GROUP_ID VARCHAR(300),NU_HEARTRATE INTEGER)");
   pstmt.executeUpdate();
   pstmt.close();
   sqlite.close();
  }catch(Exception out){
   Toast.makeText(context, "Clearing space failure",Toast.LENGTH_LONG).show();
  }}
//......................................Light Update Table.............................................
private void LightUpdateTable(){
 try{
 //.......................UPDATING DEFAULT VALUES..................................................
 sqlite = SqliteDb.ConnecrDb(context);
 pstmt=sqlite.prepareStatement("UPDATE WEKE_SETTINGS SET DT_RUN_DATE='"+format.format(calendar.getTime())+"' WHERE NU_ID=1");
 pstmt.executeUpdate();
 pstmt.close();
 sqlite.close();
 }catch(Exception out){
 Toast.makeText(context, "Clearing space failure",Toast.LENGTH_LONG).show();
 }
}
}
