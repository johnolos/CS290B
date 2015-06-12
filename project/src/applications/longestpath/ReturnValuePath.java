package applications.longestpath;

import api.ReturnValue;
import api.events.Event;
import system.Task;

import javax.swing.*;

public class ReturnValuePath extends ReturnValue<Path> {

    public ReturnValuePath(final Task task, Path path) {
        super(task, path);
    }

    public ReturnValuePath(final Task task, Path path, Event event) {
        super(task, path, event);
    }

    @Override
    public JLabel view() {
        return value().createGraphicImage();
    }

}
