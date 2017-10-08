import java.net.Socket;
import java.nio.file.Files;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.BufferedInputStream;
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
				String fileName = getFileName(file);
				setUpStreams(fileName,file);
				transferData();
				closeCrap();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
	}

	/*
	 *
	 * The getTransferFile() method takes the URL of the file to be transfered as
	 * String and then returns the URL as a String.
	 *
	 */
	private static String getTransferFile() {
		String file = "";

		JFileChooser fc = new JFileChooser();
		int ans = fc.showOpenDialog(null);
		if (ans == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile().getAbsolutePath();

		}

		return file;
	}
	
	//The getfileNameMethod gets us the filename so we can use it in the setUpStreams to send over the filename first . 

	private static String getFileName(String filePath) {
		File file = new File(filePath);
		String fileName;
		fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1);
		System.out.println(fileName);
		return fileName;
	}

	/*
	 *
	 * The setUpStreams() method sets up the BufferedInputStream p_input and
	 * BufferedOutputStream output. The p_input takes a FileInputStream pointing
	 *
	 */
	private static void setUpStreams(String fileName,String path) throws IOException {
	
		File file = new File(path);
		output = new BufferedOutputStream(socket.getOutputStream());
		try (DataOutputStream d = new DataOutputStream(output)) {
		    d.writeUTF(fileName);
		    Files.copy(file.toPath(), d);
		}

		
		
	}


	private static void transferData() throws IOException {
		while ((n = p_input.read(b, 0, buffer)) != -1) {
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
