import java.util.List;

/*
 * A Frostclash egy olyan bénító Spore, amit
 * ha egy Insect megeszik, akkor utána egy ideig
 * nem léphet, vagyis nem tud akciót végrehajtani
 * (nem mozoghat, nem ehet Spore-t és nem vághat
 * InterconnectinHypha-t).
 * A Bugger-ek egyik pontszerzési lehetősége és
 * a Mushroom-ok egyik olyan eszköze, amivel
 * később MushroomBody-t növeszthetnek.
 */
public class Frostclash extends Spore{
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Frostclash(){
        Logger.enter("Frostclash", List.of());
        food = 7;
        Logger.exit("Frostclash", "-");
    }

    /*
     * A spórát megevő Insect nem fog tudni
     * akciót végrehajtani egy ideig.
     */
    @Override
    public int setEffect(Insect i) {
        Logger.enter("setEffect", List.of("i"));
        i.setSpeed(0);
        i.setCanCut(false);
        i.setCanEat(false);
        i.setEffectDuration(2);
        Logger.exit("setEffect", "food");
        return food;
    }
}
