package org.example;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class d3Object extends d2Object {
    public final BigDecimal[] z;

    public d3Object(String id, int len) {
        super(id, len);
        this.z = new BigDecimal[len];
    }

    @Override
    public void putPoints(LocalDate timestamp, BigDecimal... decimals) {
        super.noteTimeStamp(timestamp);
        x[lastIndex] = decimals[0];
        y[lastIndex] = decimals[1];
        z[lastIndex++] = decimals[2];
    }
}
