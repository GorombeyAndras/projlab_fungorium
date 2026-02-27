import java.awt.*;

public class IngrownHyphaView extends ViewBase {
    private IngrownHypha ingrownHypha;

    public IngrownHyphaView(int x, int y, Color color, IngrownHypha ingrownHypha) {
        super(x, y, color);
        this.ingrownHypha = ingrownHypha;
    }

    @Override
    public void draw(Graphics g) {
        Tecton temp =ingrownHypha.getHomeTecton();
        TectonView temp2=View.getT(temp);
        g.setColor(color);
        g.drawLine(temp2.getX()+25, temp2.getY()+50, temp2.getX()+25, temp2.getY());
        g.drawLine(temp2.getX(), temp2.getY()+25, temp2.getX()+50, temp2.getY()+25);
    }

    public IngrownHypha getIngrownHypha() {
        return ingrownHypha;
    }
}
