package examples;
public class InstructionTest {
    public static void main(String[] args) {
        //long R = 0b10000100001001000010000000 + 0b0110011;
        //long I1 = 0b00000100001001000010000000 + 0b0010011;
        //long I2 = 0b00000100001001000010000000 + 0b0000011;
        //long I3 = 0b00000100001001000010000000 + 0b1100111;
        //long I4 = 0b00000100001001000010000000 + 0b1110011;

        //long S = 0b10000100001001000010000000 + 0b0100011;
        //long B = 0b11111110000000000000111110000000 + 0b1100011;

        //long U1 = 0b00000000000001000010000000 + 0b0110111;
        //long U2 = 0b00000000000001000010000000 + 0b0010111;

        long J = 0b11111111111111111111000000000000 + 0b1101111;
        InstructionDecode(J);
    }

    public static void InstructionDecode(long instruction) {
        int opcode = (int) (instruction & 0x7f);
        int rd = 0;
        int rs1 = 0;
        int rs2 = 0;
        int funct3 = 0;
        int funct7 = 0;
        long imm = 0;
        switch (opcode) {
            case 0b0110011: // R-type
                rd = (int) ((instruction >> 7) & 0x01f);
                funct3 = (int) ((instruction >> 12) & 0x07);    
                rs1 = (int) ((instruction >> 15) & 0x01f);
                rs2 = (int) ((instruction >> 20) & 0x01f);
                funct7 = (int) (instruction >>> 25);
                break;
            
            case 0b0010011, 0b0000011, 0b1100111, 0b1110011: // I-type
                System.out.println("I-type:");
                funct3 = (int) ((instruction >> 12) & 0x07);
                rd = (int) ((instruction >> 7) & 0x01f);
                rs1 = (int) ((instruction >> 15) & 0x01f);
                imm = (long) (instruction >>> 20);
                break;
            
            case 0b0100011: // S-type
                imm = (long) (((instruction >> 7) & 0x01f) | ((instruction >> 20) & 0x0fe0));
                funct3 = (int) ((instruction >> 12) & 0x07);
                rs1 = (int) ((instruction >> 15) & 0x01f);
                rs2 = (int) ((instruction >> 20) & 0x01f);
                break;
            
            case 0b1100011: // B-type
                imm = (long) ((((instruction >> 7) & 0x01E) | ((instruction << 4) & 0x0800)) |
                             ((instruction >> 20) & 0x07e0) | ((instruction >> 19) & 0x01000));
                funct3 = (int) ((instruction >> 12) & 0x07);
                rs1 = (int) ((instruction >> 15) & 0x01f);
                rs2 = (int) ((instruction >> 20) & 0x01f);
                break;
            
            case 0b0110111, 0b0010111: // U-type
                rd = (int) ((instruction >> 7) & 0x01f);
                imm = (long) (instruction >>> 12);
                break;
            
            case 0b1101111: // J-type
                rd = (int) ((instruction >> 7) & 0x01f);
                imm = (long) (((instruction & 0x0ff000) | ((instruction >> 9) & 0x0800)) |
                            ((instruction >> 20) & 0x07fe) | ((instruction >> 11) & 0x0100000));
                break;
            
            default:
                System.out.println("Opcode " + opcode + " not yet implemented");
                System.exit(0);

        }
        
        if (opcode != 0) {
            System.out.println("Opcode: " + Integer.toBinaryString(opcode));
        }
        if (rd != 0) {
            System.out.println("rd: " + Integer.toBinaryString(rd));
        }
        if (rs1 != 0) {
            System.out.println("rs1: " + Integer.toBinaryString(rs1));
        }
        if (rs2 != 0) {
            System.out.println("rs2: " + Integer.toBinaryString(rs2));
        }
        if (funct3 != 0) {
            System.out.println("funct3: " + Integer.toBinaryString(funct3));
        }
        if (funct7 != 0) {
            System.out.println("funct7: " + Integer.toBinaryString(funct7));
        }
        if (imm != 0) {
            System.out.println("imm: " + Long.toBinaryString(imm));
        }
    }
}
