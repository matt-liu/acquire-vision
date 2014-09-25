package com.hardwarehack.acquirevision;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Box {
    private static final int RANDOM_POINTS = 80;
    public int x1;
    public int x2;
    public int y1;
    public int y2;
    public int height;
    public int width;

    public Box(double x1, double y1, double x2, double y2) {
        this.x1 = (int)x1;
        this.y1 = (int)y1;
        this.x2 = (int)x2;
        this.y2 = (int)y2;
        this.height = Math.abs(this.y2 - this.y1);
        this.width = Math.abs(this.x2 - this.x1);
    }

    public static List<Point> getRandomPointsIn(Box box) {
        List<Point> randomPoints = new ArrayList<Point>();
        for (int i=0; i < RANDOM_POINTS; i++) {
            int x = randInt(box.x1, box.x2);
            int y = randInt(box.y1, box.y2);
            randomPoints.add(new Point(x, y));
        }

        return randomPoints;
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
