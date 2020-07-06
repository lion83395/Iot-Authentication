package SCC;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;
import java.lang.String;
import java.util.Arrays;


public class AES {
	public static void main(String[] args) throws IOException 
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
				System.out.println(str[0]+str[1]+str[2]+str[3]);
				
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
