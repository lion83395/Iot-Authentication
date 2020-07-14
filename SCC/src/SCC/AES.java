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
import java.util.Arrays;
import java.io.FileWriter;
import java.math.*;



public class AES {
	private static long transDec2(String in) {
	    try {
	        long out = Long.valueOf(in,16).intValue();
	        return out;
	    }catch (Exception e){
	        return Long.valueOf(0);
	    }
	}
	public static void change() throws IOException 
	{
		
		 String str[] = new String[7];
		try {
			File file = new File("C:\\\\Users\\\\Yosoro\\\\OneDrive\\\\桌面\\\\test.txt");
			Scanner scanner = new Scanner(file);
			int num=0;
			while (scanner.hasNext()) 
	         {
	              
	              str[num] = scanner.next();		//字串分割 存入陣列
	              num++;
	              
	         }
				//System.out.println(str[0]+str[1]+str[2]+str[3]);
				String ret[]=new String[7];
				String a[]=new String[7];
				try {
					MessageDigest md = MessageDigest.getInstance("MD5");
					for (int j=0;j<num;j++)
					{md.update(str[j].getBytes());
					ret[j] = new BigInteger(1, md.digest()).toString(16);
					 a[j]=ret[j].substring(12, 18);
					 //System.out.println(a[j]);
					 }
					
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long q[]=new long[7];
					for(int k=0;k<num;k++) {
					q[k]=transDec2(a[k]);
					//System.out.println(q[k]);
					}
				//File file1 = new File("C:\\\\Users\\\\Yosoro\\\\OneDrive\\\\桌面\\\\test1.txt");
				FileWriter fw=new FileWriter("C:\\\\\\\\Users\\\\\\\\Yosoro\\\\\\\\OneDrive\\\\\\\\桌面\\\\\\\\test.txt");
				String write_to[]=new String[7];
				String temp[]=new String[7];
				for(int n=0;n<num;n++) {
					temp[n]=Long.toString(q[n]);
				}
				for(int i=0;i<num;i++) {
					write_to[i]=temp[i];
					fw.write(temp[i]+"\n");
				}
				fw.close();

			System.out.println("Done");
				
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
