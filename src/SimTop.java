import java.io.IOException;

public class SimTop {
    public static int memSize = (int)Math.pow(2, 20);
    public static int[] program;
    public static boolean printInstructions = false;

    public static void main(String[] args) throws IOException {
        program = IO.readBin(args[0]);

        // Execute program
        Control control = new Control(memSize, program, printInstructions);
        control.executeProgram();

        // Print final register values
        System.out.println("Final register values:");
        IO.printRegs(control);
        IO.writeRes(args[0], control);
    }
}
