package com.wekebere.helpers;
public class Singleton {
private static Singleton instance = new Singleton();
private Singleton(){    
}
public static Singleton getInstance(){
return instance;
}
//........................setting Confirm Title..............
String title="";
public void setTitle(String title){
this.title=title;    
}
public String getTitle(){
return title;
}
//........................setting Confirm Code..............
String titleCode="";
public void setTitleCode(String titleCode){
this.titleCode=titleCode;
}
public String getTitleCode(){
return titleCode;
 }
//........................setting email.......................
String email="";
public void setEmail(String email){
this.email=email;	
}
public String getEmail(){
return email;	
}
//.....................dealing with bluetooth signals.....
 String MacAddress="";
 public void setMacAddress(String MacAddress){
  this.MacAddress=MacAddress;
 }
 public  String getMacAddress(){
  return MacAddress;
 }
 //.....................dealing with bluetooth signals.....
 int BeltSign=0;
 public void setBeltSign(int BeltSign){
  this.BeltSign=BeltSign;
 }
 public int getBeltSign(){
  return BeltSign;
 }

}

