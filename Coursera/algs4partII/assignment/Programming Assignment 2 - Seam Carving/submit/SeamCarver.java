//import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.introcs.*;

import java.awt.Color;

public class SeamCarver {

	private static final int BORDER_PIXEL_ENERGY = 195075;
	
	private Picture picture;
	private double[][] energy;
	private Color[][] rgb;
	
	private int oriWidth;
	private int oriHeight;
	private int newWidth;
	private int newHeight;
	
	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture)
	{
		this.picture = picture;
		
		this.oriWidth = picture.width();
		this.oriHeight = picture.height();
		this.newWidth = picture.width();
		this.newHeight = picture.height();
		
		// initialize rgb data
		//rgb = new int[width][height];
		rgb = new Color[oriWidth][oriHeight];
		for(int x = 0; x < oriWidth; x++) {
        	for(int y = 0; y < oriHeight; y++) {
        		/*
        		int R = picture.get(x, y).getRed();
        		int G = picture.get(x, y).getGreen();
        		int B = picture.get(x, y).getBlue();
        		R = (R << 16) & 0x00FF0000;
        	    G = (G << 8) & 0x0000FF00;
        	    B = B & 0x000000FF;
        	    rgb[x][y] = 0xFF000000 | R | G | B;
        	    */
        		rgb[x][y] = picture.get(x, y);
        		//rgb[y * oriWidth + x] = picture.get(x, y);
        	}
		}
				
		energy = new double[oriWidth][oriHeight];
		calcEnergy();
	}
	
	// current picture
	public Picture picture()
	{
		Picture pic = new Picture(newWidth, newHeight);
		for(int x = 0; x < newWidth; x++) {
        	for(int y = 0; y < newHeight; y++) {
        		pic.set(x, y, rgb[x][y]);
        		//pic.set(x, y, rgb[y * oriWidth + x]);
        	}
		}
		return pic;
	}
	// width of current picture
	public int width()
	{
		return this.newWidth;
	}
	// height of current picture
	public int height()
	{
		return this.newHeight;
	}
	
	private void transposeImage()
	{
		int oriTransWidth = oriHeight;
		int oriTransHeight = oriWidth;
		int transWidth = newHeight;
		int transHeight = newWidth;
		Color [][]rgbTrans = new Color[oriTransWidth][oriTransHeight];
		double [][]energyTrans = new double[oriTransWidth][oriTransHeight];
		for(int x = 0; x < transWidth; x++) {
        	for(int y = 0; y < transHeight; y++) {
        		rgbTrans[x][y] = rgb[y][x];
        		energyTrans[x][y] = energy[y][x];
        	}
		}
		
		// update transposed image information
		oriWidth = oriTransWidth;
		oriHeight = oriTransHeight;
		newWidth = transWidth;
		newHeight = transHeight;
		rgb = rgbTrans;
		energy = energyTrans;
	}
	
	private void transposeImageBack()
	{
		transposeImage();
	}
	
	private void calcEnergy()
	{
		 //make the default energy of border pixels
        for(int i = 0; i < newHeight; i++) {
            energy[0][i] = energy[newWidth-1][i] = BORDER_PIXEL_ENERGY;
        }
        
        for(int i = 0; i < newWidth; i++) {
            energy[i][0] = energy[i][newHeight-1] = BORDER_PIXEL_ENERGY;
        }
        
        for(int x = 1; x < newWidth-1; x++) {
        	for(int y = 1; y < newHeight-1; y++) {
        		int Rx = picture.get(x+1, y).getRed()	- picture.get(x-1, y).getRed();
        		int Gx = picture.get(x+1, y).getGreen()	- picture.get(x-1, y).getGreen();
        		int Bx = picture.get(x+1, y).getBlue()	- picture.get(x-1, y).getBlue();
        		int deltaX2 = Rx * Rx + Gx * Gx + Bx * Bx;
        		
        		int Ry = picture.get(x, y+1).getRed()	- picture.get(x, y-1).getRed();
        		int Gy = picture.get(x, y+1).getGreen()	- picture.get(x, y-1).getGreen();
        		int By = picture.get(x, y+1).getBlue()	- picture.get(x, y-1).getBlue();
        		int deltaY2 = Ry * Ry + Gy * Gy + By * By;
        		
        		energy[x][y] = deltaX2 + deltaY2;
        	}
        }
	}
	
	// energy of pixel at column x and row y
	// O(1)
	public double energy(int x, int y)
	{
		if (x < 0 || x >= newWidth)
			throw new IndexOutOfBoundsException("x must be between 0 and " + (newWidth-1));
        if (y < 0 || y >= newHeight)
        	throw new IndexOutOfBoundsException("y must be between 0 and " + (newHeight-1));
		return energy[x][y];
	}
	
	private void dfs(int x, int y, boolean [][]marked, Stack<Integer> reversePostOrder)
	{
		marked[x][y] = true;
		for (int i = x-1; i < x+1; i++) {
			if (i >= 0 && i < newWidth && y+1 < newHeight) {
				if (marked[i][y+1] == false)
					dfs(i, y+1, marked, reversePostOrder);
			}
		}
		reversePostOrder.push(y * newWidth + x);
	}
	
	// given a start coordinate, calculate post-order
	private Iterable<Integer> reversePost()
	{
		Stack<Integer> reversePostOrder = new Stack<Integer>();
		boolean [][]marked = new boolean[newWidth][newHeight];
		for(int x = 0; x < newWidth; x++) {
        	for(int y = 0; y < newHeight; y++) {
        		marked[x][y] = false;
        	}
		}
//		for(int x = 0; x < width; x++) {
//        	for(int y = 0; y < height; y++) {
//        		if (marked[x][y] == false)
//        			dfs(x, y, marked, reversePostOrder);
//        	}
//		}
		for(int x = newWidth-1; x >= 0; x--) {
        	for(int y = newHeight-1; y >= 0; y--) {
        		if (marked[x][y] == false)
        			dfs(x, y, marked, reversePostOrder);
        	}
		}
		return reversePostOrder;
	}
	
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam()
	{
		transposeImage(); // counterclockwise
		int []seam = findVerticalSeam();
		transposeImageBack(); // clockwise
		return seam;
	}
	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam()
	{
		int []seam = new int[newHeight];
		double [][]sumEnergy = new double[newWidth][newHeight];
		int [][]pixelTo = new int[newWidth][newHeight];
		// initialize
		// note: if dfs start from left to right, then modified below to start from right to left
		for(int x = 0; x < newWidth; x++) {
        	for(int y = 0; y < newHeight; y++) {
        		pixelTo[x][y] = y * newWidth + x;
        		sumEnergy[x][y] = Double.MAX_VALUE;
        	}
		}
		
		Iterable<Integer> reversePostOrder = reversePost();
		for (int v : reversePostOrder) {
			int x = v % newWidth;
			int y = v / newWidth;
			
			// update top line pixel
			if (y == 0) {
				sumEnergy[x][y] = energy[x][y];
			}
			// update downward pixels
			if (y+1 < newHeight) {
				// lower left
				if (x-1 >= 0) {
					if (sumEnergy[x-1][y+1] > sumEnergy[x][y] + energy[x-1][y+1]) {
						sumEnergy[x-1][y+1] = sumEnergy[x][y] + energy[x-1][y+1];
						pixelTo[x-1][y+1] = v;
					}
				}
				// down below
				if (sumEnergy[x][y+1] > sumEnergy[x][y] + energy[x][y+1]) {
					sumEnergy[x][y+1] = sumEnergy[x][y] + energy[x][y+1];
					pixelTo[x][y+1] = v;
				}
				// lower right
				if (x+1 < newWidth) {
					if (sumEnergy[x+1][y+1] > sumEnergy[x][y] + energy[x+1][y+1]) {
						sumEnergy[x+1][y+1] = sumEnergy[x][y] + energy[x+1][y+1];
						pixelTo[x+1][y+1] = v;
					}
				}
			}
		}
		
		double minEnergy = Double.MAX_VALUE;
		int minEnergyIndexX = 0;
		for (int i = 0; i < newWidth; i++) {
			if (minEnergy > sumEnergy[i][newHeight-1]) {
				minEnergy = sumEnergy[i][newHeight-1];
				minEnergyIndexX = i;
			}
		}
		
		for (int i = newHeight-1; i >= 0; i--) {
			seam[i] = minEnergyIndexX;
			// next pixel coordinate X
			minEnergyIndexX = pixelTo[minEnergyIndexX][i] % newWidth;
		}
		
		return seam;
	}
	
	private boolean checkValidSeam(int[] seam)
	{
		for (int i = 0; i < seam.length-1; i++) {
			if (Math.abs(seam[i+1] - seam[i]) > 1) {
				return false;
			}
		}
		return true;
	}
	
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam)
	{
		if (seam == null)
			throw new NullPointerException("can't set seam to null");
		if (seam.length != width())
			throw new IllegalArgumentException("seam length must be " + width());
		if (checkValidSeam(seam) == false) {
			throw new IllegalArgumentException("two adjacent entries differ by more than 1");
		}
		
		if (width() <= 1)
			throw new IllegalArgumentException("width can't be <= 1");
		if (height() <= 1)
			throw new IllegalArgumentException("height can't be <= 1");
		
//		transposeImage();
//		removeVerticalSeam(seam);
//		transposeImageBack();
		
		for (int x = 0; x < newWidth; x++) {
			int yStartPos = seam[x];
			for(int y = yStartPos; y < newHeight-1; y++) {
				rgb[x][y] = rgb[x][y+1];
				energy[x][y] = energy[x][y+1];
			}
		}
		
		this.newHeight--;
	}
	
	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam)
	{
		if (seam == null)
			throw new NullPointerException("can't set seam to null");
		if (seam.length != height())
			throw new IllegalArgumentException("seam length must be " + height());
		if (checkValidSeam(seam) == false) {
			throw new IllegalArgumentException("two adjacent entries differ by more than 1");
		}
		
		if (width() <= 1)
			throw new IllegalArgumentException("width can't be <= 1");
		if (height() <= 1)
			throw new IllegalArgumentException("height can't be <= 1");
		
		for (int y = 0; y < newHeight; y++) {
			int xStartPos = seam[y];
//			int yOffset = y * oriWidth;
//			System.arraycopy(rgb, yOffset+x+1, rgb, yOffset+x, newWidth-1-x);
//			System.arraycopy(energy[y], x+1, energy[y], x, newWidth-1-x);
			for(int x = xStartPos; x < newWidth-1; x++) {
				rgb[x][y] = rgb[x+1][y];
				energy[x][y] = energy[x+1][y];
			}
		}
		
		this.newWidth--;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
