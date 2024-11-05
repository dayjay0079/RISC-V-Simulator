import java.util.Scanner;
import java.io.*;

public class Read {
    private static boolean DEBUG = true;
    public static int[] read(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner count = new Scanner(file);

        int lineCount = 0;
        while(count.hasNextLine()) {
            lineCount++;
            count.nextLine();
        }
        count.close();
        
        Scanner in = new Scanner(file);
        int[] values = new int[lineCount];

        for (int i = 0; i < lineCount; i++) {
            String line = in.nextLine();
            values[i] = (int) Long.parseLong(line, 2);
        }

        in.close();
        return values;
    }

    public static void instructionDecode(int instruction) {
        int opcode = instruction & 0x7f;
        int rd = 0;
        int rs1 = 0;
        int rs2 = 0;
        int funct3 = 0;
        int funct7 = 0;
        long imm = 0;
        switch (opcode) {
            case 0b0110011: // R-type
                rd = (instruction >> 7) & 0x01f;
                funct3 = (instruction >> 12) & 0x07;
                rs1 = (instruction >> 15) & 0x01f;
                rs2 = (instruction >> 20) & 0x01f;
                funct7 = instruction >>> 25;
                break;
            
            case 0b0010011, 0b0000011, 0b1100111, 0b1110011: // I-type
                funct3 = (instruction >> 12) & 0x07;
                rd = (instruction >> 7) & 0x01f;
                rs1 = (instruction >> 15) & 0x01f;
                imm = (long) (instruction >>> 20);
                break;
            
            case 0b0100011: // S-type
                imm = (long) (((instruction >> 7) & 0x01f) | ((instruction >> 20) & 0x0fe0));
                funct3 = (instruction >> 12) & 0x07;
                rs1 = (instruction >> 15) & 0x01f;
                rs2 = (instruction >> 20) & 0x01f;
                break;
            
            case 0b1100011: // B-type
                imm = (long) ((((instruction >> 7) & 0x01E) | ((instruction << 4) & 0x0800)) |
                            ((instruction >> 20) & 0x07e0) | ((instruction >> 19) & 0x01000));
                funct3 = (instruction >> 12) & 0x07;
                rs1 = (instruction >> 15) & 0x01f;
                rs2 = (instruction >> 20) & 0x01f;
                break;
            
            case 0b0110111, 0b0010111: // U-type
                rd = (instruction >> 7) & 0x01f;
                imm = instruction >>> 12;
                break;
            
            case 0b1101111: // J-type
                rd = (instruction >> 7) & 0x01f;
                imm = (long) (((instruction & 0x0ff000) | ((instruction >> 9) & 0x0800)) |
                             ((instruction >> 20) & 0x07fe) | ((instruction >> 11) & 0x0100000));
                break;
            
            default:
                System.out.println("Instruction: " + Integer.toBinaryString(instruction));
                System.out.println("Opcode " + Integer.toBinaryString(opcode) + " not yet implemented");
                System.exit(0);

        }
        if (DEBUG) {
            if (opcode != 0) {
                System.out.println("opC: " + Integer.toBinaryString(opcode));
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
}