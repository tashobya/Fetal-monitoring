package com.wekebere.openapp;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.wekebere.afterhome.HistExtend;
import com.wekebere.helpers.FillArray;
import com.wekebere.helpers.SqliteDb;
import com.wekebere.helpers.getSettingsValues;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
public class HistoryFragments extends Fragment{
View main_view;
static String[] spaceProbeHeaders={"Time","Date","Value (V)"};
ArrayList<FillArray> data = new ArrayList<>();
//............................variables...................................
Connection sqlite=null;
PreparedStatement pstmt=null;
ResultSet query_value=null;
SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
main_view = inflater.inflate(R.layout.history_fragments,container,false);
TableView tableView = (TableView)main_view. findViewById(R.id.tableView);
tableView.setColumnCount(3);
FillTableView();
//SET PROP
tableView.setHeaderBackgroundColor(Color.parseColor("#e182d8"));
tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(main_view.getContext(),spaceProbeHeaders));
tableView.setDataAdapter(new HistExtend(main_view.getContext(),data));
tableView.addDataClickListener(new TableDataClickListener() {
@Override
public void onDataClicked(int rowIndex, Object clickedData) {
Toast.makeText(main_view.getContext(), ((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
}
});
return main_view;
}
//..........................filling array list..........................................................
private void FillTableView(){
    //..................getting maximum value..........................................................
try{
sqlite = SqliteDb.ConnecrDb(main_view.getContext());
pstmt =sqlite.prepareStatement("SELECT * FROM CONTRACT_TBL WHERE DT_RUN_DATE='"+new getSettingsValues().getRunDate()+"' ORDER BY DT_RUN_DATE DESC");
query_value = pstmt.executeQuery();
while(query_value.next()){
String originalString = query_value.getString("DT_TIMEIN");
Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
String time_string = new SimpleDateFormat("H:mm:ss").format(date); // 9:00
data.add(new FillArray(time_string,query_value.getString("DT_RUN_DATE"),query_value.getString("NU_SENSE")));
}
query_value.close();
pstmt.close();
sqlite.close();
}catch(Exception out){
Toast.makeText(main_view.getContext(), "processing failed",Toast.LENGTH_LONG).show();
}
}
}
