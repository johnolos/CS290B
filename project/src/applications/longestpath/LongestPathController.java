package applications.longestpath;

import api.events.Event;
import api.events.EventController;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LongestPathController extends EventController {

    public LongestPathController(String domain, int port) throws RemoteException {
        super(domain, port, TaskLongestPath.SERVICE);
    }

    @Override
    public void handle(Event event){
        switch (event.getEventType()) {
            case TEMPORARY_SOLUTION:
                Logger.getLogger(this.getClass().getCanonicalName())
                        .log(Level.INFO, "TEMPORARY_SOLUTION received.");
                Path path = (Path)event.getObject();
                fireViewUpdate(path.createGraphicImage());
                break;
            case STATUS:
                Logger.getLogger(this.getClass().getCanonicalName())
                        .log(Level.INFO, "STATUS: " + event.getObject());
                break;
            case SHARED_UPDATED:
                break;
            default:
                break;
        }
    }

}
