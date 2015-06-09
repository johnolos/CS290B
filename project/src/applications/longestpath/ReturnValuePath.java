package applications.longestpath;

import api.ReturnValue;
import system.Task;

import javax.swing.*;

public class ReturnValuePath extends ReturnValue<Path> {

    public ReturnValuePath(final Task task, Path path) {
        super(task, path);
    }

    @Override
    public JLabel view() {
        return null;
    }

}
