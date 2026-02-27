import java.util.List;
import java.util.Scanner;

//Skeleton megvalósítása
public class Skeleton {
    private static List<String> useCaseList = List.of(
        "Cut Hypha",
        "Eating Clogspore",
        "Eating Vortex",
        "Eating Drownsap",
        "Eating Frostclash",
        "Move to Tecton",
        "Disperse Spore",
        "Grow MushroomBody on Tanzanite",
        "Grow MushroomBody on Pyroxene",
        "Grow MushroomBody on Anorthite",
        "Grow Hypha on Tanzanite",
        "Grow Hypha on Pyroxene",
        "Grow Hypha on Anorthite",
        "Double grow Hypha (Spore boost)",
        "MushroomBody advancing",
        "Insect boosted",
        "Tecton breaks",
        "IngrownHypha dying",
            "InterconnectingHypha dying",
        "Hypha reconnection",
        "Anorthite Earthquake",
        "Advanced Disperse Spore"
    );

    /*
     * Képernyőre írás, kiírjuk a use case-eket amiből majd lehet választani
     */
    public static void printUseCaseList() {
        for (int i = 0; i < useCaseList.size(); i++) {
            System.out.println(useCaseList.get(i) + ": (" + (i + 1) + ")");
        }

        System.out.println("-----------------------------------");
        System.out.println("Kilépés a programból (0)");
    }

    /*
     * Futtatjuk a megfelelő use case-t, vagyis azt, amit a felhasználó megad egy számmal
     * ahol a szám 0-22-ig terjed, 0 a kilépés a többi szám egy szekvenciát indít el.
     * A szekvencia futás közben érkezhet kérdés a felhasználó felé.
     */
    public static void runUseCase(int useCaseNumber) {
        if (useCaseNumber > 0 && useCaseNumber <= useCaseList.size()) {
            System.out.println("\nA kiválasztott use case:");
        } else if(useCaseNumber != 0) {
            System.out.println("\nÉrvénytelen menüpont, próbálja újra!");
        }

        // előző use case paramétereinek törlése
        Logger.clearParamDict();

        switch (useCaseNumber) {
            case 1:
                cutHypha();
                break;
            case 2:
                eatingClogspore();
                break;
            case 3:
                eatingVortex();
                break;
            case 4:
                eatingDrownsap();
                break;
            case 5:
                eatingFrostclash();
                break;
            case 6:
                moveToTecton();
                break;
            case 7:
                disperseSpore();
                break;
            case 8:
                growMushroomBodyOnTanzanite();
                break;
            case 9:
                growMushroomBodyOnPyroxene();
                break;
            case 10:
                growMushroomBodyOnAnorthite();
                break;
            case 11:
                growHyphaOnTanzanite();
                break;
            case 12:
                growHyphaOnPyroxene();
                break;
            case 13:
                growHyphaOnAnorthite();
                break;
            case 14:
                doubleGrowHypha();
                break;
            case 15:
                mushroomBodyAdvancing();
                break;
            case 16:
                insectBoosted();
                break;
            case 17:
                tectonBreaks();
                break;
            case 18:
                ingrownHyphaDying();
                break;
            case 19:
                interconnectingHyphaDying();
                break;
            case 20:
                hyphaReconnection();
                break;
            case 21:
                anorthiteEarthquake();
                break;
            case 22:
                advancedDisperseSpore();
                break;
            default:
                break;
        }
    }

    /*
     * Gombafonál elvágása.
     * Egy rovar egy gombafonalat fog átvágni.
     */
    private static void cutHypha() {
        // Kommunikációs diagram
        Pyroxene t1 = new Pyroxene();
        Pyroxene t2 = new Pyroxene();
        Insect i = new Insect();
        Mushroom m = new Mushroom();

        MushroomBody mb = new MushroomBody();
        IngrownHypha inH1 = new IngrownHypha();

        InterconnectingHypha intH = new InterconnectingHypha();

        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
        t1.addIngrownHypha(inH1);
        intH.setHyphaEndpoints(t1, t2);
        i.setOnTecton(t1);
        m.addHypha(inH1);
        m.addHypha(intH);
        inH1.addMushroomBody(mb);

        // Szekvenciadiagram
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tud-e a rovar gombafonalat vágni? (I/N) ");
        String input = scanner.nextLine();

        i.setCanCut(input.equals("I"));
        i.cutHypha(intH);
    }

    /*
     * Clogspore fajtájú spóra elfogyasztása
     * Egy rovar egy clogspore-t fog megenni.
     * Megkérdezzük a felhasználótól, hogy képes-e a rovar spórát enni.
     * Ha képes, akkor megeszi, ha nem, akkor értelemszerűen nem.
     * Az, hogy a rovar nem képes enni, egy másik spóra előzetes elfogyasztásából következhet.
     */
    private static void eatingClogspore() {
        // Kommunikációs diagram
        Pyroxene t1 = new Pyroxene();
        Clogspore s = new Clogspore();
        Insect i = new Insect();
        Bugger b = new Bugger();

        t1.addInsectOnTecton(i);
        t1.addSpore(s);
        b.addInsect(i);

       // Szekvencia diagram
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tud-e a rovar spórát enni? (I/N) ");
        String input = scanner.nextLine();

        i.setCanEat(input.equals("I"));
        i.eatSpore(s);
    }

    /*
     * Vortex elfogyasztása.
     * Egy rovar egy vortex-et fog megenni.
     * Megkérdezzük a felhasználótól, hogy képes-e a rovar spórát enni.
     * Ha képes, akkor megeszi, ha nem, akkor értelemszerűen nem.
     * Az, hogy a rovar nem képes enni, egy másik spóra előzetes elfogyasztásából következhet.
     */
    private static void eatingVortex() {
        // Kommunikációs diagram
        Pyroxene t1 = new Pyroxene();
        Vortex s = new Vortex();
        Insect i = new Insect();
        Bugger b = new Bugger();

        t1.addInsectOnTecton(i);
        t1.addSpore(s);
        b.addInsect(i);

        // Szekvencia diagram
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tud-e a rovar spórát enni? (I/N) ");
        String input = scanner.nextLine();

        i.setCanEat(input.equals("I"));
        i.eatSpore(s);
    }

    /*
     * Drownsap elfogyasztása.
     * Egy rovar egy drownsap-et fog megenni.
     * Megkérdezzük a felhasználótól, hogy képes-e a rovar spórát enni.
     * Ha képes, akkor megeszi, ha nem, akkor értelemszerűen nem.
     * Az, hogy a rovar nem képes enni, egy másik spóra előzetes elfogyasztásából következhet.
     */
    private static void eatingDrownsap() {
        // Kommunikációs diagram
        Pyroxene t1 = new Pyroxene();
        Drownsap s = new Drownsap();
        Insect i = new Insect();
        Bugger b = new Bugger();

        t1.addInsectOnTecton(i);
        t1.addSpore(s);
        b.addInsect(i);

        // Szekvencia diagram
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tud-e a rovar spórát enni? (I/N) ");
        String input = scanner.nextLine();

        i.setCanEat(input.equals("I"));
        i.eatSpore(s);
    }

    /*
     * Frostclash elfogyasztása.
     * Egy rovar egy frostclash-et fog megenni.
     * Megkérdezzük a felhasználótól, hogy képes-e a rovar spórát enni.
     * Ha képes, akkor megeszi, ha nem, akkor értelemszerűen nem.
     * Az, hogy a rovar nem képes enni, egy másik spóra előzetes elfogyasztásából következhet.
     */
    private static void eatingFrostclash() {
        // Kommunikációs diagram
        Pyroxene t1 = new Pyroxene();
        Frostclash s = new Frostclash();
        Insect i = new Insect();
        Bugger b = new Bugger();

        t1.addInsectOnTecton(i);
        t1.addSpore(s);
        b.addInsect(i);

        // Szekvencia diagram
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tud-e a rovar spórát enni? (I/N) ");
        String input = scanner.nextLine();

        i.setCanEat(input.equals("I"));
        i.eatSpore(s);
    }

    /*
     * Rovar tektonok között mozog, feltéve, hogy van közöttük fonál.
     * Megkérdezzük, hogy tud-e mozogni a rovar.
     * Ha nem tud, akkor nem mozog.
     * Ez abból következhet, hogy előtte olyan spórát evett ami megakadályozza a mozgásban.
     */
    private static void moveToTecton() {
        Insect i = new Insect();
        Bugger b = new Bugger();
        Tecton oldTecton = new Pyroxene();
        Tecton newTecton = new Pyroxene();
        InterconnectingHypha h = new InterconnectingHypha();
        Mushroom m = new Mushroom();
        b.addInsect(i);
        oldTecton.addInsectOnTecton(i);
        h.setHyphaEndpoints(oldTecton, newTecton);
        m.addHypha(h);
        i.setOnTecton(oldTecton);
        oldTecton.addNeighbour(newTecton);
        newTecton.addNeighbour(oldTecton);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tud-e a rovar mozogni? (I/N) ");
        String input = scanner.nextLine();
        if(input.equals("I")){
            i.moveToTecton(newTecton);
        }
    }

    /*
     * Gombatest spórát dob ki magából.
     * Megkérdezzük, hogy ez az utolsó spóra, vagy sem.
     * Ha ez az utolsó, akkor meghal a test.
     * Ha nem az utolsó, akkor életben marad.
     * A szórás mindkét esetben megtörténik.
     */
    private static void disperseSpore() {
        Tecton t1 = new Pyroxene();
        Tecton t2 = new Pyroxene();
        IngrownHypha h = new IngrownHypha();
        MushroomBody mb = new MushroomBody();
        Mushroom m = new Mushroom();
        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
        h.addMushroomBody(mb);
        t1.addIngrownHypha(h);
        m.addHypha(h);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ez a gomba utolsó spórája? (I/N) ");
        String input = scanner.nextLine();
        if(input.equals("I")){
            mb.setsporesLeft(1);
        }
        mb.disperseSpore(t2);
    }

    /*
     * Gombatestet növesztünk Tanzanite-ra.
     * Ehhez szükséges 2 spóra, és egy fonál.
     * Ezen a tectonon nem képes gombatest nőni.
     */
    private static void growMushroomBodyOnTanzanite() {
        Tanzanite t1 = new Tanzanite();
        Clogspore s1 = new Clogspore();
        Clogspore s2 = new Clogspore();
        IngrownHypha h = new IngrownHypha();
        Mushroom m = new Mushroom();
        t1.addSpore(s1);
        t1.addSpore(s2);
        m.addHypha(h);
        t1.addIngrownHypha(h);

        t1.growMushroomBody(h);
    }

    /*
     * Gombatestet növesztünk Pyroxene-ra.
     * Ehhez szükséges 2 spóra, és egy fonál.
     * Ezen a tectonon képes gombatest nőni.
     */
    private static void growMushroomBodyOnPyroxene() {
        Pyroxene t1 = new Pyroxene();
        Clogspore s1 = new Clogspore();
        Clogspore s2 = new Clogspore();
        IngrownHypha h = new IngrownHypha();
        Mushroom m = new Mushroom();
        t1.addSpore(s1);
        t1.addSpore(s2);
        m.addHypha(h);
        t1.addIngrownHypha(h);

        t1.growMushroomBody(h);
    }

    /*
     * Gombatestet növesztünk Anorthite-ra.
     * Ehhez szükséges 2 spóra, és egy fonál.
     * Ezen a tectonon képes gombatest nőni.
     */
    private static void growMushroomBodyOnAnorthite() {
        Anorthite t1 = new Anorthite();
        Clogspore s1 = new Clogspore();
        Clogspore s2 = new Clogspore();
        IngrownHypha h = new IngrownHypha();
        Mushroom m = new Mushroom();
        t1.addSpore(s1);
        t1.addSpore(s2);
        m.addHypha(h);
        t1.addIngrownHypha(h);

        t1.growMushroomBody(h);
    }

    /*
     * Gombafonalat növesztünk Tanzanite-ra.
     * Ehhez szükséges egy fonál.
     * Ezen a tectonon csak egy fajta fonál lehet.
     */
    private static void growHyphaOnTanzanite() {

        //Kommunikációs rész
        Mushroom m = new Mushroom();
        Tanzanite t1 = new Tanzanite();
        Pyroxene kTecton = new Pyroxene();
        t1.addNeighbour(kTecton);
        kTecton.addNeighbour(t1);

        MushroomBody mb = new MushroomBody();

        IngrownHypha Ing = new IngrownHypha();
        Ing.addMushroomBody(mb);
        m.addHypha(Ing);
        kTecton.addIngrownHypha(Ing);

        //Szekvencia rész
        m.growHypha(kTecton,t1);

    }

    /*
     * Gombafonalat növesztünk Pyroxene-ra.
     * Ehhez szükséges egy fonál.
     * Ezen a tectonon több fajta fonál lehet.
     */
    private static void growHyphaOnPyroxene() {
        //Kommunikációs rész
        Mushroom m1 = new Mushroom();
        Mushroom m2 = new Mushroom();

        Pyroxene P = new Pyroxene();
        Pyroxene kTecton = new Pyroxene();
        Pyroxene hTecton = new Pyroxene();

        P.addNeighbour(hTecton);
        P.addNeighbour(kTecton);

        kTecton.addNeighbour(P);
        hTecton.addNeighbour(P);

        MushroomBody mb1 = new MushroomBody();
        MushroomBody mb2 = new MushroomBody();
        MushroomBody mb3 = new MushroomBody();

        IngrownHypha Ing1 = new IngrownHypha();
        IngrownHypha Ing2 = new IngrownHypha();
        IngrownHypha Ing3 = new IngrownHypha();

        kTecton.addIngrownHypha(Ing1);
        hTecton.addIngrownHypha(Ing2);
        P.addIngrownHypha(Ing3);

        m1.addHypha(Ing1);
        m2.addHypha(Ing2);
        m2.addHypha(Ing3);

        Ing1.addMushroomBody(mb1);
        Ing2.addMushroomBody(mb2);
        Ing3.addMushroomBody(mb3);


        InterconnectingHypha Int = new InterconnectingHypha();

        Int.setHyphaEndpoints(P, hTecton);

        m2.addHypha(Int);

        //Szekvencia rész
        m1.growHypha(P, kTecton);

    }

    /*
     * Gombafonalat növesztünk Anorthite-ra.
     * Ehhez szükséges egy fonál.
     * Ezen a tectonon csak egy fajta fonál lehet.
     */
    private static void growHyphaOnAnorthite() {
        //Kommunikációs rész
        Mushroom m = new Mushroom();
        Anorthite t1 = new Anorthite();
        Pyroxene kTecton = new Pyroxene();

        t1.addNeighbour(kTecton);
        kTecton.addNeighbour(t1);

        MushroomBody mb = new MushroomBody();
        MushroomBody mbA = new MushroomBody();

        IngrownHypha Ing1 = new IngrownHypha();
        IngrownHypha Ing2 = new IngrownHypha();

        Ing1.addMushroomBody(mb);
        Ing2.addMushroomBody(mbA);

        t1.addIngrownHypha(Ing1);
        kTecton.addIngrownHypha(Ing2);

        m.addHypha(Ing1);
        m.addHypha(Ing2);

        //Szekvencia rész
        m.growHypha(kTecton,t1);

    }

    /*
     * Két gombafonalat növesztünk két különböző tectonra.
     * Ehhez szükséges egy fonál és egy spóra.
     */
    private static void doubleGrowHypha() {
        //Kommunikációs rész
        Mushroom m = new Mushroom();
        Pyroxene t1 = new Pyroxene();
        Pyroxene t2 = new Pyroxene();
        Pyroxene t3 = new Pyroxene();

        Vortex v = new Vortex();
        t1.addSpore(v);

        t1.addNeighbour(t2);
        t1.addNeighbour(t3);
        t2.addNeighbour(t1);
        t3.addNeighbour(t1);

        MushroomBody mb = new MushroomBody();

        IngrownHypha Ing = new IngrownHypha();

        Ing.addMushroomBody(mb);
        t1.addIngrownHypha(Ing);
        m.addHypha(Ing);

        //Szekvencia rész
        m.doubleGrowHypha(t1,t2,t3);
    }

    /*
     * Gombatestet fejlesztünk.
     * Ehhez szükséges 3 fonál.
     * Növesztünk 3 fonalat, és az utolsó növesztésre fejlesztünk.
     */
    private static void mushroomBodyAdvancing() {
        //Kommunikációs rész
        Mushroom m = new Mushroom();
        Pyroxene t1 = new Pyroxene();
        Pyroxene t2 = new Pyroxene();
        Pyroxene t3 = new Pyroxene();
        Pyroxene t4 = new Pyroxene();


        t1.addNeighbour(t2);
        t1.addNeighbour(t3);
        t1.addNeighbour(t4);

        MushroomBody mb = new MushroomBody();
        IngrownHypha Ing = new IngrownHypha();
        Ing.addMushroomBody(mb);
        t1.addIngrownHypha(Ing);
        m.addHypha(Ing);

        m.growHypha(t1,t2);
        m.growHypha(t1,t3);

        //Szekvencia rész
        m.growHypha(t1,t4);

    }

    /*
     * Rovar spóra hatása alatt mozog.
     */
    private static void insectBoosted() {
        Pyroxene t1 = new Pyroxene();
        Pyroxene t2 = new Pyroxene();
        Pyroxene t3 = new Pyroxene();
        Mushroom m = new Mushroom();
        IngrownHypha IgH1 = new IngrownHypha();
        IngrownHypha IgH2 = new IngrownHypha();
        IngrownHypha IgH3 = new IngrownHypha();
        InterconnectingHypha IcH12 = new InterconnectingHypha();
        InterconnectingHypha IcH23 = new InterconnectingHypha();
        MushroomBody mb = new MushroomBody();
        Insect i = new Insect();

        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
        t2.addNeighbour(t3);
        t3.addNeighbour(t2);
        t1.addIngrownHypha(IgH1);
        t2.addIngrownHypha(IgH2);
        t3.addIngrownHypha(IgH3);
        IcH12.setHyphaEndpoints(t1, t2);
        IcH23.setHyphaEndpoints(t2, t3);
        IgH1.addMushroomBody(mb);
        i.setOnTecton(t1);
        m.addHypha(IgH1);
        m.addHypha(IcH12);
        m.addHypha(IgH2);
        m.addHypha(IcH23);
        m.addHypha(IgH3);

        Scanner scanner = new Scanner(System.in);
        System.out.print("A rovar tud duplát lépni? (I/N) ");
        String input = scanner.nextLine();
        if(input.equals("I")){
            i.moveToTecton(t3);
        }else{
            i.moveToTecton(t2);
        }
    }

    /*
     * Eltörik egy tekton.
     * Ennek hatására át kell a rajta lévő egyedeket mozgatni, legyen ez fonál, vagy rovar.
     */
    private static void tectonBreaks() {
        Pyroxene t1 = new Pyroxene();
        Pyroxene t2 = new Pyroxene();
        IngrownHypha IgH1 = new IngrownHypha();
        IngrownHypha IgH2 = new IngrownHypha();
        InterconnectingHypha IcH12 = new InterconnectingHypha();
        MushroomBody mb = new MushroomBody();
        Vortex s = new Vortex();
        Insect i = new Insect();

        t1.addInsectOnTecton(i);
        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
        t1.addIngrownHypha(IgH1);
        t2.addIngrownHypha(IgH2);
        IcH12.setHyphaEndpoints(t1, t2);
        IgH2.addMushroomBody(mb);

        t1.breakT();
    }

    /*
     * Meghal egy fonál.
     * Ennek hatására a rajta lévő gombatest is meghal, ha van rajta.
     */
    private static void ingrownHyphaDying() {
        Pyroxene t1 = new Pyroxene();
        IngrownHypha ig = new IngrownHypha();
        Mushroom m = new Mushroom();

        m.addHypha(ig);

        t1.addIngrownHypha(ig);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Teljen el annyi idő, hogy elhaljon a gombafonál? (I/N) ");
        String input = scanner.nextLine();

        if(input.equals("I")) {
            ig.step();
            ig.step();
        } else if(input.equals("N")) {
            ig.step();
        }
    }

    /*
     * Meghal egy fonál.
     */
    private static void interconnectingHyphaDying() {
        Pyroxene t1 = new Pyroxene();
        Pyroxene t2 = new Pyroxene();
        InterconnectingHypha ic = new InterconnectingHypha();
        Mushroom m = new Mushroom();

        m.addHypha(ic);
        ic.setHyphaEndpoints(t1, t2);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Teljen el annyi idő, hogy elhaljon a gombafonál? (I/N) ");
        String input = scanner.nextLine();

        if(input.equals("I")) {
            ic.step();
            ic.step();
        } else if(input.equals("N")) {
            ic.step();
        }
    }

    /*
     * Növesztés hatására újracsatlakoztatunk minden olyan fonalat,
     * akik ezzel a növesztéssel újra csatlakoztatva lettek egy gombatesthez.
     */
    private static void hyphaReconnection() {
        Pyroxene t1 = new Pyroxene();
        Pyroxene t2 = new Pyroxene();
        IngrownHypha ig2 = new IngrownHypha();
        Mushroom m = new Mushroom();
        MushroomBody mb = new MushroomBody();
        IngrownHypha ig1 = new IngrownHypha();

        m.addHypha(ig1);
        m.addHypha(ig2);
        ig1.addMushroomBody(mb);
        t1.addIngrownHypha(ig1);
        t2.addIngrownHypha(ig2);

        m.growHypha(t1, t2);
    }

    /*
     * Anorthite tecton különleges képessége.
     * Ennek hatására az összes rajta lévő gombafonál elhal.
     * Rovarok és spórák nem halnak meg.
     */
    private static void anorthiteEarthquake() {
        Anorthite T1 = new Anorthite();
        Anorthite T2 = new Anorthite();
        IngrownHypha IH1 = new IngrownHypha();
        IngrownHypha IH2 = new IngrownHypha();
        MushroomBody MB = new MushroomBody();
        InterconnectingHypha IC = new InterconnectingHypha();
        Mushroom M = new Mushroom();
        T1.addNeighbour(T2);
        T2.addNeighbour(T1);
        IH1.addMushroomBody(MB);
        M.addHypha(IH1);
        T1.addIngrownHypha(IH1);
        M.addHypha(IH2);
        T2.addIngrownHypha(IH2);
        M.addHypha(IC);
        IC.setHyphaEndpoints(T1, T2);

        T2.step();
        T2.step();
        T2.step();
    }

    /*
     * Fejlett gombatest spórákat szór.
     * Ehhez szükséges a gombatest fejlesztése.
     * Ha egy fejlett test szórja a spórákat, akkor messzebre tudja őket szórni.
     */
    private static void advancedDisperseSpore() {
        Tecton t1 = new Pyroxene();
        Tecton t2 = new Pyroxene();
        Tecton t3 = new Pyroxene();
        IngrownHypha h = new IngrownHypha();
        MushroomBody mb = new MushroomBody();
        Mushroom m = new Mushroom();
        t1.addNeighbour(t2);
        t2.addNeighbour(t1);
        t2.addNeighbour(t3);
        t3.addNeighbour(t2);
        h.addMushroomBody(mb);
        t1.addIngrownHypha(h);
        m.addHypha(h);
        h.upgradeMushroomBody();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ez a gomba utolsó spórája? (I/N) ");
        String input = scanner.nextLine();
        if(input.equals("I")){
            mb.setsporesLeft(1);
        }
        mb.disperseSpore(t2);
    }
}
