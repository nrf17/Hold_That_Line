package com.example.dao;
import java.awt.Point;

public class NewLine {

	public Point start;
	public Point end;

	public NewLine(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}
	
	public void setStart(Point start) {
		this.start = start;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
	
}
