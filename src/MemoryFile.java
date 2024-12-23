public class MemoryFile {
    private final byte[] data;

    public MemoryFile(int memSize, int[] program) {
        this.data = new byte[memSize];
        for (int i = 0; i < program.length; i++) {
            this.data[4*i+0] = (byte)((program[i] & 0x000000FF) >>> 0);
            this.data[4*i+1] = (byte)((program[i] & 0x0000FF00) >>> 8);
            this.data[4*i+2] = (byte)((program[i] & 0x00FF0000) >>> 16);
            this.data[4*i+3] = (byte)((program[i] & 0xFF000000) >>> 24);
        }
    }

    public int loadByte(int address) {
        return this.data[address];
    }

    public int loadHalfWord(int address) {
        return loadByte(address+1) << 8 | (loadByte(address) & 0xFF);
    }

    public int loadWord(int address) {
        return loadHalfWord(address+2) << 16 | (loadHalfWord(address) & 0xFFFF);
    }

    public int loadUnsignedByte(int address) {
        return loadByte(address) & 0xFF;
    }

    public int loadUnsignedHalfWord(int address) {
        return loadHalfWord(address) & 0xFFFF;
    }


    public void storeByte(int address, int value) {
        this.data[address] = (byte) value;
    }

    public void storeHalfWord(int address, int value) {
        storeByte(address, value);
        storeByte(address+1, value >>> 8);
    }
    public void storeWord(int address, int value) {
        storeHalfWord(address, value);
        storeHalfWord(address+2, value >>> 16);
    }
}
