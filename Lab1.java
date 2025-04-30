//COSC 20203
//Name: Micah St.Hill
//Date: 02/13/2025
//Email: micah.sthill@tcu.edu
//Assignment: Lab 1 - PDP-11 - Demonstrate the use of bitwise operators to encode and decode PDP-11 instructions




package Labs;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Lab1 extends JFrame implements ActionListener {
	private JTextField assemblerInstruction;
	private JTextField binaryInstruction;
	private JTextField hexInstruction;
	private JLabel errorLabel;
	
	public Lab1() {
		setTitle("PDP-11");
		setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		assemblerInstruction = new JTextField();
		assemblerInstruction.setBounds(25, 24, 134, 28);
		getContentPane().add(assemblerInstruction);
		assemblerInstruction.setColumns(10);

		JLabel lblAssemblyLanguage = new JLabel("Assembly Language");
		lblAssemblyLanguage.setBounds(25, 64, 160, 16);
		getContentPane().add(lblAssemblyLanguage);

		binaryInstruction = new JTextField();
		binaryInstruction.setBounds(25, 115, 180, 28);
		getContentPane().add(binaryInstruction);
		binaryInstruction.setColumns(10);

		hexInstruction = new JTextField();
		hexInstruction.setBounds(236, 115, 134, 28);
		getContentPane().add(hexInstruction);
		hexInstruction.setColumns(10);

		JLabel lblBinary = new JLabel("Binary Instruction");
		lblBinary.setBounds(25, 155, 190, 16);
		getContentPane().add(lblBinary);

		JLabel lblHexEquivalent = new JLabel("Hex Instruction");
		lblHexEquivalent.setBounds(236, 155, 131, 16);
		getContentPane().add(lblHexEquivalent);

		errorLabel = new JLabel("");
		errorLabel.setBounds(25, 235, 280, 16);
		getContentPane().add(errorLabel);

		JButton btnEncode = new JButton("Encode");
		btnEncode.setBounds(230, 25, 117, 29);
		getContentPane().add(btnEncode);
		btnEncode.addActionListener(this);

		JButton btnDecode = new JButton("Decode Binary");
		btnDecode.setBounds(30, 183, 170, 29);
		getContentPane().add(btnDecode);
		btnDecode.addActionListener(this);

		JButton btnDecodeHex = new JButton("Decode Hex");
		btnDecodeHex.setBounds(230, 183, 150, 29);
		getContentPane().add(btnDecodeHex);
		btnDecodeHex.addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) {
		errorLabel.setText("");
		if (evt.getActionCommand().equals("Encode")) {
			encode();
		} else if (evt.getActionCommand().equals("Decode Binary")) {
			decodeBin();
		} else if (evt.getActionCommand().equals("Decode Hex")) {
			decodeHex();
		}
	}

	public static void main(String[] args) {
		Lab1 window = new Lab1();
		window.setVisible(true);
	}

	String shortToHex(short x) {
		String ans="";
		for (int i=0; i<4; i++) {
			int hex = x & 15;
			char hexChar = "0123456789ABCDEF".charAt(hex);
			ans = hexChar + ans;
			x = (short)(x >> 4);
		}
		return ans;
	}

	String shortToBinary(short x) {
		String ans="";
		for(int i=0; i<16; i++) {
			ans = (x & 1) + ans;
			x = (short)(x >> 1);
		}
		return ans;
	}



/************************************************************************/
/* Put your implementation of the encode, decodeBin, and decodeHex      */
/* methods here. You may add any other methods that you think are       */
/* appropriate. However, you should not change anything in the code     */
/* that I have written.                                                 */
/************************************************************************/


	// Method to encode the assembly instruction to binary and hex
	void encode()
	{
		//Clears the text fields and error label
		errorLabel.setText("");
		binaryInstruction.setText("");
		hexInstruction.setText("");
		//Takes the text from "Assembly Instruction" TextField and normalizes it by making it uppercase, and then trims whitespace out.
		String assembly = assemblerInstruction.getText().toUpperCase().trim();
		//Breaks the text into tokens to separate the ADD from the commands.
		StringTokenizer assemToken = new StringTokenizer(assembly);
		if(assemToken.countTokens() != 2)
		{
			errorLabel.setText("ERROR: Too many tokens");
			return;
		}
		String assemCommand = assemToken.nextToken();
		//System.out.println(assemCommand);
		//Sets an int binary to 0 to hold the binary value of the equivalent word
		int binary = 0;
		//ADD is equal to 24567 as at the end of a 16 bit binary string, the 0110 will be equivalent to 24567 | 0110000000000000
		switch (assemCommand) {
			//Switch statement to determine the binary value of the command
			case "ADD":
				binary = 24576;
				break;
			//SUB is equal to 57344 as at the end of a 16 bit binary string, the 1110 will be equivalent to 57344 | 1110000000000000
			case "SUB":
				binary = 57344;
				break;
			//MOV is equal to 16384 as at the end of a 16 bit binary string, the 0100 will be equivalent to 16384 | 0100000000000000
			case "MOV":
				binary = 16384;
				break;
			//MOVB is equal to 49152 as at the end of a 16 bit binary string, the 1100 will be equivalent to 49152 | 1100000000000000
			case "MOVB":
				binary = 49152;
				break;
			//CMP is equal to 0 as at the end of a 16 bit binary string, the 0000 will be equivalent to 0 | 0000000000000000
			case "CMP":
				binary = 0;
				break;
			//CMPB is equal to 32768 as at the end of a 16 bit binary string, the 1000 will be equivalent to 32768 | 1000000000000000
			case "CMPB":
				binary = 32768;
				break;
			//If the command is not one of the above, it will set the error label to "ERROR"
			default:
				errorLabel.setText("ERROR: Invalid Command");
				break;
		}


		//Takes the next token from the tokenizer and breaks it into two registers
		String assemOperation = assemToken.nextToken();
		//Breaks the two registers into two separate tokens
		assemToken = new StringTokenizer(assemOperation, ",");

		//Takes the first token and sets it to assemSource
		String assemSource = assemToken.nextToken();
		//System.out.println(assemSource); //To check if the tokenizer is correct.

		//Shifts the binary value of the command to the left by 6 bits and then adds the binary value of the register to the binary value of the command
		//This is done to add the source register to the binary value of the command
		binary = binary | register(assemSource) << 6;
		//System.out.println(binary); //To check if the binary value is correct.

		//Takes the next token from the tokenizer and sets it to assemDestination
		String assemDestination = assemToken.nextToken();
		//System.out.println(assemDestination); // To check if tokenizer is correct.
		//Adds the binary value of the destination register to the binary value of the command and source register
		binary = binary | register(assemDestination);

		//If there is no error, it will set the binary and hex values of the instruction
		if(errorLabel.getText().equals(""))
		{
			binaryInstruction.setText(shortToBinary((short)(binary)));
			hexInstruction.setText(shortToHex((short)(binary)));
		}

	}

	//Method to determine the binary value of the register
	int register(String assemSource)
	{
		//Sets the register number to 0 and the register code to 0
		char registerNumSource = '0';
		int registerCodeSource = 0;

		//If the first character of the string is a '-' it will check if the string is a valid register command
		if(assemSource.charAt(0) == '-')
		{
			if (assemSource.charAt(1) != '(' || assemSource.charAt(4) != ')' || assemSource.charAt(2) != 'R')
			{
        		errorLabel.setText("ERROR: Invalid Register Command");
				assemblerInstruction.setText("");
			}
			//If the string is a valid register command, it will set the register code to 32 and the register number to the 3rd index character of the string
			else
			{
				registerCodeSource = 32;	
				registerNumSource = assemSource.charAt(3);
			}
		}
		//If the first character of the string is a '(' and the 4th character is a ')' and the last character is not a '+' it will check if the string is a valid register command
		else if(assemSource.charAt(0) == '(' && assemSource.charAt(3)  == ')' && assemSource.charAt(assemSource.length() - 1) != '+')
		{
			//If the string is a valid register command, it will set the register code to 8 and the register number to the 2nd index character of the string
			registerCodeSource = 8;
			registerNumSource = assemSource.charAt(2);
		}
		//If the first character of the string is a '(' and the 4th character is a ')' and the last character is a '+' it will check if the string is a valid register command
		else if(assemSource.charAt(0) == '(' && assemSource.charAt(3)  == ')' && assemSource.charAt(assemSource.length() - 1) == '+')
		{
			//If the string is a valid register command, it will set the register code to 16 and the register number to the 2nd index character of the string
			registerCodeSource = 16;
			registerNumSource = assemSource.charAt(2);
		}
		//If the first character of the string is a 'R' it will check if the string is a valid register command
		else if(assemSource.charAt(0) == 'R')
		{
			//If the string is a valid register command, it will set the register code to 0 and the register number to the 2nd index character of the string
			if(assemSource.length() != 2)
			{
				errorLabel.setText("ERROR: Invalid Register Command");
				assemblerInstruction.setText("");
			}
			registerNumSource = assemSource.charAt(1);
		}
		//If the last character of the string is a '+' it will check if the string is a valid register command
		else if(assemSource.endsWith("+"))
		{
			//If the string is a valid register command, it will set the register code to 16 and the register number to the 2nd index character of the string
			registerCodeSource = 16;
			registerNumSource = assemSource.charAt(2);
		}
		else
		{
			errorLabel.setText("ERROR: Invalid Register Command");
			assemblerInstruction.setText("");
		}

		//If the register number is not between 0 and 7, it will set the error label to "ERROR: Invalid Register Number"
		if (registerNumSource < '0' || registerNumSource > '7') 
		{
			errorLabel.setText("ERROR: Invalid Register Number");
			assemblerInstruction.setText("");
			return 0;
		}

		//System.out.println("registerCodeSource: " + registerCodeSource); //To check if the register code is correct.
		//System.out.println("registerNumSource: " + registerNumSource); //To check if the register number is correct.

		// Returns the register code and the numeric value of the character num
		return registerCodeSource + Character.getNumericValue(registerNumSource);
	}

	//Method to decode the binary instruction
	void decodeBin()
	{
		//Clears the text fields and error label
		assemblerInstruction.setText("");
		hexInstruction.setText("");
		errorLabel.setText("");
		
		//Sets the binary value to 0 and takes the text from the "Binary Instruction" TextField and trims the whitespace
		int binary = 0;
		String binaryString = binaryInstruction.getText().trim();

		//If the length of the binary string is not 16, it will set the error label to "ERROR: Invalid Binary Length"
		if(binaryString.length() != 16)
		{
			errorLabel.setText("ERROR: Invalid Binary Length");
		}

		try
		{
			binary = Integer.parseInt(binaryString, 2);
		}
		catch (NumberFormatException e)
		{
			errorLabel.setText("ERROR: Invalid Binary");
			return;
		}

		if(errorLabel.getText().equals(""))
		{
			binary = Integer.parseInt(binaryString,2);
			//Sets the hex value of the binary string
			hexInstruction.setText(shortToHex((short)(binary)));
			//Calls the decodeCommand method, passing the binary value
			decodeCommand(binary);
		}

	}

	//Method to decode the hex instruction
	void decodeHex()
	{
		//Clears the text fields and error label
		assemblerInstruction.setText("");
		binaryInstruction.setText("");
		errorLabel.setText("");

		//Sets the hex value to 0 and takes the text from the "Hex Instruction" TextField and trims the whitespace
		int hex = 0;
		String hexString = hexInstruction.getText().trim();

		//If the length of the hex string is not 4, it will set the error label to "ERROR: Invalid Hex"
		if(hexString.length() != 4)
		{
			errorLabel.setText("ERROR: Invalid Hex");

		}


		//Tries to parses the hex string to an integer
		try
		{
			hex = Integer.parseInt(hexString, 16);
		}
		catch (NumberFormatException e)
		{
			errorLabel.setText("ERROR: Invalid Hex");
			return;
		}

		if(errorLabel.getText().equals(""))
		{
			//Sets the binary value of the hex string
			binaryInstruction.setText(shortToBinary((short)(hex)));
			//Calls the decodeCommand method, passing the hex value
			decodeCommand(hex);
		}
	}

	//Method to decode the co
	void decodeCommand(int binary)
	{
		//61440 = 1111 0000 0000 0000
		String assemblyLanguage = "";

		//Switch statement to determine the assembly language of the command
		switch (binary & 61440) {
			//If the binary value is 24576, it will set the assembly language to "ADD "
			case 24576:
				assemblyLanguage = "ADD ";
				break;
			//If the binary value is 57344, it will set the assembly language to "SUB "
			case 57344:
				assemblyLanguage = "SUB ";
				break;
			//If the binary value is 16384, it will set the assembly language to "MOV "
			case 16384:
				assemblyLanguage = "MOV ";
				break;
			//If the binary value is 49152, it will set the assembly language to "MOVB "
			case 49152:
				assemblyLanguage = "MOVB ";
				break;
			//If the binary value is 0, it will set the assembly language to "CMP "
			case 0:
				assemblyLanguage = "CMP ";
				break;
			//If the binary value is 32768, it will set the assembly language to "CMPB "
			case 32768:
				assemblyLanguage = "CMPB ";
				break;
			//If the binary value is not one of the above, it will set the error label to "ERROR: Invalid Command"
			default:
				errorLabel.setText("ERROR: Invalid Command");
				break;
		}

		//Shifts the binary value to the right by 6 bits and then takes the last 6 bits of the binary value
		// 63 =  0000 0000 0011 1111
		// Needs to shift: 0000 1111 1100 0000 ==> 0000 0000 0000 1111
		int sourceBinary =  binary >> 6 & 63;
		//Calls the decodeSAndD method, passing the sourceBinary value and adds it to the assembly language
		assemblyLanguage = String.valueOf(assemblyLanguage) + decodeSourceDestination(sourceBinary);
		
		//Takes the last 6 bits of the binary value
		int destinationBinary = binary & 63;

		//Calls the decodeSourceDestination method, passing the destinationBinary value and adds it to the assembly language
		assemblyLanguage = String.valueOf(assemblyLanguage) + "," + decodeSourceDestination(destinationBinary);
		//Sets the assembly language to the "Assembly Instruction" TextField

		if (errorLabel.getText().equals(""))
		{
			assemblerInstruction.setText(assemblyLanguage);
		}
	}

	//Method to decode the source and destination registers
	String decodeSourceDestination(int registerNumber)
	{
		String sourceCode = "";

		//Takes the last 3 bits of the binary value and sets it to sourceCode
		//7 = 0000 0000 0000 0111
		sourceCode = Integer.toString(registerNumber & 7);
		
		//Takes the next 3 bits of the binary value and sets it to sourceQuantifier
		int sourceQuantifier = registerNumber >> 3 & 7;
		//Switch statement to determine the register command
		switch (sourceQuantifier) {
			//If the sourceQuantifier is 0, it will set the sourceCode to "R" + sourceCode
			// 0 is used because Rn is the default and so the 3 bits determining it are 000
			case 0:
				sourceCode = "R" + sourceCode;
				break;
			//If the sourceQuantifier is 1, it will set the sourceCode to "(R" + sourceCode + ")"
			// 1 is used because (Rn) is the default and so the 3 bits determining it are 001
			case 1:
				sourceCode = "(R" + sourceCode + ")";
				break;
			//If the sourceQuantifier is 2, it will set the sourceCode to "(R" + sourceCode + ")+"
			// 2 is used because (Rn) is the default and so the 3 bits determining it are 010
			case 2:
				sourceCode = "(R" + sourceCode + ")+";
				break;
			//If the sourceQuantifier is 3, it will set the sourceCode to "-(R" + sourceCode + ")"
			// 4 is used because Rn is the default and so the 3 bits determining it are 100
			case 4:
				sourceCode = "-(R" + sourceCode + ")";
				break;
			//If the sourceQuantifier is anything else, it will set the sourceCode to "(R" + sourceCode + ")+"
			default:
				errorLabel.setText("ERROR: Invalid Register Command");
				break;
		}
		return sourceCode;
	}

}
/************************************************************************/
/* Put your implementation of the encode, decodeBin, and decodeHex      */
/* methods here. You may add any other methods that you think are       */
/* appropriate. However, you should not change anything in the code     */
/* that I have written.                                                 */
/************************************************************************/


