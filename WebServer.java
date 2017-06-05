import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// Luis Cortes
// CS 380
// Exericse 8

public class WebServer {

	public static void main(String[] args) throws Exception {
		// Create a server socket to listen for client
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			while (true) {
				// Server found a connection
				Socket socket = serverSocket.accept();

				new ConnectionListener(socket).start();
				break; // Only need to listen once for testing
			}
		}
	}

}