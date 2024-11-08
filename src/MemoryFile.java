public class MemoryFile {
    private final byte[] data;

    public MemoryFile(int memSize, int[] program) {
        this.data = new byte[memSize];
        for (int i = 0; i < program.length; i++) {
            this.data[4*i] = (byte)((program[i] & 0xFF000000) >>> 24);
            this.data[4*i+1] = (byte)((program[i] & 0xFF0000) >>> 16);
            this.data[4*i+2] = (byte)((program[i] & 0xFF00) >>> 8);
            this.data[4*i+3] = (byte)(program[i] & 0xFF);
        }
    }

    public int loadByte(int address) {
        return this.data[address];
    }

    public int loadHalfWord(int address) {
        return loadByte(address) << 8 | (loadByte(address + 1) & 0xFF);
    }

    public int loadWord(int address) {
        return loadHalfWord(address) << 16 | (loadHalfWord(address + 2) & 0xFFFF);
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
        storeByte(address, value >>> 8);
        storeByte(address + 1, value);
    }
    public void storeWord(int address, int value) {
        storeHalfWord(address, value >>> 16);
        storeHalfWord(address + 2, value);
    }

}
