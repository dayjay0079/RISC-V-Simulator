import java.util.Scanner;
import java.io.*;

public class Read {
    private static boolean DEBUG = true;
    public static int[] read(String path) {
        File file = new File(path);
        Scanner count = null;
        Scanner in = null;
        try {
            count = new Scanner(file);
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Printing stack trace:");
            e.printStackTrace();
            System.exit(1);
        }

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

    public static int[] readBin(String path) throws IOException {
        File file = new File(path);
        InputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Printing stack trace:");
            e.printStackTrace();
            System.exit(1);
        }

        // Store file in byte array
        byte[] program =  in.readAllBytes();
        in.close();

        // Store binary instructions as integers
        int instructionCount = program.length / 4;
        int[] instructions = new int[instructionCount];
        for (int i = 0; i < instructionCount; i++) {
            int instruction = 0;
            for (int j = 0; j < 4; j++) {
                instruction |= ((int)program[i*4 + j] & 0xff) << (j*8);
            }
            instructions[i] = instruction;
        }

        return instructions;
    }

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

    public static void instructionDecode(int instruction) {
        int opcode = getOpcode(instruction);
        int rd = 0;
        int rs1 = 0;
        int rs2 = 0;
        int funct3 = 0;
        int funct7 = 0;
        int imm = 0;
        switch (opcode) {
            case 0b0110011: // R-type
                rd = getRd(instruction);
                funct3 = getFunct3(instruction);
                rs1 = getRs1(instruction);
                rs2 = getRs2(instruction);
                funct7 = getFunct7(instruction);
                break;
            
            case 0b0010011, 0b0000011, 0b1100111, 0b1110011: // I-type
                funct3 = getFunct3(instruction);
                rd = getRd(instruction);
                rs1 = getRs1(instruction);
                imm = getImmI(instruction);
                break;
            
            case 0b0100011: // S-type
                imm = getImmS(instruction);
                funct3 = getFunct3(instruction);
                rs1 = getRs1(instruction);
                rs2 = getRs2(instruction);
                break;
            
            case 0b1100011: // B-type
                imm = getImmB(instruction);
                funct3 = getFunct3(instruction);
                rs1 = getRs1(instruction);
                rs2 = getRs2(instruction);
                break;
            
            case 0b0110111, 0b0010111: // U-type
                rd = getRd(instruction);
                imm = getImmU(instruction);
                break;
            
            case 0b1101111: // J-type
                rd = getRd(instruction);
                imm = getImmJ(instruction);
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