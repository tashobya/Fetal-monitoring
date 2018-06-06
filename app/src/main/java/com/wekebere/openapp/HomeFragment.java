package com.wekebere.openapp;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wekebere.afterhome.MainActivity;
import com.wekebere.afterhome.StartDiagnosis;
import com.wekebere.helpers.Singleton;
public class HomeFragment extends Fragment implements View.OnClickListener {
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
// Inflate the layout for this fragment
View main_view =  inflater.inflate(R.layout.fragment_home, container, false);
TextView connect_device= (TextView)main_view.findViewById(R.id.connect_device);
TextView paired_status= (TextView)main_view.findViewById(R.id.paired_status);
//.......................checking where belt already connect.........................
if(Singleton.getInstance().getBeltSign()==1){
getActivity().setTitle("Wekebere belt connected");
paired_status.setText("(1) device paired");
connect_device.setText("Start diagnosis");
MainActivity.pass_menuitem.setTitle("Disonnect wekebere belt");
}else{
getActivity().setTitle(R.string.connect_belt);
paired_status.setText("(0) device paired");
MainActivity.pass_menuitem.setTitle("Connect wekebere belt");
}
//.......................setting listeners............................................
connect_device.setOnClickListener(this);
return main_view;
}

@Override
public void onClick(View view) {
if(view.getId()==R.id.connect_device){
if(Singleton.getInstance().getBeltSign() > 0){
StartDiagnosis f1 = new StartDiagnosis();
FragmentTransaction ft = getFragmentManager().beginTransaction();
ft.replace(R.id.frame, f1); // f1_container is your FrameLayout container
ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
ft.addToBackStack(null);
ft.commit();
}else{
PairedFragment f1 = new PairedFragment();
FragmentTransaction ft = getFragmentManager().beginTransaction();
ft.replace(R.id.frame, f1); // f1_container is your FrameLayout container
ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
ft.addToBackStack(null);
ft.commit();
}}
}
}
