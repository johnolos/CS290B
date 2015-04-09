package com.homework1;

import java.io.Serializable;

public class Tuple implements Serializable {
    public final int x, y;

    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
