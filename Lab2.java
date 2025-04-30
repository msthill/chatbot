//COSC 20203
//Name: Micah St.Hill
//Date: 03/13/2025
//Email: micah.sthill@tcu.edu
//Assignment: Lab 2 - Playfair Cipher - Encryption and Decryption

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Lab2 extends JFrame implements ActionListener {
	JTextArea result = new JTextArea(20,54);
	JLabel errors = new JLabel();
	JScrollPane scroller = new JScrollPane();
	JButton openButton = new JButton("Get File");
	JButton encryptButton = new JButton("Encrypt");
	JButton decryptButton = new JButton("Decrypt");
	JPanel buttonPanel = new JPanel();
	JLabel phraseLabel = new JLabel("Secret Phrase:");
	JTextField phraseTF = new JTextField(40);
	JPanel phrasePanel = new JPanel();
	JPanel southPanel = new JPanel();
	
	public Lab2() {
		setLayout(new java.awt.BorderLayout());
		setSize(700,430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		scroller.getViewport().add(result);
		result.setLineWrap(true);
		result.setWrapStyleWord(true);
		add(scroller, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new java.awt.GridLayout(3,1));
		southPanel.add(buttonPanel);
		buttonPanel.add(openButton); openButton.addActionListener(this);
		buttonPanel.add(encryptButton); encryptButton.addActionListener(this);
		buttonPanel.add(decryptButton); decryptButton.addActionListener(this);
		southPanel.add(phrasePanel);
		phrasePanel.add(phraseLabel);
		phrasePanel.add(phraseTF);
		southPanel.add(errors);
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == openButton) {
			getFile();
		}
		else if (evt.getSource() == encryptButton) {
			encrypt();
		}
		else if (evt.getSource() == decryptButton) {
			decrypt();
		}
	}
	
	public static void main(String[] args) {
		Lab2 display = new Lab2();
		display.setVisible(true);
	}
	
	//display a file dialog
	String getFileName() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath();
		else
			return null;
	}
	
	//read the file and display it in the text area
	void getFile() {
		String fileName = getFileName();
		if (fileName == null)
			return;
		result.setText("");
		try{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = in.readLine()) != null) {
				result.append(line + "\n");
			}
			in.close();
		} catch(IOException ioe) {
			errors.setText("ERROR: " + ioe);
		}
	}
	
/***********************************************************************/
	void encrypt() 
	{
		// Get the user input and secret phrase by using the .getText from the JTextFields.
		String userInput = result.getText();
		String secretPhrase = phraseTF.getText();

		// Create a string with all 81 characters, and then create an empty string matrix, which will be used to store the characters that are in the secret phrase and the 81 characters.
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
		String matrix = "";

		// Calls the validateCharacters method that passes in userInput and chars string, the method then removes any unwanted characters from the user input such as !.
		userInput = validateCharacters(userInput, chars);

		// Create a string that concatenates the secret phrase from the user into the string with the characters.
		String combinedString = secretPhrase + chars;

		// Loop through the combined string and use indexOf to check if the character exists in both the matrix, and chars string. If it does, add it to the matrix string.
		for (int i = 0; i < combinedString.length(); i++) 
		{
			char c = combinedString.charAt(i);
			if (matrix.indexOf(c) == -1 && chars.indexOf(c) != -1) 
			{
				matrix += c;
			}
		}

		// Creates an empty string called encryptedString, which will be used to store the encrypted characters.
		String encryptedString = "";

		// Loop through the user input, and initiates the char c1 to the character at the index i of the user input, the if else then checks if the character is in the matrix using indexOf.
		for (int i = 0; i < userInput.length(); i++) 
		{
			// Get the character at the index i of the user input.
			char c1 = userInput.charAt(i);

			// Check if the character is not in the matrix
			if (matrix.indexOf(c1) != -1) 
			{
				// Checks if another character exists after c1 in the user input in order to encrypt the message in pairs, this is the help when you have an odd number of characters, to ensure the final character is output without change.
				// Rule E: If the last character is the first of a pair, it is output without change.
				if (i + 1 >= userInput.length())
				{
					// If the character is the last character in the string with no pair, add it to the encrypted string.
					encryptedString += c1;
					break;
				}

				// Get the character at the index i+1 of the user input.
				char c2 = userInput.charAt(i + 1);

				// Checks if c2 is in the matrix
				if (matrix.indexOf(c2) == -1) 
				{
					// Only add c1 to the string if c2 is not in the matrix
					encryptedString += c1;
				}

				else 
				{
					// Rule D: If the two characters are the same, they are output without change.
					if (c1 == c2) 
					{
						encryptedString += c1;
						encryptedString += c2;
						i++; // Increment i to skip the next character
					} 

					else
					{
						// index1 and index2 store the index of c1 and c2, assuming that both characters exist in the matrix.
						int index1 = matrix.indexOf(c1);
						int index2 = matrix.indexOf(c2);
						// row is calculated by taking the index, and dividing by 9. This works as if you envision the 81 character string as a 9x9 matrix, there are 9 columns per row, so dividing by 9 gives the row.
						int row1 = index1 / 9;
						// col is calculated by taking the index, and using the modulo operator to get the remainder of the division by 9. This works as if you envision the 81 character string as a 9x9 matrix, the remainder will give the column.
						int col1 = index1 % 9;

						// Same as above, but for c2
						int row2 = index2 / 9;
						int col2 = index2 % 9;

						// Rule B: If I1 and I2 are in the same row (eg, “Z” and “i”), then O1 and O2 are the characters to their immediate right using a wraparound if necessary (“b” and “X”).
						if (row1 == row2) 
						{
							index1 = row1 * 9 + (col1 + 1) % 9;
							index2 = row2 * 9 + (col2 + 1) % 9;
						}
						// Rule C: If I1 and I2 are in the same column (eg, “<” and “2”), then O1 and O2 are the characters directly below using a wraparound if necessary (“y” and “(“).
						else if (col1 == col2) 
						{
							index1 = (index1 + 9) % 81;
							index2 = (index2 + 9) % 81;
						}
						// Rule A: If I1 and I2 are found at the corners of a rectangle (for example, “f” and “s”), then O1 and O2 are the other two corners starting with the same row as I1 (“Z” and “A”).
						else 
						{
							index1 = row1 * 9 + col2;
							index2 = row2 * 9 + col1;
						}

						// Add the characters to the encrypted string, and increment i by 1 to skip the next character in the user input.
						encryptedString += matrix.charAt(index1);
						encryptedString += matrix.charAt(index2);
						i++;
					}
				}
			}
		}
		// Set the text of the result JTextArea to the encrypted string.
		result.setText(encryptedString);

		// Test to print out string in matrix form
		char[] matrixArray = matrix.toCharArray();
		for (int i = 0; i < matrixArray.length; i++) 
		{
			if (i % 9 == 0) 
			{
				System.out.println();
			}
			System.out.print(matrixArray[i]);
		}
	}

	// Method to validate the characters in the user input and remove characters not included in the 81 characters, like !
	String validateCharacters(String userInput, String chars) 
	{
		// Create an empty string to store the validated encryption string
		String validatedEncryptionString = "";
		
		// Loop through the user input and check if the character is in the chars string
		for (int i = 0; i < userInput.length(); i++) 
		{
			// Get the character at the index i of the user input
			char c = userInput.charAt(i);
			// Check if the character in userinput is in the chars string
			if (chars.indexOf(c) != -1) {
				// If the character is in the chars string, add it to the validatedEncryptionString
				validatedEncryptionString += c; 
			}
		}
		
		// Return the validated encryption string
		return validatedEncryptionString;
	}
	
	
	void decrypt() 
	{
		// Get the user input and secret phrase by using the .getText from the JTextFields.
		String userInput = result.getText();
		String secretPhrase = phraseTF.getText();
	
		// Create a string with all 81 characters, and then create an empty string matrix, which will be used to store the characters that are in the secret phrase and the 81 characters.
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
		String matrix = "";
	
		// Create a string that concatenates the secret phrase from the user into the string with the characters.
		String combinedString = secretPhrase + chars;
	
		// Loop through the combined string and use indexOf to check if the character exists in both the matrix, and chars string. If it does, add it to the matrix string.
		for (int i = 0; i < combinedString.length(); i++) 
		{
			char c = combinedString.charAt(i);
			if (matrix.indexOf(c) == -1 && chars.indexOf(c) != -1) 
			{
				matrix += c;
			}
		}
	
		// Creates an empty string called decryptedString, which will be used to store the decrypted characters.
		String decryptedString = "";
	
		// Loop through the user input, and initiates the char c1 to the character at the index i of the user input, the if else then checks if the character is in the matrix using indexOf.
		for (int i = 0; i < userInput.length(); i++) 
		{
			// Get the character at the index i of the user input.
			char c1 = userInput.charAt(i);
	
			// Check if the character is not in the matrix
			if (matrix.indexOf(c1) != -1) 
			{
				// Checks if another character exists after c1 in the user input in order to decrypt the message in pairs, this is the help when you have an odd number of characters, to ensure the final character is output without change.
				if (i + 1 >= userInput.length()) 
				{
					// If the character is the last character in the string with no pair, add it to the decrypted string.
					decryptedString += c1;
					break;
				}
	
				// Get the character at the index i+1 of the user input.
				char c2 = userInput.charAt(i + 1);
	
				// Checks if c2 is in the matrix
				if (matrix.indexOf(c2) == -1) 
				{
					// Only add c1 to the string if c2 is not in the matrix
					decryptedString += c1;
				} 

				else 
				{
					// Rule D: If the two characters are the same, they are output without change.
					if (c1 == c2) 
					{
						decryptedString += c1;
						decryptedString += c2;
						i++;
					} 

					else
					{
						// index1 and index2 store the index of c1 and c2, assuming that both characters exist in the matrix.
						int index1 = matrix.indexOf(c1);
						int index2 = matrix.indexOf(c2);

						// row is calculated by taking the index, and dividing by 9. This works as if you envision the 81 character string as a 9x9 matrix, there are 9 columns per row, so dividing by 9 gives the row.
						int row1 = index1 / 9;

						// col is calculated by taking the index, and using the modulo operator to get the remainder of the division by 9. This works as if you envision the 81 character string as a 9x9 matrix, the remainder will give the column.
						int col1 = index1 % 9;
	
						// Same as above, but for c2
						int row2 = index2 / 9;
						int col2 = index2 % 9;
	
						// Rule C: If I1 and I2 are in the same column, shift up.
						if (col1 == col2) 
						{
							index1 = ((row1 - 1 + 9) % 9) * 9 + col1;
							index2 = ((row2 - 1 + 9) % 9) * 9 + col2;
						}
						// Rule B: If I1 and I2 are in the same row, shift left.
						else if (row1 == row2) 
						{
							index1 = row1 * 9 + ((col1 - 1 + 9) % 9);
							index2 = row2 * 9 + ((col2 - 1 + 9) % 9);
						}
						// Rule A: If I1 and I2 are found at the corners of a rectangle (for example, “f” and “s”), then O1 and O2 are the other two corners starting with the same row as I1 (“Z” and “A”).
						else 
						{
							index1 = row1 * 9 + col2;
							index2 = row2 * 9 + col1;
						}
	
						// Add the characters to the decrypted string, and increment i by 1 to skip the next character in the user input.
						decryptedString += matrix.charAt(index1);
						decryptedString += matrix.charAt(index2);
						i++;
					}
				}
			}
		}
		// Set the text of the result JTextArea to the decrypted string.
		result.setText(decryptedString);
	}
}

