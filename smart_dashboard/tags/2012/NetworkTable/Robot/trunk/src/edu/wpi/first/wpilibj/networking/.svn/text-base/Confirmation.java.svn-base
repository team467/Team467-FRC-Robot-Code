package edu.wpi.first.wpilibj.networking;

/**
 * This class represents a confirmation object
 * @author Joe Grinstead
 */
class Confirmation implements Data {

    static final Confirmation ONE = new Confirmation(1);
    static final Confirmation[] PRESETS = {ONE, new Confirmation(2), new Confirmation(3), new Confirmation(4)};

    private int count;

    private Confirmation(int count) {
        this.count = count;
    }

    static Confirmation combine(Confirmation a, Confirmation b) {
        int total = a.count + b.count;
        return total < PRESETS.length ? PRESETS[total - 1] : new Confirmation(total);
    }

    public void encode(Buffer buffer) {
        for (int i = count; i > 0; i -= Data.CONFIRMATION - 1) {
            buffer.writeByte(Data.CONFIRMATION | Math.min(Data.CONFIRMATION - 1, i));
        }
    }

    public String toString() {
        return "[Confirmation:" + count + "]";
    }
}
