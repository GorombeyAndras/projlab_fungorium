import java.util.Iterator;
import java.util.List;

/*
 * Olyan Tecton, amin MushroomBody nem tud nőni,
 * csak Hypha. Ezt úgy érjük el, hogy a
 * growMushroomBody(h: IngrownHypha) függvényt
 * felülírja, és a MushroomBody létrehozása helyett
 * csak visszatér (így nem tud MushroomBody nőni rajta).
 * A Tanzanite-ra szabadon léphetnek rovarok,
 * ha van hozzájuk vezető gombafonál.
 * A Tecton spontán kettétörhet.
 */
public class Tanzanite extends Tecton {
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Tanzanite() {
        Logger.enter("Tanzanite", List.of());
        Logger.exit("Tanzanite", "-");
    }

    /*
     * A Tekton spontán két részre tud törni. Az új tekton szintén Tanzanite.
     * Ekkor az újonnan létrejött példányok megtartják az effektusaikat
     * és véletlenszerűen kisorsoljuk, hogy melyik tekton kapja a rajtuk
     * lévő Hyhpa és Insect példányokat, ha vannak rajta ilyenek.
     */
    @Override
    public Tecton breakT() {
        Logger.enter("breakT", List.of());
        Tanzanite newTanzanite = new Tanzanite();

        this.addNeighbour(newTanzanite);
        newTanzanite.addNeighbour(this);

        Iterator<IngrownHypha> ighIterator = ingrownHyphaeOnTecton.iterator();
        while (ighIterator.hasNext()) {
            IngrownHypha igh = ighIterator.next();
            if (Tecton.decideOnTecton(this, newTanzanite) == newTanzanite) {
                ighIterator.remove(); // Safely remove
                newTanzanite.addIngrownHypha(igh);
            }
        }

        Iterator<InterconnectingHypha> ichIterator = interconnectingHyphaeOnTecton.iterator();
        while (ichIterator.hasNext()) {
            InterconnectingHypha ich = ichIterator.next();
            if (Tecton.decideOnTecton(this, newTanzanite) == newTanzanite) {
                ichIterator.remove(); // Safely remove
                ich.setStartingTecton(newTanzanite);
                newTanzanite.addInterconnectingHypha(ich);
            }
        }

        Iterator<Insect> insectIterator = insectsOnTecton.iterator();
        while (insectIterator.hasNext()) {
            Insect i = insectIterator.next();
            if (Tecton.decideOnTecton(this, newTanzanite) == newTanzanite) {
                insectIterator.remove();
                newTanzanite.addInsectOnTecton(i);
            }
        }

        Iterator<Spore> sporeIterator = sporesOnTecton.iterator();
        while (sporeIterator.hasNext()) {
            Spore sp = sporeIterator.next();
            if (Tecton.decideOnTecton(this, newTanzanite) == newTanzanite) {
                sporeIterator.remove();
                newTanzanite.addSpore(sp);
            }
        }

        Logger.exit("breakT", "");
        return newTanzanite;
    }

    /*
     * Megvalósítja az ISteppable interfészt,
     * érdemi működése nincs.
     */
    @Override
    public void step() {    }

    /*
     * Az ősosztálybeli függvény felülírása
     * a függvény eredeti működése helyett,
     * nem fog MushroomBody-t létrehozni.
     */
    @Override
    public MushroomBody growMushroomBody(IngrownHypha inh) {  return null;  }
}
