import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class PowerUp {

	int type;
	Rectangle rect;
	Image pic;
	int xVel, yVel;
	
	public PowerUp (int t, int x) {
		type = t;
		rect = new Rectangle(x, -50, 64, 64);
		xVel = 3;
		if(Math.random() >= 0.5) {
			xVel *= -1;
		}
		yVel = 0;
		try {
			if(t == 1) {
				pic = Toolkit.getDefaultToolkit().getImage("CaramelApple.png");
			}
			else if(t == 2) {
				pic = Toolkit.getDefaultToolkit().getImage("CottonCandy.png");
			}
			else if(t == 3) {
				pic = Toolkit.getDefaultToolkit().getImage("Pretzel.png");
			}
			else if(t == 4){
				pic = Toolkit.getDefaultToolkit().getImage("Tomato.png");
			}
			else {
				pic = Toolkit.getDefaultToolkit().getImage("Watermelon.png");
			}
		}
		catch(Exception e) {
			System.out.println("File not found");
		}
	}
	
}
