package com.wekebere.openapp;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.wekebere.helpers.Singleton;
import java.util.Set;
import java.util.UUID;
public class PairedFragment extends Fragment {
// Debugging for LOGCAT
 private static final String TAG = "PairedFragment";
 private static final boolean D = true;
 TextView connect_text;
 // Member fields
 private BluetoothAdapter mBtAdapter;
 private ArrayAdapter<String> mPairedDevicesArrayAdapter;
 View main_view=null;
 String MacAddress=null;
 // SPP UUID service - this should work for most devices
 private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
 @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
// Inflate the layout for this fragment
 main_view =  inflater.inflate(R.layout.paired_fragment, container, false);
 getActivity().setTitle("Availabe Devices");
 //.........................checking whether already connected device.............................
 if(Singleton.getInstance().getBeltSign() > 0){
 HomeFragment f1 = new HomeFragment();
 FragmentTransaction ft = getFragmentManager().beginTransaction();
 ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
 ft.addToBackStack(null);
 ft.commit();
 }
 return main_view;
}
 @Override
 public void onResume()
 {
  super.onResume();
  //***************
  checkBTState();
  connect_text = (TextView)main_view.findViewById(R.id.connecting);
  connect_text.setTextSize(40);
  connect_text.setText(" ");
  // Initialize array adapter for paired devices
  mPairedDevicesArrayAdapter = new ArrayAdapter<String>(main_view.getContext(), R.layout.device_name);
  // Find and set up the ListView for paired devices
  ListView pairedListView = (ListView) main_view.findViewById(R.id.paired_devices);
  pairedListView.setAdapter(mPairedDevicesArrayAdapter);
  pairedListView.setOnItemClickListener(mDeviceClickListener);
  // Get the local Bluetooth adapter
  mBtAdapter = BluetoothAdapter.getDefaultAdapter();
  // Get a set of currently paired devices and append to 'pairedDevices'
  Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
  // Add previosuly paired devices to the array
  if (pairedDevices.size() > 0) {
  main_view.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
  for (BluetoothDevice device : pairedDevices) {
 mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
   }
  } else {
String noDevices = getResources().getText(R.string.none_paired).toString();
mPairedDevicesArrayAdapter.add(noDevices);
}
}
 // Set up on-click listener for the list (nicked this - unsure)
  private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
  public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
  connect_text.setText("Connecting...");
 // Get the device MAC address, which is the last 17 chars in the View
  String info = ((TextView) v).getText().toString();
  MacAddress = info.substring(info.length() - 17);
  // Make an intent to start next activity while taking an extra which is the MAC address.
  Singleton.getInstance().setMacAddress(MacAddress);
  Singleton.getInstance().setBeltSign(1);
  Toast.makeText(main_view.getContext(), "Wekebere belt connected", Toast.LENGTH_LONG).show();
  HomeFragment f1 = new HomeFragment();
  FragmentTransaction ft = getFragmentManager().beginTransaction();
  ft.replace(R.id.frame,f1); // f1_container is your FrameLayout container
  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
  ft.addToBackStack(null);
  ft.commit();
  }};

 private void checkBTState() {
 // Check device has Bluetooth and that it is turned on
mBtAdapter=BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
if(mBtAdapter==null) {
Toast.makeText(main_view.getContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
} else {
if (mBtAdapter.isEnabled()) {
Log.d(TAG, "...Bluetooth ON...");
} else {
//Prompt user to turn on Bluetooth
Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
startActivityForResult(enableBtIntent, 1);
}}}
}