// Samantha Brooks
// Lab 5 (Tuesday 3:00-4:50)
// Corey Hinkle and Cindy Tanner


public class lab5 {


    public static void  main (String [] args) {
	int i, j = 0;
	Card [] handOne;  
	Card [] handTwo;
	handOne = new Card [5];
	handTwo = new Card [5];

	System.out.println("For hand one: ");
	for (i = 0; i < 6; i++)
	    {  handOne[i] = new Card();
		handOne[i].printCard();
		System.out.println();
      
	    }// end for

	System.out.println("For hand two: ");
	for (j = 0; j < 6; j++)
	    {  handTwo[j]  = new Card();
		handTwo[j].printCard() ;
		System.out.println(); 
			
	
	    }//end for


	    }//end main

    public static  void  inputHand (Card [] handOne){
	int  numberExactPair = 0, facePair = 0, suitPair = 0;
	int i = 0;

	if(handOne[i].sameFace(handOne[i+1]))
	    { 
		facePair = facePair + 1; 
		System.out.println(facePair);
	    }// end if


	if (handOne[i].sameSuit(handOne[i+1]))
	    {
	    suitPair = suitPair + 1;
	System.out.println(suitPair);
    }// end if


    if (handOne[i].equals(handOne[i+1]))
	{
	    numberExactPair = numberExactPair + 1;
    System.out.println(numberExactPair);

}// end if 


    }// end method



}// end class