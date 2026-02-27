import javax.swing.*;
import java.awt.*;
import java.util.Random;

public abstract class ViewBase {
    protected int x;
    protected int y;
    protected Color color;

    protected ViewBase(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    protected int getX(){
        return x;
    }
    protected int getY(){
        return y;
    }
    public abstract void draw(Graphics g);
    public void updatePosition(int x, int y) { this.x = x; this.y = y; }
    public int generateRandomCoord(){
        Random rand = new Random();
        return rand.nextInt(-15,15);
    }
}
