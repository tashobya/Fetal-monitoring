package com.wekebere.helpers;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.wekebere.openapp.HomeFragment;
import com.wekebere.openapp.R;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class SettingsTab extends Fragment implements View.OnClickListener {
EditText lower_limtit_cont,upper_limtit_cont,lower_fhr,upper_fhr,samp_freq,diag_timer,run_date;
TextView update_tab;
Connection sqlite=null;
PreparedStatement pstmt=null;
ResultSet query_value =null;
View main_view;
@Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  main_view =  inflater.inflate(R.layout.settings_tab, container, false);
  //..............................setting values........................................
  lower_limtit_cont= (EditText)main_view.findViewById(R.id.lower_limtit_cont);
  upper_limtit_cont= (EditText)main_view.findViewById(R.id.upper_limtit_cont);
  lower_fhr= (EditText)main_view.findViewById(R.id.lower_fhr);
  upper_fhr= (EditText)main_view.findViewById(R.id.upper_fhr);
  samp_freq= (EditText)main_view.findViewById(R.id.samp_freq);
  diag_timer= (EditText)main_view.findViewById(R.id.diag_timer);
  update_tab= (TextView)main_view.findViewById(R.id.update_tab);
  run_date= (EditText)main_view.findViewById(R.id.run_date);
//..............................querying values.......................................
  FillEditText();
  update_tab.setOnClickListener(this);
  return main_view;
}
@Override
public void onClick(View view) {
if(view.getId()==R.id.update_tab){
if(TextUtils.isEmpty(lower_limtit_cont.getText().toString()) || TextUtils.isEmpty(upper_limtit_cont.getText().toString()) || TextUtils.isEmpty(lower_fhr.getText().toString()) || TextUtils.isEmpty(upper_fhr.getText().toString())|| TextUtils.isEmpty(samp_freq.getText().toString()) || TextUtils.isEmpty(diag_timer.getText().toString())){
Toast.makeText(main_view.getContext(), "Empty fields not required",Toast.LENGTH_LONG).show();
}else {
//.......................UPDATING DEFAULT VALUES..................................................
try{
sqlite = SqliteDb.ConnecrDb(main_view.getContext());
pstmt=sqlite.prepareStatement("UPDATE WEKE_SETTINGS SET NU_LOWER_CONT='"+lower_limtit_cont.getText().toString()+"',NU_UPPER_CONT='"+upper_limtit_cont.getText().toString()+"',NU_LOWER_FHR='"+lower_fhr.getText().toString()+"',NU_UPPER_FHR='"+upper_fhr.getText().toString()+"',NU_SAMP_FREQ='"+ samp_freq.getText().toString()+"',NU_DIA_TIMER='"+diag_timer.getText().toString()+"',DT_RUN_DATE='"+run_date.getText().toString()+"' WHERE NU_ID=1");
pstmt.executeUpdate();
pstmt.close();
sqlite.close();
Toast.makeText(main_view.getContext(), "Settings updated successfully ",Toast.LENGTH_LONG).show();
HomeFragment f1 = new HomeFragment();
FragmentTransaction ft = getFragmentManager().beginTransaction();
ft.replace(R.id.frame, f1); // f1_container is your FrameLayout container
ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
ft.addToBackStack(null);
ft.commit();
}catch(Exception out){
Toast.makeText(main_view.getContext(),"Settings update filtering error",Toast.LENGTH_LONG).show();
}
}}
}
private void FillEditText(){
try{
sqlite = SqliteDb.ConnecrDb(main_view.getContext());
pstmt =sqlite.prepareStatement("SELECT * FROM WEKE_SETTINGS");
query_value = pstmt.executeQuery();
while(query_value.next()){
lower_limtit_cont.setText(query_value.getString("NU_LOWER_CONT"));
upper_limtit_cont.setText(query_value.getString("NU_UPPER_CONT"));
lower_fhr.setText(query_value.getString("NU_LOWER_FHR"));
upper_fhr.setText(query_value.getString("NU_UPPER_FHR"));
samp_freq.setText(query_value.getString("NU_SAMP_FREQ"));
diag_timer.setText(query_value.getString("NU_DIA_TIMER"));
run_date.setText(query_value.getString("DT_RUN_DATE"));
}
query_value.close();
pstmt.close();
sqlite.close();
}catch(Exception out){
Toast.makeText(main_view.getContext(), "Settings update filtering error",Toast.LENGTH_LONG).show();
}
}}