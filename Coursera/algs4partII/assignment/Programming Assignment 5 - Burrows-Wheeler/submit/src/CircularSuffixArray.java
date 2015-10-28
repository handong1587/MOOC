//import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.introcs.*;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

	private String input;
	private Integer[] index;
	   
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if  (s == null || s.equals("") ) 
	         throw new java.lang.IllegalArgumentException("Can't get suffix array for empty string!");
	      input = s;
	      index = new Integer[length()];
	      for (int i = 0; i < index.length; i++) 
	         index[i] = i;
	      
	      // algorithm: not to store strings; just compare them using number of shifts
	      Arrays.sort(index, new Comparator<Integer>() {
	         @Override
	         public int compare(Integer first, Integer second) {
	            // get start indexes of chars to compare
	            int firstIndex = first;
	            int secondIndex = second;
	            // for all characters
	            for (int i = 0; i < input.length(); i++) {
	               // if out of the last char then start from beginning
	               if (firstIndex > input.length() - 1) 
	                  firstIndex = 0;
	               if (secondIndex > input.length() - 1) 
	                  secondIndex = 0;
	               // if first string > second
	               if (input.charAt(firstIndex) < input.charAt(secondIndex)) 
	                  return -1;
	               else if (input.charAt(firstIndex) > input.charAt(secondIndex)) 
	                  return 1;
	               // watch next chars
	               firstIndex++;
	               secondIndex++;
	            }
	            // equal strings
	            return 0;
	         }
	      });
	}
	
	// length of s
    public int length() {
    	return input.length();
	}
	
	// returns index of ith sorted suffix
    public int index(int i) {
    	return index[i];
	}
	
	// unit testing of the methods (optional)
    public static void main(String[] args) {
    	CircularSuffixArray csa = new CircularSuffixArray("AAA\n");
        for (int i = 0; i < csa.length(); i++)
        	System.out.println(csa.index(i) );
        System.out.println();
        csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++)
        	System.out.println(csa.index(i) );
	}

}
