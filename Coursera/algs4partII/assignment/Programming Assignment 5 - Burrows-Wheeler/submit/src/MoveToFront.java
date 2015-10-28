import edu.princeton.cs.algs4.*;
import edu.princeton.cs.introcs.*;

//https://github.com/GlaIZier/Java-Algorithms-Coursera-Course/tree/master/src/5%20Week%20Part%202%20Burrows-Wheeler%20Data%20Compression

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MoveToFront {
	
	private static final int R = 256;
	private static char[] seq = new char[R];
	
	private static final int CHAR_BITS = 8;
	private static final int ALPHABET_SIZE = 256;
	
	private static void init() {
		for (int i = 0; i < R; i++) {
    		seq[i] = (char)(i);
    	}
	}
	
	private static List<Character> createANSIList() {
	      List<Character> ansiList = new LinkedList<Character>();
	      for (int alphabetPosition = 0; alphabetPosition < ALPHABET_SIZE ; alphabetPosition++) 
	         ansiList.add((char) alphabetPosition);
	      return ansiList;
	   }

	// apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
//    	init();  	
//    	while (!BinaryStdIn.isEmpty()) {
//            char c = BinaryStdIn.readChar();
//            BinaryStdOut.write(seq[c]);
//            
//            StdOut.print(seq[c]);
//            
//            for (int i = c; i >= 1; i--) {
//            	char pos = seq[i];
//            	seq[i] = seq[i - 1];
//            	seq[i - 1] = pos;           	
//            }
//        }
    	List<Character> moveToFront = createANSIList();
        while (!BinaryStdIn.isEmpty()) {
           char curChar = BinaryStdIn.readChar();
           int alphabetPosition = 0;
           Iterator<Character> moveToFrontIterator = moveToFront.iterator();
           while (moveToFrontIterator.hasNext()) {
              if (moveToFrontIterator.next().equals(Character.valueOf(curChar))) {
                 BinaryStdOut.write(alphabetPosition, CHAR_BITS);
                 char toFront = moveToFront.get(alphabetPosition);
                 moveToFront.remove(alphabetPosition);
                 moveToFront.add(0, toFront);
                 break;
              }
              alphabetPosition++;
           }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
//    	init();
//    	while (!BinaryStdIn.isEmpty()) {
//            char c = BinaryStdIn.readChar();
//            BinaryStdOut.write(seq[c]);
//            
//            StdOut.print(seq[c]);
//            
//            for (int i = c; i >= 1; i--) {
//            	char pos = seq[i];
//            	seq[i] = seq[i - 1];
//            	seq[i - 1] = pos;
//            }
//        }
        List<Character> moveToFront = createANSIList();
        while (!BinaryStdIn.isEmpty()) {
           int curCharPosition = BinaryStdIn.readChar();
           BinaryStdOut.write(moveToFront.get(curCharPosition), CHAR_BITS);
           char toFront = moveToFront.get(curCharPosition);
           moveToFront.remove(curCharPosition);
           moveToFront.add(0, toFront);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
    	if (args.length == 0) 
            throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
         if (args[0].equals("-")) 
            encode();
         else if (args[0].equals("+")) 
            decode();
         else 
            throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
    }

}
