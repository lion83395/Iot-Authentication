package SCC;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;
import java.lang.String;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.io.FileWriter;
import java.math.*;



public class Keymanager {
	private static long transDec2(String in) {
	    try {
	        long out = Long.valueOf(in,16).intValue();
	        return out;
	    }catch (Exception e){
	        return Long.valueOf(0);
	    }
	}
	public static void change() throws IOException, NoSuchAlgorithmException 
	{
		String s1[]=new String[7];
		String s2[]=new String[7];
  	    try {
  	      Connection connect = DriverManager.getConnection(
  	          "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC","root","123456");
  	           //連線URL為   jdbc:mysql//伺服器地址/資料庫名  ，後面的2個引數分別是登陸使用者名稱和密碼
  	      //System.out.println("Success connect Mysql server!");
  	      Statement stmt = connect.createStatement();
  	      ResultSet rs = stmt.executeQuery("select * from user");
  	               
  	     //user 為你表的名稱
  	      String aa="";
  	      int k=0;
  	      while (rs.next()) {
  	        //System.out.println(rs.getString("password"));
  	        s1[k]=rs.getString("password");
  	        k++;
  	      }
  	    MessageDigest md1 = MessageDigest.getInstance("MD5");
    	  for(int j=0;j<4;j++) {
    		  String temp="";
    		  temp=s1[j];
    		  String temp2="";
    		md1.update(temp.getBytes());
    		temp2=new BigInteger(1, md1.digest()).toString(16);
    		  s2[j]=temp2.substring(8, 24);
    	  }
    	  for(int q=0;q<4;q++) {
    	  String sql="UPDATE user set password=('"+s2[q]+"') WHERE password=('"+s1[q]+"')";
    	  int result=stmt.executeUpdate(sql);
    	  
    	  }
    	  
  	    }
  	    
  	    catch (Exception e) {
  	      System.out.print("get data error!");
  	      e.printStackTrace();
  	    }
  	 
			System.out.println("Done");
				
			
		
		
	}

}
