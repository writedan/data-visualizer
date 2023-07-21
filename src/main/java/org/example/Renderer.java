package org.example;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class Renderer extends JPanel {

    private int dimensions;
    public Renderer(int dimensions) {
        this.dimensions = dimensions;
    }

    Collection<DrawableObject> objects = new ArrayList<>();

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (DrawableObject doj : objects) {
            if (dimensions == 1) {
                int x = ((d1Object)doj).x[0].intValue();

                g2d.setColor(Color.yellow);
                g2d.fillRect(x, getHeight()/2, 20, 20);

            } else if (dimensions == 2) {
                int x = ((d2Object)doj).x[0].intValue();
                int y = ((d2Object)doj).y[0].intValue();

                g2d.setColor(Color.red);
                g2d.fillRect(x, y, 20, 20);
            } else {
                int x = ((d3Object)doj).x[0].intValue();
                int y = ((d3Object)doj).y[0].intValue();
                int z = ((d3Object)doj).z[0].intValue();
            }
        }
    }
}
