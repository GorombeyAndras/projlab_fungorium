import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Az adott gombához tartozó gombafonalak (Hypha) és gombatestek (MushroomBody) listáját tartalmazó osztály.
 */

public class Mushroom implements IPlayerScore {
    /*
     * A gombász pontjait tárolja
     */
    private int score = 0;
    /*
     * A gombász hyphainak listája
     */
    private List<Hypha> hyphaList = new ArrayList<>();
    /*
     * Default konstruktor
     */
    public Mushroom() {
        Logger.enter("Mushroom", List.of());
        Logger.exit("Mushroom", "-");
    }
    /*
     * hozzáadja a hyphaList listához a paraméterként kapott Hypha-t.
     */
    public void addHypha(Hypha h){
        Logger.enter("addHypha", List.of("h"));
        hyphaList.add(h);
        h.setMushroom(this);
        Logger.exit("addHypha", "");
    }
    /*
     * eltávolítja a hyphaList-ből a paraméterként kapott Hypha-t.
     */
    public void removeHypha(Hypha h){
        Logger.enter("removeHypha", List.of("h"));
        hyphaList.remove(h);
        h.setMushroom(null);
        Logger.exit("removeHypha", "");
    }
//    /*
//     * az összes olyan Hypha-nak, ami a paraméterként kapott Hypha-val egy vonalon van, meghívja a reconnect függvényét
//     */
//    public void reconnectAllHyphaeOnLine(InterconnectingHypha h){
//        Logger.enter("reconnectAllHyphaeOnLine", List.of("h"));
//        Set<Hypha> visitedH = new HashSet<>();
//        Set<InterconnectingHypha> buffer = getAllConnsOfHypha(h);
//        while(!buffer.isEmpty()){
//            for(InterconnectingHypha ih : buffer){
//                if(!visitedH.contains(ih)){
//                    visitedH.add(ih);
//                    Set<InterconnectingHypha> buff2 = getAllConnsOfHypha(ih);
//                    buff2.removeAll(visitedH);
//                    buffer.addAll(buff2);
//                }
//                buffer.remove(ih);
//                ih.reconnect();
//            }
//        }
//        Logger.exit("reconnectAllHyphaeOnLine", "");
//    }
//    /*
//     * Segédfüggvény az intercconectingHypha endpointjához
//     */
//    public Set<InterconnectingHypha> getAllConnsOfHypha(InterconnectingHypha h){
//        Set<InterconnectingHypha> foundInterc = new HashSet<>();
//        Tecton ending = h.getEndingTecton();
//        IngrownHypha ing = ingrownHyphaOnTectonFromThisMushroom(ending);
//        if(ing != null){
//            List<InterconnectingHypha> interc = interconnectingHyphaeOnTectonFromThisMushroom(ending);
//            foundInterc.addAll(interc);
//            ing.reconnect();
//        }
//        return foundInterc;
//
//    }
    /*
     * növeszt egy InterconnectingHypha-t a két paraméterként átadott Tecton közé, ha a t1 Tecton-on van már IngrownHypha.
     */
    public List<Hypha> growHypha(Tecton t1, Tecton t2){
        Logger.enter("growHypha", List.of("t1", "t2"));
        List<Hypha> ret = new ArrayList<>();
        if(t2.allowedToGrow(this) ){
            // Interconnecting
            InterconnectingHypha h1 = new InterconnectingHypha();
            ret.add(h1);
            if (t1.hashCode() != t2.hashCode()) {
                h1.setHyphaEndpoints(t1, t2);
                addHypha(h1);
            }

            // Ingrown a t2-re
            if(!t2.getIngrownHyphaeOnTecton().stream().anyMatch(x -> x.getMushroom().equals(this))){
                IngrownHypha h2 = new IngrownHypha();
                t2.addIngrownHypha(h2);
                if(h2.getHomeTecton() != null){
                    ret.add(h2);
                }
                addHypha(h2);
            }


            // Upgrade és reconnect
            IngrownHypha ing1 = ingrownHyphaOnTectonFromThisMushroom(t1);
            IngrownHypha ing2 = ingrownHyphaOnTectonFromThisMushroom(t2);
            if(ing1!=null&&checkForAdvancement(ing1)){

                ing1.upgradeMushroomBody();
            }
            if(ing2!=null&&checkForAdvancement(ing2)){

                ing2.upgradeMushroomBody();
            }
        }


        Logger.exit("growHypha", "");
        return ret;
    }
    /*
     *   A paraméterként kapott Hypha-nak a Tecton-ján ellenőrzi,
     *   hogy hány Hypha van rajta, az alapján eldönti,
     *   hogy feljessze-e a Hypha-nl évőMushroomBody-t.
     *   Ha fejlesztette a MushroomBody-t,
     *   akkor true-val tér vissza, egyébként false.
     */

    public boolean checkForAdvancement(IngrownHypha h){
        Logger.enter("checkForAdvancement", List.of("h"));
        if(h != null){
            Tecton t = h.getHomeTecton();
            List<InterconnectingHypha> interconecctingsOnTecton = t.getInterconnectingHyphaeOnTecton();
            List<MushroomBody> mblist = h.getMushroomBodiesList();
            if(!mblist.isEmpty()){
                if(interconecctingsOnTecton.size() >= 3 && !mblist.get(mblist.size()-1).isAdvanced()){
                    Logger.exit("checkForAdvancement", "");
                    return true;
                }
            }
            Logger.exit("checkForAdvancement", "");
        }
        return false;
    }
    /*
     * megpróbál növeszteni 2 InterconnectingHypha-t t1-t2 és t1-t3 közé
     * és 2 IngrownHypha-t t2 és t3 Tecton-okra,
     *  ha már t1 Tecton-on van IngrownHypha.
     */
    public List<Hypha> doubleGrowHypha(Tecton t1, Tecton t2, Tecton t3){
        Logger.enter("doubleGrowHypha", List.of("t1", "t2", "t3"));
        List<Hypha> ret = new ArrayList<>();
        if(t2.allowedToGrow(this) && t3.allowedToGrow(this)) {

            // 1. Interconnecting
            InterconnectingHypha h1 = new InterconnectingHypha();
            ret.add(h1);
            h1.setHyphaEndpoints(t1, t2);
            addHypha(h1);

            // 1. Ingrown
            IngrownHypha h2 = new IngrownHypha();
            t2.addIngrownHypha(h2);


            // 2. Interconnecting
            InterconnectingHypha h3 = new InterconnectingHypha();
            ret.add(h3);
            h3.setHyphaEndpoints(t2, t3);
            addHypha(h3);

            // 2. Ingrown
            IngrownHypha h4 = new IngrownHypha();
            t3.addIngrownHypha(h4);


            // Ingrown-ok hozzáadása, amennyiben lehetséges
            if (h2.getHomeTecton() != null) {
                ret.add(h2);
                addHypha(h2);
            }
            if (h4.getHomeTecton() != null) {
                ret.add(h4);
                addHypha(h4);
            }
            IngrownHypha ing1 = ingrownHyphaOnTectonFromThisMushroom(t1);
            IngrownHypha ing2 = ingrownHyphaOnTectonFromThisMushroom(t2);
            IngrownHypha ing3 = ingrownHyphaOnTectonFromThisMushroom(t2);
            if (ing1 != null && checkForAdvancement(ing1)) {
                ing1.upgradeMushroomBody();
            }
            if (ing2 != null && checkForAdvancement(ing2)) {
                ing2.upgradeMushroomBody();
            }
            if (ing3 != null && checkForAdvancement(ing3)) {
                ing3.upgradeMushroomBody();
            }
        }
        Logger.exit("doubleGrowHypha", "");
        return ret;
    }
    /*
     * megnöveli a pontszámot a paraméterben átadott értékkel
     */
    public void increaseScore(int i){
        Logger.enter("increaseScore", List.of("i"));
        score += i;
        Logger.exit("increaseScore", "");
    }
    /*
     * Visszaadja a tekton azon interconnectingHypha-it
     * amik a gombához tartoznak
     */
    public List<InterconnectingHypha> interconnectingHyphaeOnTectonFromThisMushroom(Tecton t1){
        Logger.enter("interconnectingHyphaeOnTectonFromThisMushroom", List.of("t1"));

        List<InterconnectingHypha> hyphaOnThisTecton = new ArrayList<>();
        List<InterconnectingHypha> ihot = t1.getInterconnectingHyphaeOnTecton();

        for(InterconnectingHypha ih : ihot){
            if(this.hyphaList.contains(ih)){
                hyphaOnThisTecton.add(ih);
            }
        }
        Logger.exit("interconnectingHyphaeOnTectonFromThisMushroom", "");
        return hyphaOnThisTecton;
    }
    /*
     *   Visszaadja a tekton azon ingrownHypha-it
     *   amik a gombához tartoznak
     */
    public IngrownHypha ingrownHyphaOnTectonFromThisMushroom(Tecton t1){
        Logger.enter("ingrownHyphaOnTectonFromThisMushroom", List.of("t1"));
        List<IngrownHypha> ihot = t1.getIngrownHyphaeOnTecton();

        for(IngrownHypha ih : ihot){
            if(this.hyphaList.contains(ih)){
                Logger.exit("ingrownHyphaOnTectonFromThisMushroom", "");
                return ih;
            }
        }
        Logger.exit("ingrownHyphaOnTectonFromThisMushroom", "");
        return null;
    }

    public List<Hypha> getHyphaList() {return hyphaList;}

    public int getScore() {
        return score;
    }
}