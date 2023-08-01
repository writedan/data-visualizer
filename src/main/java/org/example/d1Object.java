package org.example;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class d1Object extends DrawableObject {

    public final BigDecimal[] x;
    public d1Object(String id, int len) {
        super(id, len);
        this.x = new BigDecimal[len];
    }

    @Override
    public void putPoints(LocalDate timestamp, BigDecimal... decimals) {
        super.noteTimeStamp(timestamp);
        x[lastIndex++] = decimals[0];
    }

    public String toString() {
        String res = "(";
        for (int i = 0; i < timestamps.length; i++) {
            res = res + String.valueOf(x[i]) + " @ " + timestamps[i] + ", ";
        }
        return res.substring(0, res.lastIndexOf(',')) + ")";
    }
}
