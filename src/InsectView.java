import java.awt.*;
import java.util.Random;

public class InsectView extends ViewBase {
    private Insect insect;
    private int spotOnTectonX;
    private int spotOnTectonY;
    public InsectView(int x, int y, Color color, Insect insect) {
        super(x, y, color);
        this.insect = insect;
        this.spotOnTectonX = generateRandomCoord();
        this.spotOnTectonY = generateRandomCoord();
    }

    @Override
    public void draw(Graphics g) {
        Tecton temp = insect.getOnTecton();
        TectonView temp2=View.getT(temp);
        g.setColor(color);
        g.drawString("oε", temp2.getX()+25+spotOnTectonX, temp2.getY()+25+spotOnTectonY);
    }

    public Insect getInsect() {
        return insect;
    }
}
