import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Client {

	private static int buffer = 16000000;
	private static Socket socket;
	private static BufferedOutputStream output;
	private static BufferedInputStream p_input;
	private static byte[] b = new byte[buffer];
	private static int n;
	private static Scanner kb = new Scanner(System.in);

	public static void main(String args[]) {
		int port = 69;

		try {
			socket = new Socket("127.0.0.1", port);

			String file = getTransferFile();
			setUpStreams(file);
			transferData();
			closeCrap();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private static String getTransferFile() {
		System.out.print("Enter file url: ");
		String file = kb.nextLine();

		return file;
	}

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