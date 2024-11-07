import java.io.IOException;

public class Control {
    private final boolean PRINT_INSTRUCTIONS = false;

    private Register[] regs;
    private MemoryFile mem;
    private ProgramCounter pc;

    public Control(int memSize, int[] program) {
        initRegs();
        this.regs[2].set(memSize-4); // x2 = sp
        this.mem = new MemoryFile(memSize, program);
        this.pc = new ProgramCounter();
    }

    private void initRegs() {
        this.regs = new Register[32];
        for (int i = 0; i < 32; i++) {
            this.regs[i] = new Register(i);
        }

    }

    private void executeR(int rd, int funct3, int rs1, int rs2, int funct7) {
        int rs1Data = this.regs[rs1].get();
        int rs2Data = this.regs[rs2].get();
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
        }

        this.regs[rd].set(result);
    }

    private void executeI1(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        int rs1Data = this.regs[rs1].get();
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
            result = ALU.sltu(rs1Data, imm & 0xFFF);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3));
        }

        this.regs[rd].set(result);
    }

    private void executeI2(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        int rs1Data = this.regs[rs1].get();
        int result = 0;

        if (funct3 == 0x0) {
            System.out.println("LB x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadByte(rs1Data + imm);
        } else if (funct3 == 0x1) {
            System.out.println("LH x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadHalfWord(rs1Data + imm);
        } else if (funct3 == 0x2) {
            System.out.println("LW x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadWord(rs1Data + imm);
        } else if (funct3 == 0x4) {
            System.out.println("LBU x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadUnsignedByte(rs1Data + imm);
        } else if (funct3 == 0x5) {
            System.out.println("LHU x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadUnsignedHalfWord(rs1Data + imm);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3));
        }

        this.regs[rd].set(result);
    }

    private void executeI3(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        System.out.println("JALR x" + rd + ", x" + rs1 + ", " + imm);
        this.regs[rd].set(this.pc.getPC() + 4);
        int rs1Data = this.regs[rs1].get();
        this.pc.setPc(rs1Data + imm);
    }

    private void executeI4(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        int rs17Data = this.regs[17].get();

        if (funct3 == 0x0 && imm == 0x0) {
            System.out.print("ECALL: ");
            if (rs17Data == 10) {
                System.out.println("Program terminated");
                terminate();
            } else {
                System.out.println("ECALL code " + rs17Data + " not yet implemented");
            }
        } else {
            System.out.println("Not a valid ECALL immediate: " + imm);
        }
    }

    private void executeS(int funct3, int rs1, int rs2, int imm) {
        int rs1Data = this.regs[rs1].get();
        int rs2Data = this.regs[rs2].get();

        if (funct3 == 0x0) {
            System.out.println("SB x" + rs2 + ", " + imm + "(x" + rs1 + ")");
            this.mem.storeByte(rs1Data + imm, rs2Data);
        } else if (funct3 == 0x1) {
            System.out.println("SH x" + rs2 + ", " + imm + "(x" + rs1 + ")");
            this.mem.storeHalfWord(rs1Data + imm, rs2Data);
        } else if (funct3 == 0x2) {
            System.out.println("SW x" + rs2 + ", " + imm + "(x" + rs1 + ")");
            this.mem.storeWord(rs1Data + imm, rs2Data);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3));
        }
    }

    private void executeB(int funct3, int rs1, int rs2, int imm) {
        int rs1Data = this.regs[rs1].get();
        int rs2Data = this.regs[rs2].get();

        switch (funct3) {
            case 0x0:
                System.out.println("BEQ x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data == rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x1:
                System.out.println("BNE x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data != rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x4:
                System.out.println("BLT x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data < rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x5:
                System.out.println("BGE x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data >= rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x6:
                System.out.println("BLTU x" + rs1 + ", x" + rs2 + ", " + imm);
                if (Integer.toUnsignedLong(rs1Data) < rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x7:
                System.out.println("BGEU x" + rs1 + ", x" + rs2 + ", " + imm);
                if (Integer.toUnsignedLong(rs1Data) >= rs2Data) {
                    this.pc.jump(imm);
                }
                break;
        }
    }

    private void executeU1(int rd, int upperImm) { // LUI
        System.out.println("LUI x" + rd + ", " + upperImm);
        this.regs[rd].set(upperImm);
    }

    private void executeU2(int rd, int upperImm) { // AUIPC
        System.out.println("AUIPC x" + rd + ", " + upperImm);
        int pc = this.pc.getPC();
        this.regs[rd].set(pc + upperImm);
    }

    private void executeJ(int rd, int imm) {
        System.out.println("JAL x" + rd + ", " + imm);
        this.regs[rd].set(this.pc.getPC() + 4);
        this.pc.jump(imm);
    }

    private void executeInstruction(int instruction) {
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
                if (imm >= 0x800) {
                    imm -= 0x1000;
                }
                int imm_5_11 = (imm >> 5) & 0x7F;

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

            case 0b0110111, 0b0010111: // U-type
                rd = Read.getRd(instruction);
                imm = Read.getImmU(instruction);

                switch (opcode) {
                    case 0b0110111: // LUI
                        executeU1(rd, imm);
                        break;

                    case 0b0010111: // AUIPC
                        executeU2(rd, imm);
                        break;

                    default:
                        System.out.println("Opcode " + Integer.toBinaryString(opcode) + " invalid");
                }

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
        }
    }

    public void executeProgram() {
        while (true) {
            int instruction = this.mem.loadWord(this.pc.getPC());

            if (PRINT_INSTRUCTIONS) {
                String instructionString = Integer.toBinaryString(instruction);
                instructionString = "0".repeat(32 - instructionString.length()) + instructionString;
                System.out.println("\nInstruction address " + this.pc.getPC() + ": " + instructionString);
            }

            executeInstruction(instruction);

            this.pc.updatePC();
        }
    }

    private void terminate() {
        System.out.println("\nFinal register values:");
        for (int i = 0; i < 32; i++) {
            System.out.println("x" + i + ": " + this.regs[i].get());
        }
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        int[] program = Read.readBin("tests/task3/recursive.bin");

        Control control = new Control(1_048_576, program);

        control.executeProgram();
    }
}
