import java.util.List;

/*
 * A Spore leszármazottja, egy olyan gyorsító spóra,
 * amit ha egy rovar megeszik, akkor utána egy ideig
 * a mozgás akció hatására kettőt is mozoghat.
 * A Bugger-ek egyik pontszerzési lehetősége és a
 * Mushroom-ok egyik olyan eszköze, amivel később
 * MushroomBody-t növeszthetnek.
 */
public class Vortex extends Spore{
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Vortex(){
        Logger.enter("Vortex", List.of());
        food = 1;
        Logger.exit("Vortex", "-");
    }

    /*
     * A paraméterül kapott Insect-en beállítja
     * az előre beállított paramétereket.
     * Ez a paraméter a speed.
     */
    @Override
    public int setEffect(Insect i) {
        Logger.enter("setEffect", List.of("i"));
        i.setSpeed(2);
        i.setEffectDuration(2);
        Logger.exit("setEffect", "food");
        return food;
    }
}
