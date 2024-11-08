import java.io.IOException;
import java.util.Scanner;

public class SimTop {
    public static String path;

    private static int getMemSize(Scanner scanner) {
        System.out.print("Size of the memory in MB: ");
        int memSizeMB = scanner.nextInt();
        scanner.nextLine();
        return memSizeMB * (int) Math.pow(2, 20);
    }

    private static int[] getProgram(Scanner scanner) {
        System.out.print("Path to the binary file: ");
        path = scanner.nextLine();
        while (true) {
            try {
                return Read.readBin(path);
            } catch (Exception e) {
                System.out.println("File not found. Please enter a valid path.");
                System.out.print("Path to the binary file: ");
                path = scanner.nextLine();
            }
        }
    }

    private static boolean getPrintInstructions(Scanner scanner) {
        System.out.print("Should instructions be printed as they execute? (y/n): ");
        String print = scanner.nextLine().toLowerCase();
        while (!print.equals("y") && !print.equals("n")) {
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
            System.out.print("Should instructions be printed as they execute? (y/n): ");
            print = scanner.nextLine().toLowerCase();
        }
        return print.equals("y");
    }

    private static void checkResult(Control control) throws IOException {
        String resPath = path.replace(".bin", ".res");
        int[] res = Read.readBin(resPath);
        int[] regs = control.getAllRegValues();
        boolean match = true;
        for (int i = 0; i < 32; i++) {
            if ((regs[i] != res[i]) && i != 2) {
                System.out.println("Register " + i + " does not match the expected value.");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int memSize = getMemSize(scanner);
        int[] program = getProgram(scanner);
        boolean printInstructions = getPrintInstructions(scanner);
        scanner.close();

        Control control = new Control(memSize, program, printInstructions);
        control.executeProgram();


        System.out.println("\nProgram successfully executed!");
        System.out.println("Final register values:");
        control.printRegisters();
        checkResult(control);
    }
}
