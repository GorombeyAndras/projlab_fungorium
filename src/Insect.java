/*
 * A Bugger által irányított objektumok. Képesek InterconnectingHypha -kat vágni, Spore-okat elfogyasztani, amik hatással vannak rájuk és pontokat eredményeznek a Bugger-nek.
 */

import java.io.Console;
import java.util.List;


public class Insect implements IStepable {
    /*
     * Hatás időtartama
     */
    private int effectDuration;
    /*
     * A rovar mozgásának sebessége
     */
    private int speed;
    /*
     * Jelzi tud-e a rovar vágni
     */
    private boolean canCut;
    /*
     * Jelzi tud-e a rovar enni
     */
    private boolean canEat;
    /*
     * Jelzi tud-e osztódni a rovar enni
     */
    private boolean canMultiply;
    /*
     * Az a rovarász akihez a rovar tartozik
     */
    private Bugger buggerOwner;
    /*
     * A tekton amin a rovar tartózkodik
     */
    private Tecton onTecton;
    /*
     * Default konstruktor
     */
    public Insect() {
        Logger.enter("Insect", List.of());
        setToDefault();
        Logger.exit("Insect", "-");
    }


    /*
     * Ha az effectDuration értéke 0-ra csökken, az alapértelmezett, effektmentes állapotba állítja a rovart.
     */
    public void setToDefault() {
        Logger.enter("setToDefault", List.of());
        this.effectDuration = 0;
        this.speed = 1;
        this.canCut = true;
        this.canEat = true;
        this.canMultiply = false;
        Logger.exit("setToDefault", "");
    }
    /*
     *   A step függvény hatására meghívódik az Insect decreaseEffectDuration függvénye, ami után ellenőrzi, hogy az 0-e.
     *   Ha igen, akkor meghívja a setDefault függvényét az Insect-nek, ha nem, akkor nem történik semmi.
     */

    public void step(){
        Logger.enter("step", List.of());
        if(effectDuration != 0){
            decreaseEffectDuration();
            if(effectDuration == 0) {
                setToDefault();
            }
        }
        Logger.exit("step", "");
    }
    /*
     * Csökkenti az Insect-en lévő effect hatásának hátralevő idejét.
     */
    void decreaseEffectDuration() {
        Logger.enter("decreaseEffectDuration", List.of());
        this.effectDuration -= 1;
        Logger.exit("decreaseEffectDuration", "");
    }
    /*
     * Beállítja melyik Buggerhezt tartozik az Insect
     */
    public void setBuggerOwner(Bugger buggerOwner) {
        Logger.enter("setBuggerOwner", List.of("buggerOwner"));
        this.buggerOwner = buggerOwner;
        Logger.exit("setBuggerOwner", "");
    }
    /*
     * Spore elfogyasztása, ami a megfelelő objektumokat is értesíti.
     */
    public Insect eatSpore(Spore s) {
        Logger.enter("eatSpore", List.of("s"));
        if (this.canEat) {
            onTecton.removeSpore(s);
            int food = s.setEffect(this);
            buggerOwner.increaseScore(food);
            if(canMultiply){
                Insect i2 = new Insect();
                buggerOwner.addInsect(i2);
                onTecton.addInsectOnTecton(i2);
                this.canMultiply = false;
                Logger.exit("eatSpore", "");
                return i2;
            }
        }
        Logger.exit("eatSpore", "");
        return null;
    }
    /*
     * Beállítja az Insect jelenlegi Tectonját
     */
    public void setOnTecton(Tecton t) {
        Logger.enter("setOnTecton", List.of("t"));
        onTecton = t;
        Logger.exit("setOnTecton", "");
    }

    public Tecton getOnTecton() {
        return onTecton;
    }

    /*
     * Hypha elvágása, ami a megfelelő objektumokat is értesíti.
     */
    public void cutHypha(InterconnectingHypha h) {
        Logger.enter("cutHypha", List.of("h"));
        if(canCut){
            h.startTimeSinceCut();
        }
        Logger.exit("cutHypha", "");
    }
    /*
     * Tecton-ok közötti mozgás.
     * Csak akkor lehetséges ha Hypha van közöttük.
     */
    public void moveToTecton(Tecton t) {
        Logger.enter("moveToTecton", List.of("t"));
        if(accessibleTecton(t)){
            onTecton.removeInsectOnTecton(this);
            t.addInsectOnTecton(this);
        }
        //különben nem történik meg a mozgás
        Logger.exit("moveToTecton", "");
    }
    public void getEaten(){
        Logger.enter("getEaten", List.of());
        onTecton.removeInsectOnTecton(this);
        buggerOwner.removeInsect(this);
        Logger.exit("getEaten", "");
    }

    /*
     * Mozgási sebesség beállítása
     */
    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }
    /*
     * canCut beállítása
     */
    public void setCanCut(boolean canCut) {
        this.canCut = canCut;
    }
    /*
     * canEat beállítása
     */
    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }
    public void setCanMultiply(boolean b){
        this.canMultiply = b;
    }
    /*
     * Hatás idejének beállítása
     */
    public void setEffectDuration(int effectDuration){
        this.effectDuration = effectDuration;
    }
    public int getSpeed(){
        return speed;
    }
    public boolean accessibleTecton(Tecton t){
        for(InterconnectingHypha ich : onTecton.interconnectingHyphaeOnTecton){
            if(ich.getEndingTecton().equals(t) || ich.getStartingTecton().equals(t)){
                return true;
            }
        }
        return false;
    }
    /*
     * Az IStatus interface megvalósítása
     */
    public String status(String name){
        String prod = name+":\n\t"+
                "effectDuration: "+effectDuration+"\n\t"+
                "speed: "+ speed +"\n\t"+
                "canCut: "+ (canCut ? 1 : 0) +"\n\t"+
                "canEat: "+ (canEat ? 1 : 0)+"\n\t"+
                "buggerOwner: "+Prototype.getName(buggerOwner.hashCode())+"\n\t"+
                "onTecton: "+Prototype.getName(onTecton.hashCode())+"\n\t"+
                "canMultiply: "+(canMultiply ? 1 : 0)+"\t"
                ;
        return prod;
    }

    public int getEffectDuration() {return effectDuration;}
    public boolean getCanCut() {return canCut;}
    public boolean getCanEat() {return canEat;}
    public Bugger getBuggerOwner() {return buggerOwner;}
    public boolean getCanMultiply() {return canMultiply;}
}
