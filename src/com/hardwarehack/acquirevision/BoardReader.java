package com.hardwarehack.acquirevision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BoardReader {

    public static final double VERTICAL_SCALING_RATIO = 1.12;

    private final BufferedImage image;
    private final Point2D q1;
    private final Point2D q2;
    private final Point2D q3;
    private final Point2D q4;

    private List<Point2D> points = new ArrayList<Point2D>();

    public BoardReader(BufferedImage image, Point2D quadrant1, Point2D quadrant2, Point2D quadrant3, Point2D quadrant4){
        this.image = image;
        this.q1 = quadrant1;
        this.q2 = quadrant2;
        this.q3 = quadrant3;
        this.q4 = quadrant4;
        getIntersections();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void diagnosticDrawHorizontalLines(){
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.yellow);
        for(Double vertical : calculateVerticals()) {
            Double y = vertical+topMargin();
            g2d.drawLine(0, y.intValue(), image.getWidth(), y.intValue());
        }
        g2d.dispose();
    }

    public void diagnosticDrawVerticalLines() {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.yellow);
        ArrayList<Double> topHorizontals = calculateHorizontals(topWidth());
        ArrayList<Double> bottomHorizontals = calculateHorizontals(bottomWidth());
        for(int i = 0; i < 12; i++) {
            g2d.drawLine((int)(topHorizontals.get(i)+q2.getX()), topMargin().intValue(), (int)(bottomHorizontals.get(i)+q3.getX()), bottomMargin().intValue());
        }
        g2d.dispose();
    }

    public void getIntersections() {
        ArrayList<Double> topHorizontals = calculateHorizontals(topWidth());
        ArrayList<Double> bottomHorizontals = calculateHorizontals(bottomWidth());
        ArrayList<Double> verticals = calculateVerticals();
        for(Double vertical : verticals){
            for(int i = 0; i < 12; i++) {
                Double x1 = topHorizontals.get(i)+q2.getX();
                Double y1 = topMargin();
                Double x2 = bottomHorizontals.get(i)+q3.getX();
                Double y2 = bottomMargin();
                int x = (int) ( (x2-x1)/(y2-y1)*((vertical+topMargin())-y1) + x1 );
                points.add(new Point2D.Double(x, (vertical+topMargin())));
            }
        }
    }

    public void diagnosticDrawPoints() {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.red);
        for(Point2D point : points) {
          g2d.drawRect((int)point.getX()-1, (int)point.getY()-1, 3, 3);
        }
    }

    private ArrayList<Double> calculateVerticals() {
        ArrayList<Double> result = new ArrayList();
        double averageGridDifference = ( bottomMargin() - topMargin())/9;
        double verticalGridDifference = averageGridDifference / (Math.pow(VERTICAL_SCALING_RATIO,4));
        double offset = verticalGridDifference /2;
        result.add(offset);
        for(int i = 1; i < 9; i++) {
            result.add(result.get(i-1)+verticalGridDifference);
            verticalGridDifference *= VERTICAL_SCALING_RATIO;
        }
        return result;
    }

    private ArrayList<Double> calculateHorizontals(Double marginLength) {
        ArrayList<Double> result = new ArrayList();
        double averageGridDifference = marginLength/12;
        double offset = averageGridDifference/2;
        for(int i = 0; i < 12; i++) {
            result.add(offset + i*averageGridDifference);
        }
        return result;
    }

    private Double bottomMargin() {
        return (q3.getY() + q4.getY())/2;
    }

    private Double topMargin() {
        return (q1.getY() + q2.getY())/2;
    }

    private Double topWidth() {
        return q1.getX() - q2.getX();
    }

    private Double bottomWidth() {
        return q4.getX() - q3.getX();
    }
}