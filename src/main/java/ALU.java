public class ALU {
    // Add
    public static int add(int a, int b) {
        return a + b;
    }

    // Subtract
    public static int sub(int a, int b) {
        return a - b;
    }

    // XOR
    public static int xor(int a, int b) {
        return a ^ b;
    }

    // OR
    public static int or(int a, int b) {
        return a | b;
    }

    // AND
    public static int and(int a, int b) {
        return a & b;
    }

    // Shift Left Logical
    public static int sll(int a, int b) {
        return a << b;
    }

    // Shift Right Logical (Zero-extend)
    public static int srl(int a, int b) {
        return a >>> b;
    }

    // Shift Right Arithmetic (Sign-extend)
    public static int sra(int a, int b) {
        return a >> b;
    }

    // Set Less Than
    public static int slt(int a, int b) {
        return (a < b) ? 1 : 0;
    }

    // Set Less Than (Unsigned)
    public static int sltu(int a, int b) {
        return (Integer.toUnsignedLong(a) < b) ? 1 : 0;
    }

    public static void main(String[] args) {
        System.out.println(Integer.toUnsignedString(-1)); //Test
    }

}
