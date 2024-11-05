public class Register {
    private int data;
    private int address;
    public Register(int address) {
        this.address = address;
    }

    public int get() {
        return data;
    }

    public void set(int data) {
        if (address!=0) {
            this.data = data;
        } else {
            this.data = 0;
        }
    }
}
