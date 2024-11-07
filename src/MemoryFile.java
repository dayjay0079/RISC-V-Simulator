public class MemoryFile {
    private int[] data;

    public MemoryFile(int memSize, int[] program) {
        this.data = new int[memSize];
        for (int i = 0; i < program.length; i++) {
            this.data[i] = program[i];
        }
    }
    
    public int loadWord(int address) {
        // Load a word from memory in one location
        if (address % 4 == 0) {
            return this.data[address / 4];
        }

        // Load a word from memory in two locations if the address is not aligned
        int highWord = this.data[address / 4];
        int highBits = 32 - 8 * (address % 4);
        highWord = highWord & (0xFFFFFFFF >>> highBits);
        highWord = highWord << highBits;
        
        int lowWord = this.data[address / 4 + 1];
        int lowBits = 32 - highBits;
        lowWord = lowWord & (0xFFFFFFFF << lowBits);
        lowWord = lowWord >>> lowBits;
        
        return highWord | lowWord;
    }
    
    public void storeWord(int address, int value) {
        if (address % 4 == 0) {
            this.data[address / 4] = value;
        } else {
            int highWord = this.data[address / 4];
            int highBits = 8 * (address % 4);
            highWord = highWord & (0xFFFFFFFF << highBits);

            int lowWord = this.data[address / 4 + 1];
            int lowBits = 32 - highBits;
            lowWord = lowWord & (0xFFFFFFFF >>> lowBits);

            this.data[address / 4] = highWord | (value >>> lowBits);
            this.data[address / 4 + 1] = lowWord | (value << highBits);
        }
    }
}
