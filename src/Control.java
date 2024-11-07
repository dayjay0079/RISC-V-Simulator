import java.io.IOException;

public class Control {
    private static Register[] regs;

    public static void initRegs() {
        regs = new Register[32];
        for (int i = 0; i < 32; i++) {
            regs[i] = new Register(i);
        }
    }

    private static void executeR(int rd, int funct3, int rs1, int rs2, int funct7) {
//        System.out.println("R-type operation");

        int rs1Data = regs[rs1].get();
        int rs2Data = regs[rs2].get();
        int result = 0;

        if (funct3 == 0x0 && funct7 == 0x00) {
            System.out.println("ADD x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.add(rs1Data, rs2Data);
        } else if (funct3 == 0x0 && funct7 == 0x20) {
            System.out.println("SUB x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sub(rs1Data, rs2Data);
        } else if (funct3 == 0x4 && funct7 == 0x00) {
            System.out.println("XOR x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.xor(rs1Data, rs2Data);
        } else if (funct3 == 0x6 && funct7 == 0x00) {
            System.out.println("OR x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.or(rs1Data, rs2Data);
        } else if (funct3 == 0x7 && funct7 == 0x00) {
            System.out.println("AND x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.and(rs1Data, rs2Data);
        } else if (funct3 == 0x1 && funct7 == 0x00) {
            System.out.println("SLL x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sll(rs1Data, rs2Data);
        } else if (funct3 == 0x5 && funct7 == 0x00) {
            System.out.println("SRL x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.srl(rs1Data, rs2Data);
        } else if (funct3 == 0x5 && funct7 == 0x20) {
            System.out.println("SRA x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sra(rs1Data, rs2Data);
        } else if (funct3 == 0x2 && funct7 == 0x00) {
            System.out.println("SLT x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.slt(rs1Data, rs2Data);
        } else if (funct3 == 0x3 && funct7 == 0x00) {
            System.out.println("SLTU x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sltu(rs1Data, rs2Data);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3) +
                    " and funct7: " + Integer.toBinaryString(funct7));
//            System.exit(0);
        }

        regs[rd].set(result);
    }

    private static void executeI1(int rd, int funct3, int rs1, int imm, int imm_5_11) {

        int rs1Data = regs[rs1].get();
        int result = 0;

        if (funct3 == 0x0) {
            System.out.println("ADDI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.add(rs1Data, imm);
        } else if (funct3 == 0x4) {
            System.out.println("XORI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.xor(rs1Data, imm);
        } else if (funct3 == 0x6) {
            System.out.println("ORI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.or(rs1Data, imm);
        } else if (funct3 == 0x7) {
            System.out.println("ANDI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.and(rs1Data, imm);
        } else if (funct3 == 0x1 && imm_5_11 == 0x00) {
            System.out.println("SLLI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.sll(rs1Data, imm);
        } else if (funct3 == 0x5 && imm_5_11 == 0x00) {
            System.out.println("SRLI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.srl(rs1Data, imm);
        } else if (funct3 == 0x5 && imm_5_11 == 0x20) {
            System.out.println("SRAI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.sra(rs1Data, imm);
        } else if (funct3 == 0x2) {
            System.out.println("SLTI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.slt(rs1Data, imm);
        } else if (funct3 == 0x3) {
            System.out.println("SLTIU x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.sltu(rs1Data, imm);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3));
//            System.exit(0);
        }

        regs[rd].set(result);
    }

    private static void executeI2(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        System.out.println("Load operations not yet implemented");
//        System.exit(1);
    }

    private static void executeI3(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        System.out.println("JALR not yet implemented");
//        System.exit(1);
    }

    private static void executeI4(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        System.out.println("ECALL not yet implemented");
//        System.exit(1);
    }

    private static void executeS(int funct3, int rs1, int rs2, int imm) {
        System.out.println("Store operations not yet implemented");
//        System.exit(1);
    }

    private static void executeB(int funct3, int rs1, int rs2, int imm) {
        System.out.println("Branch operations not yet implemented");
//        System.exit(1);
    }

    private static void executeU(int rd, int imm) {
        System.out.println("LUI and AUIPC not yet implemented");
//        System.exit(1);
    }

    private static void executeJ(int rd, int imm) {
        System.out.println("JAL not yet implemented");
//        System.exit(1);
    }

    public static void executeInstruction(int instruction) {
        int opcode = Read.getOpcode(instruction);
        int rd = 0;
        int funct3 = 0;
        int rs1 = 0;
        int rs2 = 0;
        int funct7 = 0;
        int imm = 0;

        switch (opcode) {
            case 0b0110011: // R-type (ALU-Register operations)
                rd = Read.getRd(instruction);
                funct3 = Read.getFunct3(instruction);
                rs1 = Read.getRs1(instruction);
                rs2 = Read.getRs2(instruction);
                funct7 = Read.getFunct7(instruction);

                executeR(rd, funct3, rs1, rs2, funct7);

                break;

            case 0b0010011, 0b0000011, 0b1100111, 0b1110011: // I-type
                rd = Read.getRd(instruction);
                funct3 = Read.getFunct3(instruction);
                rs1 = Read.getRs1(instruction);
                imm = Read.getImmI(instruction);
                int imm_5_11 = imm >>> 5;

                switch (opcode) {
                    case 0b0010011: // I-type 1 (ALU-Immediate operations)
                        executeI1(rd, funct3, rs1, imm, imm_5_11);
                        break;

                    case 0b0000011: // I-type 2 (Load operations)
                        executeI2(rd, funct3, rs1, imm, imm_5_11);
                        break;

                    case 0b1100111: // I-type 3 (JALR)
                        executeI3(rd, funct3, rs1, imm, imm_5_11);
                        break;

                    case 0b1110011: // I-type 4 (ECALL)
                        executeI4(rd, funct3, rs1, imm, imm_5_11);
                        break;

                    default:
                        System.out.println("Opcode " + Integer.toBinaryString(opcode) + " invalid");
//                        System.exit(1);
                }


                break;

            case 0b0100011: // S-type
                funct3 = Read.getFunct3(instruction);
                rs1 = Read.getRs1(instruction);
                rs2 = Read.getRs2(instruction);
                imm = Read.getImmS(instruction);

                executeS(funct3, rs1, rs2, imm);

                break;

            case 0b1100011: // B-type
                funct3 = Read.getFunct3(instruction);
                rs1 = Read.getRs1(instruction);
                rs2 = Read.getRs2(instruction);
                imm = Read.getImmB(instruction);

                executeB(funct3, rs1, rs2, imm);

                break;

            case 0b0110111: // U-type
                rd = Read.getRd(instruction);
                imm = Read.getImmU(instruction);

                executeU(rd, imm);

                break;
                
            case 0b1101111: // J-type
                rd = Read.getRd(instruction);
                imm = Read.getImmJ(instruction);

                executeJ(rd, imm);

                break;
                
            default:
                String opcodeString = Integer.toBinaryString(opcode);
                opcodeString = "0".repeat(7 - opcodeString.length()) + opcodeString;
                System.out.println("Opcode " + opcodeString + " invalid");
//                System.exit(1);
        }
    }

    private static final boolean PRINT_INSTRUCTIONS = false;
    public static void main(String[] args) throws IOException {
        initRegs();

        // The problem is that in .bin files don't save the instructions as lines, but just as one long sequence of bits.
        // We need to read the file slightly differently.
        int[] program = Read.readBin("tests/task1/addneg.bin");

        System.out.println("Program length: " + program.length);
        for (int i = 0; i < program.length; i++) {
            int instruction = program[i];

            if (PRINT_INSTRUCTIONS) {
                String instructionString = Integer.toBinaryString(instruction);
                instructionString = "0".repeat(32 - instructionString.length()) + instructionString;
                System.out.println("\nInstruction " + i + ": " + instructionString);
            }

            executeInstruction(instruction);
        }
    }
}
