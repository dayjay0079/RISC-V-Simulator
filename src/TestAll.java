import java.io.IOException;

public class TestAll {
    private static boolean checkResult(Control control, int[] results) throws IOException {
        boolean success = true;
        int[] regs = control.getAllRegValues();
        for (int i = 0; i < 32; i++) {
            if ((regs[i] != results[i])) {
//                System.out.println("Register " + i + " does not match the expected value.");
                success = i == 2;
            }
        }
        return success;
    }

    private static void test(String path) throws IOException {
        System.out.print("Testing \"" + path + ".bin\": \t");
        int[] program = IO.readBin(path + ".bin");
        int[] result = IO.readBin(path + ".res");
        Control control = new Control((int)Math.pow(2, 20), program, false);
        control.executeProgram();
        boolean success = checkResult(control, result);
        System.out.println(success ? "Success" : "Fail");
    }

    private static void test1() throws IOException {
            String path = "tests/task1/";
            test(path + "addlarge");
            test(path + "addneg");
            test(path + "addpos");
            test(path + "bool");
            test(path + "set");
            test(path + "shift");
            test(path + "shift2");
    }

    private static void test2() throws IOException {
            String path = "tests/task2/";
            test(path + "branchcnt");
            test(path + "branchmany");
            test(path + "branchtrap");
    }

    private static void test3() throws IOException {
            String path = "tests/task3/";
            test(path + "loop");
            test(path + "recursive");
            test(path + "string");
            test(path + "width");
    }

    private static void test4() throws IOException {
        String path = "tests/task4/t";
        for (int i = 1; i <= 15; i++) {
            test(path + i);
        }
    }

    public static void main(String[] args) throws IOException {
        test1();
        System.out.println();

        test2();
        System.out.println();

        test3();
        System.out.println();

        test4();
    }
}
