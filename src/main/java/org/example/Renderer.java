package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.FileInputStream;
import java.io.IOException;
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
        System.out.println(g);
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (DrawableObject doj : objects) {
            System.out.println(doj);
            if (dimensions == 1) {
                int x = ((d1Object) doj).x[0].intValue();

                try {
                    BufferedImage icon = ImageIO.read(new FileInputStream(doj.id));
                    g2d.drawImage(icon, getWidth() / 2 + 60, x, 60, 60, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return true;
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (dimensions == 2) {
                int x = ((d2Object) doj).x[0].intValue();
                int y = ((d2Object) doj).y[0].intValue();

                try {
                    BufferedImage icon = ImageIO.read(new FileInputStream(doj.id));
                    g2d.drawImage(icon, x, y, 60, 60, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return true;
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        final int TICKS = 15;

        g2d.setColor(Color.red);
        g2d.fillRect(getWidth()/2, 0, 1, getHeight());

        for (int i = 1; i < TICKS; i++) {
            g2d.fillRect(getWidth()/2 - 2, i * getHeight()/TICKS, 5, 1);
            g2d.drawString(String.valueOf(i * 900/TICKS), getWidth()/2 + 5, i * getHeight()/TICKS + 5);
        }

        if (dimensions > 1) {
            g2d.setColor(Color.green.darker());
            g2d.fillRect(0, getHeight()/2, getWidth(), 1);

            for (int i = 1; i < TICKS; i++) {
                g2d.fillRect(i * getWidth()/TICKS, getHeight()/2 - 2, 1, 5);
                g2d.drawString(String.valueOf(i * 900/TICKS), i * getWidth()/TICKS - 7, getWidth()/2 + 5);
            }
        }
    }
}
