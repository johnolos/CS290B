package tasks;

import java.io.Serializable;

import api.Task;

public class TaskMandelbrotSet implements Task, Serializable{

	private double _leftCornerX;
	private double _leftCornerY; 
	private double _edgeLength;
	private int _n; 
	private int _iterationLimit; 
	
	public TaskMandelbrotSet(double leftCornerX, double leftCornerY, double edgeLength, int n, int iterationLimit) {
		set_leftCornerX(leftCornerX);
		set_leftCornerY(leftCornerY);
		set_edgeLength(edgeLength);
		set_n(n);
		set_iterationLimit(iterationLimit);
	}
	
	public Integer[][] execute() {
		
		double edgeLength = get_edgeLength(); 
		int nSquares = get_n();
		double iterationLimit = get_iterationLimit();
		
		double numberOfSquares = edgeLength / nSquares; 
		
		Integer[][] countArray = new Integer[nSquares][nSquares]; 
		
		double xc = get_leftCornerX();
        double yc = get_leftCornerY();
		
		for (int row = 0; row < nSquares; row ++) {
			for (int col = 0; col < nSquares; col ++){ 
				
				double x = 0.0, y = 0.0; 
				int iteration = 0; 
				while (x*x+y*y < 4 && iteration < iterationLimit) {
					double x_new = x*x-y*y + xc; 
					y = 2*x*y + yc; 
					x = x_new; 
					iteration ++; 
				}
				countArray[col][row] = iteration;
				xc += numberOfSquares;
				
				
			}
			yc += numberOfSquares;
			xc = get_leftCornerX();
		}
		return countArray; 
	}

	
	
	
	public double get_leftCornerX() {
		return _leftCornerX;
	}

	public void set_leftCornerX(double _leftCornerX) {
		this._leftCornerX = _leftCornerX;
	}

	public double get_leftCornerY() {
		return _leftCornerY;
	}

	public void set_leftCornerY(double _leftCornerY) {
		this._leftCornerY = _leftCornerY;
	}

	public double get_edgeLength() {
		return _edgeLength;
	}

	public void set_edgeLength(double _edgeLength) {
		this._edgeLength = _edgeLength;
	}

	public int get_n() {
		return _n;
	}

	public void set_n(int _n) {
		this._n = _n;
	}

	public int get_iterationLimit() {
		return _iterationLimit;
	}

	public void set_iterationLimit(int _iterationLimit) {
		this._iterationLimit = _iterationLimit;
	} 
	
	
}
