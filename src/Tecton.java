import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
* A Tekton spontán két részre tud törni.
* Ekkor az újonnan létrejött példányok megtartják az effektusaikat
* és véletlenszerűen kisorsoljuk, hogy melyik tekton kapja a rajtuk
* lévő Hyhpa és Insect példányokat, ha vannak rajta ilyenek.
*/
abstract class Tecton implements IStepable {
    /*
    * Insect osztály mely példányai tartózkodnak egy adott tektonon.
    */
    protected List<Insect> insectsOnTecton = new ArrayList<Insect>();

    /*
     * A rajta lévő Spore-ok listája.
     */
    protected List<Spore> sporesOnTecton = new ArrayList<Spore>();

    /*
     * A szomszédos Tecton-ok listája.
     */
    protected List<Tecton> neighbourList = new ArrayList<Tecton>();

    /*
     * A rajta lévő IngrownHypha-k listája.
     */
    protected List<IngrownHypha> ingrownHyphaeOnTecton = new ArrayList<IngrownHypha>();

    /*
     * A rajta lévő InterconnectingHypha-k listája.
     */
    protected List<InterconnectingHypha> interconnectingHyphaeOnTecton = new ArrayList<InterconnectingHypha>();

    /*
     * A Tekton spontán két részre tud törni.
     * Ekkor az újonnan létrejött példányok megtartják az effektusaikat
     * és véletlenszerűen kisorsoljuk, hogy melyik tekton kapja a rajtuk
     * lévő Hyhpa és Insect példányokat, ha vannak rajta ilyenek.
     */
    public abstract Tecton breakT();

    /*
     * A Tecton-ra InterconnectingHypha-t növesztünk.
     * Ekkor a rajta lévő InterconnectingHypha-kat nyilvántartó listát frissítjük.
     */
    public void addInterconnectingHypha(InterconnectingHypha inh) {
        Logger.enter("addInterconnectingHypha", List.of("inh"));
        interconnectingHyphaeOnTecton.add(inh);
        Logger.exit("addInterconnectingHypha", "");
    }

    /*
     * A Tecton-ról InterconnectingHypha-t távolítunk el.
     * Ekkor a rajta lévő InterconnectingHypha-kat nyilvántartó listát frissítjük.
     */
    public void removeInterconnectingHypha(InterconnectingHypha inh){
        Logger.enter("removeInterconnectingHypha", List.of("inh"));
        interconnectingHyphaeOnTecton.remove(inh);
        Logger.exit("removeInterconnectingHypha", "");
    }

    /*
     * Játék léptetésének hatására bekövetkező események különbözőek,
     * így ezt a függvényt a leszármazottakban felüldefiniáljuk.
     */
    public abstract void step();

    /*
     * Absztrakt függvény ami MushroomBody-t növeszt a megfelelő IngrownHypha-ra,
     * amit a megfelelő leszármazott felüldefiniál a saját viselkedésével.
     */
    public abstract MushroomBody growMushroomBody(IngrownHypha inh);

    /*
     * Felvesszük a paraméterként kapott Insect-et az insectsOnTecton listába.
     */
    public void addInsectOnTecton(Insect i){
        Logger.enter("addInsectOnTecton", List.of("i"));
        insectsOnTecton.add(i);
        i.setOnTecton(this);
        Logger.exit("addInsectOnTecton", "");
    }

    /*
     * Eltávolítjuk a paraméterként kapott Insect-et az insectsOnTecton listából.
     */
    public void removeInsectOnTecton(Insect in){insectsOnTecton.remove(in);}

    /*
     * Statikus függvény, aminek segítségével kisorsolhatjuk,
     * hogy a Tecton-on lévő Insect-ek és Hypha-k hova kerüljenek a break() hatására
     */
    public static Tecton decideOnTecton(Tecton t1, Tecton t2){
        Random rand = new Random();
        int rnd = rand.nextInt(2);
        if(rnd == 0){
            return t1;
        }
        return t2;
    }

    /*
     * Spore hozzáadása a sporesOnTecton listából.
     */
    public void addSpore(Spore sp){sporesOnTecton.add(sp);}

    /*
     * Absztrakt függvény ami MushroomBody-t növeszt a megfelelő IngrownHypha-ra,
     * amit a megfelelő leszármazott felüldefiniál a saját viselkedésével.
     */
    public void removeSpore(Spore sp){
        Logger.enter("removeSpore", List.of("sp"));
        sporesOnTecton.remove(sp);
        Logger.exit("removeSpore", "");
    }

    /*
     * A Tecton-ra IngrownHypha-t növesztünk.
     * Ekkor a rajta lévő IngrownHypha-kat nyilvántartó listát frissítjük.
     */
    public void addIngrownHypha(IngrownHypha inh){
        Logger.enter("addIngrownHypha", List.of("inh"));
        int i = ingrownHyphaeOnTecton.size();
        while (i > 0){
            if(ingrownHyphaeOnTecton.get(i-1).mushroom == inh.mushroom){
                break;
            }
            i--;
        }
        if(i == 0){
            inh.setHomeTecton(this);
            ingrownHyphaeOnTecton.add(inh);
        }
        Logger.exit("addIngrownHypha", "");
    }

    /*
     * A Tecton-ról IngrownHypha-t távolítunk el.
     * Ekkor a rajta lévő IngrownHypha-kat nyilvántartó listát frissítjük.
     */
    public void removeIngrownHypha(IngrownHypha inh){
        Logger.enter("removeIngrownHypha", List.of("inh"));
        ingrownHyphaeOnTecton.remove(inh);
        Logger.exit("removeIngrownHypha", "");
    }

    /*
     * A gombatest növesztéshez szükséges számú spórát eltávolít ömagáról,
     * véletlenszerűen választva ki azokat.
     */
    public void removeSpores(){
        removeSpore(sporesOnTecton.get(0));
        removeSpore(sporesOnTecton.get(0));
    }

    /*
     * Ha egy másik tekton és közötte új Hypha nőhet, akkor a tektonok szomszédosak kell hogy legyenek.
     */
    public void addNeighbour(Tecton tecton){
        Logger.enter("addNeighbour", List.of("tecton"));
        if(!neighbourList.contains(tecton)){
            neighbourList.add(tecton);
        }
        Logger.exit("addNeighbour", "");
    }
    /*
     * Visszatér igaz vagy hamissal, attól függően van-e gombatest az egyik IngrownHypha-ján
     */
    public boolean thereIsABody(){
        Logger.enter("thereIsABody", List.of(""));
        for(IngrownHypha ing : ingrownHyphaeOnTecton){
            if(!ing.getMushroomBodiesList().isEmpty()){
                return true;
            }
        }
        return false;
    }
    public boolean allowedToGrow(Mushroom mushroom){
        Logger.enter("allowedToGrow", List.of("mushroom"));
        if(!ingrownHyphaeOnTecton.stream().anyMatch(x -> !x.getMushroom().equals(mushroom)) || ingrownHyphaeOnTecton.isEmpty()){
            return true;
        }
        Logger.exit("allowedToGrow", "");
        return false;
    }
    /*
     * Getter függvények
     */
    public List<InterconnectingHypha> getInterconnectingHyphaeOnTecton(){
        return interconnectingHyphaeOnTecton;
    }
    public List<IngrownHypha> getIngrownHyphaeOnTecton(){
        return ingrownHyphaeOnTecton;
    }
    public List<Spore> getSporesOnTecton() { return sporesOnTecton; }
}
