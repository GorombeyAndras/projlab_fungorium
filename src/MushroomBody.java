import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/*
 * Tárolja a Spore-okat (nem attribútum szinten, csak a még létrehozható darabszámuk)
 * és el is dobja őket annak érdekében, hogy további MushroomBody-kat növeszthessünk.
 * A MushroomBody növesztése pontokat eredményez, de csak akkor lehetséges,
 * ha megfelelő Tecton-on próbálkozunk és van elegendő Spore.
 */
public class MushroomBody {
    /*
     * Hány Spore-t vagyunk képesek még kiszórni.
     * Ha ez a szám nulla, akkor elhal a MushroomBody
     */
    private int sporesLeft;

    /*
     * Ha a MushroomBody három szomszédos Tecton-ra
     * növesztett már Hypha-t, akkor fejlődik
     */
    private boolean advanced;

    /*
     * az a Hypha amin a MushroomBody található
     */
    private IngrownHypha ingrownHypha;

    /*
     *  Konstruktor
     */
    public MushroomBody() {
        Logger.enter("MushroomBody", List.of());
        this.advanced = false;
        this.sporesLeft = 3;
        Logger.exit("MushroomBody", "-");
    }

    /*
     * a függvény a paraméterként kapott Tecton-ra létrehoz egy új Spore-t
     */
    public Spore disperseSpore(Tecton tecton) {
        Logger.enter("disperseSpore", List.of("tecton"));
        Random rand = new Random();
        int choice = rand.nextInt(5);
        Spore spore = null;
        switch (choice) {
            case 0:
                spore = new Clogspore();
                tecton.addSpore(spore);
                break;
            case 1:
                spore = new Drownsap();
                tecton.addSpore(spore);
                break;
            case 2:
                spore = new Frostclash();
                tecton.addSpore(spore);
                break;
            case 3:
                spore = new Vortex();
                tecton.addSpore(spore);
                break;
            case 4:
                spore = new Splitspore();
                tecton.addSpore(spore);
                break;
        }

        decremetSporesLeft();

        if(sporesLeft == 0) {
            ingrownHypha.removeMushroomBody(this);
        }

        Logger.exit("disperseSpore", "");

        return spore;
    }

    /*
     * ha a elérte a megfelelő gombafonál számot a tekton amin található,
     * akkor az ingrownHypha meg fogja hívni ezt a függvényét,
     * mely növeli a sporesLeft értékét és az advanced attribútumot igazra állítja
     */
    public void upgradeMushroomBody(){
        Logger.enter("upgradeMushroomBody", List.of());
        if(!advanced) {
            sporesLeft += 2;
            advanced = true;
        }
        Logger.exit("upgradeMushroomBody", "");
    }

    /*
     * csökkenti a MushroomBody-ban eltárolt
     * Spore-ok számát eggyel
     */
    public void decremetSporesLeft(){
        Logger.enter("decremetSporesLeft", List.of());
        sporesLeft--;
//        if(sporesLeft == 0){
//            this.getIngrownHypha().removeMushroomBody(this);
//        }
        Logger.exit("decremetSporesLeft", "");
    }

    public void lifePulse(){
        //hajrá dave have fun --> meg is van (volt)!
        Logger.enter("lifePulse", List.of());

        Set<Hypha> visitedH = new HashSet<>();
        Set<Tecton> visitedT = new HashSet<>();

        Set<InterconnectingHypha> IcH_buffer = new HashSet<>();
        Set<IngrownHypha> IgH_buffer = new HashSet<>();

        IgH_buffer.add(ingrownHypha);

        while(!(IgH_buffer.isEmpty())){
            for(IngrownHypha igh : IgH_buffer){

                if(!visitedH.contains(igh)){
                    visitedH.add(igh);
                    visitedT.add(igh.getHomeTecton());
                    igh.timeSinceDisconnected = 0;

                    for(InterconnectingHypha ich : igh.getHomeTecton().getInterconnectingHyphaeOnTecton()){
                        if(ich.mushroom == igh.mushroom && !visitedH.contains(ich)){
                            IcH_buffer.add(ich);
                        }
                    }
                }
            }
            IgH_buffer.clear();

            for(InterconnectingHypha ich : IcH_buffer){
                if(!visitedH.contains(ich)){
                    visitedH.add(ich);
                    ich.timeSinceDisconnected = 0;

                    if(!visitedT.contains(ich.getStartingTecton())){
                        for(IngrownHypha igh : ich.getStartingTecton().getIngrownHyphaeOnTecton()){
                            if(ich.mushroom == igh.mushroom && !visitedH.contains(igh)){
                                IgH_buffer.add(igh);
                            }
                        }
                    }else if(!visitedT.contains(ich.getEndingTecton())){
                        for(IngrownHypha igh : ich.getEndingTecton().getIngrownHyphaeOnTecton()){
                            if(ich.mushroom == igh.mushroom && !visitedH.contains(igh)){
                                IgH_buffer.add(igh);
                            }
                        }
                    }
                }
            }
            IcH_buffer.clear();
        }


        Logger.exit("lifePulse", "");
    }

    //setters
    public void setIngrownHypha(IngrownHypha ingrownHypha){
        Logger.enter("setIngrownHypha", List.of("ingrownHypha"));
        this.ingrownHypha = ingrownHypha;
        Logger.exit("setIngrownHypha", "");
    }

    public void setAdvanced(boolean advanced){ this.advanced = advanced; }
    public void setsporesLeft(int sporesLeft){ this.sporesLeft = sporesLeft; }
    //getters
    public int getSporesLeft(){ return sporesLeft; }
    public boolean isAdvanced(){ return advanced; }
    public IngrownHypha getIngrownHypha(){ return ingrownHypha; }
}