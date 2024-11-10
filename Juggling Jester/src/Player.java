public class Player {
	int xPos;
	int yPos;
	int playerWidth;
	int playerHeight;
	int speed;
	int maxSpeed;
	int acceleration;
	
	public Player (int xPos, int yPos, int playerWidth, int playerHeight, int maxSpeed, int acceleration) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.playerWidth = playerWidth;
		this.playerHeight = playerHeight;
		this.maxSpeed = maxSpeed;
		this.acceleration = acceleration;
		speed = 0;
	}
	
	public void move() {
		speed += acceleration;
		xPos += speed;
		if (xPos > 650) xPos = 1 - playerWidth;
		if (xPos + playerWidth < 0) xPos = 650;
	}
}
