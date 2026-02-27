import java.util.List;

/*
 * A Clogspore egy olyan (Hypha vágást gátló) Spore,
 * amit ha egy Insect megeszik, akkor utána egy ideig
 * nem tud Hypha-t vágni. A Bugger-ek egyik pontszerzési
 * lehetősége és a Mushroom-ok egyik olyan eszköze,
 * amivel később MushroomBody-t növeszthetnek.
 */
public class Clogspore extends Spore{
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Clogspore(){
        Logger.enter("Clogspore", List.of());
        food = 3;
        Logger.exit("Clogspore", "-");
    }

    /*
     * A spórát megevő Insect nem fog tudni Hypha-t vágni egy ideig.
     */
    @Override
    public int setEffect(Insect i) {
        Logger.enter("setEffect", List.of("i"));
        i.setCanCut(false);
        i.setEffectDuration(2);
        Logger.exit("setEffect", "food");
        return food;
    }
}
