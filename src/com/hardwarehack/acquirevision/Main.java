package com.hardwarehack.acquirevision;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        try {
            BufferedImage image = ImageIO.read(new File("src/sample1transform.jpg"));
            BoardReader boardReader = new BoardReader(
                image
            );
            boardReader.calculateGrid();
            boardReader.drawGrid();
            boardReader.fillBox(3, 3);
            displayImage(boardReader.getImage());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayImage(Image image) throws IOException
    {
        ImageIcon icon=new ImageIcon(image);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(1080,750);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}