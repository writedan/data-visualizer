package org.example;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class d2Object extends d1Object {

    public final BigDecimal[] y;
    public d2Object(String id, int len) {
        super(id, len);
        this.y = new BigDecimal[len];
    }

    @Override
    public void putPoints(LocalDate timestamp, BigDecimal... decimals) {
        super.noteTimeStamp(timestamp);
        x[lastIndex] = decimals[0];
        y[lastIndex++] = decimals[1];
    }
}
