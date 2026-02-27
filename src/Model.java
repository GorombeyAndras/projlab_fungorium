import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

/*
 * Az MVC modell Model részét megvalósító osztály.
 * Tartalmazza a pályán szereplő összes pályaelemet, illetve
 * kezeli az azokkal történő (például új pályaelem vagy Hypha elhalása)
 * eseményeket.
 */

public class Model {
    /*
     * A Tecton-okat tároló LinkedHashMap, ahol a kulcs a pályaelem neve,
     * az érték pedig a maga a pályaelem objektum.
     */
    private static LinkedHashMap<String, Tecton> tectonHashMap = new LinkedHashMap<>();

    /*
     * A Hypha-kat tároló LinkedHashMap, ahol a kulcs a pályaelem neve,
     * az érték pedig a maga a pályaelem objektum.
     */
    private static LinkedHashMap<String, Hypha> hyphaHashMap = new LinkedHashMap<>();

    /*
     * A Spore-okat tároló LinkedHashMap, ahol a kulcs a pályaelem neve,
     * az érték pedig a maga a pályaelem objektum.
     */
    private static LinkedHashMap<String, Spore> sporeHashMap = new LinkedHashMap<>();

    /*
     * Az Insect-eket tároló LinkedHashMap, ahol a kulcs a pályaelem neve,
     * az érték pedig a maga a pályaelem objektum.
     */
    private static LinkedHashMap<String, Insect> insectHashMap = new LinkedHashMap<>();

    /*
     * A Bugger-eket tároló LinkedHashMap, ahol a kulcs a pályaelem neve,
     * az érték pedig a maga a pályaelem objektum.
     */
    private static LinkedHashMap<String, Bugger> buggerHashMap = new LinkedHashMap<>();

    /*
     * A Mushroom-okat tároló LinkedHashMap, ahol a kulcs a pályaelem neve,
     * az érték pedig a maga a pályaelem objektum.
     */
    private static LinkedHashMap<String, Mushroom> mushroomHashMap = new LinkedHashMap<>();

    /*
     * A MushroomBody-kat tároló LinkedHashMap, ahol a kulcs a pályaelem neve,
     * az érték pedig a maga a pályaelem objektum.
     */
    private static LinkedHashMap<String, MushroomBody> mushroomBodyHashMap = new LinkedHashMap<>();

    /*
     * Az IStepable interfészt megvalósító pályaelemek listája.
     * Szerepe az, hogy a step() függvényben ezen végigiterálva
     * hívódjanak meg az objektumok step() függvénye.
     */
    private static List<IStepable> stepableList = new ArrayList<>();

    /*
     * Az elhalt pályaelemek nevét tároló lista
     */
    private static List<String> graveyard = new ArrayList<>();

    /**
     * Az Insect győzelemhez szükséges pontok száma
     */
    private static int insectWinPoint = 8;

    /**
     * A Mushroom győzelemhez szükséges pontok száma
     */
    private static int mushroomWinPoint = 3;

    /**
     * Az eddig létrehozott InterconnectingHypha-k számlálója.
     * Az InterconnectingHypha-k egyediségét garantálja.
     */
    private static int interconnectingHyphaCounter = 0;

    /**
     * Az eddig létrehozott IngrownHypha-k számlálója.
     * Az IngrownHypha-k egyediségét garantálja.
     */
    private static int ingrownHyphaCounter = 0;

    /**
     * Az eddig létrehozott Insect-ek számlálója.
     * Az Insect-ek egyediségét garantálja.
     */
    private static int insectCounter = 0;

    /**
     * Az eddig létrehozott MushroomBody-k számlálója.
     * A MushroomBody-k egyediségét garantálja.
     */
    private static int mushroomBodyCounter = 0;

    /**
     * Az eddig létrehozott Spore-ok számlálója.
     * A Spore-ok egyediségét garantálja.
     */
    private static int sporeCounter = 0;

    /**
     * Az eddig létrehozott Tecton-ok számlálója.
     * A Tecton-ok egyediségét garantálja.
     */
    private static int tectonCounter = 0;

    /**
     * A játék során az aktuális nap száma.
     */
    private static int day = 0;

    /**
     * A napon belül még lépésre váró Bugger játékosok
     * neveit tartalmazó lista.
     */
    private static List<String> buggerQueue;

    /**
     * A napon belül még lépésre váró Mushroom játékosok
     * neveit tartalmazó lista.
     */
    private static List<String> mushroomQueue;

    /**
     * A pályaelemek "törlését" szolgáló függvény.
     * Új játék indításakor használatos.
     */
    public static void clearMap() {
        // HashMapek törlése
        tectonHashMap = new LinkedHashMap<>();
        hyphaHashMap = new LinkedHashMap<>();
        sporeHashMap = new LinkedHashMap<>();
        insectHashMap = new LinkedHashMap<>();
        buggerHashMap = new LinkedHashMap<>();
        mushroomHashMap = new LinkedHashMap<>();
        mushroomBodyHashMap = new LinkedHashMap<>();

        // Az elhalt pályaelemek és léptethető objektumok törlése
        graveyard = new ArrayList<>();
        stepableList = new ArrayList<>();

        // Mindenből 0 darab legyen
        interconnectingHyphaCounter = 0;
        ingrownHyphaCounter = 0;
        insectCounter = 0;
        mushroomBodyCounter = 0;
        sporeCounter = 0;
        tectonCounter = 0;
    }

    /**
     * Új Tecton létrehozására szolgáló generikus függvény
     *
     * @param name Az új Tecton egyedi neve
     * @param c Az új Tecton osztálya, amely Tecton alaposztály leszármazottja
     * @param x x-tengely pozíciója a Canvason
     * @param y x y-tengely pozíciója a Canvason
     * @return Az újonnan létrehozott Tecton
     * @param <T> Sablonparaméter, Tecton leszármazott
     */
    public static <T extends Tecton> T addTecton(String name, Class<T> c, int x, int y) {
        try {
            // Model
            T newTecton = c.getDeclaredConstructor().newInstance();
            tectonHashMap.put(name, newTecton);
            stepableList.add(newTecton);

            // View
            View.addTView(new TectonView(x, y, View.getTectonColor(c), newTecton, name));

            return newTecton;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // Hiba, ha nincs ilyen konstruktor
            System.err.println("Hiba: nem létezik konstruktor vagy nem példányosítható az osztály.");
        }
        return null;
    }

    /**
     * Új Spore létrehozására szolgáló generikus függvény
     *
     * @param name Az új Spore egyedi neve
     * @param tectonName A Spore-t tartalmazó Tecton neve. Feltételezi, hogy létezik már ilyen
     *                   nevű Tecton a HashMap-ben
     * @param c Az új Spore osztálya
     * @param x x-tengely pozíciója a Canvason
     * @param y x y-tengely pozíciója a Canvason
     * @return Az újonnan létrehozott Spore
     * @param <T> Sablonparaméter, Spore leszármazott
     */
    public static <T extends Spore> T addSpore(String name, String tectonName, Class<T> c, int x, int y) {
        try {
            // Model
            T newSpore = c.getDeclaredConstructor().newInstance();
            sporeHashMap.put(name, newSpore);
            tectonHashMap.get(tectonName).addSpore(newSpore);

            // View
            View.addSView(new SporeView(x, y, View.getSporeColor(), newSpore));

            return newSpore;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // Hiba, ha nincs ilyen konstruktor
            System.err.println("Hiba: nem létezik konstruktor vagy nem példányosítható az osztály.");
        }

        return null;
    }

    /**
     * Új Insect felvételét megvalósító függvény.
     *
     * @param name Az új Insect egyedi neve.
     * @param buggerName Az Insecthez tartozó Bugger neve. Feltételezi,
     *                   hogy létezik ilyen nevű Bugger a HashMapban.
     * @param tecton Azon Tecton neve, melyen az Insect megtalálható.
     *               Feltételezi, hogy létezik ilyen nevű Tecton már.
     * @return Az újonnan létrehozott Insect objektumra referencia
     */
    public static Insect addInsect(String name, String buggerName, String tecton) {
        // Model
        Insect insect = new Insect();
        insectHashMap.put(name, insect);
        buggerHashMap.get(buggerName).addInsect(insect);
        stepableList.add(insect);
        tectonHashMap.get(tecton).addInsectOnTecton(insect);

        // View
        Random r = new Random();
        View.addIView(new InsectView(0, 0, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)), insect));

        return insect;
    }

    /**
     * Az új Bugger létrehozását megvalósító függvény.
     *
     * @param name Az új Bugger egyedi neve.
     * @return A létrehozott Bugger objektumra referencia.
     */
    public static Bugger addBugger(String name) {
        // Model
        Bugger bugger = new Bugger();
        buggerHashMap.put(name, bugger);
        return bugger;
    }

    /**
     * Az új Mushroom létrehozását megvalósító függvény.
     *
     * @param name Az új Mushroom egyedi neve.
     * @return A létrehozott Mushroom objektumra referencia.
     */
    public static Mushroom addMushroom(String name) {
        // Model
        Mushroom mushroom = new Mushroom();
        mushroomHashMap.put(name, mushroom);
        return mushroom;
    }

    /**
     * MushroomBody-k hozzáadását megvalósító függvény.
     * Fontos, hogy nem(!) a modelbéli grow függvény felhasználásával
     * jön létre a MushroomBody. Pályainicializáláskor használatos.
     *
     * @param name Az új MushroomBody egyedi neve.
     * @param ingrownHyphaName A MushroomBody-hoz tartozó IngrownHypha neve.
     *                         Feltételezi, hogy létezik ilyen nevű IngrownHypha a HasMap-ben.
     * @return Az újonnan létrehozott MushroomBody objektumra referencia.
     */
    public static MushroomBody addMushroomBody(String name, String ingrownHyphaName) {
        // Model
        MushroomBody mushroomBody = new MushroomBody();
        mushroomBodyHashMap.put(name, mushroomBody);
        ((IngrownHypha) hyphaHashMap.get(ingrownHyphaName)).addMushroomBody(mushroomBody);

        // View
        View.addBView(new MushroomBodyView(0, 0, View.getBodyColor(), mushroomBody));

        return mushroomBody;
    }

    /**
     * A Hypha-k növesztését megvalósító függvény.
     * Az új Hypha-k eLnevezése: ICX és INHX ahol x=egész
     *
     * @param mushroom_name A Hypha-hoz tartozó Mushroom neve. Feltételezi,
     *                      hogy már létezik ilyen nevű Mushroom.
     * @param tecton1 A kiindulási Tecton neve. Feltételezi, hogy létezik ilyen nevű
     *                Tecton.
     * @param tecton2 A cél Tecton neve. Feltételezi, hogy létezik ilyen nevű
     *                Tecton.
     * @param tecton3 Kétszeres növesztés esetén a második cél Tecton neve.
     *                Feltételezi, hogy létezik ilyen nevű Tecton.
     *                Egyszeres növesztés esetén ""-nek kell lennie ennek a paraméternek.
     */
    public static void connect(String mushroom_name, String tecton1, String tecton2, String tecton3) {
        String hypha_name1 = "IC" + interconnectingHyphaCounter;
        interconnectingHyphaCounter++;

        String ingrown_hypha_name1 = "ING" + ingrownHyphaCounter;
        ingrownHyphaCounter++;

        Tecton tectonFrom = tectonHashMap.get(tecton1);
        Tecton tectonTo = tectonHashMap.get(tecton2);
        Mushroom mushroom = mushroomHashMap.get(mushroom_name);

        // Kétszeres növesztés esetén:
        String hypha_name2 = null;
        String ingrown_hypha_name2 = null;
        Tecton tectonTo2 = null;

        if (!tecton3.equals("")) {
            hypha_name2 = "IC" + interconnectingHyphaCounter;
            interconnectingHyphaCounter++;

            ingrown_hypha_name2 = "ING" + ingrownHyphaCounter;
            ingrownHyphaCounter++;

            tectonTo2 = tectonHashMap.get(tecton3);
        }

        // Egyszeres növesztés
        if (tecton3.equals("")) {
            if (!tecton1.equals(tecton2)) { // InteconnectingHypha növesztés
                List<Hypha> listOfHyphae = mushroom.growHypha(tectonFrom, tectonTo);

                if(listOfHyphae.isEmpty()) {
                    return;
                }

                hyphaHashMap.put(hypha_name1, listOfHyphae.get(0));
                stepableList.add(0,hyphaHashMap.get(hypha_name1));
                View.addINTView(new InterconnectingHyphaView(0, 0, new Color(255, 0, 255), (InterconnectingHypha) listOfHyphae.get(0)));

                // Van Ingrown is
                if (listOfHyphae.size() == 2) {
                    hyphaHashMap.put(ingrown_hypha_name1, listOfHyphae.get(1));
                    stepableList.add(0,hyphaHashMap.get(ingrown_hypha_name1));
                    View.addINGView(new IngrownHyphaView(0, 0, new Color(100, 255, 255), (IngrownHypha) listOfHyphae.get(1)));
                }
            } else {
                // Csak az IngrownHypha kerül hozzáadásra
                IngrownHypha h = new IngrownHypha();
                tectonFrom.addIngrownHypha(h);
                mushroom.addHypha(h);

                hyphaHashMap.put(ingrown_hypha_name1, h);
                stepableList.add(0,hyphaHashMap.get(ingrown_hypha_name1));
                View.addINGView(new IngrownHyphaView(0, 0, new Color(100, 255, 255), h));
            }
            return;
        }

        // DoubleGrow
        List<Hypha> listOfHyphae = mushroom.doubleGrowHypha(tectonFrom, tectonTo, tectonTo2);

        // Nem sikerült a növesztés
        if(listOfHyphae.isEmpty()) {
            return;
        }

        // Az első InterconnectingHypha
        hyphaHashMap.put(hypha_name1, listOfHyphae.get(0));
        stepableList.add(0,hyphaHashMap.get(hypha_name1));
        View.addINTView(new InterconnectingHyphaView(0, 0, new Color(255, 0, 255), (InterconnectingHypha) listOfHyphae.get(0)));

        // Az első IngrownHypha
        if (listOfHyphae.size() >= 3) {
            hyphaHashMap.put(ingrown_hypha_name1, listOfHyphae.get(2));
            stepableList.add(0,hyphaHashMap.get(ingrown_hypha_name1));
            View.addINGView(new IngrownHyphaView(0, 0, new Color(100, 255, 255), (IngrownHypha) listOfHyphae.get(2)));
        }

        // A második InterconnectingHypha
        hyphaHashMap.put(hypha_name2, listOfHyphae.get(1));
        stepableList.add(0,hyphaHashMap.get(hypha_name2));
        View.addINTView(new InterconnectingHyphaView(0, 0, new Color(255, 0, 255), (InterconnectingHypha) listOfHyphae.get(1)));

        // A második IngrownHypha
        if (listOfHyphae.size() == 4) {
            hyphaHashMap.put(ingrown_hypha_name2, listOfHyphae.get(3));
            stepableList.add(0,hyphaHashMap.get(ingrown_hypha_name2));
            View.addINGView(new IngrownHyphaView(0, 0, new Color(100, 255, 255), (IngrownHypha) listOfHyphae.get(3)));
        }
    }

    /**
     * Az Insect mozgatását megvalósító függvény.
     *
     * @param insectName Az Insect neve.
     * @param tectonName A cél Tecton neve. Feltételezi, hogy létezik.
     */
    public static void move(String insectName, String tectonName) {
        Insect insect = insectHashMap.get(insectName);
        Tecton tecton = tectonHashMap.get(tectonName);

        // Ha nincs ilyen Insect, akkor nem történik mozgatás.
        if (insect == null) {
            System.err.println("Hiba: nem létezik ilyen nevű Insect.");
            return;
        }

        // Ha még nincs Tectonon az Insect, akkor egy egyszerű setter hívás
        if (insect.getOnTecton() == null) {
            insect.setOnTecton(tecton);
            tecton.addInsectOnTecton(insect);
        } else {
            // Egyébként modelbéli függvény hívása
            insect.moveToTecton(tecton);
        }
    }

    /**
     * A spóraevést megvalósító függvény.
     *
     * @param insectName Az Insect neve. Feltételezi, hogy létezik.
     */
    public static void eat_spore(String insectName) {
        Insect insect = insectHashMap.get(insectName);
        Spore spore = insect.getOnTecton().getSporesOnTecton().get(0);
        Insect i2 = insect.eatSpore(spore);
        String sporeName = sporeHashMap.entrySet().stream().filter(x -> x.getValue().equals(spore)).findFirst().get().getKey();

        // Splitspore esetén az új Insect létrehozása
        if(i2 != null) {
            String insectName2 = "I" + insectCounter;
            insectCounter++;

            insectHashMap.put(insectName2, i2);
            stepableList.add(insectHashMap.get(insectName2));

            addInsect(insectName,
                    buggerHashMap.entrySet().stream().filter(x -> x.getValue().equals(insect.getBuggerOwner())).findFirst().get().getKey(),
                    tectonHashMap.entrySet().stream().filter(x -> x.getValue().equals(insect.getOnTecton())).findFirst().get().getKey());
        }

        // Spóra "elhal"
        graveyard.add(sporeName);

        if(View.checkInsectVictoryAndShowWindow(insect.getBuggerOwner().getScore())){
            View.state= View.State.startgame;
            View.stateChanged();
        }
    }

    /**
     * A rovarevést megvalósító függvény.
     *
     * @param insectName A bénult, bekebelezésre váró Insect neve.
     *                   Feltételezi, hogy létezik ilyen nevű Insect.
     * @param mushroomName Azon Mushroom neve, melynek az IngrownHypha-ja megeszi
     *                     az Insectet. Feltételezi, hogy létezik ilyen nevű Mushroom.
     */
    public static void eat_insect(String insectName, String mushroomName) {
        Insect insectonTecton = getInsectFromName(insectName);
        Tecton t1 = insectonTecton.getOnTecton();
        Mushroom mushroom = getMushroomFromName(mushroomName);
        IngrownHypha ingrownHypha = mushroom.ingrownHyphaOnTectonFromThisMushroom(t1);
        Insect insect = insectHashMap.get(insectName);

        // Model
        MushroomBody mb1 = ingrownHypha.consumeBug(insect);
        mushroomBodyHashMap.put("MB" + mushroomBodyCounter, mb1);
        mushroomBodyCounter++;

        // View
        View.addBView(new MushroomBodyView(0, 0, View.getBodyColor(), mb1));
        View.removeInsectView(View.getI(insect));

        // Az Insect "elhal"
        graveyard.add(insectName);

        if(View.checkMushroomVictoryAndShowWindow(mushroom.getScore())){
            View.state= View.State.startgame;
            View.stateChanged();
        }
    }

    /**
     * A spóraszórást megvalósító függvény.
     *
     * @param mushroomBody A Spore-t szóró MushroomBody.
     * @param tecton_name A spóraszórás cél Tectonja. Feltételezi,
     *                    hogy van ilyen nevű Tecton.
     */
    public static void disperse(MushroomBody mushroomBody, String tecton_name) {
        Tecton tecton = tectonHashMap.get(tecton_name);
        String mushroombody_name = getNameFromBody(mushroomBody);
        Spore spore = mushroomBody.disperseSpore(tecton);

        // A MushroomBody elhal, ha nem marad spórája
        if (mushroomBody.getSporesLeft() == 0) {
            graveyard.add(mushroombody_name);
        }

        // Model
        String sporeName = Character.toString(spore.getClass().getName().charAt(0)) + sporeCounter;
        sporeCounter++;
        sporeHashMap.put(sporeName, spore);
        tecton.addSpore(spore);

        // View
        View.addSView(new SporeView(0, 0, View.getSporeColor(), spore));
    }

    /**
     * A gombatestnövesztést megvalósító függvény.
     *
     * @param mushroom_name A növesztést valósító Mushroom neve. Feltételezi,
     *                      hogy létezik ilyen nevű Mushroom.
     * @param ingrownHypha_name Az új MushroomBody IngrownHypha-janak neve.
     *                          Feltételezi, hogy létezik ilyen nevű IngrownHypha.
     */
    public static void grow(String mushroom_name, String ingrownHypha_name) {
        Mushroom mushroom = mushroomHashMap.get(mushroom_name);
        IngrownHypha ingrownHypha = (IngrownHypha) hyphaHashMap.get(ingrownHypha_name);

        Tecton tecton = ingrownHypha.getHomeTecton();

        int i = 0;
        MushroomBody newMushroomBody = tecton.growMushroomBody(ingrownHypha);

        // 2 Spóra törlése, ami szükséges egy növesztéshez
        for (Map.Entry<String, Spore> entry : sporeHashMap.entrySet()) {
            if (i < 2 && newMushroomBody != null) {
                if(tecton.getSporesOnTecton().contains(entry.getValue())) {
                    tecton.removeSpore(entry.getValue());
                    View.removeSView(View.getS(entry.getValue()));
                    graveyard.add(entry.getKey());
                    i++;
                }
            }
        }

        // Model
        String newMushroomBodyName = "MB" + mushroomBodyCounter;
        mushroomBodyCounter++;
        mushroomBodyHashMap.put(newMushroomBodyName, newMushroomBody);

        // View
        View.addBView(new MushroomBodyView(0, 0, View.getBodyColor(), newMushroomBody));

        if(View.checkMushroomVictoryAndShowWindow(mushroom.getScore())){
            View.state= View.State.startgame;
            View.stateChanged();
        }
    }

    /**
     * Két Tecton szomszédosságát megvalósító függvény.
     *
     * @param tecton1_name Az első Tecton neve. Feltételezi,
     *                     hogy létezik ilyen nevű Tecton.
     * @param tecton2_name A második Tecton neve. Feltételezi,
     *                     hogy létezik ilyen nevű Tecton.
     */
    public static void neighbour(String tecton1_name, String tecton2_name) {
        Tecton tecton1 = tectonHashMap.get(tecton1_name);
        Tecton tecton2 = tectonHashMap.get(tecton2_name);

        tecton1.addNeighbour(tecton2);
        tecton2.addNeighbour(tecton1);
    }

    /**
     * Az InterconnectingHypha vágást megvalósító függvény.
     *
     * @param insect_name Az Insect neve, amely elvágja a Hypha-t.
     *                    Feltételezi, hogy létezik ilyen nevű InterconnectingHypha.
     * @param ich Az InterconnectingHypha, amit elvág a rovar.
     */
    public static void cut(String insect_name, InterconnectingHypha ich) {
        Insect insect = insectHashMap.get(insect_name);
        insect.cutHypha(ich);

        // View
        View.removeInterconnectingHyphaView(View.getICH(ich));
    }

    /**
     * A tektontörést megvalósító függvény.
     *
     * @param tecton1_name A Tecton neve, amelyik kettétörik. Feltételezi,
     *                    hogy létezik ilyen nevű Tecton.
     */
    public static void break_tecton(String tecton1_name) {
        Tecton tecton1 = tectonHashMap.get(tecton1_name);
        Tecton tecton2 = tecton1.breakT();

        // Model
        String tecton2Name = "T" + tectonCounter;
        tectonHashMap.put(tecton2Name, tecton2);
        tectonCounter++;

        // View
        int offset = 30;
        TectonView t1 = View.getT(tecton1);
        View.addTView(new TectonView(t1.getX() + offset, t1.getY(), View.getTectonColor(tecton2.getClass()), tecton2, tecton2Name));
        t1.updatePosition(t1.getX() - offset, t1.getY());
        View.refreshMap();
    }

    /**
     * A pályaelemek léptetését megvalósító függvény.
     * Hypha-k elhalásakor értesíti a View-t, hogy szűnjön meg
     * a HyphaView.
     */
    public static void step() {
        // IngrownHypha léptetés
        for(Map.Entry<String, Hypha> h : hyphaHashMap.entrySet()){
            if(h.getValue() instanceof IngrownHypha && !((IngrownHypha) h.getValue()).getMushroomBodiesList().isEmpty()){
                h.getValue().step();
            }
        }

        // Minden elem léptetése
        Iterator<IStepable> stepIterator = stepableList.iterator();
        while (stepIterator.hasNext()) {
            IStepable stepable = stepIterator.next();
            stepable.step();
            for (Map.Entry<String, Hypha> entry : hyphaHashMap.entrySet()) {
                if (entry.getValue().getTimeSinceDisconnected() >= 2) {
                    if(entry.getValue() instanceof InterconnectingHypha)
                        // Interconnecting törlés View-ból
                        View.removeInterconnectingHyphaView(View.getICH((InterconnectingHypha) entry.getValue()));
                    else{
                        // Ingrown törlés View-ból
                        View.removeIngrownHyphaView(View.getING((IngrownHypha) entry.getValue()));
                    }

                    // Törlés a Model-ből
                    graveyard.add(entry.getKey());
                    stepableList.remove(entry.getKey());
                }
                if (entry.getValue().getMushroom() == null) {
                    graveyard.add(entry.getKey());
                    stepableList.remove(entry.getKey());
                }
            }
        }

        // IngrownHypha léptetés
        for(Map.Entry<String, Hypha> h : hyphaHashMap.entrySet()){
            if(h.getValue() instanceof IngrownHypha && !((IngrownHypha) h.getValue()).getMushroomBodiesList().isEmpty()){
                h.getValue().step();
            }
        }
    }

    /**
     * A következő nap inicializálását megvalósító függvény.
     * Feltölti a játékosok neveit tartalmazó queue-kat, hogy
     * ki léphet még. Ha a nap nem 0, akkor:
     * - léptet minden léptethető pályaelemet
     * - véletlenszerűen eltör egy véletlenszerűen választott Tecton-t
     */
    public static void nextDay() {
        buggerQueue = new LinkedList<>(buggerHashMap.keySet());
        mushroomQueue = new LinkedList<>(mushroomHashMap.keySet());

        if (day != 0) {
            // Léptetés
            step();

            // Tectontörés
            Random random = new Random();
            int r = random.nextInt(0,4);

            if (r == 1 && tectonHashMap.size() > 0) {
                break_tecton("T" + random.nextInt(tectonHashMap.size()));
            }
        }

        // A napok számának inkrementálása
        day++;
    }

    /**
     * A következő játékos nevének lekérése.
     *
     * @return A következő játékos neve Stringként.
     */
    public static String getCurrentPlayer() {
        // Ha nincs következő játékos, akkor új nap jön
        if(buggerQueue.isEmpty() && mushroomQueue.isEmpty()){
            nextDay();
        }
        // Először a Bugger-ek lépnek
        if (!buggerQueue.isEmpty()) {
            return buggerQueue.remove(0);
        }
        // Majd a Mushroom-ok
        if (!mushroomQueue.isEmpty()) {
            return mushroomQueue.remove(0);
        }
        return null;
    }

    /**
     * Lekérdezi a Modelből név alapján a névhez tartozó
     * Insect objektumot.
     *
     * @param name Az Insect neve.
     * @return Az Insect objektumra referencia.
     * Ha nem találja, akkor null-al tér vissza.
     */
    public static Insect getInsectFromName(String name) {
        for(Map.Entry<String, Insect> h : insectHashMap.entrySet()){
            if(h.getKey().equals(name)){
                return h.getValue();
            }
        }
        return null;
    }

    /**
     * Lekérdezi a Modelből név alapján a névhez tartozó
     * Tecton objektumot.
     *
     * @param name Az Tecton neve.
     * @return Az Tecton objektumra referencia.
     * Ha nem találja, akkor null-al tér vissza.
     */
    public static Tecton getTectonFromName(String name) {
        for(Map.Entry<String, Tecton> h : tectonHashMap.entrySet()){
            if(h.getKey().equals(name)){
                return h.getValue();
            }
        }
        return null;
    }

    /**
     * Lekérdezi a Modelből név alapján a névhez tartozó
     * MushroomBody objektumot.
     *
     * @param name Az MushroomBody neve.
     * @return Az MushroomBody objektumra referencia.
     * Ha nem találja, akkor null-al tér vissza.
     */
    public static MushroomBody getMushroomBodyFromName(String name) {
        for(Map.Entry<String, MushroomBody> h : mushroomBodyHashMap.entrySet()){
            if(h.getKey().equals(name)){
                return h.getValue();
            }
        }
        return null;
    }

    /**
     * Lekérdezi a Modelből név alapján a névhez tartozó
     * Spore objektumot.
     *
     * @param name Az Spore neve.
     * @return Az Spore objektumra referencia.
     * Ha nem találja, akkor null-al tér vissza.
     */
    public static Spore getSporeFromName(String name) {
        for(Map.Entry<String, Spore> h : sporeHashMap.entrySet()){
            if(h.getKey().equals(name)){
                return h.getValue();
            }
        }
        return null;
    }

    /**
     * Lekérdezi a Modelből név alapján a névhez tartozó
     * Mushroom objektumot.
     *
     * @param name Az Mushroom neve.
     * @return Az Mushroom objektumra referencia.
     * Ha nem találja, akkor null-al tér vissza.
     */
    public static Mushroom getMushroomFromName(String name) {
        for(Map.Entry<String, Mushroom> h : mushroomHashMap.entrySet()){
            if(h.getKey().equals(name)){
                return h.getValue();
            }
        }
        return null;
    }

    /**
     * Lekérdezi a Modelből név alapján a névhez tartozó
     * Bugger objektumot.
     *
     * @param name Az Bugger neve.
     * @return Az Bugger objektumra referencia.
     * Ha nem találja, akkor null-al tér vissza.
     */
    public static Bugger getBuggerFromName(String name) {
        for(Map.Entry<String, Bugger> h : buggerHashMap.entrySet()){
            if(h.getKey().equals(name)){
                return h.getValue();
            }
        }
        return null;
    }

    /**
     * Lekérdezi a Model-ből az i objektumhoz tartozó IngrownHypha nevét
     *
     * @param i A keresett névhez tartozó IngrownHypha
     * @return Ha megtalálta a nevet akkor azzal, ha nem akkor
     * nullal tér vissza
     */
    public static String getIngrownHyphaName(IngrownHypha i) {
        for (Map.Entry<String, Hypha> e : hyphaHashMap.entrySet()) {
            if (e.getValue() == i) {
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * A játék indításakor a Model-hez hozzáadja a Mushroom-okat és Bugger-okat
     *
     * @param numberOfMushroomers A Gombászok száma
     * @param numberOfBuggers A Bogarászok száma
     */
    public static void start(int numberOfMushroomers, int numberOfBuggers) {
        for (int i = 0; i < numberOfMushroomers; i++) {
            Model.addMushroom("Gombász" + i);
        }

        for (int i = 0; i < numberOfBuggers; i++) {
            Model.addBugger("Bogarász" + i);
        }

        nextDay();
    }

    /**
     * Inicializálja a pályát:
     * Felvesz Tektonokat
     * Megfelelően szomszédosítja őket
     * Felveszi a Gombászokat és a Rovarászokat
     */
    public static void init(){
        Model.addTecton("T0", Emerald.class, 100, 50);
        Model.addTecton("T1", Emerald.class, 200, 50);
        Model.addTecton("T2", Tanzanite.class, 300, 50);
        Model.addTecton("T3", Pyroxene.class, 400, 50);

        Model.addTecton("T6", Tanzanite.class, 300, 125);
        Model.addTecton("T5", Emerald.class, 200, 125);
        Model.addTecton("T4", Tanzanite.class, 100, 125);
        Model.addTecton("T7", Pyroxene.class, 400, 125);

        Model.addTecton("T11", Anorthite.class, 400, 200);
        Model.addTecton("T10", Anorthite.class, 300, 200);
        Model.addTecton("T9", Tanzanite.class, 200, 200);
        Model.addTecton("T8", Pyroxene.class, 100, 200);

        Model.addTecton("T15", Anorthite.class, 400, 275);
        Model.addTecton("T13", Emerald.class, 200, 275);
        Model.addTecton("T14", Tanzanite.class, 300, 275);
        Model.addTecton("T12", Pyroxene.class, 100, 275);

        tectonCounter = 16;

        getTectonFromName("T0").addNeighbour(getTectonFromName("T1"));
        getTectonFromName("T0").addNeighbour(getTectonFromName("T4"));

        getTectonFromName("T1").addNeighbour(getTectonFromName("T2"));
        getTectonFromName("T1").addNeighbour(getTectonFromName("T5"));
        getTectonFromName("T1").addNeighbour(getTectonFromName("T0"));

        getTectonFromName("T2").addNeighbour(getTectonFromName("T3"));
        getTectonFromName("T2").addNeighbour(getTectonFromName("T6"));
        getTectonFromName("T2").addNeighbour(getTectonFromName("T1"));

        getTectonFromName("T3").addNeighbour(getTectonFromName("T7"));
        getTectonFromName("T3").addNeighbour(getTectonFromName("T2"));



        getTectonFromName("T4").addNeighbour(getTectonFromName("T5"));
        getTectonFromName("T4").addNeighbour(getTectonFromName("T8"));
        getTectonFromName("T4").addNeighbour(getTectonFromName("T0"));

        getTectonFromName("T5").addNeighbour(getTectonFromName("T6"));
        getTectonFromName("T5").addNeighbour(getTectonFromName("T9"));
        getTectonFromName("T5").addNeighbour(getTectonFromName("T4"));
        getTectonFromName("T5").addNeighbour(getTectonFromName("T1"));

        getTectonFromName("T6").addNeighbour(getTectonFromName("T5"));
        getTectonFromName("T6").addNeighbour(getTectonFromName("T2"));
        getTectonFromName("T6").addNeighbour(getTectonFromName("T7"));
        getTectonFromName("T6").addNeighbour(getTectonFromName("T10"));

        getTectonFromName("T7").addNeighbour(getTectonFromName("T3"));
        getTectonFromName("T7").addNeighbour(getTectonFromName("T6"));
        getTectonFromName("T7").addNeighbour(getTectonFromName("T11"));

        getTectonFromName("T8").addNeighbour(getTectonFromName("T4"));
        getTectonFromName("T8").addNeighbour(getTectonFromName("T9"));
        getTectonFromName("T8").addNeighbour(getTectonFromName("T12"));

        getTectonFromName("T9").addNeighbour(getTectonFromName("T5"));
        getTectonFromName("T9").addNeighbour(getTectonFromName("T8"));
        getTectonFromName("T9").addNeighbour(getTectonFromName("T10"));
        getTectonFromName("T9").addNeighbour(getTectonFromName("T13"));

        getTectonFromName("T10").addNeighbour(getTectonFromName("T6"));
        getTectonFromName("T10").addNeighbour(getTectonFromName("T9"));
        getTectonFromName("T10").addNeighbour(getTectonFromName("T11"));
        getTectonFromName("T10").addNeighbour(getTectonFromName("T14"));

        getTectonFromName("T11").addNeighbour(getTectonFromName("T7"));
        getTectonFromName("T11").addNeighbour(getTectonFromName("T10"));
        getTectonFromName("T11").addNeighbour(getTectonFromName("T15"));

        getTectonFromName("T12").addNeighbour(getTectonFromName("T8"));
        getTectonFromName("T12").addNeighbour(getTectonFromName("T13"));

        getTectonFromName("T13").addNeighbour(getTectonFromName("T9"));
        getTectonFromName("T13").addNeighbour(getTectonFromName("T12"));
        getTectonFromName("T13").addNeighbour(getTectonFromName("T14"));


        getTectonFromName("T14").addNeighbour(getTectonFromName("T10"));
        getTectonFromName("T14").addNeighbour(getTectonFromName("T13"));
        getTectonFromName("T14").addNeighbour(getTectonFromName("T15"));

        getTectonFromName("T15").addNeighbour(getTectonFromName("T11"));
        getTectonFromName("T15").addNeighbour(getTectonFromName("T14"));


        // Mushroomok
        for (int i = 0; i < mushroomHashMap.size(); i++) {
            Model.connect("Gombász" + i, "T" + i, "T" + i, "");
            Model.connect("Gombász" + i, "T" + i, "T" + (i + 4), "");

            Model.addSpore("E" + i, "T" + i, Vortex.class, 100, 100);
            sporeCounter++;
            Model.addMushroomBody("MB" + i, "ING" + (i * 2));
            mushroomBodyCounter++;
        }
        //Buggerek
        for (int i = 0; i < buggerHashMap.size(); i++) {
            Model.addInsect("I" + i, "Bogarász" + i, "T" + i);
            insectCounter++;
        }
    }

    /**
     * Egy adott nevű játékos pontjainak számát megadó függvény.
     *
     * @param playerName A játékos neve, akinek a pontjaira kíváncsiak vagyunk
     * @return  A játékos pontjainak száma
     */
    public static int getPlayerScore(String playerName) {
        for (Map.Entry<String, Bugger> entry : buggerHashMap.entrySet()) {
            if (entry.getKey().equals(playerName)) {
                return entry.getValue().getScore();
            }
        }

        for (Map.Entry<String, Mushroom> entry : mushroomHashMap.entrySet()) {
            if (entry.getKey().equals(playerName)) {
                return entry.getValue().getScore();
            }
        }

        return 0;
    }

    /**
     * Lekérdezi a Model-ből az i objektumhoz tartozó Insect nevét
     *
     * @param i A keresett névhez tartozó Insect
     * @return Ha megtalálta a nevet akkor azzal, ha nem akkor
     * nullal tér vissza
     */
    public static String getNameFromInsect(Insect i) {
        for (Map.Entry<String, Insect> h : insectHashMap.entrySet()) {
            if (h.getValue().equals(i)) {
                return h.getKey();
            }
        }
        return null;
    }

    /**
     * Lekérdezi a Model-ből az mb objektumhoz tartozó MushroomBody nevét
     *
     * @param mb A keresett névhez tartozó MushroomBody
     * @return Ha megtalálta a nevet akkor azzal, ha nem akkor
     * nullal tér vissza
     */
    public static String getNameFromBody(MushroomBody mb) {
        for(Map.Entry<String, MushroomBody> h : mushroomBodyHashMap.entrySet()){
            if(h.getValue().equals(mb)){
                return h.getKey();
            }
        }
        return null;
    }

    /**
     * A Tektonok neveit listába kigyűjtő függvény
     *
     * @return A Tektonok listájával tér vissza
     */
    public static List<String> getTectonNames() {
        List<String> names = new ArrayList<>();
        for(Map.Entry<String, Tecton> h : tectonHashMap.entrySet()){
            names.add(h.getKey());
        }
        return names;
    }

    /**
     * Az Insectek neveit listába kigyűjtő függvény
     *
     * @return Az Insectek listájával tér vissza
     */
    public static List<String> getInsectNames() {
        List<String> names = new ArrayList<>();
        for(Map.Entry<String, Insect> h : insectHashMap.entrySet()){
            names.add(h.getKey());
        }
        return names;
    }

    /**
     * Lekérdezi a Rovászok győzelméhez szükséges pontok számát
     *
     * @return Visszatér a Rovászok győzelméhez szükséges pontok számával
     */
    public static int getInsectWinPoint(){
        return insectWinPoint;
    }

    /**
     * Lekérdezi a Gombászok győzelméhez szükséges pontok számát
     *
     * @return Visszatér a Gombászok győzelméhez szükséges pontok számával
     */
    public static int getMushroomWinPoint(){
        return mushroomWinPoint;
    }
}
