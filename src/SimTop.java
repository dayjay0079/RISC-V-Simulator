import java.io.IOException;
import java.util.Scanner;

public class SimTop {
    public static int memSize;
    public static int[] program;
    public static boolean printInstructions;

    private static void getMemSize(Scanner scanner) {
        System.out.print("Size of the memory in MB: ");
        int memSizeMB = scanner.nextInt();
        scanner.nextLine();
        memSize = memSizeMB * (int) Math.pow(2, 20);
    }

    private static void getProgram(Scanner scanner) {
        System.out.print("Path to the binary file: ");
        String path = scanner.nextLine();
        while (true) {
            try {
                program = IO.readBin(path);
                return;
            } catch (Exception e) {
                System.out.println("File not found. Please enter a valid path.");
                System.out.print("Path to the binary file: ");
                path = scanner.nextLine();
            }
        }
    }

    private static void getPrintInstructions(Scanner scanner) {
        System.out.print("Should instructions be printed as they execute? (y/n): ");
        String print = scanner.nextLine().toLowerCase();
        while (!print.equals("y") && !print.equals("n")) {
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
            System.out.print("Should instructions be printed as they execute? (y/n): ");
            print = scanner.nextLine().toLowerCase();
        }
        printInstructions = print.equals("y");
    }

    public static void main(String[] args) throws IOException {
        // Get user input
        Scanner scanner = new Scanner(System.in);
        getMemSize(scanner);
        getProgram(scanner);
        getPrintInstructions(scanner);
        scanner.close();

        // Execute program
        Control control = new Control(memSize, program, printInstructions);
        control.executeProgram();

        // Print final register values
        System.out.println("\nProgram successfully executed!");
        System.out.println("Final register values:");
        IO.printRegs(control);
    }
}
