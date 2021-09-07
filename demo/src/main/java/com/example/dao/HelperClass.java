package com.example.dao;

import java.awt.Point;
import java.util.ArrayList;

public class HelperClass {
	
	// Checks to see if the point to point line is a valid move
	public static boolean isValidAngle(Point p1, Point p2){
    	int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        //get the angle to see the line drawn
        float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        if(angle < 0){ angle += 360; }
        //Return true if valid else return false
        if(angle == 0 || angle == 45 || angle == 90 || angle == 135 || angle == 180 || angle == 225 || angle == 270 || angle == 315){
        	return true;
        } else{
        	return false;
        }
        // 0, 180 L -> R || R -> L
        // 90, 270 U -> D || D -> U
        // 45, 225 L -> R-D || R -> L-U
        // 315, 135 L -> R-U || R -> L-D
    }
	
	
	
	// Check to see which type of line it is
	public static float getAngle(Point p1, Point p2){
		int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        //get the angle to see the line drawn
        float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        if(angle < 0){ angle += 360; }
        return angle;
	}
	
	
	
	// Check to see who is the current player
	public static String getPlayer(int playerCount){
		String player = "";
		if(playerCount % 2 == 0){
			player = "Player 2";
		}else{
			player = "Player 1";
		}
		return player;
	}
	
	
	
	// Adds extra Nodes that are in between on longer line segments 
	public static ArrayList<Point> getExtraEndNodes(NewLine newLine, ArrayList<Point> endNodes){
		Point start = newLine.start;
		Point end = newLine.end;
		float angle = getAngle(start, end);
		
		//         0 = Start -> Right   || 180 = Start -> Left
		//         (0-3, #) -> (0-3, #) || (3-0, #) -> (3-0, #)
		// Lower X.val -> Higher X.val  || Higher X.val -> Lower X.val
		if(angle == 0 || angle == 180){
			int hi;
			int lo;
			//set the points to traverse through
			if(end.x > start.x){ hi = end.x; lo = start.x; }
			else{ hi = start.x; lo = end.x; }

			//if a single line segment, exit
			int diff = hi - lo;
			if(diff == 1){ return endNodes; }
			
			//traverse through the points && add them to end nodes
			for(; lo+1 < hi; lo++){
				Point tmp = new Point(lo+1, end.y);
				endNodes.add(tmp);
			}
			
			return endNodes;
		} //ends Angle = 0 || Angle = 180

		
		
		//         90 = Start -> Down   || 270 = Start -> Up
		//         (#, 0-3) -> (#, 0-3) || (#, 3-0) -> (#, 3-0)
		// Lower Y.val -> Higher Y.val  || Higher Y.val -> Lower Y.val
		if(angle == 90 || angle == 270){
			int hi;
			int lo;
			//set the points to traverse through
			if(end.y > start.y){ hi = end.y; lo = start.y; }
			else{ hi = start.y; lo = end.y; }

			//if a single line segment exit
			int diff = hi - lo;
			if(diff == 1){ return endNodes; }
			
			//traverse through the points && add them to end nodes
			for(; lo+1 < hi; lo++){
				Point tmp = new Point(end.x, lo+1);
				endNodes.add(tmp);
			}
			
			return endNodes;
		} //ends Angle = 90 || Angle = 270


				
		//      45 = Start -> Right-Down || 225 = Start -> Left-Up
		//          (#, #) -> (#+1, #+1) || (#, #) -> (#-1, #-1)
		// Lower XY.val -> Higher XY.val || Higher XY.val -> Lower XY.val
		if(angle == 45 || angle == 225){
			//set the points to traverse through
			int loX;
			int hiX;
			if(end.x > start.x){ hiX = end.x; loX = start.x; }
			else{ hiX = start.x; loX = end.x; }
			
			int loY;
			int hiY;
			if(end.y > start.y){ hiY = end.y; loY = start.y; }
			else{ hiY = start.y; loY = end.y; }
			
			//if a single line segment, exit
			int diff = hiX - loX;
			if(diff == 1){ return endNodes; }
			
			//traverse through the points && add them to end nodes
			for(; loX+1 < hiX; loX++){
				Point tmp = new Point(loX+1, loY+1);
				endNodes.add(tmp);
				loY++;
			}
			
			return endNodes;
		} //ends Angle = 45 || Angle = 225
		
		
		
		//       315 = Start -> Right-Up || 135 = Start -> Left-Down
		//          (#, #) => (#+1, #-1) || (#, #) => (#-1, #+1)
		// X => [X + #] && Y => [Y - #]  || X => [X - #] && Y => [Y + #]
		if(angle == 135 || angle == 315){
			//set up the points to traverse through
			int loX;
			int hiX;
			int loY;
			int hiY;
			if(end.x > start.x){ hiX = end.x; loY = end.y; loX = start.x; hiY = start.y; }
			else{ hiX = start.x; loY = start.y; loX = end.x; hiY = end.y; }
			
			//if a single line segment, exit
			int diff = hiX - loX;
			if(diff == 1){ return endNodes; }
			
			//traverse through the points && add them to end nodes
			for(; loX+1 < hiX; loX++){
				Point tmp = new Point(loX+1, hiY-1);
				endNodes.add(tmp);
				hiY--;
			}
			
			return endNodes;
		} //ends Angle = 135 || Angle = 315
		
		
		return endNodes;
	}
	
	
	
	// Check to see the orientation
	// Return 1 = Clockwise || Return 2 = Counter Clockwise
	public static int orientation(Point s, Point e, Point r){

		int val = (e.y - s.y) * (r.x - e.x) - (e.x - s.x) * (r.y - e.y);


		return (val > 0)? 1: 2;
	}
	
	
	
	// Check to see if two line segments intersect
	public static boolean doesIntersect(Point s1, Point e1, Point s2, Point e2){
		// Basically all the lines in the game do intersect since they are connected to one another
		// We only want lines that pass through one another
		
		//check to see if the lines are connected, if they are return false
		if(s1.equals(e2) || e1.equals(s2)){ return false; }
		
		// Find what types of lines they are
		int r1 = orientation(s1, e1, s2);
		int r2 = orientation(s1, e1, e2);
		int r3 = orientation(s2, e2, s1);
		int r4 = orientation(s2, e2, e1);

		// Lines cross through each other
		if (r1 != r2 && r3 != r4){ return true; }

		return false; //Does not intersect
	}
	
	
	
	//======================================================================================================================================
	public static boolean gameOverHelper(Point vs, Point xy, ArrayList<NewLine> lineArr){
		
		boolean result = false;
		
		for(NewLine l : lineArr){
			Point p1 = l.start;
			Point p2 = l.end;
			result = doesIntersect(vs, xy, p1, p2);
			if(result == true){ return true; }
		}
		
		return result;
	}
	
	
	
	
	public static boolean gameOver(Point vs1, Point vs2, ArrayList<Point> endNodes, ArrayList<NewLine> lineArr){
		//check all moves to the left
		//check all moves center (up 1, down 1)
		//check all moves to the right
		
		//Once points are created check to see if
		// 1.) it is the other start node
		// 2.) it is in the list of end nodes
		int xL = vs1.x - 1;
		int xC = vs1.x;
		int xR = vs1.x + 1;
		int yU = vs1.y - 1;
		int yC = vs1.y;
		int yD = vs1.y + 1;
		
		//Center Column
		if(yC == 0){
			Point cD = new Point(xC, yD);
			//
			boolean doesInt = gameOverHelper(vs1, cD, lineArr);
			if(!cD.equals(vs2) && !endNodes.contains(cD) && doesInt == false){ return false; }
		}
		else if(yC == 3){
			Point cU = new Point(xC, yU);
			//
			boolean doesInt = gameOverHelper(vs1, cU, lineArr);
			if(!cU.equals(vs2) && !endNodes.contains(cU) && doesInt == false){ return false; }
		}
		else{
			Point cU = new Point(xC, yU);
			//
			boolean doesInt = gameOverHelper(vs1, cU, lineArr);
			if(!cU.equals(vs2) && !endNodes.contains(cU) && doesInt == false){ return false; }
			Point cD = new Point(xC, yD);
			//
			doesInt = gameOverHelper(vs1, cD, lineArr);
			if(!cD.equals(vs2) && !endNodes.contains(cD) && doesInt == false){ return false; }
		}
		// Ends Center Column
		
		
		//____________________________________________________________________________________________________________________
		//Left Column
		if(xL > -1){
			if(yC == 0){
				Point lC = new Point(xL, yC);
				//
				boolean doesInt = gameOverHelper(vs1, lC, lineArr);
				if(!lC.equals(vs2) && !endNodes.contains(lC) && doesInt == false){ return false; }
				Point lD = new Point(xL, yD);
				//
				doesInt = gameOverHelper(vs1, lD, lineArr);
				if(!lD.equals(vs2) && !endNodes.contains(lD) && doesInt == false){ return false; }
			}
			else if(yC == 3){
				Point lU = new Point(xL, yU);
				//
				boolean doesInt = gameOverHelper(vs1, lU, lineArr);
				if(!lU.equals(vs2) && !endNodes.contains(lU) && doesInt == false){ return false; }
				Point lC = new Point(xL, yC);
				//
				doesInt = gameOverHelper(vs1, lC, lineArr);
				if(!lC.equals(vs2) && !endNodes.contains(lC) && doesInt == false){ return false; }
			}
			else{
				Point lU = new Point(xL, yU);
				//
				boolean doesInt = gameOverHelper(vs1, lU, lineArr);
				if(!lU.equals(vs2) && !endNodes.contains(lU) && doesInt == false){ return false; }
				Point lC = new Point(xL, yC);
				//
				doesInt = gameOverHelper(vs1, lC, lineArr);
				if(!lC.equals(vs2) && !endNodes.contains(lC) && doesInt == false){ return false; }
				Point lD = new Point(xL, yD);
				//
				doesInt = gameOverHelper(vs1, lD, lineArr);
				if(!lD.equals(vs2) && !endNodes.contains(lD) && doesInt == false){ return false; }
			}
		}// Ends Left Column
		//____________________________________________________________________________________________________________________
		
		
		//--------------------------------------------------------------------------------------------------------------------
		//Right Column
		if(xR < 4){
			if(yC == 0){
				Point rC = new Point(xR, yC);
				//
				boolean doesInt = gameOverHelper(vs1, rC, lineArr);
				if(!rC.equals(vs2) && !endNodes.contains(rC) && doesInt == false){ return false; }
				Point rD = new Point(xR, yD);
				//
				doesInt = gameOverHelper(vs1, rD, lineArr);
				if(!rD.equals(vs2) && !endNodes.contains(rD) && doesInt == false){ return false; }
			}
			else if(yC == 3){
				Point rU = new Point(xR, yU);
				//
				boolean doesInt = gameOverHelper(vs1, rU, lineArr);
				if(!rU.equals(vs2) && !endNodes.contains(rU) && doesInt == false){ return false; }
				Point rC = new Point(xR, yC);
				//
				doesInt = gameOverHelper(vs1, rC, lineArr);
				if(!rC.equals(vs2) && !endNodes.contains(rC) && doesInt == false){ return false; }
			}
			else{
				Point rU = new Point(xR, yU);
				//
				boolean doesInt = gameOverHelper(vs1, rU, lineArr);
				if(!rU.equals(vs2) && !endNodes.contains(rU) && doesInt == false){ return false; }
				Point rC = new Point(xR, yC);
				//
				doesInt = gameOverHelper(vs1, rC, lineArr);
				if(!rC.equals(vs2) && !endNodes.contains(rC) && doesInt == false){ return false; }
				Point rD = new Point(xR, yD);
				//
				doesInt = gameOverHelper(vs1, rD, lineArr);
				if(!rD.equals(vs2) && !endNodes.contains(rD) && doesInt == false){ return false; }
			}
		}// Ends Right Column
		//--------------------------------------------------------------------------------------------------------------------
		
		
		return true;
	}//Ends Method

	
	
}
