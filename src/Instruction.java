public class Instruction {
    public static int getOpcode(int instruction) {
        return instruction & 0x7f;
    }

    public static int getRd(int instruction) {
        return (instruction >> 7) & 0x01f;
    }

    public static int getFunct3(int instruction) {
        return (instruction >> 12) & 0x07;
    }

    public static int getRs1(int instruction) {
        return (instruction >> 15) & 0x01f;
    }

    public static int getRs2(int instruction) {
        return (instruction >> 20) & 0x01f;
    }

    public static int getFunct7(int instruction) {
        return (instruction >> 25) & 0x07f;
    }

    public static int getImmI(int instruction) {
        int imm = instruction >>> 20;
        return imm >= 0x800 ? imm - 0x1000 : imm;
    }

    public static int getImmS(int instruction) {
        int imm = ((instruction >> 7) & 0x01f) | ((instruction >> 20) & 0x0fe0);
        return imm >= 0x800 ? imm - 0x1000 : imm;
    }

    public static int getImmB(int instruction) {
        int imm = (((instruction >> 7) & 0x01E) | ((instruction << 4) & 0x0800)) |
                ((instruction >> 20) & 0x07e0) | ((instruction >> 19) & 0x01000);
        return imm >= 0x1000 ? imm - 0x2000 : imm;
    }

    public static int getImmU(int instruction) {
        return instruction & 0xfffff000;
    }

    public static int getImmJ(int instruction) {
        int imm = ((instruction & 0x0ff000) | ((instruction >> 9) & 0x0800)) |
                ((instruction >> 20) & 0x07fe) | ((instruction >> 11) & 0x0100000);
        return imm >= 0x100000 ? imm - 0x200000 : imm;
    }
}
