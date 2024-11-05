package examples;

import java.io.FileNotFoundException;

public class MainTester {
    public static void main(String[] args) throws FileNotFoundException {
        int[] instructions = ReadTest.read("input.txt");
        System.out.println(instructions[0] + ", " + (instructions[0] == -2147483648));
        System.out.println(instructions[1] + ", " + (instructions[1] == 2147483647));
        System.out.println(instructions[2] + ", " + (instructions[2] == -1));
        System.out.println(instructions[3] + ", " + (instructions[3] == 0));
    }
}
