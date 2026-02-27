import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Az Insect-eket irányító játékosok a Bugger-ek. A legnagyobb pontszámmal rendelkező Bugger nyer.
 * Lehetőségük van Tecton-ok között mozogni, InterconnectingHypha-t vágni és Spore-okat fogyasztani.
 */


public class Bugger implements IPlayerScore {
    /*
     * A rovarász pontszáma
     */
    private int score = 0;
    /*
     * A rovarász bogarainak listája
     */
    private List<Insect> insectList = new ArrayList<Insect>();

    /*
     * Definiálja az IPlayerScore interfész függvényét, a Bugger pontszámát tudja növelni
     */
    public void increaseScore(int i) {
        score += i;
    }
    /*
     * Insect felvétele a listára és a rovar tájékozatása erről
     */
    public void addInsect(Insect i) {
        Logger.enter("addInsect", List.of("i"));
        insectList.add(i);
        i.setBuggerOwner(this);
        Logger.exit("addInsect", "");
    }
    public void removeInsect(Insect i) {
        Logger.enter("removeInsect", List.of("i"));
        insectList.remove(i);
        Logger.exit("removeInsect", "");
    }

    public List<Insect> getInsectList() {return insectList;}
    public int getScore() {return score;}
}
