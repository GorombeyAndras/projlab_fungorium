import java.awt.*;

public class InterconnectingHyphaView extends ViewBase {
    private InterconnectingHypha interconnectingHypha;

    public InterconnectingHyphaView(int x, int y, Color color, InterconnectingHypha interconnectingHypha) {
        super(x, y, color);
        this.interconnectingHypha = interconnectingHypha;
    }

    @Override
    public void draw(Graphics g) {
        Tecton temp1 =interconnectingHypha.getStartingTecton();
        Tecton temp2 =interconnectingHypha.getEndingTecton();
        TectonView temp3=View.getT(temp1);
        TectonView temp4=View.getT(temp2);
        g.setColor(color);
        g.drawLine(temp3.getX()+25, temp3.getY()+25, temp4.getX()+25, temp4.getY()+25);

    }

    public InterconnectingHypha getInterconnectingHypha() {
        return interconnectingHypha;
    }
}
