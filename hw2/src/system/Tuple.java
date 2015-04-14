package system;

import java.io.Serializable;

/**
 * Data structure for a tuple in Java.
 */
public class Tuple implements Serializable {
    public final int x, y;

    /**
     * Tuple
     * @param x X value
     * @param y Y value
     */
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * toString()
     * @return String with contents of the tuple.
     */
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
