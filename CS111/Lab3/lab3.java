// Lab 3
// Samantha Brooks
// Ta: Corey Hinkle 
// Instructor: Cindy Tanner

import java.util.Scanner;
import java.io.FileReader;

public class lab3
{

    public static void main(String [] Args) throws Exception{

	Scanner in = new Scanner(new FileReader("lab3.txt"));
	int[][] myArray;
	myArray = new int [3][10];
	int currentSize = 0;
	int sum = 0; 


	for (int i = 0; i < 3; i++)
	    {   
		for (int j = 0; j < 9; j++)
		    {  
			myArray [i][j] = in.nextInt(); 
		
			
		    } // end inner for
	    }// end outter for


	for (int i = 0; i < 3; i++)
	    {   
		for (int j = 0; j < 9; j++)
		    {  

			sum +=myArray[i][j];
			myArray[i][9] = sum;
			

		    } // end inner for
		sum = 0;
	    }// end outter for

	print(myArray);
    }

    public static void print(int myArray[][])
    {
		

	for(int i = 0; i <  3; i++)
	    {
		for (int j = 0 ; j < 10 ; j++)
		    {	 
			System.out.print(myArray[i][j] + " " );
		         }// end inner for

		System.out.println();

	    }// end outter for
	    } //end print



    }// end class