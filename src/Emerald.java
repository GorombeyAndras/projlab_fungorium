import java.util.Iterator;
import java.util.List;

public class Emerald extends Tecton{
    /*
     * Paraméter nélkül hívható konstruktor
     */
    public Emerald() {
        Logger.enter("Emerald", List.of());
        Logger.exit("Emerald", "-");
    }

    /*
     * Megvalósítja az ISteppable interfészt,
     * A közvetlenül rajta lévő fonalakat életben tartja,
     *  akár kapcsolüdnak MushroomBodíhoz, akár nem.
     */
    @Override
    public void step() {
        Iterator<IngrownHypha> ighIterator = ingrownHyphaeOnTecton.iterator();
        while (ighIterator.hasNext()) {
            IngrownHypha igh = ighIterator.next();
            igh.setTimeSinceDisconnected(0);
        }

        Iterator<InterconnectingHypha> ichIterator = interconnectingHyphaeOnTecton.iterator();
        while (ichIterator.hasNext()) {
            InterconnectingHypha ich = ichIterator.next();
            ich.setTimeSinceDisconnected(0);
        }
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

    /*
     * A Tekton spontán két részre tud törni. Az új tekton szintén Emerald.
     * Ekkor az újonnan létrejött példányok megtartják az effektusaikat
     * és véletlenszerűen kisorsoljuk, hogy melyik tekton kapja a rajtuk
     * lévő Hyhpa és Insect példányokat, ha vannak rajta ilyenek.
     */
    @Override
    public Tecton breakT() {
        Logger.enter("breakT", List.of());
        Emerald newEmerald = new Emerald();

        this.addNeighbour(newEmerald);
        newEmerald.addNeighbour(this);

        Iterator<IngrownHypha> ighIterator = ingrownHyphaeOnTecton.iterator();
        while (ighIterator.hasNext()) {
            IngrownHypha igh = ighIterator.next();
            if (Tecton.decideOnTecton(this, newEmerald) == newEmerald) {
                ighIterator.remove(); // Safely remove
                newEmerald.addIngrownHypha(igh);
            }
        }

        Iterator<InterconnectingHypha> ichIterator = interconnectingHyphaeOnTecton.iterator();
        while (ichIterator.hasNext()) {
            InterconnectingHypha ich = ichIterator.next();
            if (Tecton.decideOnTecton(this, newEmerald) == newEmerald) {
                ichIterator.remove(); // Safely remove
                ich.setStartingTecton(newEmerald);
                newEmerald.addInterconnectingHypha(ich);
            }
        }

        Iterator<Insect> insectIterator = insectsOnTecton.iterator();
        while (insectIterator.hasNext()) {
            Insect i = insectIterator.next();
            if (Tecton.decideOnTecton(this, newEmerald) == newEmerald) {
                insectIterator.remove();
                newEmerald.addInsectOnTecton(i);
            }
        }

        Iterator<Spore> sporeIterator = sporesOnTecton.iterator();
        while (sporeIterator.hasNext()) {
            Spore sp = sporeIterator.next();
            if (Tecton.decideOnTecton(this, newEmerald) == newEmerald) {
                sporeIterator.remove();
                newEmerald.addSpore(sp);
            }
        }

        Logger.exit("breakT", "");
        return newEmerald;
    }
}
