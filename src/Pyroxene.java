import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Tecton, amin több gombafaj fonalai is
 * kereszteződni tudnak.  A Tecton-ra szabadon
 * léphetnek Insect-ek, ha van hozzá vezető Hypha.
 * A tekton spontán kettétörhet. Az addIngrownHypha()
 * és addInterconnectingHypha() függvényt felülírja.
 */
public class Pyroxene extends Tecton {
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Pyroxene() {
        Logger.enter("Pyroxene", List.of());
        Logger.exit("Pyroxene", "-");
    }

    /*
     * A Tekton spontán két részre tud törni. Az új tekton szintén Pyroxene.
     * Ekkor az újonnan létrejött példányok megtartják az effektusaikat
     * és véletlenszerűen kisorsoljuk, hogy melyik tekton kapja a rajtuk
     * lévő Hyhpa és Insect példányokat, ha vannak rajta ilyenek.
     */
    @Override
    public Tecton breakT() {
        Logger.enter("breakT", List.of());
        Pyroxene newPyroxene = new Pyroxene();

        this.addNeighbour(newPyroxene);
        newPyroxene.addNeighbour(this);

        Iterator<IngrownHypha> ighIterator = ingrownHyphaeOnTecton.iterator();
        while (ighIterator.hasNext()) {
            IngrownHypha igh = ighIterator.next();
            if (Tecton.decideOnTecton(this, newPyroxene) == newPyroxene) {
                ighIterator.remove(); // Safely remove
                newPyroxene.addIngrownHypha(igh);
            }
        }

        Iterator<InterconnectingHypha> ichIterator = interconnectingHyphaeOnTecton.iterator();
        while (ichIterator.hasNext()) {
            InterconnectingHypha ich = ichIterator.next();
            if (Tecton.decideOnTecton(this, newPyroxene) == newPyroxene) {
                ichIterator.remove(); // Safely remove
                ich.setStartingTecton(newPyroxene);
                newPyroxene.addInterconnectingHypha(ich);
            }
        }
        Iterator<Insect> insectIterator = insectsOnTecton.iterator();
        while (insectIterator.hasNext()) {
            Insect i = insectIterator.next();
            if (Tecton.decideOnTecton(this, newPyroxene) == newPyroxene) {
                insectIterator.remove();
                newPyroxene.addInsectOnTecton(i);
            }
        }
        Iterator<Spore> sporeIterator = sporesOnTecton.iterator();
        while (sporeIterator.hasNext()) {
            Spore sp = sporeIterator.next();
            if (Tecton.decideOnTecton(this, newPyroxene) == newPyroxene) {
                sporeIterator.remove();
                newPyroxene.addSpore(sp);
            }
        }
        Logger.exit("breakT", "");
        return newPyroxene;
    }

    /*
     * Megvalósítja az ISteppable interfészt,
     * érdemi működése nincs.
     */
    @Override
    public void step() {  }

    @Override
    public boolean allowedToGrow(Mushroom mushroom){
        Logger.enter("allowedToGrow", List.of("mushroom"));
        //Ha önmaga már rajta van egyszer nem lehet mégegyszer
        if(getIngrownHyphaeOnTecton().stream().anyMatch(x -> x.getMushroom().equals(mushroom))){
            Logger.exit("allowedToGrow", "");
            return false;
        }
        Logger.exit("allowedToGrow", "");
        return true;
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
}
