import java.util.Iterator;
import java.util.List;

/*
* Specifikus effektusokkal rendelkező Tecton.
* Az időnként bekövetkező földrengések végett
*  felszívódnak a rájuk növesztett Hypha-k.
* Nőhetnek rá Hypha-k és léphetnek rá Insect-ek.
*/

public class Anorthite extends Tecton {
    /*
    * Számláló, hogy mennyi idő van vissza a földrengésig
    */
    private int timeLeftToEarthquake;

    /*
    * Paraméter nélkül hívható konstruktor
    */
    public Anorthite(){
        Logger.enter("Anorthite", List.of());
        timeLeftToEarthquake = 3;
        Logger.exit("Anorthite", "-");
    }

    /*
     * A Tekton spontán két részre tud törni. Az új tekton szintén Anorthite.
     * Ekkor az újonnan létrejött példányok megtartják az effektusaikat
     * és véletlenszerűen kisorsoljuk, hogy melyik tekton kapja a rajtuk
     * lévő Hyhpa és Insect példányokat, ha vannak rajta ilyenek.
     */
    @Override
    public Tecton breakT() {
        Logger.enter("breakT", List.of());
        Anorthite newAnorthite = new Anorthite();

        this.addNeighbour(newAnorthite);
        newAnorthite.addNeighbour(this);

        Iterator<IngrownHypha> ighIterator = ingrownHyphaeOnTecton.iterator();
        while (ighIterator.hasNext()) {
            IngrownHypha igh = ighIterator.next();
            if (Tecton.decideOnTecton(this, newAnorthite) == newAnorthite) {
                ighIterator.remove(); // Safely remove
                newAnorthite.addIngrownHypha(igh);
            }
        }

        Iterator<InterconnectingHypha> ichIterator = interconnectingHyphaeOnTecton.iterator();
        while (ichIterator.hasNext()) {
            InterconnectingHypha ich = ichIterator.next();
            if (Tecton.decideOnTecton(this, newAnorthite) == newAnorthite) {
                ichIterator.remove(); // Safely remove
                ich.setStartingTecton(newAnorthite);
                newAnorthite.addInterconnectingHypha(ich);
            }
        }

        Iterator<Insect> insectIterator = insectsOnTecton.iterator();
        while (insectIterator.hasNext()) {
            Insect i = insectIterator.next();
            if (Tecton.decideOnTecton(this, newAnorthite) == newAnorthite) {
                insectIterator.remove();
                newAnorthite.addInsectOnTecton(i);
            }
        }

        Iterator<Spore> sporeIterator = sporesOnTecton.iterator();
        while (sporeIterator.hasNext()) {
            Spore sp = sporeIterator.next();
            if (Tecton.decideOnTecton(this, newAnorthite) == newAnorthite) {
                sporeIterator.remove();
                newAnorthite.addSpore(sp);
            }
        }

        Logger.exit("breakT", "");
        return newAnorthite;
    }

    /*
     * Csökkenti a timeLeftToEarthquake változó értékét eggyel.
     * Amennyiben az már 0, törli a Tecton-on lévő Hypha-kat
     * és visszaállítja a számlálót 3-ra.
     */
    @Override
    public void step() {
        Logger.enter("step", List.of());
        timeLeftToEarthquake--;
        if(timeLeftToEarthquake == 0){
            for(int i = interconnectingHyphaeOnTecton.size(); i > 0; i--){
                interconnectingHyphaeOnTecton.get(i - 1).destroyHypha();
            }
            for(int i = ingrownHyphaeOnTecton.size(); i > 0; i--){
                ingrownHyphaeOnTecton.get(i - 1).destroyHypha();
            }
            timeLeftToEarthquake = 3;
        }
        Logger.exit("step", "");
    }

    /*
     * Absztrakt függvény megvalósítása,
     * ami MushroomBody-t növeszt a megfelelő IngrownHypha-ra,
     * amit a megfelelő leszármazott felüldefiniál a saját viselkedésével.
     */
    @Override
    public MushroomBody growMushroomBody(IngrownHypha inh) {
        Logger.enter("growMushroomBody", List.of("inh"));
        if(sporesOnTecton.size() >= 2){
            MushroomBody mb = new MushroomBody();
            mb.setIngrownHypha(inh);
            inh.addMushroomBody(mb);
            //inh.getHomeTecton().removeSpores();
            Logger.exit("growMushroomBody", "");
            return mb;
        }
        Logger.exit("growMushroomBody", "");
        return null;
    }

    public int getTimeLeftToEarthquake() {
        return timeLeftToEarthquake;
    }
}
