import java.awt.*;

public class SporeView extends ViewBase {
    private Spore spore;
    private int spotOnTectonX;
    private int spotOnTectonY;
    public SporeView(int x, int y, Color color, Spore spore) {
        super(x, y, color);
        this.spore = spore;
        this.spotOnTectonX = generateRandomCoord();
        this.spotOnTectonY = generateRandomCoord();
    }
    public Spore getSpore() {
        return spore;
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x+spotOnTectonX, y+spotOnTectonY,10, 10);

    }
}
