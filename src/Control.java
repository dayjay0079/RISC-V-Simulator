import java.io.IOException;

public class Control {
    private static final boolean DEBUG_PRINT = false;

    private boolean execute;
    private final String[] regStrings = new String[32];

    private final Register[] regs = new Register[32];
    private final MemoryFile mem;
    private final ProgramCounter pc;

    private final boolean printInstructions;
    private String instructionString;

    public Control(int memSize, int[] program, boolean printInstructions) {
        this.execute = true;
        initRegs(memSize);
        this.mem = new MemoryFile(memSize, program);
        this.pc = new ProgramCounter();
        this.printInstructions = printInstructions;
        this.instructionString = "";
    }

    private void initRegs(int memSize) {
        for (int i = 0; i < 32; i++) {
            this.regs[i] = new Register(i);
        }
        this.regs[2].set(memSize-4); // x2 = sp
    }

    private void excArithmetic(int rd, int funct3, int rs1, int rs2, int funct7) {
        int rs1Data = this.regs[rs1].get();
        int rs2Data = this.regs[rs2].get();
        int result = 0;

        if (funct3 == 0x0 && funct7 == 0x00) {
            this.instructionString = ("ADD x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.add(rs1Data, rs2Data);
        } else if (funct3 == 0x0 && funct7 == 0x20) {
            this.instructionString = ("SUB x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sub(rs1Data, rs2Data);
        } else if (funct3 == 0x4 && funct7 == 0x00) {
            this.instructionString = ("XOR x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.xor(rs1Data, rs2Data);
        } else if (funct3 == 0x6 && funct7 == 0x00) {
            this.instructionString = ("OR x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.or(rs1Data, rs2Data);
        } else if (funct3 == 0x7 && funct7 == 0x00) {
            this.instructionString = ("AND x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.and(rs1Data, rs2Data);
        } else if (funct3 == 0x1 && funct7 == 0x00) {
            this.instructionString = ("SLL x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sll(rs1Data, rs2Data);
        } else if (funct3 == 0x5 && funct7 == 0x00) {
            this.instructionString = ("SRL x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.srl(rs1Data, rs2Data);
        } else if (funct3 == 0x5 && funct7 == 0x20) {
            this.instructionString = ("SRA x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sra(rs1Data, rs2Data);
        } else if (funct3 == 0x2 && funct7 == 0x00) {
            this.instructionString = ("SLT x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.slt(rs1Data, rs2Data);
        } else if (funct3 == 0x3 && funct7 == 0x00) {
            this.instructionString = ("SLTU x" + rd + ", x" + rs1 + ", x" + rs2);
            result = ALU.sltu(rs1Data, rs2Data);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3) +
                    " and funct7: " + Integer.toBinaryString(funct7));
        }

        this.regs[rd].set(result);
    }

    private void excArithmeticImm(int rd, int funct3, int rs1, int imm, int imm_5_11) {
        int rs1Data = this.regs[rs1].get();
        int result = 0;

        if (funct3 == 0x0) {
            this.instructionString = ("ADDI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.add(rs1Data, imm);
        } else if (funct3 == 0x4) {
            this.instructionString = ("XORI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.xor(rs1Data, imm);
        } else if (funct3 == 0x6) {
            this.instructionString = ("ORI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.or(rs1Data, imm);
        } else if (funct3 == 0x7) {
            this.instructionString = ("ANDI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.and(rs1Data, imm);
        } else if (funct3 == 0x1 && imm_5_11 == 0x00) {
            this.instructionString = ("SLLI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.sll(rs1Data, imm);
        } else if (funct3 == 0x5 && imm_5_11 == 0x00) {
            this.instructionString = ("SRLI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.srl(rs1Data, imm);
        } else if (funct3 == 0x5 && imm_5_11 == 0x20) {
            this.instructionString = ("SRAI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.sra(rs1Data, imm);
        } else if (funct3 == 0x2) {
            this.instructionString = ("SLTI x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.slt(rs1Data, imm);
        } else if (funct3 == 0x3) {
            this.instructionString = ("SLTIU x" + rd + ", x" + rs1 + ", " + imm);
            result = ALU.sltu(rs1Data, imm & 0xFFF);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3));
        }

        this.regs[rd].set(result);
    }

    private void excLoad(int rd, int funct3, int rs1, int imm) {
        int rs1Data = this.regs[rs1].get();
        int result = 0;

        if (funct3 == 0x0) {
            this.instructionString = ("LB x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadByte(rs1Data + imm);
        } else if (funct3 == 0x1) {
            this.instructionString = ("LH x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadHalfWord(rs1Data + imm);
        } else if (funct3 == 0x2) {
            this.instructionString = ("LW x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadWord(rs1Data + imm);
        } else if (funct3 == 0x4) {
            this.instructionString = ("LBU x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadUnsignedByte(rs1Data + imm);
        } else if (funct3 == 0x5) {
            this.instructionString = ("LHU x" + rd + ", " + imm + "(x" + rs1 + ")");
            result = this.mem.loadUnsignedHalfWord(rs1Data + imm);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3));
        }

        this.regs[rd].set(result);
    }

    private void excJALR(int rd, int funct3, int rs1, int imm) {
        this.instructionString = ("JALR x" + rd + ", x" + rs1 + ", " + imm);
        this.regs[rd].set(this.pc.getPC() + 4);
        int rs1Data = this.regs[rs1].get();
        this.pc.setPc(rs1Data + imm);
    }

    private void excECALL(int rd, int funct3, int rs1, int imm) {
        int rs17Data = this.regs[17].get();

        if (funct3 == 0x0 && imm == 0x0) {
            if (rs17Data == 10) {
                this.instructionString = ("ECALL: Program terminated");
                terminate();
            } else {
                System.out.println("ECALL code " + rs17Data + " not yet implemented");
            }
        } else {
            System.out.println("Not a valid ECALL immediate: " + imm);
        }
    }

    private void excStore(int funct3, int rs1, int rs2, int imm) {
        int rs1Data = this.regs[rs1].get();
        int rs2Data = this.regs[rs2].get();

        if (funct3 == 0x0) {
            this.instructionString = ("SB x" + rs2 + ", " + imm + "(x" + rs1 + ")");
            this.mem.storeByte(rs1Data + imm, rs2Data);
        } else if (funct3 == 0x1) {
            this.instructionString = ("SH x" + rs2 + ", " + imm + "(x" + rs1 + ")");
            this.mem.storeHalfWord(rs1Data + imm, rs2Data);
        } else if (funct3 == 0x2) {
            this.instructionString = ("SW x" + rs2 + ", " + imm + "(x" + rs1 + ")");
            this.mem.storeWord(rs1Data + imm, rs2Data);
        } else {
            System.out.println("Not a valid funct3: " + Integer.toBinaryString(funct3));
        }
    }

    private void excBranch(int funct3, int rs1, int rs2, int imm) {
        int rs1Data = this.regs[rs1].get();
        int rs2Data = this.regs[rs2].get();

        switch (funct3) {
            case 0x0:
                this.instructionString = ("BEQ x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data == rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x1:
                this.instructionString = ("BNE x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data != rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x4:
                this.instructionString = ("BLT x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data < rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x5:
                this.instructionString = ("BGE x" + rs1 + ", x" + rs2 + ", " + imm);
                if (rs1Data >= rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x6:
                this.instructionString = ("BLTU x" + rs1 + ", x" + rs2 + ", " + imm);
                if (Integer.toUnsignedLong(rs1Data) < rs2Data) {
                    this.pc.jump(imm);
                }
                break;

            case 0x7:
                this.instructionString = ("BGEU x" + rs1 + ", x" + rs2 + ", " + imm);
                if (Integer.toUnsignedLong(rs1Data) >= rs2Data) {
                    this.pc.jump(imm);
                }
                break;
        }
    }

    private void excLUI(int rd, int upperImm) { // LUI
        this.instructionString = ("LUI x" + rd + ", " + upperImm);
        this.regs[rd].set(upperImm);
    }

    private void excAUIPC(int rd, int upperImm) { // AUIPC
        this.instructionString = ("AUIPC x" + rd + ", " + upperImm);
        int pc = this.pc.getPC();
        this.regs[rd].set(pc + upperImm);
    }

    private void excJAL(int rd, int imm) {
        this.instructionString = ("JAL x" + rd + ", " + imm);
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

                excArithmetic(rd, funct3, rs1, rs2, funct7);

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
                        excArithmeticImm(rd, funct3, rs1, imm, imm_5_11);
                        break;

                    case 0b0000011: // I-type 2 (Load operations)
                        excLoad(rd, funct3, rs1, imm);
                        break;

                    case 0b1100111: // I-type 3 (JALR)
                        excJALR(rd, funct3, rs1, imm);
                        break;

                    case 0b1110011: // I-type 4 (ECALL)
                        excECALL(rd, funct3, rs1, imm);
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

                excStore(funct3, rs1, rs2, imm);
                break;

            case 0b1100011: // B-type
                funct3 = Read.getFunct3(instruction);
                rs1 = Read.getRs1(instruction);
                rs2 = Read.getRs2(instruction);
                imm = Read.getImmB(instruction);

                excBranch(funct3, rs1, rs2, imm);
                break;

            case 0b0110111, 0b0010111: // U-type
                rd = Read.getRd(instruction);
                imm = Read.getImmU(instruction);

                switch (opcode) {
                    case 0b0110111: // LUI
                        excLUI(rd, imm);
                        break;

                    case 0b0010111: // AUIPC
                        excAUIPC(rd, imm);
                        break;

                    default:
                        System.out.println("Opcode " + Integer.toBinaryString(opcode) + " invalid");
                }

                break;
                
            case 0b1101111: // J-type
                rd = Read.getRd(instruction);
                imm = Read.getImmJ(instruction);

                excJAL(rd, imm);

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

            if (DEBUG_PRINT) {
                String instructionString = Integer.toBinaryString(instruction);
                instructionString = "0".repeat(32 - instructionString.length()) + instructionString;
                System.out.println("\nInstruction address " + this.pc.getPC() + ": " + instructionString);
            }

            executeInstruction(instruction);

            if (this.printInstructions) {
                System.out.println(this.instructionString);
            }

            if (!this.execute) {
                break;
            }

            this.pc.updatePC();
        }
    }

    private void terminate() {
        this.execute = false;
        saveCurrentRegisters();
    }

    private void resetRegStrings() {
        for (int i = 0; i < 32; i++) {
            this.regStrings[i] = "x" + i + (i < 10 ? "  = 0x" : " = 0x");
        }
    }

    public int[] getAllRegValues() {
        int[] regValues = new int[32];
        for (int i = 0; i < 32; i++) {
            regValues[i] = this.regs[i].get();
        }
        return regValues;
    }

    private void saveCurrentRegisters() {
        resetRegStrings();
        for (int i = 0; i < 32; i++) {
            String outputHex = Integer.toHexString(this.regs[i].get());
            this.regStrings[i] += "0".repeat(8 - outputHex.length()) + outputHex;
        }
    }

    public void printRegisters() {
        for (int i = 0; i < 32; i++) {
            System.out.println(this.regStrings[i]);
        }
    }

    public static void main(String[] args) throws IOException {
        int[] program = Read.readBin("tests/task3/recursive.bin");

        Control control = new Control((int)Math.pow(2, 20), program, true);

        control.executeProgram();
    }
}
