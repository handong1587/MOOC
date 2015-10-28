import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.introcs.*;

// TODO: paths cannot applied to non-QU case

public class BoggleSolver {
	
	private BoggleBoard board;
	
	private List<String> boradWords;
	
	private Set<String> boradWords2;
	
	private MyTrieSET wordsTrie;
	//private TrieSET wordsTrie;
	private int rows;
	private int cols;
	private int mat[][];
	private boolean[][] visited;
	
	private Map<String, ArrayList<Integer>> paths;
	private Map<ArrayList<Integer>, ArrayList<Integer>> paths2;

	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	//boradWords = new ArrayList<String>();
    	
    	//boradWords2 = new TreeSet<String>();
    	
    	wordsTrie = new MyTrieSET();
    	//wordsTrie = new TrieSET();
    	for (int i = 0; i < dictionary.length; i++) {
    		wordsTrie.add(dictionary[i]);
    	}    	
    }
    
    private void resetVisited() {
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < cols; j++) {
    			visited[i][j] = false;
    		}
    	}
    }
    
    private void initParam() {
    	mat = new int[rows][cols];
    	visited = new boolean[rows][cols];
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < cols; j++) {
    			mat[i][j] = j * rows + i;
    			visited[i][j] = false;
    		}
    	}
    	
    	boradWords = new ArrayList<String>();
    	boradWords2 = new TreeSet<String>();
    	
    	paths = new HashMap<String, ArrayList<Integer>>();
    	paths2 = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();
    }
    
    private int getIterableSize(Iterable<String> strings) {
    	int size = 0;
    	for (String word : strings) {
    		size++;
    	}
    	return size;
    }
    
    private void recursiveDFS(int cx, int cy, StringBuilder sb) {    	
    	char c = board.getLetter(cx, cy);
    	
    	// special case for processing "QU"
    	if (c == 'Q') {
    		sb.append("QU");
    		if (getIterableSize(wordsTrie.keysWithPrefix(sb.toString())) == 0) {
    			if (sb.length() > 0) {
    				sb.deleteCharAt(sb.length() - 1);
    				sb.deleteCharAt(sb.length() - 1);
    			}
    			return;
    		}
    		else if (wordsTrie.contains(sb.toString())) {  			
    			if (sb.toString().length() >= 3) {
    				boradWords2.add(sb.toString());
    			}
    		}
    		
        	for (int dx = -1; dx <= 1; dx++) {
    			for (int dy = -1; dy <= 1; dy++) {
    				int nx = cx + dx;
    				int ny = cy + dy;   				   				
    				
    				if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && visited[nx][ny] == false) {
    					if (paths.containsKey(sb.toString())) {
        					ArrayList<Integer> al = paths.get(sb.toString());
        					if (al.contains(mat[nx][ny])) {
        						continue;
        					}
        				}
								
						visited[nx][ny] = true;
						recursiveDFS(nx, ny, sb);
						visited[nx][ny] = false;
						
						if (paths.containsKey(sb.toString())) {
							ArrayList<Integer> al = paths.get(sb.toString());
							al.add(mat[nx][ny]);
							paths.put(sb.toString(), al);
						} else {
							ArrayList<Integer> al = new ArrayList<Integer>();
							al.add(mat[nx][ny]);
							paths.put(sb.toString(), al);
						}												
    				}
    			}
        	}
        	
        	if (sb.length() > 0) {
        		sb.deleteCharAt(sb.length() - 1);
        		sb.deleteCharAt(sb.length() - 1);
    		}
    	}
    	else {
    		sb.append(c);
    		
    		if (getIterableSize(wordsTrie.keysWithPrefix(sb.toString())) == 0) {
    			if (sb.length() > 0) {
    				sb.deleteCharAt(sb.length() - 1);
    			}
    			return;
    		}
    		else if (wordsTrie.contains(sb.toString())) {   			
    			if (sb.toString().length() >= 3) {
    				boradWords2.add(sb.toString());
    			}
    		}
    		
        	for (int dx = -1; dx <= 1; dx++) {
    			for (int dy = -1; dy <= 1; dy++) {
    				int nx = cx + dx;
    				int ny = cy + dy;
    				
    				if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && visited[nx][ny] == false) {
//    					if (paths.containsKey(sb.toString())) {
//        					ArrayList<Integer> al = paths.get(sb.toString());
//        					if (al.contains(mat[nx][ny])) {
//        						continue;
//        					}
//        				}
    					
    					visited[nx][ny] = true;
						recursiveDFS(nx, ny, sb);
						visited[nx][ny] = false;
						
						if (paths.containsKey(sb.toString())) {
							ArrayList<Integer> al = paths.get(sb.toString());
							al.add(mat[nx][ny]);
							paths.put(sb.toString(), al);
						} else {
							ArrayList<Integer> al = new ArrayList<Integer>();
							al.add(mat[nx][ny]);
							paths.put(sb.toString(), al);
						}
    				}
    			}
        	}
        	
        	if (sb.length() > 0) {
        		sb.deleteCharAt(sb.length() - 1);
    		}
    	}
    }
    
    private void nonRecursiveDFS(int sx, int sy) {
		int dx[] = {-1, 0, 1, 0};
		int dy[] = {0, -1, 0, 1};
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(mat[sx][sy]);
		visited[sx][sy] = true;
		
		StringBuilder sb = new StringBuilder();
		char c = board.getLetter(sx, sy);
		sb.append(c);
		if (wordsTrie.contains(sb.toString())) {
			if (!boradWords.contains(sb.toString())) {
				boradWords.add(sb.toString());
			}
		}
		
		while (!stack.empty()) {
			int top = stack.peek();
			int cx = top % rows;
			int cy = top / rows;
			
			boolean isBreak = false;
			boolean isValidPrefix = false;
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int nx = cx + dx[i];
					int ny = cy + dy[j];
					if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
						if (visited[nx][ny] == false) {
							visited[nx][ny] = true;
							stack.push(mat[nx][ny]);
							
//							System.out.print(mat[nx][ny]);
//							System.out.println();
							
							c = board.getLetter(nx, ny);
							sb.append(c);
							if (wordsTrie.contains(sb.toString())) {
								StdOut.print("s = " + sb.toString());
								StdOut.println();
								if (!boradWords.contains(sb.toString())) {
									boradWords.add(sb.toString());
								}
								isValidPrefix = true;
							} else if (wordsTrie.keysWithPrefix(sb.toString()) != null) {
								isValidPrefix = true;
							} else {
								isValidPrefix = false;
								stack.pop();
								visited[nx][ny] = false;
								continue;
							}
							
							isBreak = true;
							break;
						}
					}
				}
				if (isBreak)
					break;
			}
			
			// pop the top element only when it do not have a adjacent element
			if (isBreak == false) {
				stack.pop();
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
			}
		}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	this.board = board;
    	rows = board.rows();
    	cols = board.cols();
    	if (rows <= 0 || cols <= 0) {
    		return null;
    	}
    	    	
    	initParam();
    	
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < rows; i++) {
    		for (int j = 0; j < cols; j++) {
    			resetVisited();
    			//nonRecursiveDFS(i, j);
    			
    			visited[i][j] = true;
    			if (sb.length() > 0)
    				sb.delete(0, sb.length() - 1);
    			recursiveDFS(i, j, sb);
    		}
    	}    	
    	
    	//return boradWords;
    	return boradWords2;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if (wordsTrie.contains(word)) {
    		if (word.length() <= 2) {
    			return 0;
    		} else if (word.length() <= 4) {
    			return 1;
    		} else if (word.length() <= 5) {
    			return 2;
    		} else if (word.length() <= 6) {
    			return 3;
    		} else if (word.length() <= 7) {
    			return 5;
    		} else {
    			return 11;
    		}
    	}
    	return 0;
    }
    
    public static void main(String[] args)
    {
//        In in = new In(args[0]);
//        String[] dictionary = in.readAllStrings();
//        BoggleSolver solver = new BoggleSolver(dictionary);
//        BoggleBoard board = new BoggleBoard(args[1]);
//        int score = 0;
//        for (String word : solver.getAllValidWords(board))
//        {
//            StdOut.println(word);
//            score += solver.scoreOf(word);
//        }
//        StdOut.println("Score = " + score);
        
        String dir1 = "E:\\work\\self study\\coursera\\Algorithms, Part II(by Kevin Wayne, Robert Sedgewick)\\assignment\\";
        String dir2 = "Programming Assignment 4 - Boggle\\boggle-testing\\boggle\\";
        In in = new In(dir1 + dir2 + "dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);     
        
        String[] boardnames = {"board-points0", "board-points1", "board-points2", "board-points3", "board-points4", "board-points5", 
        		"board-points100", "board-points200", "board-points300", "board-points400", "board-points500", "board-points750", 
        		"board-points1000", "board-points1250", "board-points1500", "board-points2000", "board-points4410", 
        		"board-points4527", "board-points4540", "board-points13464", "board-points26539"};
        
        for (int i = 0; i < boardnames.length; i++) {
        	BoggleBoard board = new BoggleBoard(dir1 + dir2 + boardnames[i] + ".txt");
            int score = 0;
            int number = 0;
            for (String word : solver.getAllValidWords(board))
            {
                //StdOut.println(word);
                score += solver.scoreOf(word);
                number++;
            }
            StdOut.println("Board = " + boardnames[i]);
            StdOut.println(" Score = " + score);
            //StdOut.println(" Number of Words = " + number);
        }
    }

}
