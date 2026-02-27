import javax.sound.sampled.Line;
import java.io.*;
import java.util.*;

public class Prototype {
    private static String commandName;
    private static List<String> args = new ArrayList<>();

    private static LinkedHashMap<String, Tecton> tectonHashMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Hypha> hyphaHashMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Spore> sporeHashMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Insect> insectHashMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Bugger> buggerHashMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Mushroom> mushroomHashMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, MushroomBody> mushroomBodyHashMap = new LinkedHashMap<>();
    private static List<IStepable> stepableList = new ArrayList<>();
    private static List<String> graveyard = new ArrayList<>();

    private static List<String> statusBuffer = new LinkedList<>();
    private static boolean statusState = true;

    public static void clearMap() {
        tectonHashMap = new LinkedHashMap<>();
        hyphaHashMap = new LinkedHashMap<>();
        sporeHashMap = new LinkedHashMap<>();
        insectHashMap = new LinkedHashMap<>();
        buggerHashMap = new LinkedHashMap<>();
        mushroomHashMap = new LinkedHashMap<>();
        mushroomBodyHashMap = new LinkedHashMap<>();

        graveyard = new ArrayList<>();
        stepableList = new ArrayList<>();
    }

    public static void nextCommand(String line) {
        commandName = line.strip().split(" ")[0];
        args = new ArrayList<>();

        args.addAll(Arrays.asList(line.strip().split(" ")).subList(1, line.strip().split(" ").length));

        switch (commandName) {
            case "add":
                add();
                break;

            case "connect":
                connect();
                break;

            case "move":
                move();
                break;

            case "eat_spore":
                eat_spore();
                break;

            case "eat_insect":
                eat_insect();
                break;

            case "disperse":
                disperse();
                break;

            case "grow":
                grow();
                break;

            case "neighbour":
                neighbour();
                break;

            case "cut":
                cut();
                break;

            case "break":
                break_tecton();
                break;

            case "load":
                load();
                break;

            case "status":
                status();
                break;

            case "save":
                save();
                break;

            case "step":
                step();
                break;

            case "run_all_tests":
                run_all_tests();
                break;

            // Kilépéskor ne írjon hibaüzenetet
            case "exit":
                break;

            case "status_all":
                status_all();
                break;

            default:
                System.out.println("Hiba: nem felismerhető parancs.");
                break;
        }

    }

    /// BEMENETI NYELV

    private static void add() {
        String typeString = args.get(0);
        String nameString = args.get(1);
        String ownerString = new String();

        // 3 argumentum van
        if (args.size() == 3) {
            ownerString = args.get(2);
        }

        switch (typeString) {
            // Tectonok
            case "Anorthite":
                tectonHashMap.put(nameString, new Anorthite());
                stepableList.add(tectonHashMap.get(nameString));
                break;

            case "Pyroxene":
                tectonHashMap.put(nameString, new Pyroxene());
                stepableList.add(tectonHashMap.get(nameString));
                break;

            case "Emerald":
                tectonHashMap.put(nameString, new Emerald());
                stepableList.add(tectonHashMap.get(nameString));
                break;

            case "Tanzanite":
                tectonHashMap.put(nameString, new Tanzanite());
                stepableList.add(tectonHashMap.get(nameString));
                break;

            // Spore
            case "Vortex":
                sporeHashMap.put(nameString, new Vortex());
                tectonHashMap.get(ownerString).addSpore(sporeHashMap.get(nameString));
                break;

            case "Drownsap":
                sporeHashMap.put(nameString, new Drownsap());
                tectonHashMap.get(ownerString).addSpore(sporeHashMap.get(nameString));
                break;

            case "Frostclash":
                sporeHashMap.put(nameString, new Frostclash());
                tectonHashMap.get(ownerString).addSpore(sporeHashMap.get(nameString));
                break;

            case "Splitspore":
                sporeHashMap.put(nameString, new Splitspore());
                tectonHashMap.get(ownerString).addSpore(sporeHashMap.get(nameString));
                break;

            case "Clogspore":
                sporeHashMap.put(nameString, new Clogspore());
                tectonHashMap.get(ownerString).addSpore(sporeHashMap.get(nameString));
                break;

            case "Insect":
                insectHashMap.put(nameString, new Insect());
                buggerHashMap.get(ownerString).addInsect(insectHashMap.get(nameString));
                stepableList.add(insectHashMap.get(nameString));
                break;

            case "Bugger":
                buggerHashMap.put(nameString, new Bugger());
                break;

            case "Mushroom":
                mushroomHashMap.put(nameString, new Mushroom());
                break;

            case "MushroomBody":
                mushroomBodyHashMap.put(nameString, new MushroomBody());
                ((IngrownHypha) hyphaHashMap.get(ownerString)).addMushroomBody(mushroomBodyHashMap.get(nameString));
                break;


            default:
                System.err.println("Hiba: helytelen típusnév.");
                break;
        }

    }

    private static void connect() {
        String hyphaName1 = args.get(0);
        String ingrownHyphaName1 = args.get(1);
        Mushroom mushroom = mushroomHashMap.get(args.get(2));
        Tecton tecton1 = tectonHashMap.get(args.get(3));
        Tecton tecton2 = tectonHashMap.get(args.get(4));
        Tecton tecton3 = null;
        String hyphaName2 = null;
        String ingrownHyphaName2 = null;

        if (args.size() >= 6) {
            tecton3 = tectonHashMap.get(args.get(5));
        }

        if (args.size() >= 7) {
            hyphaName2 = args.get(6);
        }

        if (args.size() == 8) {
            ingrownHyphaName2 = args.get(7);
        }

        // Egyszeres növesztés
        if (args.size() <= 5) {
            if (!args.get(3).equals(args.get(4))) {
                List<Hypha> listOfHyphae = mushroom.growHypha(tecton1, tecton2);

                if(listOfHyphae.isEmpty()) {
                    return;
                }

                hyphaHashMap.put(hyphaName1, listOfHyphae.get(0));
                stepableList.add(0,hyphaHashMap.get(hyphaName1));

                // Van Ingrown is
                if (listOfHyphae.size() == 2 && tecton1  != tecton2 ) {
                    hyphaHashMap.put(ingrownHyphaName1, listOfHyphae.get(1));
                    stepableList.add(0,hyphaHashMap.get(ingrownHyphaName1));
                }
            } else {
                // Csak az IngrownHypha kerül hozzáadásra
                    IngrownHypha h = new IngrownHypha();
                    tecton1.addIngrownHypha(h);
                    mushroom.addHypha(h);

                    hyphaHashMap.put(ingrownHyphaName1, h);
                    stepableList.add(0,hyphaHashMap.get(ingrownHyphaName1));
            }


        } else { // DoubleGrow
            List<Hypha> listOfHyphae = mushroom.doubleGrowHypha(tecton1, tecton2, tecton3);
            if(listOfHyphae.isEmpty()) {
                return;
            }
            hyphaHashMap.put(hyphaName1, listOfHyphae.get(0));
            stepableList.add(0,hyphaHashMap.get(hyphaName1));

            if (listOfHyphae.size() >= 3) {
                hyphaHashMap.put(ingrownHyphaName1, listOfHyphae.get(2));
                stepableList.add(0,hyphaHashMap.get(ingrownHyphaName1));
            }

            hyphaHashMap.put(hyphaName2, listOfHyphae.get(1));
            stepableList.add(0,hyphaHashMap.get(hyphaName2));

            //System.out.println(hyphaName1 + " " + hyphaName2 + " " + ingrownHyphaName1 + " " + ingrownHyphaName2);

            // Ingrown-ok

            if (listOfHyphae.size() == 4) {
                hyphaHashMap.put(ingrownHyphaName2, listOfHyphae.get(3));
                stepableList.add(0,hyphaHashMap.get(ingrownHyphaName2));
            }
        }
    }

    private static void move() {
        String insectName = args.get(0);
        String tectonName = args.get(1);

        Insect insect = insectHashMap.get(insectName);
        Tecton tecton = tectonHashMap.get(tectonName);

        if (insect == null) {
            System.err.println("Hiba: nem létezik ilyen nevű Insect.");
            return;
        }

        // ha még nincs Tectonon az Insect, akkor egy egyszerű setter hívás
        if (insect.getOnTecton() == null) {
            insect.setOnTecton(tecton);
            tecton.addInsectOnTecton(insect);
        } else {
            insect.moveToTecton(tecton);
        }
    }

    private static void eat_spore() {
        Insect insect = insectHashMap.get(args.get(0));
        Spore spore = sporeHashMap.get(args.get(1));

        List<Spore> sporesList = insect.getOnTecton().getSporesOnTecton();

        boolean isSporeOnTecton = false;

        for (Spore s : sporesList) {
            if (s  == spore ) {
                isSporeOnTecton = true;
                break;
            }
        }

        if (isSporeOnTecton) {
            Insect i2 = insect.eatSpore(spore);

            if(i2 != null) {
                insectHashMap.put(args.get(2), i2);
                stepableList.add(insectHashMap.get(args.get(2)));
            }

            graveyard.add(args.get(1));
        } else {
            // Ez ide lehet nem kell
            System.err.println("Hiba: nem ugyanazon a Tectonon van a Spore és az Insect.");
        }
    }

    private static void eat_insect() {
        IngrownHypha ingrownHypha = (IngrownHypha) hyphaHashMap.get(args.get(0));
        Insect insect = insectHashMap.get(args.get(1));

        if (ingrownHypha.getHomeTecton()  == insect.getOnTecton() ) {
            MushroomBody mb1 = ingrownHypha.consumeBug(insect);
            mushroomBodyHashMap.put("MB1", mb1);
            graveyard.add(args.get(1));
        } else {
            System.err.println("Hiba: nem ugyanazon a Tectonon van az Insect és az IngrownHypha.");
        }
    }

    private static void disperse() {
        MushroomBody mushroomBody = mushroomBodyHashMap.get(args.get(0));
        Tecton tecton = tectonHashMap.get(args.get(1));

        // MushroomBody elpusztulást nem nézi!
        mushroomBody.decremetSporesLeft();


        //A végső megoldásban a dispersesporet kell majd hívni!
        //mushroomBody.disperseSpore(tecton);

        if (mushroomBody.getSporesLeft() == 0) {
            graveyard.add(args.get(0));
            mushroomBody.getIngrownHypha().removeMushroomBody(mushroomBody);
        }

        Spore spore = null;
        String sporeName = args.get(3);

        switch (args.get(2)) {
            case "Vortex":
                spore = new Vortex();
                break;

            case "Clogspore":
                spore = new Clogspore();
                break;

            case "Drownsap":
                spore = new Drownsap();
                break;

            case "Frostclash":
                spore = new Frostclash();
                break;

            case "Splitspore":
                spore = new Splitspore();
                break;

            default:
                break;
        }

        if (spore != null) {
            sporeHashMap.put(sporeName, spore);
            tecton.addSpore(spore);
        } else {
            System.err.println("Hiba: helytelen Spore típusnév.");
        }

    }

    private static void grow() {
        Mushroom mushroom = mushroomHashMap.get(args.get(0));
        IngrownHypha ingrownHypha = (IngrownHypha) hyphaHashMap.get(args.get(1));

        Tecton tecton = ingrownHypha.getHomeTecton();

        int i = 0;
        MushroomBody newMushroomBody = tecton.growMushroomBody(ingrownHypha);
        for (Map.Entry<String, Spore> entry : sporeHashMap.entrySet()) {
            if (i < 2 && newMushroomBody != null) {
                if(tecton.getSporesOnTecton().contains(entry.getValue())) {
                    //A tesztek idejére a prototype végzi a levételt, mivel máshogy nem tudjuk ellenőrizni
                    tecton.removeSpore(entry.getValue());
                    graveyard.add(entry.getKey());
                    i++;
                }
            }
        }
        mushroomBodyHashMap.put(args.get(2), newMushroomBody);

    }

    private static void neighbour() {
        Tecton tecton1 = tectonHashMap.get(args.get(0));
        Tecton tecton2 = tectonHashMap.get(args.get(1));

        tecton1.addNeighbour(tecton2);
        tecton2.addNeighbour(tecton1);
    }

    private static void cut() {
        Insect insect = insectHashMap.get(args.get(0));
        InterconnectingHypha interconnectingHypha = (InterconnectingHypha) hyphaHashMap.get(args.get(1));

        insect.cutHypha(interconnectingHypha);
    }

    private static void break_tecton() {
        Tecton tecton1 = tectonHashMap.get(args.get(0));

        Tecton tecton2 = tecton1.breakT();
        tectonHashMap.put(args.get(1), tecton2);
    }

    private static void step() {
        for(Map.Entry<String, Hypha> h : hyphaHashMap.entrySet()){
            if(h.getValue() instanceof IngrownHypha && !((IngrownHypha) h.getValue()).getMushroomBodiesList().isEmpty()){
                h.getValue().step();
            }
        }
        Iterator<IStepable> stepIterator = stepableList.iterator();
        while (stepIterator.hasNext()) {
            IStepable stepable = stepIterator.next();
            stepable.step();
            for (Map.Entry<String, Hypha> entry : hyphaHashMap.entrySet()) {
                if (entry.getValue().getTimeSinceDisconnected() >= 2) {
                    graveyard.add(entry.getKey());
                    stepableList.remove(entry.getKey());
                }
                if (entry.getValue().getMushroom() == null) {
                    graveyard.add(entry.getKey());
                    stepableList.remove(entry.getKey());
                }
            }
        }
        for(Map.Entry<String, Hypha> h : hyphaHashMap.entrySet()){
            if(h.getValue() instanceof IngrownHypha && !((IngrownHypha) h.getValue()).getMushroomBodiesList().isEmpty()){
                h.getValue().step();
            }
        }


    }

    private static void load() {
        Scanner file = null;

        try {
            file = new Scanner(new File((new File("")).getAbsolutePath() + "\\tests\\inputs\\" + args.get(0)));

            while (file.hasNext()) {
                nextCommand(file.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Hiba: nem található nevű ilyen fájl.");
        }

        if (file != null) {
            file.close();
        }
    }

    /// KIMENETI NYELV

    private static void writeStatus(String prod) {
        if (graveyard.contains(args.get(0))) {
            prod = args.get(0) + ":\n\t" + "dead";
        }

        if (statusState)
            System.out.println(prod);
        statusBuffer.add(prod + "\n");
    }

    private static void status() {
        // Tectonok
        Tecton tectonElement = tectonHashMap.get(args.get(0));
        if(tectonElement!=null) {
            String prod = "";

            if (tectonElement instanceof Anorthite casted) {
                prod = args.get(0)+":\n\t"+
                        "insectsOnTecton: "+ casted.insectsOnTecton.size()+"\n\t" +
                        "interconnectingHyphaeOnTecton: "+casted.interconnectingHyphaeOnTecton.size()+"\n\t" +
                        "ingrownHyphaeOnTecton: "+casted.ingrownHyphaeOnTecton.size()+"\n\t" +
                        "neighbourList: "+casted.neighbourList.size()+"\n\t" +
                        "sporesOnTecton: "+casted.sporesOnTecton.size()+"\n\t" +
                        "timeLeftToEarthQuake: "+casted.getTimeLeftToEarthquake();
            }

            if (tectonElement instanceof Pyroxene casted) {
                prod = args.get(0)+":\n\t"+
                        "insectsOnTecton: "+casted.insectsOnTecton.size()+"\n\t" +
                        "interconnectingHyphaeOnTecton: "+casted.interconnectingHyphaeOnTecton.size()+"\n\t" +
                        "ingrownHyphaeOnTecton: "+casted.ingrownHyphaeOnTecton.size()+"\n\t" +
                        "neighbourList: "+casted.neighbourList.size()+"\n\t" +
                        "sporesOnTecton: "+casted.sporesOnTecton.size()+"\t";
            }

            if (tectonElement instanceof Tanzanite casted) {
                prod = args.get(0)+":\n\t"+
                        "insectsOnTecton: "+casted.insectsOnTecton.size()+"\n\t" +
                        "interconnectingHyphaeOnTecton: "+casted.interconnectingHyphaeOnTecton.size()+"\n\t" +
                        "ingrownHyphaeOnTecton: "+casted.ingrownHyphaeOnTecton.size()+"\n\t" +
                        "neighbourList: "+casted.neighbourList.size()+"\n\t" +
                        "sporesOnTecton: "+casted.sporesOnTecton.size()+"\t";
            }

            if (tectonElement instanceof Emerald casted) {
                prod = args.get(0)+":\n\t"+
                        "insectsOnTecton: "+casted.insectsOnTecton.size()+"\n\t" +
                        "interconnectingHyphaeOnTecton: "+casted.interconnectingHyphaeOnTecton.size()+"\n\t" +
                        "ingrownHyphaeOnTecton: "+casted.ingrownHyphaeOnTecton.size()+"\n\t" +
                        "neighbourList: "+casted.neighbourList.size()+"\n\t" +
                        "sporesOnTecton: "+casted.sporesOnTecton.size()+"\t";
            }

            if (graveyard.contains(args.get(0))) {
                prod = args.get(0) + ":\n\t" + "dead";
            }

            if (statusState)
                System.out.println(prod);
            statusBuffer.add(prod + "\n");
            return;
        }

        // Hypha
        Hypha hyphaElement = hyphaHashMap.get(args.get(0));
        if(hyphaElement!=null) {
            String prod = "";

            if (hyphaElement instanceof IngrownHypha casted) {
                prod = args.get(0)+":\n\t"+
                        "timeSinceDisconnected: "+casted.timeSinceDisconnected+"\n\t"+
                        "homeTecton: "+getName(casted.getHomeTecton())+"\n\t"+
                        "mushroomBodiesList: "+ casted.getMushroomBodiesList().size()+"\t";
            }

            if (hyphaElement instanceof InterconnectingHypha casted) {
                prod = args.get(0)+":\n\t"+
                        "timeSinceDisconnected: "+casted.timeSinceDisconnected+"\n\t"+
                        "timeSinceCut: "+casted.timeSinceCut+"\n\t" +
                        "startingTecton: "+getName(casted.getStartingTecton())+"\n\t"+
                        "endingTecton: "+getName(casted.getEndingTecton())+"\t";
            }

            writeStatus(prod);
            return;
        }

        // Spore
        Spore sporeElement = sporeHashMap.get(args.get(0));
        if(sporeElement!=null) {
            String prod = args.get(0)+":\n\t"+
                        "food: "+sporeElement.getFood()+"\t";


            writeStatus(prod);
            return;
        }

        // Insect
        Insect insectElement = insectHashMap.get(args.get(0));
        if(insectElement != null) {
            String prod = args.get(0)+":\n\t"+
                    "effectDuration: "+insectElement.getEffectDuration()+"\n\t"+
                    "speed: "+ insectElement.getSpeed() +"\n\t"+
                    "canCut: "+ (insectElement.getCanCut() ? 1 : 0) +"\n\t"+
                    "canEat: "+ (insectElement.getCanEat() ? 1 : 0)+"\n\t"+
                    "buggerOwner: "+getName(insectElement.getBuggerOwner())+"\n\t"+
                    "onTecton: "+getName(insectElement.getOnTecton())+"\n\t"+
                    "canMultiply: "+(insectElement.getCanMultiply() ? 1 : 0)+"\t";

            writeStatus(prod);
            return;
        }

        // Bugger
        Bugger buggerElement = buggerHashMap.get(args.get(0));
        if(buggerElement!=null) {
            String prod = args.get(0)+":\n\t"+
                    "insectList: "+ buggerElement.getInsectList().size()+"\n\t"+
                    "score: "+buggerElement.getScore()+"\t";

            writeStatus(prod);
            return;
        }

        // Mushroom
        Mushroom mushroomElement = mushroomHashMap.get(args.get(0));
        if(mushroomElement!=null) {
            String prod = args.get(0)+":\n\t"+
                    "hyphaList: "+ mushroomElement.getHyphaList().size()+"\n\t"+
                    "score: "+ mushroomElement.getScore() +"\t";

            writeStatus(prod);
            return;
        }

        // MushroomBody
        MushroomBody mushroomBodyElement = mushroomBodyHashMap.get(args.get(0));
        if(mushroomBodyElement!=null) {
            String prod = args.get(0)+":\n\t"+
                    "sporesLeft: "+mushroomBodyElement.getSporesLeft()+"\n\t"+
                    "advanced: "+ mushroomBodyElement.isAdvanced() +"\n\t"+
                    "ingrownHypha: "+Prototype.getName(mushroomBodyElement.getIngrownHypha())+"\t";

            writeStatus(prod);
        }
    }

    private static void status_all() {
        for (String name : mushroomHashMap.keySet()) {
            args = new ArrayList<>(List.of(name));
            status();
        }

        for (String name : buggerHashMap.keySet()) {
            args = new ArrayList<>(List.of(name));
            status();
        }

        for (String name : tectonHashMap.keySet()) {
            args = new ArrayList<>(List.of(name));
            status();
        }

        for (String name : sporeHashMap.keySet()) {
            args = new ArrayList<>(List.of(name));
            status();
        }

        for (String name : hyphaHashMap.keySet()) {
            args = new ArrayList<>(List.of(name));
            status();
        }

        for (String name : mushroomBodyHashMap.keySet()) {
            args = new ArrayList<>(List.of(name));
            status();
        }

        for (String name : insectHashMap.keySet()) {
            args = new ArrayList<>(List.of(name));
            status();
        }
    }

    private static void save() {
        BufferedWriter file = null;

        try {
            file = new BufferedWriter(new FileWriter((new File("").getAbsolutePath() + "\\tests\\saved_outputs\\" + args.get(0))));

            for (String string : statusBuffer) {
                file.write(string);
            }

            statusBuffer = new ArrayList<>(); // Buffer törlése

            clearMap();

            file.close();
        } catch (IOException e) {
            System.err.println("Hiba: nem található nevű ilyen fájl.");
        }
    }

    private static void run_all_tests() {
        int numberOfFiles = (new File((new File("")).getAbsolutePath() + "\\tests\\inputs\\")).list().length;
        int successful = 0;

        statusState = false;

        for (int index = 1; index <= numberOfFiles; index++) {
            args = new ArrayList<>(List.of("i_test" + index + ".txt"));
            load();
            args = new ArrayList<>(List.of("saved_test" + index + ".txt"));
            save();

            // Összehasonlítás
            try {
                Scanner expected = new Scanner(new File((new File("")).getAbsolutePath() + "\\tests\\outputs\\" + "o_test" + index + ".txt"));
                Scanner got = new Scanner(new File((new File("")).getAbsolutePath() + "\\tests\\saved_outputs\\" + "saved_test" + index + ".txt"));

                System.out.println("\n-----------------------------------");
                System.out.println("TEST " + index);
                HashMap<String, String> differences = new LinkedHashMap<>();

                while (got.hasNext() && expected.hasNext()) {
                    String gotLine = got.nextLine();
                    String expectedLine = expected.nextLine();

                    if (!gotLine.strip().equals(expectedLine.strip())) {
                        differences.put(expectedLine, gotLine);
                    }
                }

                if (differences.isEmpty()) {
                    System.out.println("Sikeres teszt!");
                    successful++;
                } else {
                    System.out.println("Hibás teszt!\nEltérések:");
                    for (Map.Entry<String, String> entry : differences.entrySet()) {
                        System.out.println("Várt: " + entry.getKey() + "\nKapott: " + entry.getValue());
                    }
                }
            } catch (IOException e) {
                System.err.println("Hiba: fájl nem található.");
            }
        }

        System.out.println("\n-----------------------------------");
        System.out.println("Összes/sikeres: " + numberOfFiles + "/" + successful);

        statusState = true;
    }

    public static String getName(Object object){
        for (Map.Entry<String, Tecton> entry : tectonHashMap.entrySet()) {
            if(entry.getValue() == object) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, Hypha> entry : hyphaHashMap.entrySet()) {
            if(entry.getValue() == object) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, Spore> entry : sporeHashMap.entrySet()) {
            if(entry.getValue() == object) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, Insect> entry : insectHashMap.entrySet()) {
            if(entry.getValue() == object) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, Bugger> entry : buggerHashMap.entrySet()) {
            if(entry.getValue() == object) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, Mushroom> entry : mushroomHashMap.entrySet()) {
            if(entry.getValue() == object) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, MushroomBody> entry : mushroomBodyHashMap.entrySet()) {
            if(entry.getValue() == object) {
                return entry.getKey();
            }
        }
        return null;
    }
}
