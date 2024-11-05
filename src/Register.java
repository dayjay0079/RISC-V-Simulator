public class Register {
    private int data;
    private int address;
    public Register(int address) {
        this.address = address;
    }

    public int get() {
        return (address != 0) ? this.data : 0;
    }

    public void set(int data) {
        this.data = (address != 0) ? data : 0;
    }
}
