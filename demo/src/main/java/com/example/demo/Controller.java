package com.example.demo;


import java.awt.Point;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.Body;
import com.example.dao.HelperClass;
import com.example.dao.Initialize;
import com.example.dao.NewLine;


@RestController
public class Controller {
	
	public ArrayList<NewLine> lineArr = new ArrayList<NewLine>(); //keeps track of lines drawn
	public ArrayList<Point> endNodes = new ArrayList<Point>(); //keeps track of the Invalid End Nodes
	
	public int clickCount = 0; //selecting 1st or 2nd node
	public int playerCount = 1; //tell if player 1 or player 2
	public boolean turnOne = true; //tell if it is the 1st move of the game
	public String currPlayer = "Player 1"; //current player display
	
	public Point validStartNode1 = new Point(-1,-1); //keeps track of Valid Start Node
    public Point validStartNode2 = new Point(-1,-1); //keeps track of Valid Start Node
    public Point currStartNode = new Point(-1,-1); //saves correct start node from the previous click
    
    //TO-DO ----------------------------------------------------------------------------------------------------------
    //game end check
    //----------------------------------------------------------------------------------------------------------------
    //_____________________________________________________________________________________________________________________________________
	
    
	@GetMapping("/initialize")
	public Initialize initialize() {
		//Starts the game and resets variables on a new game start
		Body body = new Body(null, "Player 1", "Awaiting Player 1's Move");
		Initialize rePayload = new Initialize("INITIALIZE", body);
		clickCount = 0;
		playerCount = 1;
	    currPlayer = "Player 1";
	    lineArr.clear();
	    endNodes.clear();
	    turnOne = true;
		return rePayload;
	}

	
	@RequestMapping(value = "/node-clicked", method = RequestMethod.POST)
	public Initialize node_clicked(@RequestBody Point inputPayload) {
		
		//1st turn of the game
		if(turnOne == true){
			if (clickCount == 0){
				//Any node clicked is valid on the start of the game
		        currPlayer = HelperClass.getPlayer(playerCount);
		        Body body = new Body(null, currPlayer, "Select a second node to complete the line.");
		        Initialize rePayload = new Initialize("VALID_START_NODE", body);
		        currStartNode = inputPayload;
		        clickCount++;
		        return rePayload;
		    }
			// else if(clickCount == 1)
		    else {
		    	boolean validAngle = HelperClass.isValidAngle(currStartNode,inputPayload);
		    	
		    	//Invalid End Node
		    	//Checks if the line will be valid && Selecting the same Node twice
		    	if(validAngle == false || inputPayload.equals(currStartNode) ){
		    		//Reset player turn
		    		currPlayer = HelperClass.getPlayer(playerCount);
		    		Body body = new Body(null, currPlayer, "Invalid move!");
			        Initialize rePayload = new Initialize("INVALID_END_NODE", body);
			        clickCount = 0;
		    		return rePayload;
		    	}// End Invalid End Node
		    	
		    	//  Below is the Valid End Node Code
		    	else{
		    		//Line will be drawn, update start nodes
		    		validStartNode1 = currStartNode;
		    		validStartNode2 = inputPayload;
		    		NewLine newLine = new NewLine(validStartNode1, validStartNode2);
		    		playerCount++;
		    		currPlayer = HelperClass.getPlayer(playerCount);
		    		Body body = new Body(newLine, currPlayer, null);
		    		Initialize rePayload = new Initialize("VALID_END_NODE", body);
		    		//Set up for next player turn && all other turns in the game
		    		clickCount = 0;
		    		turnOne = false;
		    		//save the line drawn
		    		lineArr.add(newLine);
		    		//add extra end points on a longer line segment
		    		endNodes = HelperClass.getExtraEndNodes(newLine, endNodes);
		    		return rePayload;
		    	}// End Valid End Node
		    	
		    }// ends (clickCount == 1)
		}// ends turnOne
		
		
		
		
		
		//===============================================================================================================================
		// ALL OTHER TURNS
		else {
			
			if (clickCount == 0){
				//Valid Start Node (was clicked) 
				if(inputPayload.equals(validStartNode1) || inputPayload.equals(validStartNode2)){
					currPlayer = HelperClass.getPlayer(playerCount);
					Body body = new Body(null, currPlayer, "Select a second node to complete the line.");
					Initialize rePayload = new Initialize("VALID_START_NODE", body);
					clickCount++;
					// save the start node that was clicked
					if(inputPayload.equals(validStartNode1)){ currStartNode = validStartNode1; }
					else if(inputPayload.equals(validStartNode2)){ currStartNode = validStartNode2; }
					return rePayload;
				}// ends Valid Start Node
				
				//Invalid Start Node
				else{
					currPlayer = HelperClass.getPlayer(playerCount);
					Body body = new Body(null, currPlayer, "Not a valid starting position.");
					Initialize rePayload = new Initialize("INVALID_START_NODE", body);
					return rePayload;
				}// ends Invalid Start Node
				
			}// Ends Click Count == 0
		
			
			//______________________________________________________________________________________________________________________________
			// else if(clickCount == 1)
			else {
				boolean validAngle = HelperClass.isValidAngle(currStartNode,inputPayload);
				NewLine newLine = new NewLine(currStartNode, inputPayload);
				//check to make sure the new line will not intersect any of the lines already drawn
				boolean doesInter = false;
				for(NewLine l : lineArr){
					Point p1 = l.start;
					Point p2 = l.end;
					doesInter = HelperClass.doesIntersect(currStartNode, inputPayload, p1, p2);
					if(doesInter == true){ break; }
				}
				
				//Invalid End Node
				//check if line will be valid, end node selected is ok, line does not intersect with others, && selecting the same twice
				if(validAngle == false || doesInter == true || endNodes.contains(inputPayload)
						|| inputPayload.equals(validStartNode1) || inputPayload.equals(validStartNode2) ){
					//reset player turn
					currPlayer = HelperClass.getPlayer(playerCount);
					Body body = new Body(null, currPlayer, "Invalid move!");
					Initialize rePayload = new Initialize("INVALID_END_NODE", body);
					clickCount = 0;
					return rePayload;
				} // Ends Invalid End Node
			
				// Below is the Valid End Node Code
				else{
					//line will be drawn
					playerCount++;
					currPlayer = HelperClass.getPlayer(playerCount);
					Body body = new Body(newLine, currPlayer, null);
					Initialize rePayload = new Initialize("VALID_END_NODE", body);
					//set up for the next player
					clickCount = 0;
					//save the line drawn
					lineArr.add(newLine);
					//add extra end points on a longer line segment
					endNodes = HelperClass.getExtraEndNodes(newLine, endNodes);
					//ADD THE OLD START NODE TO END NODES List
					//UPDATE THE START NODE
					if(currStartNode.equals(validStartNode1)){ endNodes.add(validStartNode1); validStartNode1 = inputPayload; }
					else if(currStartNode.equals(validStartNode2)){ endNodes.add(validStartNode2); validStartNode2 = inputPayload; }
					//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
					boolean gameOver1 = HelperClass.gameOver(validStartNode1, validStartNode2, endNodes, lineArr);
					boolean gameOver2 = HelperClass.gameOver(validStartNode2, validStartNode1, endNodes, lineArr);
					if(gameOver1 == true && gameOver2 == true){
						String winPlayer = currPlayer + " Wins!";
						Body gameOverBody = new Body(newLine, "Game Over", winPlayer);
						Initialize gameOverResp = new Initialize("Game_Over", gameOverBody);
						return gameOverResp;
					}
					//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
					return rePayload;
				}// Ends Valid End Node
				
			}//ends (clickCount == 1)
			//______________________________________________________________________________________________________________________________
		
		}// ENDS ALL OTHER TURNS
		//===============================================================================================================================
		
		
	}//ends node-clicked
		
			
	@RequestMapping(value = "/error", method = RequestMethod.POST)
	public Error error(){
		Error error = new Error("Invalid type for `id`: Expected INT but got a STRING");
		return error;
	}
}
