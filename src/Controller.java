import java.util.List;
/*
* Az MVC model Controller részét megvalósító
* osztály.
 */
public class Controller {

    /*
     * Kezeli a hyphanövesztést
     */
    public boolean connectButtonPressed(String fromTecton, String toTecton, String thirdOptTecton, String mushroomName) {
        Tecton t1 = Model.getTectonFromName(fromTecton);
        Tecton t2 = Model.getTectonFromName(toTecton);
        //String mushroomName = Model.getCurrentPlayer();
        Mushroom mushroom = Model.getMushroomFromName(mushroomName);
        if(thirdOptTecton.isEmpty()) {
            //normal grow check: t1 és t2 szomszédok, illetve t1-en van megfelelő ingrownHypha
            if(t1.neighbourList.contains(t2) &&
                    t1.getIngrownHyphaeOnTecton().stream().anyMatch(x -> x.getMushroom() == mushroom)) {
                Model.connect(mushroomName, fromTecton, toTecton, thirdOptTecton);
            }else{
                return false;
            }
        }else{
            Tecton t3 = Model.getTectonFromName(thirdOptTecton);
            //doubleGrowcheck: ugyan az mint a normal csak t2 és t3 szomszédossága is vizsgálva van
            if(t1.neighbourList.contains(t2) && t2.neighbourList.contains(t3) &&
                    t1.getIngrownHyphaeOnTecton().stream().anyMatch(x -> x.getMushroom() == mushroom)) {
                Model.connect(mushroomName, fromTecton, toTecton, thirdOptTecton);
            }else{
                return false;
            }
        }

        return true;
    }
    /*
    * A rovarok mozgását kezelő fv
     */
    public boolean moveButtonPressed(String fromTecton, String toTecton, String insect) {
        Tecton t1 = Model.getTectonFromName(fromTecton);
        Tecton t2 = Model.getTectonFromName(toTecton);
        Insect insectonTecton = Model.getInsectFromName(insect);

        if(t1.getInterconnectingHyphaeOnTecton().stream().anyMatch(x -> t2.getInterconnectingHyphaeOnTecton().contains(x))
                && t1.insectsOnTecton.contains(insectonTecton)){
            Model.move(insect, toTecton);
        }else{
            return false;
        }
        return true;
    }
    /*
     * A rovarok spóraevését kezelő fv
     */
    public boolean eat_sporeButtonPressed(String insect) {
        String buggerName = Model.getCurrentPlayer();
        Bugger bugger = Model.getBuggerFromName(buggerName);
        Insect insectonTecton = Model.getInsectFromName(insect);
        Tecton t1 = insectonTecton.getOnTecton();
        if(!t1.sporesOnTecton.isEmpty()){
            Model.eat_spore(insect);
        }else{
            return false;
        }
        return true;
    }
    /*
     * A gombák rovarevését kezelő fv
     */
    public boolean eat_insectButtonPressed(String insect, String mushroomName) {
        Insect insectonTecton = Model.getInsectFromName(insect);
        Tecton t1 = insectonTecton.getOnTecton();
        Mushroom mushroom = Model.getMushroomFromName(mushroomName);
        IngrownHypha igh = mushroom.ingrownHyphaOnTectonFromThisMushroom(t1);
        if(igh.getTimeSinceDisconnected() != 1 && igh.getTimeSinceCut() != 1 && insectonTecton.getSpeed() == 0){
            Model.eat_insect(insect, mushroomName);
        }else{
            return false;
        }
        return true;
    }
    /*
     * A gombák spóraszórását kezelő fv
     */
    public boolean disperseButtonPressed(String fromTecton, String toTecton, String mushroomName) {
        Tecton t1 = Model.getTectonFromName(fromTecton);
        Tecton t2 = Model.getTectonFromName(toTecton);
        Mushroom mushroom = Model.getMushroomFromName(mushroomName);
        IngrownHypha igh = mushroom.ingrownHyphaOnTectonFromThisMushroom(t1);
        List<MushroomBody> mblist = igh.getMushroomBodiesList();
        MushroomBody mb;
        if(mblist.size() == 0){
            return false;
        } else{
            mb = mblist.get(0);
        }
        if(mb.isAdvanced()){
            //t1 és t2-nek van e közös szomszédja
            if(t1.neighbourList.stream().anyMatch(x -> t2.neighbourList.contains(x)) ){
                Model.disperse(mb, toTecton);
            }else{
                return false;
            }
        }else{
            //t1 és t2 szomszédosak-e
            if(t1.neighbourList.contains(t2)){
                Model.disperse(mb, toTecton);
            }else{
                return false;
            }
        }
        return true;
    }
    /*
     * A gombák fonalnövesztését kezelő fv
     */
    public boolean growButtonPressed(String toTecton, String mushroomName) {
        Tecton t1 = Model.getTectonFromName(toTecton);
        Mushroom mushroom = Model.getMushroomFromName(mushroomName);
        if(!t1.thereIsABody() && t1.getSporesOnTecton().size() >= 2 && mushroom.ingrownHyphaOnTectonFromThisMushroom(t1) != null){
            IngrownHypha igh = mushroom.ingrownHyphaOnTectonFromThisMushroom(t1);
            Model.grow(mushroomName, Model.getIngrownHyphaName(igh));
        }else{
            return false;
        }
        return true;
    }
    /*
     * A fonalvágást kezelő fv
     */
    public boolean cutButtonPressed(String fromTecton, String toTecton, String insectName) {
        Tecton t1 = Model.getTectonFromName(fromTecton);
        Tecton t2 = Model.getTectonFromName(toTecton);
        Insect insect = Model.getInsectFromName(insectName);
//        InterconnectingHypha ich = t1.getInterconnectingHyphaeOnTecton().stream()
//                .filter(x -> t2.getInterconnectingHyphaeOnTecton().contains(x))
//                .findFirst().get();
        InterconnectingHypha ich = t1.getInterconnectingHyphaeOnTecton().stream()
                .filter(x -> t2.getInterconnectingHyphaeOnTecton().contains(x))
                .findFirst()
                .orElse(null);

        if (ich == null) return false;

        if(ich.timeSinceCut != 1 && t1.interconnectingHyphaeOnTecton.contains(ich) && t1.insectsOnTecton.contains(insect)){
            Model.cut(insectName, ich);
        }else{
            return false;
        }
        return true;
    }
}
