package org.example;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class Renderer extends JPanel {

    private int dimensions;

    double xMin, xMax;
    double yMin, yMax;
    double zMin, zMax;

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
            System.out.println(doj);
            if (dimensions == 1) {
                int x = ((d1Object)doj).x[0].intValue();

                g2d.setColor(Color.yellow);
                g2d.fillRect(x, getHeight()/2, 40, 40);

            } else if (dimensions == 2) {
                int x = ((d2Object)doj).x[0].intValue();
                int y = ((d2Object)doj).y[0].intValue();

                g2d.setColor(Color.red);
                g2d.fillRect(x, y, 40, 40);
            }
        }

        g2d.setColor(Color.red);
        g2d.fillRect(getWidth()/2, 0, 1, getHeight());

        if (dimensions > 1) {
            g2d.setColor(Color.green);
            g2d.fillRect(0, getHeight()/2, getWidth(), 1);
        }
    }
}
