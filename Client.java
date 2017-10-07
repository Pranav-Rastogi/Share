import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class Client {

	private static final Scanner kb = new Scanner(System.in);
	private static BufferedOutputStream output;
	private static BufferedInputStream p_input;
	private static final int buffer = 16000000;
	private static final byte[] b = new byte[buffer];
	private static Socket socket;
	private static int n;

	public static void main(String args[]) {
		int port = 69;

		System.out.print("Enter The Server IP Address: ");
		String ip = kb.nextLine();

		try {
			socket = new Socket(ip, port);

			String file = getTransferFile();
			setUpStreams(file);
			transferData();
			closeCrap();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/*
	 *
	 * The getTransferFile() method takes the URL of the file to be transfered
	 * as String and then returns the URL as a String.
	 *
	 */
	private static String getTransferFile() {
		String file = "";

		JFileChooser fc = new JFileChooser();
		int ans = fc.showOpenDialog(null);
		if(ans == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile().getAbsolutePath();
		}

		return file;
	}

	/*
	 *
	 * The setUpStreams() method sets up the BufferedInputStream p_input and
	 * BufferedOutputStream output. The p_input takes a FileInputStream pointing
	 *
	 */
	private static void setUpStreams(String file) throws IOException {
		p_input = new BufferedInputStream(new FileInputStream(file), buffer);
		output = new BufferedOutputStream(socket.getOutputStream(), buffer);
	}

	private static void transferData() throws IOException {
		while((n = p_input.read(b, 0, buffer)) != -1) {
				output.write(b, 0, n);
		}
	}

	private static void closeCrap() throws IOException {
		kb.close();
		p_input.close();
		output.close();
		socket.close();
	}
}
