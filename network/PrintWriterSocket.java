package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import controller.NetworkController;

public class PrintWriterSocket extends PrintWriter {

	public PrintWriterSocket(Socket s) throws IOException {
		super(s.getOutputStream(), true);
	}

	public void printSocket(String str) {
		String s = str + NetworkController.ENDLINE;
		print(s);
		flush();
	}
}
