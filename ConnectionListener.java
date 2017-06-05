import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
// Luis Cortes
// CS 380
// Exercise 8


/**
 *	This class handles the connections between the server and the web browser client
 */
public class ConnectionListener extends Thread {
	private final Socket socket;
	private final BufferedReader buffReader;

	public ConnectionListener(Socket socket) throws Exception {
		this.socket = socket;
		this.buffReader = new BufferedReader(
			new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		StringBuilder string = new StringBuilder();
		String input;

		try {
			// Read in requests from client
			while( buffReader != null) {
				input = buffReader.readLine();
				
				if (input.contains("GET")) {
					processReq(input.split(" "));	
					break;		
				}
			}

			// System.out.println(input);
		} catch (Exception e) {e.printStackTrace();}
	}

	/**
	 *	Send requested path along with header, or send error file along with header
	 */
	private void processReq(String[] tokens) throws Exception {
		String path = tokens[1]; // Location to Path
		File file = new File("www"+path); // www/path is where file should exist

		if (file.exists()) { // File exists
			respond(file, "200 OK");
			// System.out.println("File exists." + file.getAbsoluteFile());
		} else { // File not found
			File file2 = new File("www/fileNotFound.html");
			System.out.println("File not found");
			respond(file2, "404 Not Found");
		}
	}

	/**
	 *	Set the header and send the appropriate file
	 */
	private void respond(File file, String statusCode) throws Exception {
		// Send header info out
		PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
		StringBuilder header = new StringBuilder();

		header.append("HTTP/1.1 "+statusCode+" \r\n");
		header.append("Content-type: text/html \r\n");
		header.append("Connection: close \r\n");
		header.append("Content-length: "+file.length()+" \r\n");
		header.append("\r\n");
		
		System.out.print(header.toString());
		writer.write(header.toString());

		// Read in text file locally and then send to client
		BufferedReader reader = new BufferedReader(new FileReader(file));

		for (String line; (line = reader.readLine()) != null;) {
			writer.println(line);
		}
		// writer.flush();
		System.out.println("RESPONDED");

	}
}