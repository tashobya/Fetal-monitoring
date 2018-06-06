package com.wekebere.helpers;
public class FillArray {
String TimeValue;
String DateFile;
String ContractValue;
public FillArray(String TimeValue,String DateFile,String ContractValue){
this.TimeValue=TimeValue;
this.DateFile=DateFile;
this.ContractValue=ContractValue;
}
public String getTimeValue(){
return TimeValue;
}
public String getDateFile(){
return DateFile;
}
public String getContractValue(){
return ContractValue;
}
}
