package SCC;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class Client{
  
    private static Socket socket;
    public static String SessionKey;
    public static String SessionKey2;
    public static int client_key;
    public static int aug1;
    public static int aug2;
    public static String clientchallenge;
    public static int legal_flag;
    public static String Sessionkey_com;
    public static int counter=0;
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
  	      Class.forName("com.mysql.cj.jdbc.Driver");     //載入MYSQL JDBC驅動程式   
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
	    		temp[i]=key[i].substring(8,12);//從資料庫中取其中片段
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
    public static void connect() throws IOException
    {
        try
        {
            String host = "localhost";
            int port = 5030;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            
            int pw1=(int)q[0];
            int pw2=(int)q[1];
            int pw3=(int)q[2];
            int pw4=(int)q[3];
            int[] pw= {0,pw1,pw2,pw3,pw4};
            String DeviceID="552";
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            
            Scanner in = new Scanner(System.in);
            System.out.println("Link start");

            
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            
            
            
           // while(!DeviceID.equals("END")) { //verify the syntax
            
            	String sendMessage = DeviceID + "\n";
                bw.write(sendMessage);
                bw.flush();
                //System.out.println("DeviceID sent to the server : "+sendMessage);
            //Get the return message from the server
         
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            //System.out.println("c1&challenge received from the server : " +message);
            
            String regex = "\\d*";
            Pattern p = Pattern.compile(regex);
            int n=0;
            Matcher m = p.matcher(message);
            int[] x;
            x=new int[10];
            String[] y;
            y=new String[10];
            //取字串中的數字
            while (m.find()) {
            if (!"".equals(m.group()))
            y[n]=m.group();
            n=n+1;
            }
            //把數字加完後二元化成16位元數字
            String a=y[0];
            String b=y[2];
            int i = Integer.valueOf(a);
            int k = Integer.valueOf(b);
            int key=pw[i]|pw[k];
            client_key=key;            
            int quotient = 0;//商數
  		  String output = " "; 
  		  System.out.println("Initial received from server:c1={"+a+","+b+"} r1="+y[5]);
  		  //計算結果
  		  for(int n1 = 1; n1 <= 4; n1++ )//計算每四個字元要空白
  		  {
  		   for(int i1 = 1; i1 <= 4 ; i1 ++)
  		   {
  		     int remainder = key % 2;//餘數
  		     quotient = key / 2 ;
  		     key = quotient;
  		     output = remainder + output;  
  		   }  
  		  
  		  output = "" + output;//印出空白
  		  }
  		final String secretKey = output;
  		Random ran=new Random();
  		
    	String Sessionkey=Integer.toString(ran.nextInt(1000));
    	String Client_challenge=Integer.toString(ran.nextInt(1000));
    	
  		SessionKey=Sessionkey;
  		clientchallenge=Client_challenge;
  		
  		String random_key=Integer.toString(ran.nextInt(4)+1);
    	String random_key1=Integer.toString(ran.nextInt(4)+1);
    	
    	aug1=Integer.valueOf(random_key);
    	aug2=Integer.valueOf(random_key1);
    	
	    //加密
	    String originalString = y[5]+" "+Sessionkey+" "+random_key+" "+random_key1+" "+Client_challenge;
	    String encryptedString = AESM.encrypt(originalString, secretKey) ;
	    System.out.println("Original message send to Server: r2="+Client_challenge+" t2="+Sessionkey+" c2={"+random_key+","+random_key1+"} r1="+y[5]);
	     
	    //System.out.println("original message:challenge/sessionkey1/c2/client challenge"+originalString);
	   
	    
	    bw.write(encryptedString);
        bw.flush();
        System.out.println("Encrypted Message sent to the server : "+encryptedString);
            //System.out.println("Stop");
            
            
            
          //  }
            
        }
        
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        
        finally
        {
            //Closing the socket
            try
            {
            	
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
         
    }
    public static void connect1() throws IOException
    {
        try
        {	
		
		int pw1=(int)q[0];
        int pw2=(int)q[1];
        int pw3=(int)q[2];
        int pw4=(int)q[3];
        int[] pw= {0,pw1,pw2,pw3,pw4};
            String host = "localhost";
            int port = 5031;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            
            Scanner in = new Scanner(System.in);
           
            
            
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            System.out.println("Message received from the server : " +message);
            int key2=pw[aug1]|pw[aug2];
            int key1key2=key2|client_key;

            int quotient1 = 0;//商數
  		  String output_server = " "; 
  		  
  		  //計算結果
  		  for(int n2 = 1; n2 <= 4; n2++ )//計算每四個字元要空白
  		  {
  		   for(int i2 = 1; i2 <= 4 ; i2 ++)
  		   {
  		     int remainder1 = key1key2 % 2;//餘數
  		     quotient1 = key1key2 / 2 ;
  		     key1key2 = quotient1;
  		     output_server = remainder1 + output_server;  
  		   }  
  		  
  		  output_server =" " + output_server;//印出空白
  		  }
  		  final String secretKey1 = output_server;
  		  
  		String decryptedString = AESM.decrypt(message, secretKey1) ;
  		//System.out.println(decryptedString);
  			
  		String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        int n=0;
        Matcher m = p.matcher(decryptedString);  
        String[] y;
        y=new String[10];
        //取字串中的數字
        while (m.find()) {
        if (!"".equals(m.group()))
        y[n]=m.group();
        n=n+1;
        }
        System.out.println("Decrypted message: r2="+clientchallenge+" t2= "+y[2]);
        int a=Integer.valueOf(clientchallenge);
        int b=Integer.valueOf(y[0]);
        SessionKey2=y[2];
        if(a==b) {System.out.println("It's legal");
        legal_flag=1;}
        else
        {System.out.println("illegal");}
            
            }
        
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        
        finally
        {
            //Closing the socket
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }}
    public static void connect2() {
    	try
        {	
            String host = "localhost";
            int port = 5032;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            
            Scanner in = new Scanner(System.in);
            String s="";
            System.out.println("After authentication,communication start!");
            int n=10;
            Random ran=new Random();
      		
        	
            while(n!=0){
            	
            	
            	Thread.sleep(1000);
            	s=Integer.toString(ran.nextInt(1000));
            	System.out.println("Message to send:"+s);
            	String temp_n=Integer.toString(n);
                String sendMessage = temp_n + " "+s+"\n";
                String encrypted_message=AESM.encrypt(sendMessage, Sessionkey_com)+"\n";
                bw.write(encrypted_message);
                bw.flush();
                
                long d=System.currentTimeMillis();
                
            
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message="";
          long d2=System.currentTimeMillis();
            //System.out.println(d2);
            if((d2-d)>3000) {counter--;break;}
           
            	message = br.readLine();
            	
            
            System.out.println("Message received from the server : " +message);
            
            
            String decrypted_message=AESM.decrypt(message, Sessionkey_com);
            System.out.println("Decrypted message:"+decrypted_message);
            String regex = "\\d*";
            Pattern p = Pattern.compile(regex);
            int x=0;
            Matcher m = p.matcher(decrypted_message);
            
            String[] y;
            y=new String[10];
            //取字串中的數字
            while (m.find()) {
            if (!"".equals(m.group()))           	
            y[x]=m.group();
            x=x+1;
            }
           
           
            int temp=Integer.valueOf(y[3]);
            temp=temp-1;
            n=temp;
            System.out.printf("Communication times left: %d",n);
            System.out.printf("\n");
            String ending=AESM.encrypt("Session end", Sessionkey_com);
            if(n==0) {
            	
            	bw.write(ending);
                bw.flush();
            }
            
        }
        }
  		
        
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        
        finally
        {
            
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    	}

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException{
    	database();
    	long startTime=System.currentTimeMillis(); 
    	connect();
    	connect1();
    	long endTime=System.currentTimeMillis(); 
    	System.out.println("Authentication time：" +(endTime-startTime)+"ms"); 
    	int ses1=Integer.valueOf(SessionKey);
    	int ses2=Integer.valueOf(SessionKey2);
    	//System.out.println("ses1: "+SessionKey+" ses2: "+SessionKey2);
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
    	
    	
    	if(legal_flag==1)
    	{System.out.println("Authentication ok");
    	System.out.println("===============================");   		
    	}
    	long startTime1=System.currentTimeMillis(); 
    	connect2();
    	long endTime1=System.currentTimeMillis(); 
    	System.out.println("Communication time ：" +(endTime1-startTime1)+"ms"); 
    	System.out.println("OK,Session end");
    	System.out.println("session end");
    	System.out.println("===============================");
    	if(counter!=0) {
    		System.out.print("Counter="+counter+"\n");
    		Keymanager.change();
    		System.out.println("Change complete");
    	}
    	else {
    		Boolean end=true;
    		while(end ==true){

    		System.out.print("Counter="+counter+"\n");
    		System.out.println("Do not change");
    		Thread.sleep(3000);
    		System.out.println("Wait for 3 second..");
    		String host = "localhost";
            int port = 5035;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            String s1="Environment test";
           // System.out.println(s1);
            bw.write(s1);
            bw.flush();
            long startTime2=System.currentTimeMillis(); 
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s;
            s=br.readLine();
            System.out.println(s);
            long endTime2=System.currentTimeMillis();
            if((startTime2-endTime2)<3000) {
            	System.out.println("Ok,Authentication again");
            	end=false;
            	break;}
            }
    		
    	}
    		
    	
    	
    }
	
}
