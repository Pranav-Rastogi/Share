import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.EOFException;
import javax.swing.JFileChooser;

public class Server {

	private static int buffer = 16000000;
	private static ServerSocket server_socket;
	private static Socket socket;
	private static BufferedOutputStream p_output;
	private static BufferedInputStream input;
	private static byte[] b = new byte[buffer];
	private static int n;

	/*
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

			try {
				while(true) {
					waitForConnection();
					setupStreams();
					transferData();
				}
			} catch(EOFException eofe) {
				System.out.println("Error: " + eofe);
			} finally {
				closeCrap();
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
		System.out.println("Setting output stream...");
		String dir = getDirectory();
		p_output = new BufferedOutputStream(new FileOutputStream(dir + "/new"), buffer);

		System.out.println("Output stream set\nSetting up input stream...");
		input = new BufferedInputStream(socket.getInputStream(), buffer);
		System.out.println("Input stream set");
	}

	/*
	 *
	 * The read() method takes data from BufferedInputStream input and writes
	 * it to the byte array b from element 0 to 16000000-1. The number of bytes
	 * read are stored in n.
	 *
	 * The write() method writes data from b to BufferedOutputStream output
	 * from position 0 to the number of bytes written.
	 *
	 * The write() method writes only till n as writting the whole array
	 * creates problem during the last iteration when all array elements are
	 * not reinitialized and the last few elemnts contain leftover values from
	 * the last iteration.
	 *
	 */
	private static void transferData() throws IOException {
		while((n=input.read(b, 0, buffer)) != -1) {
			p_output.write(b, 0, n);
		}
	}

	/*
	 *
	 * closeCrap() Closes all BufferedInputStream, BufferedOutputStream,
	 * ServerSocket and Socket after all the work is done. 
	 *
	 */
	private static void closeCrap() throws IOException {
		server_socket.close();
		socket.close();
		p_output.close();
		input.close();
	}

	/*
	 *
	 * Use a JFileChooser to select the directory where the server wishes to
	 * save the files that will be sent by the client.
	 *
	 */
	private static String getDirectory() {
		String file = "";

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ans = fc.showOpenDialog(null);

		if(ans == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile().getAbsolutePath();
		}

		return file;
	}
}