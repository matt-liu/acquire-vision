package com.hardwarehack.acquirevision;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BoardReader {

    public static final double VERTICAL_SCALING_RATIO = 1.12;
    public static final int COLUMNS = 12;
    public static final int ROWS = 9;

    private final BufferedImage image;
    private final Point2D q1;
    private final Point2D q2;
    private final Point2D q3;
    private final Point2D q4;

    private GridModel gridModel;

    public BoardReader(BufferedImage image, Point2D quadrant1, Point2D quadrant2, Point2D quadrant3, Point2D quadrant4){
        this.image = image;
        this.q1 = quadrant1;
        this.q2 = quadrant2;
        this.q3 = quadrant3;
        this.q4 = quadrant4;
    }

    public BoardReader(BufferedImage image) {
        this.image = image;
        this.q1 = new Point2D.Double(image.getWidth(), 0);
        this.q2 = new Point2D.Double(0, 0);
        this.q3 = new Point2D.Double(0, image.getHeight());
        this.q4 = new Point2D.Double(image.getWidth(), image.getHeight());
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

    public void diagnosticDrawVerticalLines(){
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.yellow);
        ArrayList<Double> topHorizontals = calculateHorizontals(topWidth());
        ArrayList<Double> bottomHorizontals = calculateHorizontals(bottomWidth());
        for(int i = 0; i < 12; i++) {
            g2d.drawLine((int)(topHorizontals.get(i)+q2.getX()), topMargin().intValue(), (int)(bottomHorizontals.get(i)+q3.getX()), bottomMargin().intValue());
        }
        g2d.dispose();
    }

    public void calculateGrid() {
        gridModel = new GridModel(ROWS, COLUMNS, image.getWidth(), image.getHeight());
    }

    public void drawGrid() {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.yellow);
        for(int i=0; i < COLUMNS; i++) {
            Box box = gridModel.getBox(i, 0);
            int xPosition = (int)box.x1;
            g2d.drawLine(xPosition, 0, xPosition, image.getHeight());
        }

        for(int i=0; i < ROWS; i++) {
            Box box = gridModel.getBox(0, i);
            int yPosition = (int)box.y1;
            g2d.drawLine(0, yPosition, image.getWidth(), yPosition);
        }
    }

    public void fillBox(int x, int y) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.blue);

        Box box = gridModel.getBox(x, y);
        g2d.fillRect(box.x1, box.y1, box.width, box.height);
    }

    public int getStatusOf(int x, int y) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.red);

        Box box = gridModel.getBox(x, y);
        List<Point> randomPoints = Box.getRandomPointsIn(box);
        List<Color> colors  = new ArrayList<Color>();
        for(Point point: randomPoints) {
            int xPixel = (int)point.getX();
            int yPixel = (int)point.getY();
            colors.add(new Color(image.getRGB(xPixel, yPixel), true));
            g2d.drawLine(xPixel, yPixel, xPixel, yPixel);
        }

        Color averageColor = getAverageColor(colors);
        int brightness = getBrightness(averageColor);
        System.out.println(averageColor);
        System.out.println(brightness);
        String occupiedStatus = brightness > 115 ? "occupied" : "empty";
        System.out.println("The block at " + x + ", " + y + " is " + occupiedStatus);

        return brightness;
    }

    private int getBrightness(Color color) {
        int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
        return brightness;
    }
    private Color getAverageColor(List<Color> colors) {
        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        for(Color color: colors) {
            redBucket += color.getRed();
            greenBucket += color.getGreen();
            blueBucket += color.getBlue();
        }

        return new Color(redBucket/colors.size(),
            greenBucket/colors.size(),
            blueBucket/colors.size());
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