import java.awt.*;

public class MushroomBodyView extends ViewBase {
    private MushroomBody mushroomBody;
    private int spotOnTectonX;
    private int spotOnTectonY;
    public MushroomBodyView(int x, int y, Color color, MushroomBody mushroomBody) {
        super(x, y, color);
        this.mushroomBody = mushroomBody;
        this.spotOnTectonX = generateRandomCoord();
        this.spotOnTectonY = generateRandomCoord();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        Tecton temp = mushroomBody.getIngrownHypha().getHomeTecton();
        TectonView temp2=View.getT(temp);
        g.fillRect(temp2.getX()+25+spotOnTectonX, temp2.getY()+25+spotOnTectonY,7, 7);
    }
}
