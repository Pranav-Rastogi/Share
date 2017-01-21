import java.net.*;
import java.io.*;
public class Server {

	private static ServerSocket server_socket;
	private static Socket socket;
	private static BufferedOutputStream output, p_output;
	private static BufferedInputStream input;
	private static byte[] b = new byte[16000000];
	private static int n;

	/*
	 *
	 * @Author: Pranav Rastogi
	 *
	 * ServerSocket class sets up a server on the local machine that listens on
	 * port number 69 and can support a backlog of 10 users.
	 * 
	 */
	public static void main(String args[]) {
		int port = 69;
		int backlog = 10;

		try {
			server_socket = new ServerSocket(port, backlog);

			while(true) {
				try {
					waitForConnection();
					setupStreams();	
					transferData();
				} catch(EOFException eofe) {

				} finally {
					//closeCrap();
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/*
	 *
	 * Waits till a client connects to the server then prints the IP address of
	 * the client.
	 *
	 * The accept() method loops till a connection is recieved and then returns
	 * a java.net.Socket object containing the info about the remote computer.
	 *
	 * The getInetAddress() method gets the IP address and the getHostName()
	 * converts it to String.
	 *
	 */
	private static void waitForConnection() throws IOException {
		System.out.println("Waiting for connection...");
		socket = server_socket.accept();
		String host_name = socket.getInetAddress().getHostName();
		System.out.println("Connected to " + host_name);
	}

	/*
	 *
	 * Sets up the output and input streams of the server.
	 * The output stream of the server is automatically connected
	 * to the input stream of the client and vice-versa.
	 *
	 * The socket.getOutputStream() and socket.getInputStream()
	 * methods return the respective stream objects for the
	 * local machine.
	 * 
	 * The flush() method flushes the output stream thus clearing
	 * the output buffer in case there is some data left in the
	 * buffer. There is no flush method for the input stream.
	 *
	 */
	private static void setupStreams() throws IOException {
		output = new BufferedOutputStream(socket.getOutputStream(), 16000000);
		output.flush();
		p_output = new BufferedOutputStream(new FileOutputStream("new"));
		p_output.flush();
		input = new BufferedInputStream(socket.getInputStream(), 16000000);
	}

	/*
	 *
	 */
	private static void transferData() throws IOException {
		while((n=input.read(b, 0, 16000000)) != -1) {
			output.write(b, 0, n);
		}
	}
}