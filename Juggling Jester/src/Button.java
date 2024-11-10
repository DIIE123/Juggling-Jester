import java.awt.*;

public class Button {
	int x; // top left
	int y; // top left
	int width;
	int height;
	
	public Button (int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
	}
	
	public boolean checkInBounds(int mouseX, int mouseY) {
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
			return true;
		}
		return false;
	}
}
