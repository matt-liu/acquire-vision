package com.hardwarehack.acquirevision;

public class Box {
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

}
