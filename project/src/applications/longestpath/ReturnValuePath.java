package applications.longestpath;

import api.ReturnValue;
import system.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ReturnValuePath extends ReturnValue<Path> {

    static final private int NUM_PIXELS = 600;

    public ReturnValuePath(final Task task, Path path) {
        super(task, path);
    }

    @Override
    public JLabel view() {
        List<Integer> nodes = value().getPath();
        String path = "Path:";
        for(int i = 0; i < nodes.size(); i++) {
            path += String.valueOf(i) + " ";
        }
        path += "Cost: " + value().cost();
        return testView(path);
    }

    private JLabel testView(String text) {
        final Image image = new BufferedImage( NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB );
        final Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawString(text, NUM_PIXELS / 2, NUM_PIXELS / 2);
        final ImageIcon imageIcon = new ImageIcon( image );
        return new JLabel( imageIcon );
    }

}
