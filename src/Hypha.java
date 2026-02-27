import java.util.List;

/*
 * Az IngrownHypha és az InterconnectingHypha őse.
 * Az Insect-ek csak rajtuk keresztül mozoghatnak.
 * Csak akkor maradnak életben, ha legalább egy MushroomBody-val kapcsolatban vannak.
 */
abstract class Hypha implements IStepable {
    /*
     * rovarok (Insect) képesek elvágni a gombafonalakat,
     * ekkor a gombafonál 2 nap után elpusztul: ezen változó tárolja,
     * hogy hány napja vágták el az őt éltető MushroomBody-tól
     */
    protected int timeSinceDisconnected = 0;

    /*
     * a Mushroom, amihez a Hypha tartozik
     */
    protected Mushroom mushroom;
    protected int timeSinceCut = -1;

    /*
     * absztrakt függvény, amely a Hypha elpusztításáért felelős
     */
    abstract void destroyHypha();

    /*
     * a timeSinceDisconnected tagváltozó értékét növeli eggyel
     */
    public void incrementTimeSinceDisconnected(){
        timeSinceDisconnected += 1;
    }

    /*
     *  a timeSinceDisconnected tagváltozó értékét nullázza
     */
    public void reconnect(){
        Logger.enter("reconnect", List.of());
        timeSinceDisconnected = 0;
        Logger.exit("reconnect", "");
    }

    /*
     * kis idő eltelését jelenti a modellben, aminek hatására a timeSinceDisconnected
     * értékét növeli (az incrementTimeSinceDisconnected() függvény használatával).
     * Ekkor ellenőrzni, hogy elérte-e már az elpusztuláshoz szükséges időt a Hypha.
     * Ha igen, akkor elpusztítja, ha nem, akkor nem történik semmi.
     */
    public void step(){
        Logger.enter("step", List.of());
        incrementTimeSinceDisconnected();
        if(timeSinceCut != -1){
            incrementTimeSinceCut();
        }
        if(timeSinceDisconnected == 2 || timeSinceCut == 2){
            destroyHypha();
        }
        Logger.exit("step", "");
    }

    //setters
    public void setMushroom(Mushroom m){
        Logger.enter("setMushroom", List.of("m"));
        mushroom = m;
        Logger.exit("setMushroom", "");
    }
    public void startTimeSinceCut(){
        timeSinceCut = 0;
    }
    public void setTimeSinceDisconnected(int i){ timeSinceDisconnected = i; }
    public void incrementTimeSinceCut(){timeSinceCut += 1;}

    //getters
    public Mushroom getMushroom() { return mushroom; }
    public int getTimeSinceDisconnected() { return timeSinceDisconnected; }
    public int getTimeSinceCut() { return timeSinceCut; }

}
