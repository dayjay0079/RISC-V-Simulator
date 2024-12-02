import java.io.*;

public class IO {
    public static int[] readBin(String path) throws IOException {
        File file = new File(path);
        InputStream in = new DataInputStream(new FileInputStream(file));

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

    public static void printRegs(Control control) {
        String[] regStrings = control.getRegStrings();
        for (int i = 0; i < 32; i++) {
            System.out.println(regStrings[i]);
        }
    }

    public static void writeRes(String filename, Control control) {
        String path = "SimRes/" + filename + ".res";
        File outputFile = new File(path);
        int[] regs = control.getAllRegValues();
        try {
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            OutputStream out = new DataOutputStream(new FileOutputStream(outputFile));
            for (int i = 0; i < 32; i++) {
                out.write(regs[i] & 0xff);
                out.write((regs[i] >> 8) & 0xff);
                out.write((regs[i] >> 16) & 0xff);
                out.write((regs[i] >> 24) & 0xff);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
