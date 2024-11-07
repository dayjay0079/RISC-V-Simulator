public class ProgramCounter {
    private int pc;
    private boolean jump;
    private int jumpOffset;

    public ProgramCounter() {
        this.pc = 0;
        this.jump = false;
        this.jumpOffset = 0;
    }

    public int getPC() {
        return this.pc;
    }

    public void updatePC() {
        this.pc += this.jump ? this.jumpOffset : 4;
        this.jump = false;
        this.jumpOffset = 0;
    }

    public void jump(int offset) {
        this.jump = true;
        this.jumpOffset = offset;
    }
}
