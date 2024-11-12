import java.io.FileNotFoundException;

public class MainTester {
    public static void main(String[] args) throws FileNotFoundException {
        readTest();
    }

    public static void readTest() throws FileNotFoundException {
        System.out.println("Checking integer bounds:");
        int[] bounds = Read.read("input.txt");
        System.out.println(bounds[0] + ", " + (bounds[0] == -2147483648));
        System.out.println(bounds[1] + ", " + (bounds[1] == 2147483647));
        System.out.println(bounds[2] + ", " + (bounds[2] == -1));
        System.out.println(bounds[3] + ", " + (bounds[3] == 0));
        System.out.println();

        System.out.println("Checking integer opcode:");
        int[] opcode = Read.read("instructions.txt");
        for (int k : opcode) {
            Read.instructionDecode(k);
            System.out.println();
        }

        int[] addLarge = Read.read("./tests/task1/addLarge.bin");
        for (int j : addLarge) {
            Read.instructionDecode(j);
            System.out.println();
        }
    }
}
