package SCC;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

public class Server {

    public final static int DEFAULT_PORT = 5030; 
    public static String message_to_send;
    public static String Sessionkey1;
    public static String SessionKey2;
    public static int legal_flag;
    public static String Sessionkey_com;
    public static int counter;
    public static long q[]=new long[7];
    private static long transDec2(String in) {
	    try {
	        long out = Long.valueOf(in,16).intValue();
	        return out;
	    }catch (Exception e){
	        return Long.valueOf(0);
	    }
	}
    public static void database() {
    	try {
  	      Class.forName("com.mysql.jdbc.Driver");     //載入MYSQL JDBC驅動程式   
  	      //Class.forName("org.gjt.mm.mysql.Driver");     System.out.println("Success loading Mysql Driver!");
  	    }
  	    catch (Exception e) {
  	      System.out.print("Error loading Mysql Driver!");
  	      e.printStackTrace();
  	    }
  	    try {
  	      Connection connect = DriverManager.getConnection(
  	          "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC","root","123456");
  	           //連線URL為   jdbc:mysql//伺服器地址/資料庫名  ，後面的2個引數分別是登陸使用者名稱和密碼
  	      //System.out.println("Success connect Mysql server!");
  	      Statement stmt = connect.createStatement();
  	      ResultSet rs = stmt.executeQuery("select * from user");
  	      int num=0;
  	      String key[]=new String[7];
  	      String temp[]=new String[7];
  	      while (rs.next()) {
  	        //System.out.println(rs.getString("password"));
  	        key[num]=rs.getString("password");
  	        num++;
  	      }
  	    for(int i=0;i<num;i++) {
	    		temp[i]=key[i].substring(8,12);
	    	}
  	    
  	    for (int j=0;j<num;j++) {
  	    	q[j]=transDec2(temp[j]); 	    	
  	    }
  	    
  	    
  	    
  	    
  	    }
  	    	
  	     catch (Exception e) {
  	      System.out.print("get data error!");
  	      e.printStackTrace();
  	    }
    }
    public static void connecting() 
    {
        int port = DEFAULT_PORT;
        
        final String legaldeviceID=("552");
        /*if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.err.println("Error1");
            }
        }
        */
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            
    		int pw1=(int)q[0];
            int pw2=(int)q[1];
            int pw3=(int)q[2];
            int pw4=(int)q[3];
            int[] pw= {0,pw1,pw2,pw3,pw4};
            Socket newSock    = null;
            Boolean stop = true;
            Boolean end = true;
            while(stop == true) { 
                System.out.println("Server TCP ready at the port: " + port + "..." );

                //Waiting for the connection with the client
                newSock = serverSocket.accept(); 
                System.out.println("accept");
                while(end == true){
                System.out.println("success connect");
                BufferedReader is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
                PrintWriter os = new PrintWriter(newSock.getOutputStream(), true); 
                String inputLine = is.readLine(); 
                System.out.println("Received DeviceID: " + inputLine);
                int flag;
                flag=inputLine.compareTo(legaldeviceID);
                if(flag==0) {
                	System.out.println("legal Device");
                	Scanner in = new Scanner(System.in);
                	Random ran=new Random();
                	String random_number=Integer.toString(ran.nextInt(100));
                	String s2=random_number;
                	int challenge_int=Integer.valueOf(s2);
                	System.out.println("Random number for challenge: "+random_number);
                	String random_key=Integer.toString(ran.nextInt(4)+1);
                	String random_key1=Integer.toString(ran.nextInt(4)+1);
                    //System.out.println("Response a Challenge");
                    String s="";
                    int c1=Integer.valueOf(random_key);
                    int c2=Integer.valueOf(random_key1);
                    s=s+random_key+" "+random_key1;
                    //s = in.nextLine();
                    String sendMessage = s+" "+"("+random_number+")";
                    System.out.println("pair of number and challenge: "+sendMessage);
                    os.println(sendMessage);
                    //os.println(inputLine.toUpperCase());	
                    os.flush(); 
                    BufferedReader is1 = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
                    PrintWriter os1 = new PrintWriter(newSock.getOutputStream(), true); 
                    String inputLine1 = is1.readLine(); 
                    System.out.println("Received encrypted message: " + inputLine1);
       
                    int key1=pw[c1]|pw[c2];
                    int key1_temp=key1;
                    int quotient = 0;//商數
            		  String output = " "; 
            		  
            		  //計算結果
            		  for(int n1 = 1; n1 <= 4; n1++ )//計算每四個字元要空白
            		  {
            		   for(int i1 = 1; i1 <= 4 ; i1 ++)
            		   {
            		     int remainder = key1 % 2;//餘數
            		     quotient = key1 / 2 ;
            		     key1 = quotient;
            		     output = remainder + output;  
            		   }  
            		  
            		  output = "" + output;//印出空白
            		  }
            		  final String secretKey = output;
            		  String decryptedString = AESM.decrypt(inputLine1, secretKey) ;
            		  System.out.println("decyrpted message: "+decryptedString);
            		  //check
            		  String regex = "\\d*";
                      Pattern p = Pattern.compile(regex);
                      int n=0;
                      Matcher m = p.matcher(decryptedString);
                      int[] x;
                      x=new int[10];
                      String[] y;
                      y=new String[10];
                      while (m.find()) {
                          if (!"".equals(m.group()))
                          //System.out.println("come here:" + m.group());
                         
                          y[n]=m.group();
                          n=n+1;
                          }
                      Sessionkey1=y[2];
                      int response_int=Integer.valueOf(y[0]);
                      if(response_int==challenge_int) {System.out.println("Correct challenge&response");}
                      else {
                    	  System.out.println("illegal device");break;}
                      int legal_flag=1;
                      int a=Integer.valueOf(y[4]);
                      int b=Integer.valueOf(y[6]);                      
                      int temp=pw[a]|pw[b];
                      int key_server=temp|key1_temp;
                      int quotient1 = 0;//商數
            		  String output_server = " "; 
            		  
            		  //計算結果
            		  for(int n2 = 1; n2 <= 4; n2++ )//計算每四個字元要空白
            		  {
            		   for(int i2 = 1; i2 <= 4 ; i2 ++)
            		   {
            		     int remainder1 = key_server % 2;//餘數
            		     quotient1 = key_server / 2 ;
            		     key_server = quotient1;
            		     output_server = remainder1 + output_server;  
            		   }  
            		  
            		  output_server =" " + output_server;//印出空白
            		  }
            		  final String secretKey1 = output_server;
            		  
            	      String Sessionkey=Integer.toString(ran.nextInt(1000));
            		  String originalString = y[8]+" "+Sessionkey;
            		  SessionKey2=Sessionkey;
            		  String encryptedString = AESM.encrypt(originalString, secretKey1) ;
            		  String decryptedString1 = AESM.decrypt(encryptedString, secretKey1) ;
            		  System.out.println("message send to client:challenge from client "+originalString);
            		  System.out.println("encyrpted message: "+encryptedString);
            		  message_to_send=encryptedString;
            		  legal_flag=1;
            		  
                      
                }
                else {
                	System.out.println("illegal Device");
                	break;
                }
                is.close();
                os.close();
                newSock.close();
                stop = false;
                end = false;
                break;
         /*       if(inputLine.equals("END")) {
                    System.out.println("ciclo uscita");
                    end = false;
                    is.close();
                    os.close();
                    newSock.close();               
                }
                
                if(inputLine.equals("STOP")) {
                    os.println( "Server aborted!");
                    is.close();
                    os.close();
                    newSock.close();
                    stop = false;
                    end = false;
                    }
      
                */
                }
                }
                

                
                //Thread th = new HandleConnectionThread(newSock);
               // th.start();
            
        } 
        catch (IOException e) {
            System.err.println("Error2 " + e);
        }
    }
    public static void connecting1() throws IOException {
    	int port = DEFAULT_PORT+1;
        
        
        
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);            
            Socket newSock  = null;
            Boolean stop = true;
            Boolean end = true;
            while(stop == true) { 
                //Waiting for the connection with the client
                newSock = serverSocket.accept(); 
                while(end == true){
                	BufferedReader is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
                    PrintWriter os = new PrintWriter(newSock.getOutputStream(), true);
                	os.println(message_to_send);
                	os.flush(); 
                	stop = false;
                	end = false;
                }}}
               
        catch (IOException e) {
            System.err.println("Error2 " + e);
        }
    	
    	
    	
    }
    public static void connect2() {
int port = DEFAULT_PORT+2;
        
        
        
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);            
            Socket newSock  = null;
            Boolean stop = true;
            Boolean end = true;
            int temp_k=0;
            while(stop == true) { 
            	//System.out.println("Server TCP ready at the port: " + port + "..." );

                //Waiting for the connection with the client
                newSock = serverSocket.accept(); 
                while(end == true){
                	System.out.println("success connect");
                    BufferedReader is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
                    PrintWriter os = new PrintWriter(newSock.getOutputStream(), true); 
                    String inputLine = is.readLine(); 
                  //  System.out.println("Received message: " + inputLine);
                   // String decrypted_message=AESM.decrypt(inputLine,Sessionkey_com);
                    System.out.println(inputLine);
                    String decrypted_message=AESM.decrypt(inputLine, Sessionkey_com);
                    System.out.println(decrypted_message);
                    String regex = "\\d*";
                    Pattern p = Pattern.compile(regex);
                    int n=0;
                    Matcher m = p.matcher(decrypted_message);
                    int[] x;
                    x=new int[10];
                    String[] y;
                    y=new String[10];
                    while (m.find()) {
                        if (!"".equals(m.group()))
                        //System.out.println("come here:" + m.group());
                       
                        y[n]=m.group();
                        n=n+1;
                        }
                    int temp=Integer.valueOf(y[0]);
                    if(temp_k==temp) {counter--;}
                    temp_k=temp;
                    temp=temp-1;
                    String flag_temp=Integer.toString(temp);
                    String message="ok"+" "+flag_temp;
                    message=AESM.encrypt(message, Sessionkey_com);
                    System.out.printf("Communication times left: %d",temp);
                    System.out.printf("\n");
                    os.println(message);
                	os.flush(); 
                	int flag;
                	flag=message.compareTo("over");
                	 if(flag==0)
                	{stop = false;
                	end = false;}
                }}}
               
        catch (IOException e) {
            System.err.println("Error2 " + e);
        }
    	
}
    // end main
    public static void main(String[] args) throws IOException{
    	database();
    	connecting();
    	connecting1();
    	System.out.println("sessionkey1: "+Sessionkey1+" Sessionkey2: "+SessionKey2);
    	System.out.println("Authentication done");
    	System.out.println("===============================");
    	int ses1=Integer.valueOf(Sessionkey1);
    	int ses2=Integer.valueOf(SessionKey2);
    	System.out.println("ses1: "+Sessionkey1+" ses2: "+SessionKey2);
    	int com_sessionkey=ses1|ses2;
    	int quotient1 = 0;//商數
		  String output_server = " "; 
		  
		  //計算結果
		  for(int n2 = 1; n2 <= 4; n2++ )//計算每四個字元要空白
		  {
		   for(int i2 = 1; i2 <= 4 ; i2 ++)
		   {
		     int remainder1 = com_sessionkey % 2;//餘數
		     quotient1 = com_sessionkey / 2 ;
		     com_sessionkey = quotient1;
		     output_server = remainder1 + output_server;  
		   }  
		  
		  output_server =" " + output_server;//印出空白
		  }
		  Sessionkey_com = output_server;
		 // System.out.println(Sessionkey_com);
    	int n;
    	
    		connect2();
    		System.out.println("Session end");
    		System.out.println("===============================");
    	
    }
}