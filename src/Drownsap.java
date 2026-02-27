import java.util.List;

/*
 * A Drownsap egy olyan lassító Spore, amit ha egy
 * Insect megeszik, akkor utána egy ideig nem mozoghat
 * (csak Hypha-t vághat vagy Spore-t ehet).
 * A Bugger-ek egyik pontszerzési lehetősége és a Mushroom-ok
 * egyik olyan eszköze, amivel később MushroomBody-t növeszthetnek.
 */
public class Drownsap extends Spore{
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Drownsap(){
        Logger.enter("Drownsap", List.of());
        food = 5;
        Logger.exit("Drownsap", "-");
    }

    /*
     * A spórát megevő Insect nem fog tudni mozogni egy ideig.
     */
    @Override
    public int setEffect(Insect i) {
        Logger.enter("setEffect", List.of("i"));
        i.setSpeed(0);
        i.setEffectDuration(2);
        Logger.exit("setEffect", "food");
        return food;
    }
}
