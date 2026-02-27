import java.util.List;

/*
 * A Tecton-okat összekötő Hypha, aminek segítségével az Insect-ek
 * át tudnak mászni egyik Tecton-ról a másikra.
 * Az Insect-ek képesek elvágni.
 */
public class InterconnectingHypha extends Hypha {
    /*
     *  az a Tecon, amin az InterconnectingHypha egyik végpontja van
     */
    private Tecton startingTecton;

    /*
     *  az a Tecon, amin az InterconnectingHypha másik végpontja van
     */
    private Tecton endingTecton;

    /*
     *  Konstruktor
     */
    public InterconnectingHypha() {
        Logger.enter("InterconnectingHypha", List.of());
        Logger.exit("InterconnectingHypha", "-");
    }

    /*
     * ha az a Tecton, amin az InterconnectingHypha egyik végpontja van kettétörik,
     * és a rajta lévő végpont az új Tecton-ra kerül,
     * ez a függvény felelős annak végrehajtásáért.
     */
//    public void updateHyphaEndpoints(Tecton oldEnd, Tecton newEnd){
//        Logger.enter("updateHyphaEndpoints", List.of("oldEnd", "newEnd"));
//        if(startingTecton.equals(oldEnd)){
//            oldEnd.removeInterconnectingHypha(this);
//            startingTecton = newEnd;
//            newEnd.addInterconnectingHypha(this);
//        } else {
//            oldEnd.addInterconnectingHypha(this);
//            endingTecton = newEnd;
//            newEnd.addInterconnectingHypha(this);
//        }
//        Logger.exit("updateHyphaEndpoints", "");
//    }

    /*
     * az elpusztításért felelős függvény
     */
    public void destroyHypha(){
        Logger.enter("destroyHypha", List.of());

        if (startingTecton != null)
            startingTecton.removeInterconnectingHypha(this);

        if (endingTecton != null)
            endingTecton.removeInterconnectingHypha(this);

        if (mushroom != null)
            mushroom.removeHypha(this);
        Logger.exit("destroyHypha", "");
    }

    //getters
    public Tecton getStartingTecton(){
        return startingTecton;
    }
    public Tecton getEndingTecton(){
        return endingTecton;
    }

    //setters
    public void setStartingTecton(Tecton startingTecton){
        this.startingTecton = startingTecton;
    }
    public void setEndingTecton(Tecton endingTecton){
        this.endingTecton = endingTecton;
    }

    /*
     * A paraméterül kapott Tecton-okra állítja be
     * az InterconnectingHypha végpontjait.
     */
    public void setHyphaEndpoints(Tecton startingTecton, Tecton endingTecton) {
        Logger.enter("setHyphaEndpoints", List.of("startingTecton", "endingTecton"));

        if (this.startingTecton != null) {
            startingTecton.removeInterconnectingHypha(this);
        }

        this.startingTecton = startingTecton;
        startingTecton.addInterconnectingHypha(this);

        if(startingTecton.getIngrownHyphaeOnTecton().size() + startingTecton.getInterconnectingHyphaeOnTecton().size() == 3){
            for ( int i = 0; i < startingTecton.getIngrownHyphaeOnTecton().size(); i++){
                if(!startingTecton.getIngrownHyphaeOnTecton().get(i).getMushroomBodiesList().isEmpty()){
                    startingTecton.getIngrownHyphaeOnTecton().get(i).getMushroomBodiesList().get(0).setAdvanced(true);
                }
            }
        }

        if (this.endingTecton != null) {
            endingTecton.removeInterconnectingHypha(this);
        }

        this.endingTecton = endingTecton;
        endingTecton.addInterconnectingHypha(this);
        if(endingTecton.getIngrownHyphaeOnTecton().size() + endingTecton.getInterconnectingHyphaeOnTecton().size() == 3){
            for ( int i = 0; i < endingTecton.getIngrownHyphaeOnTecton().size(); i++){
                if(!endingTecton.getIngrownHyphaeOnTecton().get(i).getMushroomBodiesList().isEmpty()){
                    endingTecton.getIngrownHyphaeOnTecton().get(i).getMushroomBodiesList().get(0).setAdvanced(true);
                }
            }
        }

        Logger.exit("setHyphaEndpoints", "");
    }
}
