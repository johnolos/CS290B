package applications.longestpath;

import api.ReturnValue;
import system.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ReturnValuePath extends ReturnValue<Path> {

    static final private int NUM_PIXELS = 600;
    static final private int COST_X = 50;
    static final private int COST_Y = 550;
    static final private int EDGE_MARGIN = 5;

    public ReturnValuePath(final Task task, Path path) {
        super(task, path);
    }

    @Override
    public JLabel view() {
        return value().createGraphicImage();
    }

}
