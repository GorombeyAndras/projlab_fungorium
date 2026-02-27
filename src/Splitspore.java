import java.util.List;

public class Splitspore extends Spore{
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Splitspore(){
        Logger.enter("Splitspore", List.of());
        food = 1;
        Logger.exit("Splitspore", "-");
    }

    /*
     * A spórát megevő Insect osztódni fog
     */
    @Override
    public int setEffect(Insect i) {
        Logger.enter("setEffect", List.of("i"));
        i.setCanMultiply(true);
        Logger.exit("setEffect", "food");
        return food;
    }
}
