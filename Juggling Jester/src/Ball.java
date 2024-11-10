import java.io.*;
import java.util.*;


public class Ball {

	private int posX, posY;
	private int radius;
	private int xVel, yVel;
	private boolean hitBall = false;
	
	public Ball(int x, int y, int r) {
		posX = x;
		posY = y;
		radius = r;
		
		if(r >= 20 && r < 30) {
			xVel = 5;
		}
		else if(r >= 30 && r < 40) {
			xVel = 4;
		}
		else if(r >= 40 && r < 50) {
			xVel = 3;
		}
		else if(r >= 50 && r < 60) {
			xVel = 2;
		}
		else {
			xVel = 1;
		}
		if(Math.random() >= 0.5) {
			xVel *= -1;
		}
		yVel = 0;
	}
	public int getX() {
		return posX;
	}
	public int getY() {
		return posY;
	}
	public int getR() {
		return radius;
	}
	public int getXVel() {
		return xVel;
	}
	public int getYVel() {
		return yVel;
	}
	public int getCenter() {
		return posX + radius/2;
	}
	
	public boolean getHitball() {
		return hitBall;
	}
	public void setHitball(boolean s) {
		hitBall = s;
	}
	
	public void setX(int x) {
		posX = x;
	}
	public void setY(int y) {
		posY = y;
	}
	public void setXVel(int x) {
		xVel = x;
	}
	public void setYVel(int y) {
		yVel = y;
	}
	
	public void decreaseY() {
		yVel++;;
	}
	
	
	
}
