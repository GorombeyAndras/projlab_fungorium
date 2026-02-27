import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Felhasznált forrás: https://docs.oracle.com/javase/7/docs/api/java/lang/StackTraceElement.html

/*
 * A szekvenciadiagramok függvényhívásainak kiírását megvalósító osztály
 */
public class Logger {
    private static List<Set<String>> nameList = new ArrayList<>();
    private static List<Set<String>> paramList = new ArrayList<>();
    private static LinkedHashMap<String, String> paramDict = new LinkedHashMap<>();

    private static boolean loggerState = true;

    public static void turnOnLogger() {
        loggerState = true;
    }

    public static void turnOffLogger() {
        loggerState = false;
    }

    /*
     * Kiírja a meghívott függvény nevét és paramétereit
     */
    public static void enter(String funcName, List<String> paramsListed) {
        if (!loggerState) {
            return;
        }

        Map<Thread,StackTraceElement[]> threadMap = Thread.getAllStackTraces();

        // Cél: visszafejteni a változó nevét, amelyen hívták a függvényt a stack segítségével
        String variableName = "";
        Set<String> stringSet = new LinkedHashSet<>();
        Set<String> paramSet = new HashSet<>();
        boolean constructorCalled = false;

        for (Thread t : threadMap.keySet()) {
            for (StackTraceElement st : threadMap.get(t)) {
                // Létezik-e a stack eleméhez tartozó fájlnév
                if (st.getFileName() != null) {
                    File file = new File("src/" + st.getFileName());

                    // Létezik-e ilyen nevű fájl az src/ mappában
                    if (file.exists() && file.canRead()) {
                        Scanner f;

                        try {
                            f = new Scanner(file);
                        } catch (FileNotFoundException e) {
                            continue;
                        }

                        String line = "";

                        // Cél meghatározni azt a sort, amelyben a függvényt meghívták
                        for (int i = 0; i < st.getLineNumber() && f.hasNext(); i++) {
                            line = f.nextLine();
                        }

                        // Ha tartalmazza a sor a "new" kulcsszót, akkor konstruktor
                        if (line.contains(" new " + funcName)) {
                            variableName = line.strip().split("=")[0].split(" ")[1];
                            paramSet.add(line.strip().substring(line.strip().indexOf('(') + 1, line.strip().indexOf(')')));
                            constructorCalled = true;
                        } else if (line.contains("." + funcName) && line.contains("=")) { // Ha a függvény metódus és van visszatérési értéke
                            variableName = line.strip().split("=")[1].split("\\.")[0].strip();
                            String params = line.strip().substring(line.strip().indexOf('(') + 1, line.strip().indexOf(')'));

                            if (stringSet.iterator().hasNext()) {
                                params = params.replace("this", stringSet.iterator().next());
                            }

                            paramSet.add(params);
                        } else if (line.contains("." + funcName)) { // Ha a függvény nevét .fv() alakban tartalmazza, akkor változóra hívták
                            variableName = line.strip().split("\\.")[0];
                            String params = line.strip().substring(line.strip().indexOf('(') + 1, line.strip().indexOf(')'));

                            if (stringSet.iterator().hasNext()) {
                                params = params.replace("this", stringSet.iterator().next());
                            }

                            paramSet.add(params);
                        } else if(!nameList.isEmpty()) { // Ebben az esetben osztályon belül hívták a másik tagfüggvényt
                            variableName = nameList.get(nameList.size() - 1).iterator().next();
                        }

                        if (!variableName.isEmpty()) {
                            stringSet.add(variableName);
                        }
                    }
                }
            }
        }

        nameList.add(stringSet);
        paramList.add(paramSet);

        // Behúzás attól függően, hogy hány másik függvényen keresztül történt a függvényhívás
        for (int i = 1; i < nameList.size(); i++) {
            System.out.print("\t");
        }

        if (paramSet.iterator().hasNext()) {
            int idx = 0;
            for (String p : paramSet.iterator().next().split(", ")) {
                if (paramsListed.size() > idx) {
                    paramDict.put(paramsListed.get(idx), p);
                }

                idx++;
            }
        }

        if (constructorCalled) {
            System.out.println("-->" + funcName);
        } else {
            Iterator<String> iter = stringSet.iterator();
            String vName = "";

            while (iter.hasNext()) {
                vName = iter.next();
            }

            String getName = paramDict.get(vName);

            if (getName != null) {
                vName = getName;
            }

            System.out.print("-->" + vName + "." + funcName + "(");

            if (!paramSet.isEmpty()) {
                System.out.print(paramSet.iterator().next());
            }

            System.out.println(")");
        }
    }

    /*
     * Kiírja a visszatérő függvény nevét és paraméterét
     */
    public static void exit(String funcName, String retValue) {
        if (!loggerState) {
            return;
        }

        if (!nameList.isEmpty()) {
            // Behúzás attól függően, hogy hány másik függvényen keresztül történt a függvényhívás
            for (int i = 1; i < nameList.size(); i++) {
                System.out.print("\t");
            }

            Iterator<String> iter = nameList.get(nameList.size() - 1).iterator();
            String vName = "";

            while (iter.hasNext()) {
                vName = iter.next();
            }

            String getName = paramDict.get(vName);

            if (getName != null) {
                vName = getName;
            }

            // Konstruktor tér vissza
            if (retValue.equals("-")) {
               System.out.println("<--" + funcName + ":" + vName);
            } else {
               System.out.println("<--" + vName + "." + funcName + (retValue.isEmpty() ? "" : ":" + retValue));
            }

            //System.out.println(paramDict);

            nameList.remove(nameList.size() - 1);
            paramList.remove(paramList.size() - 1);
        }
    }

    // Minden use-case hívás esetén ki kell szedni az előző
    public static void clearParamDict() {
        paramDict.clear();
    }
}
