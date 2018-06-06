package com.wekebere.helpers;
import android.content.Context;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MysqlConnector{
private static Connection conn  = null;
private static String DbUser        ="wekebkdr_app";
private static String DBPassword    ="weke@2018";
private static String URL           ="jdbc:mysql://162.251.80.22/wekebkdr_app";
String DriverName                   ="com.mysql.jdbc.Driver";
Context context;
public MysqlConnector(){
getConnection();
}
public static Connection getConnection(){
try {
Class.forName("com.mysql.jdbc.Driver");
} catch(java.lang.ClassNotFoundException out) {
}
try {
conn = DriverManager.getConnection(URL, DbUser, DBPassword);
} catch(SQLException out) {
return null;
}
return conn;
}
}
