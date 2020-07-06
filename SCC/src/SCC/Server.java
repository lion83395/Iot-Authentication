package SCC;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
            File file = new File("C:\\\\Users\\\\Yosoro\\\\OneDrive\\\\桌面\\\\test.txt");
    		Scanner scanner = new Scanner(file);
    		String str[] = new String[7];
    		int num=0;
    		while (scanner.hasNext()) 
             {
                  
                  str[num] = scanner.next();		//字串分割 存入陣列
                  num++;
                  
             }
    		scanner.close();
            int pw1=Integer.valueOf(str[0]);
            int pw2=Integer.valueOf(str[1]);
            int pw3=Integer.valueOf(str[2]);
            int pw4=Integer.valueOf(str[3]);
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
    // end main
    public static void main(String[] args) throws IOException{
    	connecting();
    	
    	connecting1();
    	System.out.println("sessionkey1: "+Sessionkey1+" Sessionkey2: "+SessionKey2);
    	System.out.println("ok");
    }
}