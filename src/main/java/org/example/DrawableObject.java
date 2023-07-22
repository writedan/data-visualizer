package org.example;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class DrawableObject {
    public final String id;

    protected final LocalDate[] timestamps;
    protected int lastIndex;

    public DrawableObject(String id, int len) {
        this.id = id;
        timestamps = new LocalDate[len];
    }

    public abstract void putPoints(LocalDate timestamp, BigDecimal... decimals);

    protected void noteTimeStamp(LocalDate timestamp) {
        timestamps[lastIndex] = timestamp;
    }
}
