/*
 * 
 * 2.1 Cover Sheet 
*
* Name: Samantha Brooks
* Computer Login-ID (WVU ID): sbrooks9
* WVU Student ID Number: 701118894
* Programming Assignment Number: 1
* Date: 9/22/2014
* 
* 2.2 Status Sheet
* Compiled: Yes
* Works on Shell:  Yes
* Preprocessing input text and initial transformation from ASCII to 64 bit blocks: Yes
* Generating the key schedule: Yes
* Initial permutation of DES: Yes	
* Decrypts correctly: Yes
* DES cycle: Yes
* Inverse initial permutation: Yes
* Final and intermediate output files, and documentation (including README.txt): Yes
* Prints encrypted message in correct Ascii: I think so. It is  hard to tell what it is supposed to look like since it is  not an actual readable message.
* Prints decrypted message in correct Ascii: Yes
* Output File: Output.txt
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Scanner;

public class desSubmission {

	/* 
	 * This is the main method this is where the key and message is requested and scanned in.
	 *  Then, the appropriate functions are called to encrypt and decrypt the message with the 
	 *  key specified.
	 */

	public static void main(String []args) throws IOException{
		Scanner scan = new Scanner (System.in);
		String[] permutatedMessage = new String[30];
		String[] messageHalves = new String[30];
		String[] sBoxParts = new String[8];
		String[] xORStore = new String[20];
		String[] keysStored = new String[20];
		String[] finalStringArray = new String[20];
		PrintWriter writer = new PrintWriter("Output.txt", "UTF-8");
		StringBuilder sBoxPartsAdded = new StringBuilder();
		StringBuilder lastAppendString = new StringBuilder();
		StringBuilder finalResultGroup = new StringBuilder();
		int counter = 0;
		int arrayCounter = 0;
		int arrayCounter2 = 0;

		System.out.println("Please enter the key you would like to use to encrypt your text.");
		writer.println("Please enter the key you would like to use to encrypt your text.");
		String key = scan.next();
		System.out.println(key);
		writer.println(key);

		writer.println("The name of the file you'd like encrypted.");
		writer.println("plaintext.txt");
		String fileString = readFile("plaintext.txt");
		System.out.println("The text in the document says:");
		writer.println("The text in the document says:");
		System.out.println(fileString);
		writer.println(fileString);

		System.out.println("Here is the message in binary:");
		writer.println("Here is the message in binary:");
		String messageInBinary = convertMessageToBinary(fileString);
		System.out.println(messageInBinary);
		writer.println(messageInBinary);

		System.out.println("Here is the key in binary:");
		writer.println("Here is the key in binary:");
		String keyInBinary = convertKeyToBinary(key);
		System.out.println(keyInBinary);
		writer.println(keyInBinary);
		System.out.println("");
		writer.println("");
		String permutatedKey = permutate(keyInBinary);
		System.out.println("Key after permute is:");
		System.out.println(permutatedKey);
		writer.println("Key after permute is:");
		writer.println(permutatedKey);


		String[] partsToShift = halfString(permutatedKey);	
		keysStored = shiftParts(partsToShift);

		String[] finalKeys = new String [20];
		int n = 1;
		for(int f = 0; f < 16; f++){
			finalKeys[f] = permutate2(keysStored[f]);
			System.out.println("Key " + n + " is:");
			System.out.println(finalKeys[f]);
			writer.println("Key " + n + " is:");
			writer.println(finalKeys[f]);
			n++;
		}	


		String[] messageBlocks = blockMessage(messageInBinary);
		System.out.println("The data after peprocessing:");
		writer.println("The data after peprocessing:");
		for (int i = 0; i < messageBlocks.length; i++){
			System.out.println(messageBlocks[i]);
			writer.println(messageBlocks[i]);
		}
		int intialLength = messageInBinary.length();

		/* 
		 * This section of code is where the sixteen iterations of encryption occur for each block 
		 * of 64 bit data.
		 */
		

		for (int s = 0; s < messageBlocks.length; s++){
			permutatedMessage[s] = initialPermutation(messageBlocks[s]);
			System.out.println("Intial Permutation results:");
			System.out.println(permutatedMessage[s]);
			writer.println("Intial Permutation results:");
			writer.println(permutatedMessage[s]);
			messageHalves = halfString(permutatedMessage[s]);
			int j = 0;
			for (int i = 0; i < 16; i++){
				System.out.println();
				System.out.println("Iteration " + j++ + ":");
				System.out.println("L_i-1:");
				System.out.println(messageHalves[0]);
				System.out.println("R_i-1:");
				System.out.println(messageHalves[1]);

				writer.println();
				writer.println("Iteration " + j + ":");
				writer.println("L_i-1:");
				writer.println(messageHalves[0]);
				writer.println("R_i-1:");
				writer.println(messageHalves[1]);

				String leftHalf = selectionTable(messageHalves[1]);
				System.out.println("The expanded permutation:");
				System.out.println(leftHalf);
				System.out.println("XOR with key:");
				writer.println("The expanded permutation:");
				writer.println(leftHalf);
				writer.println("XOR with key:");
				String[] xORParts = new String[9];
				String xORString = xOR(leftHalf, finalKeys[i]);
				System.out.println(xORString);
				writer.println(xORString);
				xORParts[0] = xORString.substring(0,6);
				xORParts[1] = xORString.substring(6,12);
				xORParts[2] = xORString.substring(12,18);
				xORParts[3] = xORString.substring(18,24);
				xORParts[4] = xORString.substring(24,30);
				xORParts[5] = xORString.substring(30,36);
				xORParts[6] = xORString.substring(36,42);
				xORParts[7] = xORString.substring(42,48);

				System.out.println("S-Box Substitution:");
				writer.println("S-Box Substitution:");
				for (int z = 0; z < 8; z++){
					sBoxParts[z] = sBox(xORParts[z], counter);
					System.out.println(sBoxParts[z]);
					writer.println(sBoxParts[z]);
					sBoxPartsAdded.append(sBoxParts[z]);
					counter++;
				}
				counter = 0;
				String pBoxString = sBoxPartsAdded.toString();
				sBoxPartsAdded.setLength(0);
				System.out.println("P-Box permutation:");
				writer.println("P-Box permutation:");
				pBoxString = pBox(pBoxString);
				System.out.println(pBoxString);
				writer.println(pBoxString);
				System.out.println("XOR with L_i-1 (This is R_i):");
				writer.println("XOR with L_i-1 (This is R_i):");
				xORStore[i] = xOR(pBoxString, messageHalves[0]);
				System.out.println(xORStore[i]);
				writer.println(xORStore[i]);
				messageHalves[0] = messageHalves[1];
				messageHalves[1] = xORStore[i];

				if (i == 15){
					lastAppendString.setLength(0);
					lastAppendString.append(messageHalves[1]).append(messageHalves[0]);
					String finalString = lastAppendString.toString();
					System.out.println("Final Encrypt permutation:");
					writer.println("Final Encrypt permutation:");
					finalStringArray[arrayCounter] = finalPermutation(finalString);
					System.out.println(finalStringArray[arrayCounter]);
					writer.println(finalStringArray[arrayCounter]);
					finalResultGroup.append(finalStringArray[arrayCounter]);
					arrayCounter++;
				}
			}
		}
		
		/* 
		 * This section of code is where the ASCII value of the encrypted message is computed.
		 */
		

		String encrypted = finalResultGroup.toString();
		String toAscii = encrypted.toString();
		BigInteger temp = new BigInteger (toAscii, 2);
		byte[] encryptedBytes = temp.toByteArray();
		StringBuilder encryptedAscii = new StringBuilder();
		for(int i = 0; i < encryptedBytes.length; i++){
			encryptedAscii.append((char)encryptedBytes[i]);
		}
		String finalAsciiEncrypted = encryptedAscii.toString();
		System.out.println("The original message after encryption:");
		System.out.println(finalAsciiEncrypted);
		writer.println("The original message after encryption:");
		writer.println(finalAsciiEncrypted);
		finalResultGroup.setLength(0);

		/* 
		 * This section of code is where the sixteen iterations of decryption occur for each 
		 * 64 bit block of encrypted data.
		 */

		System.out.println();
		System.out.println();
		writer.println();
		writer.println();

		writer.println("Decrypting now..........");

		for (int s = 0; s < arrayCounter; s++){
			permutatedMessage[s] = initialPermutation(finalStringArray[s]);
			messageHalves = halfString(permutatedMessage[s]);
			System.out.println("Intial Permutation results:");
			System.out.println(permutatedMessage[s]);
			writer.println("Intial Permutation results:");
			writer.println(permutatedMessage[s]);
			messageHalves = halfString(permutatedMessage[s]);
			int j = 0;
			for (int i = 15; i >= 0; i--){
				System.out.println();
				System.out.println("Iteration " + j++ + ":");
				System.out.println("L_i-1:");
				System.out.println(messageHalves[0]);
				System.out.println("R_i-1:");
				System.out.println(messageHalves[1]);

				writer.println();
				writer.println("Iteration " + j + ":");
				writer.println("L_i-1:");
				writer.println(messageHalves[0]);
				writer.println("R_i-1:");
				writer.println(messageHalves[1]);

				String leftHalf = selectionTable(messageHalves[1]);
				System.out.println("The expanded permutation:");
				System.out.println(leftHalf);
				System.out.println("XOR with key:");
				writer.println("The expanded permutation:");
				writer.println(leftHalf);
				writer.println("XOR with key:");
				String[] xORParts = new String[9];
				String xORString = xOR(leftHalf, finalKeys[i]);
				System.out.println(xORString);
				writer.println(xORString);
				xORParts[0] = xORString.substring(0,6);
				xORParts[1] = xORString.substring(6,12);
				xORParts[2] = xORString.substring(12,18);
				xORParts[3] = xORString.substring(18,24);
				xORParts[4] = xORString.substring(24,30);
				xORParts[5] = xORString.substring(30,36);
				xORParts[6] = xORString.substring(36,42);
				xORParts[7] = xORString.substring(42,48);

				System.out.println("S-Box Substitution:");
				writer.println("S-Box Substitution:");
				for (int z = 0; z < 8; z++){
					sBoxParts[z] = sBox(xORParts[z], counter);
					System.out.println(sBoxParts[z]);
					writer.println(sBoxParts[z]);
					sBoxPartsAdded.append(sBoxParts[z]);
					counter++;
				}
				counter = 0;
				String pBoxString = sBoxPartsAdded.toString();
				sBoxPartsAdded.setLength(0);
				System.out.println("P-Box permutation:");
				writer.println("P-Box permutation:");
				pBoxString = pBox(pBoxString);
				System.out.println(pBoxString);
				writer.println(pBoxString);
				System.out.println("XOR with L_i-1 (This is R_i):");
				writer.println("XOR with L_i-1 (This is R_i):");
				xORStore[i] = xOR(pBoxString, messageHalves[0]);
				System.out.println(xORStore[i]);
				writer.println(xORStore[i]);
				messageHalves[0] = messageHalves[1];
				messageHalves[1] = xORStore[i];
				if (i == 0){
					lastAppendString.setLength(0);
					lastAppendString.append(messageHalves[1]).append(messageHalves[0]);
					String finalString = lastAppendString.toString();
					System.out.println("Final Decrypt permutation:");
					writer.println("Final Decrypt permutation:");
					finalStringArray[arrayCounter2] = finalPermutation(finalString);
					System.out.println(finalStringArray[arrayCounter2]);
					writer.println(finalStringArray[arrayCounter2]);
					finalResultGroup.append(finalStringArray[arrayCounter2]);
					arrayCounter2++;
				}
			}}
		if (intialLength%64 == 0) {
			intialLength = 0;
		}
		else if(intialLength > 64){
			int numberOfBlocks = intialLength / 64;
			intialLength = 64 - (intialLength - (numberOfBlocks*64));
		}

		/* 
		 * This section of code is where the ASCII value of the decrypted message is computed.
		 */
		
		
		String decryption = finalResultGroup.toString();
		String toAsciiDecryption = decryption.substring(0, decryption.length() - intialLength);
		BigInteger tempDecrypt = new BigInteger (toAsciiDecryption, 2);
		byte[] decryptiondBytes = tempDecrypt.toByteArray();
		StringBuilder decryptedAscii = new StringBuilder();
		for(int i = 0; i < decryptiondBytes.length; i++){
			decryptedAscii.append((char)decryptiondBytes[i]);
		}
		String finalAsciiDecryption = decryptedAscii.toString();
		System.out.println("The original message after encryption and decryption:");
		System.out.println(finalAsciiDecryption);
		writer.println("The original message after encryption and decryption:");
		writer.println(finalAsciiDecryption);
		finalResultGroup.setLength(0);



		writer.close();
	}

	/* 
	 * This method is responsible for shifting the appropriate parts the necessary
	 * amount of times to encrypt the key. It returns an array of the shifted keys. 
	 */

	public static String[] shiftParts(String[] partsToShift) {
		String[] storeKeys = new String[16];
		storeKeys[0] = shiftOne(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[0]);
		storeKeys[1] = shiftOne(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[1]);

		storeKeys[2] = shiftTwo(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[2]);

		storeKeys[3] = shiftTwo(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[3]);

		storeKeys[4] = shiftTwo(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[4]);

		storeKeys[5] = shiftTwo(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[5]);

		storeKeys[6] = shiftTwo(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[6]);

		storeKeys[7] = shiftTwo(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[7]);

		storeKeys[8] = shiftOne(partsToShift[0], partsToShift[1]);
		partsToShift = halfString(storeKeys[8]);
		for (int i = 9; i < 15; i++){ 
			storeKeys[i] = shiftTwo(partsToShift[0], partsToShift[1]);
			partsToShift = halfString(storeKeys[i]);
		}
		storeKeys[15] = shiftOne(partsToShift[0], partsToShift[1]);

		return storeKeys;
	}

	/*
	 * This is the method that reads in a file. The program ensures that white spaces
	 * and special characters are removed. The separate lines of the file are then appended together to 
	 * create one string value. 
	 */
	
	public static String readFile(String fileName) throws IOException{
		String[] fileArray = new String [20];
		String fileLine;
		int lineAmount = 0;
		StringBuilder finalString = new StringBuilder();
		System.out.println("Reading " + fileName + ".txt and making appropriate corrections...");
		System.out.println("Here is the message with special characters, digits, and white spaces removed:");
		FileReader encryptFile = new FileReader("plaintext.txt");
		BufferedReader buffRead = new BufferedReader(encryptFile);
		while((fileLine = buffRead.readLine()) != null){
			fileLine = fileLine.replaceAll("\\W","");
			fileLine = fileLine.replaceAll("\n","");
			fileArray[lineAmount] = fileLine;
			lineAmount++;
		}
		for (int j = 0; j < lineAmount; j++){
			finalString.append(fileArray[j]);
		}
		String appendedString = finalString.toString();
		System.out.println(appendedString);
		return appendedString;
	}

	/*
	 * 
	 * The code below deals with implementing the KeyScheduler
	 * 
	 * 
	 */


	/* 
	 * This method converts the key to Binary. While doing this conversion, it determines if an extra
	 * 1 or zero needs to be added to ensure that there are a odd number of 1's in each byte.
	 */

	public static String convertKeyToBinary(String message) throws UnsupportedEncodingException{
		int length = 0, i = 0, countOnes = 0;
		byte[] messageBytes = message.getBytes("US-ASCII");
		Byte[] convert = new Byte[100];
		int [] intMessage = new int[100];
		String checkString = null;;
		StringBuilder appendedString = new StringBuilder();
		String finalString = null;

		for(length = 0; length < messageBytes.length; length++){
			convert[length] =  messageBytes[length];
			intMessage[length] = convert[length].intValue();
			checkString = Integer.toBinaryString(intMessage[length]);
			for(i = 0; i < checkString.length(); i++){
				if (checkString.charAt(i) == '1'){
					countOnes++;
				}
			}
			if(countOnes == 1 || countOnes == 3 || countOnes == 5 || countOnes == 7){
				appendedString.append(checkString);
				appendedString.append(0);
			}
			else{
				appendedString.append(checkString);
				appendedString.append(1);
			}
			countOnes = 0;
		}
		finalString = appendedString.toString();
		return finalString;
	}



	/*
	 * This converts the message to binary. 
	 */

	public static String convertMessageToBinary(String message) throws UnsupportedEncodingException{
		int i = 0;
		int []number = new int [500];
		String[] appendString = new String[500];
		StringBuilder convertString = new StringBuilder();
		String finalString = null;

		for(i  = 0; i < message.length(); i++){
			number[i] = (int) message.charAt(i);
			appendString[i]= Integer.toBinaryString(number[i]);
			convertString.append("0" + appendString[i]);
		}
		finalString = convertString.toString();
		return finalString;
	}

	/*
	 * This method takes the key and references the permuted choice 1 table to 
	 * make the appropriate swapping.
	 */

	public static String permutate(String key){	
		char[] p1 = {57, 49, 41, 33,  25, 17, 9, 01, 58, 50, 42, 34, 26, 18, 
				10,  02, 59, 51,  43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
				63, 55, 47, 39,  31, 23, 15,  07, 62,54, 46, 38, 30, 22,
				14,  06, 61, 53,  45, 37, 29, 21, 13,5,  28, 20, 12, 04};

		char [] binary = key.toCharArray();

		p1[0] = binary[56];
		p1[1] = binary[48];
		p1[2] = binary[40];
		p1[3] = binary[32];
		p1[4] = binary[24];
		p1[5] = binary[16];
		p1[6] = binary[8];

		p1[7] = binary[0];
		p1[8] = binary[57];
		p1[9] = binary[49];
		p1[10] = binary[41];
		p1[11] = binary[33];
		p1[12] = binary[25];
		p1[13] = binary[17];

		p1[14] = binary[9];
		p1[15] = binary[1];
		p1[16] = binary[58];
		p1[17] = binary[50];
		p1[18] = binary[42];
		p1[19] = binary[34];
		p1[20] = binary[26];

		p1[21] = binary[18];
		p1[22] = binary[10];
		p1[23] = binary[2];
		p1[24] = binary[59];
		p1[25] = binary[51];
		p1[26] = binary[43];
		p1[27] = binary[35];

		p1[28] = binary[62];
		p1[29] = binary[54];
		p1[30] = binary[46];
		p1[31] = binary[38];
		p1[32] = binary[30];
		p1[33] = binary[22];
		p1[34] = binary[14];

		p1[35] = binary[6];
		p1[36] = binary[61];
		p1[37] = binary[53];
		p1[38] = binary[45];
		p1[39] = binary[37];
		p1[40] = binary[29];
		p1[41] = binary[21];

		p1[42] = binary[13];
		p1[43] = binary[5];
		p1[44] = binary[60];
		p1[45] = binary[52];
		p1[46] = binary[44];
		p1[47] = binary[36];
		p1[48] = binary[28];

		p1[49] = binary[20];
		p1[50] = binary[12];
		p1[51] = binary[4];
		p1[52] = binary[27];
		p1[53] = binary[19];
		p1[54] = binary[11];
		p1[55] = binary[3];

		String permutatedKey = new String(p1);
		return permutatedKey; 
	}

	public static String[] halfString(String message){
		int mid = message.length() / 2;
		String[] halvedString = {
				message.substring(0, mid),
				message.substring(mid),
		};		
		return halvedString;
	}

	/*
	 * The method is responsible for shifting each string to the left once.
	 */

	public static String shiftOne(String partA, String partB){
		StringBuilder returnString = new StringBuilder();
		char[] part1 = new char [200];
		part1 = partA.toCharArray();
		char[] part2 = new char[200];
		part2 = partB.toCharArray();
		char temp1 = part1[0], temp2 = part2[0];

		for(int i = 1; i < part1.length; i++){
			part1[i-1] = part1[i];
		}
		part1[27]=temp1;

		for(int i = 1; i < part2.length; i++){
			part2[i-1] = part2[i];
		}
		part2[27]=temp2;

		returnString.append(part1);
		returnString.append(part2);
		String returnThis = returnString.toString();
		return returnThis;
	}


	/*
	 * The method is responsible for shifting each string to the left twice.
	 */


	public static String shiftTwo(String partA, String partB){
		StringBuilder returnString = new StringBuilder();
		char[] part1 = new char [20];
		part1 = partA.toCharArray();
		char[] part2 = new char [20];
		part2 = partB.toCharArray();
		char temp1 = part1[0], temp1B = part1[1], temp2 = part2[0], temp2B = part2[1];

		for(int i = 1; i < part1.length; i++){
			part1[i-1] = part1[i];
		}
		for(int i = 1; i < part1.length; i++){
			part1[i-1] = part1[i];
		}
		part1[26]=temp1;
		part1[27]=temp1B;

		for(int i = 1; i < part2.length; i++){
			part2[i-1] = part2[i];
		}
		for(int i = 1; i < part2.length; i++){
			part2[i-1] = part2[i];
		}
		part2[26]=temp2;
		part2[27]=temp2B;

		returnString.append(part1);
		returnString.append(part2);
		String returnThis = returnString.toString();
		return returnThis;

	}

	/*
	 * The method is responsible for the second permutation. 
	 */

	public static String permutate2(String permutateThis){
		char[] p2 ={14, 17, 11, 24,  1,  5,
				3, 28, 15,  6, 21, 10,
				23, 19, 12,  4, 26,  8,
				16,  7, 27, 20, 13,  2,
				41, 52, 31, 37, 47, 55,
				30, 40, 51, 45, 33, 48,
				44, 49, 39, 56, 34, 53,
				46, 42, 50, 36, 29, 32};

		char [] shifted = permutateThis.toCharArray();

		p2[0] = shifted[13];
		p2[1] = shifted[16];
		p2[2] = shifted[10];
		p2[3] = shifted[23];
		p2[4] = shifted[0];
		p2[5] = shifted[4];

		p2[6] = shifted[2];
		p2[7] = shifted[27];
		p2[8] = shifted[14];
		p2[9] = shifted[5];
		p2[10] = shifted[20];
		p2[11] = shifted[9];

		p2[12] = shifted[22];
		p2[13] = shifted[18];
		p2[14] = shifted[11];
		p2[15] = shifted[3];
		p2[16] = shifted[25];
		p2[17] = shifted[7];

		p2[18] = shifted[15];
		p2[19] = shifted[6];
		p2[20] = shifted[26];  
		p2[21] = shifted[19];
		p2[22] = shifted[12];
		p2[23] = shifted[1];

		p2[24] = shifted[40];
		p2[25] = shifted[51];
		p2[26] = shifted[30];
		p2[27] = shifted[36];
		p2[28] = shifted[46];
		p2[29] = shifted[54];

		p2[30] = shifted[29];
		p2[31] = shifted[39];
		p2[32] = shifted[50];
		p2[33] = shifted[44];
		p2[34] = shifted[32];
		p2[35] = shifted[47];

		p2[36] = shifted[43];
		p2[37] = shifted[48];
		p2[38] = shifted[38];
		p2[39] = shifted[55];
		p2[40] = shifted[33];
		p2[41] = shifted[52];

		p2[42] = shifted[45];
		p2[43] = shifted[41];
		p2[44] = shifted[49];
		p2[45] = shifted[35];
		p2[46] = shifted[28];
		p2[47] = shifted[31];

		String permutatedKey2 = new String(p2);
		return permutatedKey2; 
	}

	/*
	 * 
	 * The code below deals with implementing DES
	 * 
	 * 
	 */


	/*
	 * The method is responsible for splitting the message into blocks of 
	 * 64 bits and padding the last block to equal 64 if it doesn't already.
	 */


	public static String[] blockMessage(String message){
		int dividedLength = message.length()/64; 
		int length = message.length();
		if (message.length()%64 != 0){
			dividedLength = dividedLength+1;
		}

		String[] blocks = new String [dividedLength];
		if (dividedLength<= 6 && dividedLength >5){
			blocks[0] = message.substring(0,64);
			blocks[1] = message.substring(64,128);
			blocks[2] = message.substring(128,192);
			blocks[3] = message.substring(192,256);
			blocks[4] = message.substring(256,320);
			for (int i = length; i <= 384; i++){
				message = message + '0';
				if (i == 384) {
					blocks[4] = message.substring(320,384);
				}
			}
		}

		else if(dividedLength<= 5 && dividedLength >4){
			blocks[0] = message.substring(0,64);
			blocks[1] = message.substring(64,128);
			blocks[2] = message.substring(128,192);
			blocks[3] = message.substring(192,256);
			for (int i = length; i <= 321; i++){
				message = message + '0';
				if (i == 320) {
					blocks[4] = message.substring(256,320);
				}
			}
		}
		else if(dividedLength<= 4 && dividedLength >3 ){
			blocks[0] = message.substring(0,64);
			blocks[1] = message.substring(64,128);
			blocks[2] = message.substring(128,192);
			for (int i = length; i <= 257; i++){
				message = message + '0';
				if (i == 256) {
					blocks[3] = message.substring(192,256);
				}
			}
		}
		else if(dividedLength <= 3 && dividedLength>2){
			blocks[0] = message.substring(0,64);
			blocks[1] = message.substring(64,128);
			for (int i = length; i <= 193; i++){
				message = message + '0';
				if (i == 192) {
					blocks[2] = message.substring(128,192);
				}
			}	
		}
		else if( dividedLength<= 2 && dividedLength >1 ){
			blocks[0] = message.substring(0,64);
			for (int i = length; i <= 128; i++){
				message = message + '0';
				if (i == 128) {
					blocks[1] = message.substring(64,128);
				}
			}	
		}
		else if( dividedLength<= 1 && dividedLength >0 ){
			for (int i = length; i <= 64; i++){
				message = message + '0';
				if (i == 64) {
					blocks[0] = message.substring(0,64);
				}
			}	
		}
		else {
			System.out.println(message.length());
			for(int i = 0; i < dividedLength; i++){
				if (i == (dividedLength-1)) {
					for (int z = length; z <= (dividedLength*64); z++){
						message = message + '0';
						if (z == (dividedLength*64)) {
							blocks[i] = message.substring(i*64,((i+1)*64));
						}
					}
				}
				else {
					blocks[i] = message.substring(i*64,((i+1)*64));	
				}
			}
		} 
		return blocks;
	}



	/*
	 * The method is responsible for the initial permutation.
	 */


	public static String initialPermutation(String message){
		char[] ip = {58, 50, 42, 34,  26, 18, 10, 02, 
				60,  52, 44, 36,  28, 20, 12, 4, 
				62, 54, 46, 38, 30, 22, 14, 6,
				64, 56, 48, 40, 32, 24, 16, 8,
				57, 49, 41, 33, 25, 17, 9, 1,
				59, 51, 43, 35, 27, 19, 11, 3,
				61, 53, 45, 37, 29, 21, 13, 5,
				63, 55, 47, 39, 31, 23, 15, 7};

		char [] mParts = new char [65];
		mParts = message.toCharArray();

		ip[0] = mParts[57];
		ip[1] = mParts[49];
		ip[2] = mParts[41];
		ip[3] = mParts[33];
		ip[4] = mParts[25];
		ip[5] = mParts[17];
		ip[6] = mParts[9];
		ip[7] = mParts[1];

		ip[8] = mParts[59];
		ip[9] = mParts[51];
		ip[10] = mParts[43];
		ip[11] = mParts[35];
		ip[12] = mParts[27];
		ip[13] = mParts[19];
		ip[14] = mParts[11];
		ip[15] = mParts[3];

		ip[16] = mParts[61];
		ip[17] = mParts[53];
		ip[18] = mParts[45];
		ip[19] = mParts[37];
		ip[20] = mParts[29];
		ip[21] = mParts[21];
		ip[22] = mParts[13];
		ip[23] = mParts[5];

		ip[24] = mParts[63];
		ip[25] = mParts[55];
		ip[26] = mParts[47];
		ip[27] = mParts[39];
		ip[28] = mParts[31];
		ip[29] = mParts[23];
		ip[30] = mParts[15];
		ip[31] = mParts[7];

		ip[32] = mParts[56];
		ip[33] = mParts[48];
		ip[34] = mParts[40];
		ip[35] = mParts[32];
		ip[36] = mParts[24];
		ip[37] = mParts[16];
		ip[38] = mParts[8];
		ip[39] = mParts[0];

		ip[40] = mParts[58];
		ip[41] = mParts[50];
		ip[42] = mParts[42];
		ip[43] = mParts[34];
		ip[44] = mParts[26];
		ip[45] = mParts[18];
		ip[46] = mParts[10];
		ip[47] = mParts[2];

		ip[48] = mParts[60];
		ip[49] = mParts[52];
		ip[50] = mParts[44];
		ip[51] = mParts[36];
		ip[52] = mParts[28];
		ip[53] = mParts[20];
		ip[54] = mParts[12];
		ip[55] = mParts[4];

		ip[56] = mParts[62];
		ip[57] = mParts[54];
		ip[58] = mParts[46];
		ip[59] = mParts[38];
		ip[60] = mParts[30];
		ip[61] = mParts[22];
		ip[62] = mParts[14];
		ip[63] = mParts[6];

		String permutated = new String(ip);
		return permutated;
	}

	/*
	 * This method is for implementing the E selection table.
	 */

	public static String selectionTable(String message){
		char[] ip = {32, 1, 2, 3,  4, 5, 
				4,  5, 6, 7,  8, 9,
				8, 9, 10, 11, 12, 13, 
				12, 13, 14, 15, 16, 17,
				16, 17, 18, 18, 20, 21,
				20, 21, 22, 23, 24, 25,
				24, 25, 26, 27, 28, 29, 
				28, 29, 30, 31, 32, 1};

		char[] selectBits = message.toCharArray();	

		ip[0] = selectBits[31];
		ip[1] = selectBits[0];
		ip[2] = selectBits[1];
		ip[3] = selectBits[2];
		ip[4] = selectBits[3];
		ip[5] = selectBits[4];

		ip[6] = selectBits[3];
		ip[7] = selectBits[4];
		ip[8] = selectBits[5];
		ip[9] = selectBits[6];
		ip[10] = selectBits[7];
		ip[11] = selectBits[8];

		ip[12] = selectBits[7];
		ip[13] = selectBits[8];
		ip[14] = selectBits[9];
		ip[15] = selectBits[10];
		ip[16] = selectBits[11];
		ip[17] = selectBits[12];

		ip[18] = selectBits[11];
		ip[19] = selectBits[12];
		ip[20] = selectBits[13];
		ip[21] = selectBits[14];
		ip[22] = selectBits[15];
		ip[23] = selectBits[16];

		ip[24] = selectBits[15];
		ip[25] = selectBits[16];
		ip[26] = selectBits[17];
		ip[27] = selectBits[18];
		ip[28] = selectBits[19];
		ip[29] = selectBits[20];

		ip[30] = selectBits[19];
		ip[31] = selectBits[20];
		ip[32] = selectBits[21];
		ip[33] = selectBits[22];
		ip[34] = selectBits[23];
		ip[35] = selectBits[24];

		ip[36] = selectBits[23];
		ip[37] = selectBits[24];
		ip[38] = selectBits[25];
		ip[39] = selectBits[26];
		ip[40] = selectBits[27];
		ip[41] = selectBits[28];		

		ip[42] = selectBits[27];
		ip[43] = selectBits[28];
		ip[44] = selectBits[29];
		ip[45] = selectBits[30];
		ip[46] = selectBits[31];
		ip[47] = selectBits[0];		

		String selectedValues = new String(ip);
		return selectedValues;
	}

	/*
	 * This method is responsible for implementing xor calculations.
	 */

	public static String xOR(String messageHalves, String keys){
		char [] keysChar = keys.toCharArray();
		char [] messageChar = messageHalves.toCharArray();
		int [] keyInt = new int[keysChar.length];
		int [] messageInt = new int[messageChar.length];
		int [] xOR = new int[200];
		StringBuilder appendXOR = new StringBuilder();
		String xORreturn = null;
		for(int i=0; i <keysChar.length; i++){
			keyInt[i] = Integer.valueOf(keysChar[i]);
		}
		for(int i=0; i < messageChar.length; i++){
			messageInt[i] = Integer.valueOf(messageChar[i]);
		}
		for(int i = 0; i < messageInt.length ; i++){
			xOR[i] = messageInt[i] ^ keyInt[i]; 
			appendXOR = appendXOR.append(Integer.toString(xOR[i]));
		}
		xORreturn = appendXOR.toString();
		return xORreturn;
	}


	/*
	 * This method is responsible for implementing the sBox.
	 */

	public static String sBox(String xORParts, int counter){
		char[] sBoxChar = xORParts.toCharArray();
		int[] result = new int[5];
		StringBuilder partA = new StringBuilder();
		StringBuilder partB = new StringBuilder();

		int[][] s1 = new int[] [] {
				{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
				{0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
				{4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
				{15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
		};
		int[][] s2 = new int[] []{
				{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10}, 
				{3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
				{0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
				{13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
		}; 
		int[][] s3 = new int[] []{
				{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
				{13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
				{13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
				{1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
		};
		int[][] s4 = new int[][]{
				{7, 13, 14, 3, 0,  6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
				{13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
				{10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
				{3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
		};
		int[][] s5 = new int[][]{
				{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
				{14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
				{4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
				{11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
		};
		int[][] s6 = new int[][]{
				{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
				{10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
				{9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
				{4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
		};
		int[][] s7 = new int[][]{
				{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
				{13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
				{1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
				{6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
		};
		int[][] s8 = new int[][]{
				{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
				{1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
				{7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
				{2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
		};

		partA.append(sBoxChar[0]).append(sBoxChar[5]);
		partB.append(sBoxChar[1]).append(sBoxChar[2]).append(sBoxChar[3]).append(sBoxChar[4]);
		String partAString = partA.toString();
		String partBString = partB.toString();
		int valueA = Integer.parseInt(partAString,2);
		int valueB = Integer.parseInt(partBString,2);
		if(counter == 0){
			result[0] = s1[valueA][valueB];
		}
		else if(counter == 1){
			result[0] = s2[valueA][valueB];
		}
		else if(counter == 2){
			result[0] = s3[valueA][valueB];
		}
		else if(counter == 3){
			result[0] = s4[valueA][valueB];
		}
		else if(counter == 4){
			result[0] = s5[valueA][valueB];
		}
		else if(counter == 5){
			result[0] = s6[valueA][valueB];
		}
		else if(counter == 6){
			result[0] = s7[valueA][valueB];
		} 
		else if(counter == 7){
			result[0] = s8[valueA][valueB];
		} 
		partA.setLength(0);
		partB.setLength(0);
		partA.append(Integer.toBinaryString(result[0]));
		if(partA.length() <= 4){
			if(partA.length() == 4){
				partB.append(partA);
			}
			else if(partA.length() == 3){
				partB.append("0"+partA);
			}
			else if(partA.length() == 2){
				partB.append("00"+partA);
			}
			else if(partA.length() == 1){
				partB.append("000"+partA);
			}
			else if(partA.length() == 0){
				partB.append("0000");
			}
		}
		String returnString = partB.toString();
		return returnString;
	}

	/*
	 * This method is responsible for implementing the pBox.
	 */

	public static String pBox(String addedParts){
		char[] pBoxChar = addedParts.toCharArray();	
		char[] pB = {16, 7, 20, 21, 
				29,  12, 28, 17, 
				1, 15, 23, 26,
				5, 18, 31, 10,
				2, 8, 24, 14,
				32, 27, 3, 9, 	
				19, 13, 30, 6, 
				22, 11, 4, 25};

		pB[0] = pBoxChar[15];
		pB[1] = pBoxChar[6];
		pB[2] = pBoxChar[19];
		pB[3] = pBoxChar[20];

		pB[4] = pBoxChar[28];
		pB[5] = pBoxChar[11];
		pB[6] = pBoxChar[27];
		pB[7] = pBoxChar[16];

		pB[8] = pBoxChar[0];
		pB[9] = pBoxChar[14];
		pB[10] = pBoxChar[22];
		pB[11] = pBoxChar[25];

		pB[12] = pBoxChar[4];
		pB[13] = pBoxChar[17];
		pB[14] = pBoxChar[30];
		pB[15] = pBoxChar[9];

		pB[16] = pBoxChar[1];
		pB[17] = pBoxChar[7];
		pB[18] = pBoxChar[23];
		pB[19] = pBoxChar[13];

		pB[20] = pBoxChar[31];
		pB[21] = pBoxChar[26];
		pB[22] = pBoxChar[2];
		pB[23] = pBoxChar[8];

		pB[24] = pBoxChar[18];
		pB[25] = pBoxChar[12];
		pB[26] = pBoxChar[29];
		pB[27] = pBoxChar[5];

		pB[28] = pBoxChar[21];
		pB[29] = pBoxChar[10];
		pB[30] = pBoxChar[3];
		pB[31] = pBoxChar[24];

		String pBoxString = new String(pB);
		return pBoxString; 
	}

	/*
	 * This method is responsible for implementing the final permutation. 
	 */

	public static String finalPermutation(String xORStore){
		char[] finalS = xORStore.toCharArray();
		char[] fp = {40, 8, 48, 16,  56, 24, 64, 32,
				39, 7, 47, 15,  55, 23, 63, 31,
				38, 6, 46, 14, 54, 22, 62, 30,
				37, 5, 45, 13, 53, 21, 61, 29,
				36, 4, 44, 12, 52, 20, 60, 28,
				35, 3, 43, 11, 51, 19, 59, 27,
				34, 2, 42, 10, 50, 18, 58, 26,
				33, 1, 41, 9, 49, 17, 57, 25};


		fp[0] = finalS[39];
		fp[1] = finalS[7];
		fp[2] = finalS[47];
		fp[3] = finalS[15];
		fp[4] = finalS[55];
		fp[5] = finalS[23];
		fp[6] = finalS[63];
		fp[7] = finalS[31];

		fp[8] = finalS[38];
		fp[9] = finalS[6];
		fp[10] = finalS[46];
		fp[11] = finalS[14];
		fp[12] = finalS[54];
		fp[13] = finalS[22];
		fp[14] = finalS[62];
		fp[15] = finalS[30];

		fp[16] = finalS[37];
		fp[17] = finalS[5];
		fp[18] = finalS[45];
		fp[19] = finalS[13];
		fp[20] = finalS[53];
		fp[21] = finalS[21];
		fp[22] = finalS[61];
		fp[23] = finalS[29];

		fp[24] = finalS[36];
		fp[25] = finalS[4];
		fp[26] = finalS[44];
		fp[27] = finalS[12];
		fp[28] = finalS[52];
		fp[29] = finalS[20];
		fp[30] = finalS[60];
		fp[31] = finalS[28];


		fp[32] = finalS[35];
		fp[33] = finalS[3];
		fp[34] = finalS[43];
		fp[35] = finalS[11];
		fp[36] = finalS[51];
		fp[37] = finalS[19];
		fp[38] = finalS[59];
		fp[39] = finalS[27];

		fp[40] = finalS[34];
		fp[41] = finalS[2];
		fp[42] = finalS[42];
		fp[43] = finalS[10];
		fp[44] = finalS[50];
		fp[45] = finalS[18];
		fp[46] = finalS[58];
		fp[47] = finalS[26];

		fp[48] = finalS[33];
		fp[49] = finalS[1];
		fp[50] = finalS[41];
		fp[51] = finalS[9];
		fp[52] = finalS[49];
		fp[53] = finalS[17];
		fp[54] = finalS[57];
		fp[55] = finalS[25];

		fp[56] = finalS[32];
		fp[57] = finalS[0];
		fp[58] = finalS[40];
		fp[59] = finalS[8];
		fp[60] = finalS[48];
		fp[61] = finalS[16];
		fp[62] = finalS[56];
		fp[63] = finalS[24];

		String lastString = new String(fp);
		return lastString; 
	}
}

