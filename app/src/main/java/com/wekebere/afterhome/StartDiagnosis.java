package com.wekebere.afterhome;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.wekebere.helpers.RandNumbers;
import com.wekebere.helpers.Singleton;
import com.wekebere.helpers.SqliteDb;
import com.wekebere.helpers.getSettingsValues;
import com.wekebere.openapp.HomeFragment;
import com.wekebere.openapp.R;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.UUID;
public class StartDiagnosis extends Fragment implements View.OnClickListener{
//.............................wadgets...................................
TextView contract_duration,heart_rate,contract_freq,temp,dialog_info,start_again,timer_value;
ProgressBar Progress_Bar;
LinearLayout bottom_display;
ScrollView hide_panel;
 //............................variables...................................
Connection sqlite=null;
PreparedStatement pstmt=null;
ResultSet query_value=null;
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
GregorianCalendar calendar = new GregorianCalendar();
View main_view;
Handler bluetoothIn;
final int handlerState = 0; //used to identify handler message
private BluetoothAdapter btAdapter = null;
private BluetoothSocket btSocket = null;
int get_current=0,LOWER_TM=0,UPPER_TM=0;
private StringBuilder recDataString = new StringBuilder();
private ConnectedThread mConnectedThread;
BigInteger seq_id;

//..............................heart rate variables.......................
ArrayList<Float> hold_heart=new ArrayList();
private float ARRAY_SIZE;
private float BPM=0.00f,duration_in_second=0.00f,duration_in_minutes=0.00f;
DecimalFormat df = new DecimalFormat("#");
// SPP UUID service - this should work for most devices
private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//.........................timer settings...................................
private int seconds =0; //THIS WILL RUN FOR 10 SECONDS
private boolean stopTimer = false;
@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
main_view=inflater.inflate(R.layout.start_diagnosis,container,false);
contract_duration= (TextView)main_view.findViewById(R.id.contract_duration);
heart_rate= (TextView)main_view.findViewById(R.id.heart_rate);
contract_freq= (TextView)main_view.findViewById(R.id.contract_freq);
temp= (TextView)main_view.findViewById(R.id.temp);
dialog_info= (TextView)main_view.findViewById(R.id.dialog_info);
Progress_Bar= (ProgressBar)main_view.findViewById(R.id.Progress_Bar);
start_again= (TextView)main_view.findViewById(R.id.start_again);
timer_value= (TextView)main_view.findViewById(R.id.timer_value);
bottom_display= (LinearLayout)main_view.findViewById(R.id.bottom_display);
hide_panel= (ScrollView)main_view.findViewById(R.id.hide_panel);
//.....................picking seq_id........................................
seq_id=new RandNumbers().generateRandomDate();
//....................setting listener.......................................
bluetoothIn = new Handler() {
public void handleMessage(android.os.Message msg) {
if (msg.what == handlerState) {		//if message is what we want
String readMessage = (String) msg.obj; // msg.arg1 = bytes from connect thread
recDataString.append(readMessage);   //keep appending to string until ~
int endOfLineIndex = recDataString.indexOf("~");   // determine the end-of-line
if (endOfLineIndex > 0) {   // make sure there data before ~
String dataInPrint = recDataString.substring(0, endOfLineIndex);  // extract string

int dataLength = dataInPrint.length();	//get length of data received

if (recDataString.charAt(0) == '#')	//if it starts with # we know it is what we are looking for
 {
  String sensor0 = recDataString.substring(1, 5);//.....contraction
  String sensor1 = recDataString.substring(6, 10);//.....heart rate
  String sensor2 = recDataString.substring(11, 15);//.....temperature

 //.........................measuring contration.....................................................
 try {
 if (Double.parseDouble(sensor0) > new getSettingsValues().getlowerCont() && seconds != 0) {
  sqlite = SqliteDb.ConnecrDb(main_view.getContext());
  pstmt = sqlite.prepareStatement("INSERT INTO CONTRACT_TBL (TRANS_ID,NU_SENSE,NU_SEC,DT_RUN_DATE,DT_TIMEIN,NU_GROUP_ID,NU_HEARTRATE) VALUES (NULL,'"+sensor0+"',"+seconds+",'"+format.format(calendar.getTime())+"',CURRENT_TIMESTAMP,'"+seq_id+"','"+sensor1+"')");
  int i=pstmt.executeUpdate();
  pstmt.close();
  sqlite.close();
  }
  }catch(Exception out){
  Toast.makeText(main_view.getContext(), "processing failed",Toast.LENGTH_LONG).show();
  }

 //....................................calculating temperature........................................

temp.setText(CalcTemp(Double.parseDouble(sensor2))+"°C");
 //.....................................calculating heart rate........................................
hold_heart.add(Float.parseFloat(sensor1));
}
recDataString.delete(0, recDataString.length()); //clear all string data
// strIncom =" ";
dataInPrint = " ";
}}}
};
//.............................main checkings...............................
timer();
btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
checkBTState();
start_again.setOnClickListener(this);
return main_view;
}
//............................screate bluetooth socket........
private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
//creates secure outgoing connecetion with BT device using UUID
}
@Override
public void onResume() {
super.onResume();
//create device and set the MAC address
BluetoothDevice device = btAdapter.getRemoteDevice(Singleton.getInstance().getMacAddress());
try {
btSocket = createBluetoothSocket(device);
} catch (IOException e) {
Toast.makeText(main_view.getContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
}
// Establish the Bluetooth socket connection.
try
{
btSocket.connect();
} catch (IOException e) {
try
{
btSocket.close();
} catch (IOException e2)
{
//insert code to deal with this
}}
mConnectedThread =new ConnectedThread(btSocket);
mConnectedThread.start();
//I send a character when resuming.beginning transmission to check device is connected
// If it is not an exception will be thrown in the write method and finish() will be called
mConnectedThread.write("1");
}

@Override
public void onPause()
{
super.onPause();
try
{
//Don't leave Bluetooth sockets open when leaving activity
 btSocket.close();
} catch (IOException e2) {
 //insert code to deal with this
}
}
//Checks that the Android device Bluetooth is available and prompts to be turned on if off
private void checkBTState() {
 if(btAdapter==null) {
 Toast.makeText(main_view.getContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
} else {
 if (btAdapter.isEnabled()) {
} else {
Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
startActivityForResult(enableBtIntent, 1);
 }
}
}
 @Override
 public void onClick(View view) {
if(view.getId()==R.id.start_again){
stopTimer = false;
seconds=0;
//.....................picking seq_id........................................
seq_id=seq_id=new RandNumbers().generateRandomDate();
timer();
}}
 //create new class for connect thread
private class ConnectedThread extends Thread {
private final InputStream mmInStream;
private final OutputStream mmOutStream;
//creation of the connect thread
public ConnectedThread(BluetoothSocket socket) {
InputStream tmpIn = null;
OutputStream tmpOut = null;
try {
//Create I/O streams for connection
tmpIn = socket.getInputStream();
tmpOut = socket.getOutputStream();
} catch (IOException e) { }
mmInStream = tmpIn;
mmOutStream = tmpOut;
}

public void run() {
 byte[] buffer = new byte[256];
 int bytes;
// Keep looping to listen for received messages
while (true) {
try {
bytes = mmInStream.read(buffer);        	//read bytes from input buffer
 String readMessage = new String(buffer, 0, bytes);
// Send the obtained bytes to the UI Activity via handler
bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
} catch (IOException e) {
 break;
 }}
}
//write method
 public void write(String input) {
byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
try {
 mmOutStream.write(msgBuffer);
 Singleton.getInstance().setBeltSign(1);
 getActivity().setTitle("Diagnosing");
//write bytes over BT connection via outstream
 } catch (IOException e) {
 //if you cannot write, close the application
  Toast.makeText(main_view.getContext(), "Connection Failure", Toast.LENGTH_LONG).show();
  Singleton.getInstance().setBeltSign(0);
  HomeFragment f1 = new HomeFragment();
  FragmentTransaction ft = getFragmentManager().beginTransaction();
  ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
  ft.addToBackStack(null);
  ft.commit();
  }}}
//...................calculating temp................................................................
 public double CalcTemp(double volts){
 return Math.round(((5.0*volts*100.0)/1024.0)-18.0)*-1;
 }
 //....................starting timer................................................................
 private void timer() {
 final Handler handler = new Handler();
 handler.post(new Runnable() {
@Override
 public void run() {
 seconds+=1;
 if (new getSettingsValues().getDiagTimer()==seconds) {
// DO SOMETHING WHEN TIMES UP
  stopTimer = true;
  timer_value.setText(seconds+"(s)");
  Progress_Bar.setVisibility(View.GONE);
  start_again.setVisibility(View.VISIBLE);
  dialog_info.setText("Diagnosis finished");
  hide_panel.setVisibility(View.VISIBLE);
  //...............................calculating heart rate...........................................
  fetalHeartRate();
  //...............................pushing layout bottom............................................
  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
  params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
  params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
  bottom_display.setLayoutParams(params);
  try{

  //..................getting maximum value..........................................................
  sqlite = SqliteDb.ConnecrDb(main_view.getContext());
  pstmt =sqlite.prepareStatement("SELECT * FROM CONTRACT_TBL WHERE NU_GROUP_ID='"+seq_id+"' ORDER BY NU_SENSE DESC LIMIT 1");
  query_value = pstmt.executeQuery();
  while(query_value.next()){
  UPPER_TM=query_value.getInt("NU_SEC");
  }
  query_value.close();
  pstmt.close();
  sqlite.close();

  //..................getting maximum value..........................................................

  sqlite = SqliteDb.ConnecrDb(main_view.getContext());
  pstmt =sqlite.prepareStatement("SELECT * FROM CONTRACT_TBL WHERE NU_GROUP_ID='"+seq_id+"' ORDER BY NU_SENSE ASC LIMIT 1");
  query_value = pstmt.executeQuery();
  while(query_value.next()){
  LOWER_TM=query_value.getInt("NU_SEC");
  }
  query_value.close();
  pstmt.close();
  sqlite.close();
  //.................calculating frequency...........................................................
  sqlite = SqliteDb.ConnecrDb(main_view.getContext());
  pstmt =sqlite.prepareStatement("SELECT SUM(NU_SENSE-"+(new getSettingsValues().getDiagTimer()/60)+") AS SUM_TOT FROM CONTRACT_TBL WHERE NU_GROUP_ID='"+seq_id+"'");
  query_value = pstmt.executeQuery();
  while(query_value.next()){
  contract_freq.setText(query_value.getInt("SUM_TOT")+"(mins)");
  }
   query_value.close();
   pstmt.close();
   sqlite.close();
  //................outputting contraction............................................................
  if((UPPER_TM - LOWER_TM) > 0){
  contract_duration.setText((UPPER_TM - LOWER_TM)+ "(s)");
  }else {
  contract_duration.setText(((UPPER_TM - LOWER_TM) * -1)+ "(s)");
  }
  }catch(Exception out){
  Toast.makeText(main_view.getContext(), "processing failed",Toast.LENGTH_LONG).show();
  }
 }else{
 timer_value.setText(seconds+"(s)");
 Progress_Bar.setVisibility(View.VISIBLE);
 start_again.setVisibility(View.GONE);
 dialog_info.setText("Processing");
 hide_panel.setVisibility(View.GONE);
  //...............................pushing layout top...........................................
 RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
 params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
 params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
 bottom_display.setLayoutParams(params);
 }
if(stopTimer == false) {
handler.postDelayed(this, 1000);
}}
});
 }

private void fetalHeartRate(){
//....................................checking whether an array is null...........
if (hold_heart.size() > 1) {
int beat_count = 0;
for (int counter = 2; counter < hold_heart.size(); counter++) {
if (hold_heart.get(counter) > hold_heart.get(counter - 1) && hold_heart.get(counter) > hold_heart.get(counter + 1) && hold_heart.get(counter) > 1) {
beat_count += 1;
}}
ARRAY_SIZE = hold_heart.size();
duration_in_second = ARRAY_SIZE / new getSettingsValues().getSampleFreg();
duration_in_minutes = duration_in_second / 60;
BPM = beat_count / duration_in_minutes;
//...................final BPM..........................
heart_rate.setText(df.format(BPM));
}}
}
