package edu.wpi.first.wpilibj.networking;

/**
 * This class represents a confirmation object
 * @author Joe Grinstead
 */
class Denial implements Data {

    static final Denial ONE = new Denial(1);
    static final Denial[] PRESETS = {ONE, new Denial(2), new Denial(3), new Denial(4)};

    private int count;

    private Denial(int count) {
        this.count = count;
    }

    static Denial combine(Denial a, Denial b) {
        int total = a.count + b.count;
        return total < PRESETS.length ? PRESETS[total - 1] : new Denial(total);
    }

    public void encode(Buffer buffer) {
        for (int i = count; i > 0; i -= Data.DENIAL - 1) {
            buffer.writeByte(Data.DENIAL | Math.min(Data.DENIAL - 1, i));
        }
    }

    @Override
    public String toString() {
        return "[Denial:" + count + "]";
    }
}
