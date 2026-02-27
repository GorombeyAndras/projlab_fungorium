import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Projlab=sigma\nVálasszon az alábbi menüpontok közül:");
        System.out.println("Skeleton program (1)");
        System.out.println("Prototípus program (2)");
        System.out.println("Grafikus program (3)");

        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();

        while(num != 1 && num != 2&& num!=3) {
            System.err.println("Hibás bemenet, a fenti menüpontok közül válasszon!");
            num = sc.nextInt();
        }

        if (num == 1) {
            System.out.print("\nSkeleton program");

            Logger.turnOnLogger();

            do {
                System.out.println("\n-----------------------------------");
                Skeleton.printUseCaseList();
                System.out.print("Válasszon: ");
                if (sc.hasNext()) {
                    num = sc.nextInt();
                }

                Skeleton.runUseCase(num);
                sc.nextLine();
            } while(num != 0);
        } else if(num == 2) {
            Logger.turnOffLogger();

            sc.nextLine();
            System.out.println("\nPrototípus program\nKilépés az \"exit\" parancs segítségével lehetséges.");
            String command = new String();

            do {
                System.out.print(">");

                if (sc.hasNext()) {
                    command = sc.nextLine();
                }

                Prototype.nextCommand(command);
            } while(!command.equals("exit"));
        }
        //num3
        else{
            setup();
        }
        sc.close();
    }
    public static void setup(){
        Logger.turnOffLogger();

        Model.start(0, 0);
        View.Setup();
        Model.init();
    }
}