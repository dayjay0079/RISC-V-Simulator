public class MemoryFile {
    private final byte[] data;

    public MemoryFile(int memSize, int[] program) {
        this.data = new byte[memSize];
        for (int i = 0; i < program.length; i++) {
            this.data[4*i+0] = (byte)((program[i] & 0xFF000000) >>> 24);
            this.data[4*i+1] = (byte)((program[i] & 0x00FF0000) >>> 16);
            this.data[4*i+2] = (byte)((program[i] & 0x0000FF00) >>> 8);
            this.data[4*i+3] = (byte)((program[i] & 0x000000FF) >>> 0);
        }
    }

    public int loadByte(int address) {
        int byteAddress = (address & 0xFFFFFFFC) + (3 - (address % 4));
        return this.data[byteAddress];
    }

    public int loadHalfWord(int address) {
        return (this.data[address] << 8) | (this.data[address + 1] & 0xFF);
    }

    public int loadWord(int address) {
        return (loadHalfWord(address) << 16) | (loadHalfWord(address + 2) & 0xFFFF);
    }

    public int loadUnsignedByte(int address) {
        return loadByte(address) & 0xFF;
    }

    public int loadUnsignedHalfWord(int address) {
        return loadHalfWord(address) & 0xFFFF;
    }


    public void storeByte(int address, int value) {
        int byteAddress = (byte) ((address & 0xFFFFFFFC) + (3 - (address % 4)));
        this.data[byteAddress] = (byte) value;
    }

    public void storeHalfWord(int address, int value) {
        this.data[address] = (byte) ((value >> 8) & 0xFF);
        this.data[address + 1] = (byte) (value & 0xFF);
    }
    public void storeWord(int address, int value) {
        storeHalfWord(address, value >>> 16);
        storeHalfWord(address + 2, value & 0xFFFF);
    }
}
