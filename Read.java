import java.util.Scanner;
import java.io.*;

public class Read {
    private static boolean DEBUG = true;

    public static int[] read(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner count = new Scanner(file);
        Scanner in =  new Scanner(file);

        // Count amount of instructions
        int lineCount = 0;
        while(count.hasNextLine()) {
            lineCount++;
            count.nextLine();
        }
        count.close();

        // Store binary instructions as integers
        int[] values = new int[lineCount];
        for (int i = 0; i < lineCount; i++) {
            String line = in.nextLine();
            values[i] = (int) Long.parseLong(line, 2);
        }

        in.close();
        return values;
    }

    public static void instructionDecode(int instruction) {
        int opcode = Instruction.getOpcode(instruction);
        int rd = 0;
        int rs1 = 0;
        int rs2 = 0;
        int funct3 = 0;
        int funct7 = 0;
        int imm = 0;
        switch (opcode) {
            case 0b0110011: // R-type
                rd = Instruction.getRd(instruction);
                funct3 = Instruction.getFunct3(instruction);
                rs1 = Instruction.getRs1(instruction);
                rs2 = Instruction.getRs2(instruction);
                funct7 = Instruction.getFunct7(instruction);
                break;
            
            case 0b0010011, 0b0000011, 0b1100111, 0b1110011: // I-type
                funct3 = Instruction.getFunct3(instruction);
                rd = Instruction.getRd(instruction);
                rs1 = Instruction.getRs1(instruction);
                imm = Instruction.getImmI(instruction);
                break;
            
            case 0b0100011: // S-type
                imm = Instruction.getImmS(instruction);
                funct3 = Instruction.getFunct3(instruction);
                rs1 = Instruction.getRs1(instruction);
                rs2 = Instruction.getRs2(instruction);
                break;
            
            case 0b1100011: // B-type
                imm = Instruction.getImmB(instruction);
                funct3 = Instruction.getFunct3(instruction);
                rs1 = Instruction.getRs1(instruction);
                rs2 = Instruction.getRs2(instruction);
                break;
            
            case 0b0110111, 0b0010111: // U-type
                rd = Instruction.getRd(instruction);
                imm = Instruction.getImmU(instruction);
                break;
            
            case 0b1101111: // J-type
                rd = Instruction.getRd(instruction);
                imm = Instruction.getImmJ(instruction);
                break;
            
            default:
                System.out.println("Error on instruction: " + Integer.toBinaryString(instruction));
                System.out.println("Opcode " + Integer.toBinaryString(opcode) + " not yet implemented");
//                System.exit(0);
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