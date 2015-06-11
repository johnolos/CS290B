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
        final Image image = new BufferedImage(NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = image.getGraphics();

        // Information
        int[][] nodes = new int[10][];
        int[][] graph = new int[10][];


        // Draw edges as black lines
        int x1, x2, y1, y2;
        int edge_x, edge_y;
        graphics.setColor(Color.BLACK);
        for(int i = 0; i < graph.length; i++) {
            x1 = nodes[i][0];
            y1 = nodes[i][1];
            for(int j = 0; j < graph[i].length; j+=2) {
                x2 = nodes[graph[i][j]][0];
                y2 = nodes[graph[i][j]][1];
                graphics.drawLine(x1, y1, x2, y2);
                edge_x = x1 > x2 ? (x1 - x2) / 2 + x2 : (x2 - x1) / 2 + x1;
                edge_y = y1 > y2 ? (y1 - y2) / 2 + y2 : (y2 - y1) / 2 + y1;
                graphics.drawString(
                        "Edge: " + graph[i][j+1],
                        edge_x + EDGE_MARGIN,
                        edge_y - EDGE_MARGIN);
            }
        }

        // Draw nodes as red circles
        graphics.setColor(Color.RED);
        final int VERTEX_DIAMETER = 6;
        for(int i = 0; i < nodes.length; i++) {
            int x = nodes[i][0];
            int y = nodes[i][1];
            graphics.fillOval(
                    x - VERTEX_DIAMETER/2, y - VERTEX_DIAMETER/2,
                    VERTEX_DIAMETER, VERTEX_DIAMETER
            );

        }

        // Draw path cost at a stationary location.
        graphics.setColor(Color.BLACK);
        graphics.drawString("Path cost: " + value().cost(), COST_X, COST_Y);
        final ImageIcon imageIcon = new ImageIcon(image);
        return new JLabel(imageIcon);
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
