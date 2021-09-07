package com.example.dao;

public class Body {

	public NewLine newLine; 
	public String heading;
	public String message;
	
	public Body(NewLine newLine, String heading, String message) {
		this.newLine = newLine;
		this.heading = heading;		
		this.message = message;
	}

	public NewLine getNewLine() {
		return newLine;
	}

	public String getHeading() {
		return heading;
	}

	public String getMessage() {
		return message;
	}
	
	public void setNewLine(NewLine newLine) {
		this.newLine = newLine;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
