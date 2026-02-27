import java.io.Serializable;

/*
 * A Bugger-ek pontszerzési lehetősége
 * és a Mushroom-ok azon eszköze amivel
 * később MushroomBody-t növeszthetnek.
 */
public abstract class Spore {
    /*
     * A Spore elfogyasztása mennyi pontot
     * eredményez a rovart irányító játékosnak.
     */
    protected int food;
    public int getFood() {return food;}

    /*
     * A Spore elfogyasztásának hatására (Insect eatSpore(s: Spore))
     * beállítjuk az aktív effektust a paraméterként kapott Insect-en,
     * visszatérési értéke az elfogyasztott Spore tápanyagértékét.
     */
    public abstract int setEffect(Insect i);
}
