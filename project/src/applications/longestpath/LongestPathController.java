package applications.longestpath;

import api.events.Event;
import api.events.EventController;
import api.events.EventView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class LongestPathController implements EventController {

    static final private int NUM_PIXELS = 600;

    private List<EventView> views = new ArrayList<>();
    private int updateNumber = 0;

    final private String domain;
    final private int port;
    final private String service = "LONGEST";


    public LongestPathController(String domain, int port) throws RemoteException {
        this.domain = domain;
        this.port = port;
    }

    private void fireViewUpdate(JLabel label) {
        for(EventView view : views) {
            view.view(label);
        }
    }

    public String url() {
        return "rmi://" + domain + ":" + port + "/" + service;
    }

    @Override
    public void handle(Event event) throws RemoteException {
        switch (event.getEvent()) {
            case SHARED_UPDATED:
                fireViewUpdate(sharedUpdatedEvent());
                updateNumber++;
                break;
            case TEST:
                fireViewUpdate(testEvent());
                updateNumber++;
                break;
            default:
                break;
        }
    }

    private JLabel sharedUpdatedEvent() {
        final Image image = new BufferedImage( NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB );
        final Graphics graphics = image.getGraphics();
        graphics.drawString("Shared Updated : " + String.valueOf(updateNumber), NUM_PIXELS / 2, NUM_PIXELS / 2);
        final ImageIcon imageIcon = new ImageIcon( image );
        return new JLabel( imageIcon );
    }

    private JLabel testEvent() {
        final Image image = new BufferedImage( NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB );
        final Graphics graphics = image.getGraphics();
        graphics.drawString(String.valueOf(updateNumber), NUM_PIXELS / 2, NUM_PIXELS / 2);
        final ImageIcon imageIcon = new ImageIcon( image );
        return new JLabel( imageIcon );
    }

    public void register(EventView view) {
        views.add(view);
    }

    public void unregister(EventView view) {
        views.remove(view);
    }


}
