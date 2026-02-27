import java.util.ArrayList;
import java.util.List;
/*
 * A Tecton-okon növő Hypha-kat megvalósító osztály, csak rajtuk nőhet MushroomBody.
 */
public class IngrownHypha extends Hypha {
    /*
     * az a Tecton, amin az IngrownHypha van.
     */
    private Tecton homeTecton;

    /*
     * tárolja a rajta lévő MushroomBody-t, ha van.
     */
    private List<MushroomBody> mushroomBodiesList;

    /*
     * Konstruktor
     */
    public IngrownHypha() {
        Logger.enter("IngrownHypha", List.of());
        mushroomBodiesList = new ArrayList<>();
        Logger.exit("IngrownHypha", "-");
    }

    /*
     * az IngrownHypha-n lévő MushroomBody fejlesztéséért felelős függvény.
     */
    public void upgradeMushroomBody(){
        Logger.enter("upgradeMushroomBody", List.of());
        if(!mushroomBodiesList.isEmpty()){
            mushroomBodiesList.get(0).upgradeMushroomBody();
        }
        Logger.exit("upgradeMushroomBody", "-");
    }

    /*
     * MushroomBody-t  növeszt az IngrownHypha-ra
     */
    public void addMushroomBody(MushroomBody mb){
        Logger.enter("addMushroomBody", List.of("mb"));
        mushroomBodiesList.add(mb);
        this.getMushroom().increaseScore(1);
        mb.setIngrownHypha(this);
        Logger.exit("addMushroomBody", "");
    }

    /*
     * kitörli a MushroomBody-t az IngrownHypha-ról, ha az elpusztul (a Spore szórás következtében)
     */
    public void removeMushroomBody(MushroomBody mb){
        Logger.enter("removeMushroomBody", List.of("mb"));
        mushroomBodiesList.remove(mb);
        Logger.exit("removeMushroomBody", "");
    }

    /*
     * az elpusztításért felelős függvény.
     */
    public void destroyHypha(){
        Logger.enter("destroyHypha", List.of());
        homeTecton.removeIngrownHypha(this);
        mushroom.removeHypha(this);
        timeSinceDisconnected = 2;
        Logger.exit("destroyHypha", "");
    }
    /*
     * Ha a rovar nem tud mozogni, akkor meghívja rá a getEaten függvényét, illetve ha nincs még gombatest a tektonon akkor növeszt egyet
     */
    public MushroomBody consumeBug(Insect i){
        Logger.enter("consumeBug", List.of());
        if(i.getSpeed() == 0){
            i.getEaten();
            boolean thereIsABody= homeTecton.thereIsABody();
            if(!thereIsABody){
                MushroomBody mb = new MushroomBody();
                addMushroomBody(mb);
                Logger.exit("consumeBug", "");
                return mb;
            }
        }
        Logger.exit("consumeBug", "");
        return null;
    }
    @Override
    public void step(){
        Logger.enter("step", List.of());
        incrementTimeSinceDisconnected();
        if(!mushroomBodiesList.isEmpty()){
             mushroomBodiesList.get(0).lifePulse();
        }
        if(timeSinceDisconnected == 2){
            destroyHypha();
        }
        Logger.exit("step", "");
    }

    //setter
    public void setHomeTecton(Tecton homeTecton){
        Logger.enter("setHomeTecton", List.of("homeTecton"));
        this.homeTecton = homeTecton;
        Logger.exit("setHomeTecton", "");
    }

    //getters
    public Tecton getHomeTecton(){ return homeTecton; }
    public List<MushroomBody> getMushroomBodiesList() {
        return mushroomBodiesList;
    }
}
