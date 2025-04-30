import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class SimpleClient extends JFrame implements ActionListener {
	JTextArea result = new JTextArea(20,40);
	JTextField userInput = new JTextField(20);
	JButton connectButton = new JButton("Connect");
	JButton sendButton = new JButton("Send");
	JLabel errors = new JLabel();
	JScrollPane scroller = new JScrollPane();
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	Thread thread;
	
	public SimpleClient() {
		setLayout(new java.awt.FlowLayout());
		setSize(500,430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		scroller.getViewport().add(result);
		add(scroller);
		add(userInput); userInput.addActionListener(this);
		add(sendButton); sendButton.addActionListener(this); sendButton.setEnabled(false);
		add(connectButton); connectButton.addActionListener(this);
		add(errors);
	}
	
	public void actionPerformed(ActionEvent evt) {
		try {
			if (evt.getActionCommand().equals("Connect") || 
					connectButton.getText().equals("Connect") && evt.getSource() == userInput) {
				Scanner scanner = new Scanner(userInput.getText());
				if (!scanner.hasNext()) return;
				String host = scanner.next();
				if (!scanner.hasNextInt()) return;
				int port = scanner.nextInt();
				socket = new Socket(host, port);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), false);
				thread = new ReadThread(in, result);
				thread.start();
				sendButton.setEnabled(true);
				connectButton.setText("Disconnect");
				userInput.setText("");
			}
			else if (evt.getActionCommand().equals("Disconnect")) {
				thread.interrupt();
				socket.close();
				in.close();
				out.close();
				sendButton.setEnabled(false);
				connectButton.setText("Connect");
			}
			else if (evt.getActionCommand().equals("Send") || 
						sendButton.isEnabled() && evt.getSource() == userInput) {
				out.print(userInput.getText() + "\r\n");
				out.flush();
				result.append("> " + userInput.getText() + '\n');
				userInput.setText("");
			}
		} catch(UnknownHostException uhe) {
			errors.setText(uhe.getMessage());
		} catch(IOException ioe) {
			errors.setText(ioe.getMessage());
		}
	}
	
	public static void main(String[] args) {
		SimpleTelnet display = new SimpleTelnet();
		display.setVisible(true);
	}
}

class ReadThread extends Thread {
	BufferedReader in;
	JTextArea display;
	public ReadThread(BufferedReader br, JTextArea jta) {
		in = br;
		display = jta;
	}
	public void run() {
		String s;
		try {
			while ((s = in.readLine()) != null) {
				display.append(s + '\n');
			}
		} catch (IOException ioe) {
			System.out.println("Error reading from socket");
		}
	}
}