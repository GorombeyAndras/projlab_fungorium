import java.awt.*;

public class TectonView extends ViewBase {
    private Tecton tecton;
    private String name;

    public TectonView(int x, int y, Color color, Tecton tecton, String name) {
        super(x, y, color);
        this.tecton = tecton;
        this.name = name;
    }
    public Tecton getTecton() {
        return tecton;
    }
    @Override
    public void draw(Graphics g) {

        g.setColor(color);
        g.fillOval(x, y,50, 50);
        for(SporeView v: View.getSporeViewList()){
            if(tecton.getSporesOnTecton().contains(v.getSpore())){
                v.updatePosition(this.x+25, this.y+25);
                v.draw(g);
            }
        }
        //<3



    }
    public void drawText(Graphics g) {
        if(name != null){
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.PLAIN, 28));
            g.drawString(name, x + 3, y + 34);
            g.setFont(new Font("Arial", Font.PLAIN, 15));
        }
    }
}
