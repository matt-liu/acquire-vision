package com.hardwarehack.acquirevision;

import java.awt.*;
import java.awt.geom.Point2D;

public class GridModel {
    private Point2D[][] grid;
    private double columnWidth;
    private double rowHeight;

    public GridModel(int rows, int columns, int width, int height) {
        this.columnWidth = width/columns;
        this.rowHeight = height/rows;
        this.grid = new Point2D[columns][rows];

        for (int i=0; i < columns; i++) {
            for (int j=0; j < rows; j++) {
                double topLeftX = i*columnWidth;
                double topLeftY = j*rowHeight;
                grid[i][j] = new Point.Double(topLeftX, topLeftY);
            }
        }
    }

    public Box getBox(int x, int y) {
        Point2D topLeft = grid[x][y];
        Box box = new Box(topLeft.getX(), topLeft.getY(), topLeft.getX() + columnWidth, topLeft.getY() + rowHeight);
        return box;
    }
}
