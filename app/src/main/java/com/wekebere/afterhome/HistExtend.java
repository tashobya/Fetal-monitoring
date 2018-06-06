package com.wekebere.afterhome;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wekebere.helpers.FillArray;
import java.util.List;
import de.codecrafters.tableview.TableDataAdapter;
public class HistExtend extends TableDataAdapter<FillArray> {
private static final int TEXT_SIZE =14;
public HistExtend(Context context, List<FillArray> data) {
super(context, data);
}
@Override
public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
FillArray fill_data = getRowData(rowIndex);
View renderedView = null;
switch (columnIndex) {
case 0:
renderedView=renderTimeValue(fill_data);
break;
case 1:
renderedView = renderDateValue(fill_data);
break;
case 2:
renderedView = renderContractValue(fill_data);
break;
}
return renderedView;
}
private View renderTimeValue(final FillArray fill_rander) {
return renderString(fill_rander.getTimeValue());
}
private View renderDateValue(final FillArray fill_rander) {
return renderString(fill_rander.getDateFile());
}
private View renderContractValue(final FillArray fill_rander) {
return renderString(fill_rander.getContractValue());
}

private View renderString(final String value) {
final TextView textView = new TextView(getContext());
textView.setText(value);
textView.setPadding(20, 10, 20, 10);
textView.setTextSize(TEXT_SIZE);
return textView;
}
}