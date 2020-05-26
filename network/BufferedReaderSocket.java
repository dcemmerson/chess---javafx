package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import controller.NetworkController;

public class BufferedReaderSocket extends BufferedReader {
	public static int NUMBER_SUBSTRINGS = 2;
	
	public BufferedReaderSocket(Socket s) throws IOException{
		super(new InputStreamReader(s.getInputStream()));
	}
	
	public String readSocket() throws IOException {
		String str = "";
		String s[] = new String[NUMBER_SUBSTRINGS];
		
		
		while(!str.contains(NetworkController.ENDLINE)) {
			str += (char)read();
		}
		
		s = str.split(NetworkController.ENDLINE_REGEX, NUMBER_SUBSTRINGS);
		System.out.println("line read: " + s[0]);
		return s[0];
	}
	
	
}
