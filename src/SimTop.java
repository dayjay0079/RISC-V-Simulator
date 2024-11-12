import java.io.IOException;
import java.util.Scanner;

public class SimTop {
    public static int memSize;
    public static int[] program;
    public static int[] resultTest;
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
            String resultPath = path.replace(".bin", ".res");
            try {
                program = IO.readBin(path);
                resultTest = IO.readBin(resultPath);
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

    private static void checkResult(Control control) throws IOException {
        int[] regs = control.getAllRegValues();
        for (int i = 0; i < 32; i++) {
            if (regs[i] != resultTest[i]) {
                System.out.println("Register " + i + " does not match the expected value.");
            }
        }
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

        // Check if the final register values match the expected values
        checkResult(control);
    }
}
