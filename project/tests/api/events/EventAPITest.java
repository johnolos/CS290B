package api.events;

import api.Space;
import org.junit.Before;
import org.junit.Test;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.Assert.*;

public class EventAPITest {

    // Setup
    CustomEventController controller;
    EventController rmiController;

    // Setup
    String HOST = "localhost";
    int PORT_NUMBER = 2189;
    String SERVICE = "HAW328";

    // Test objects
    final private double TEST_NUMBER = 100.0;
    final private String TEXT_STRING = "message";

    // Test views
    EventView<Double> doubleView = (x) -> assertEquals(TEST_NUMBER, x, 0.0);
    EventView<Object> objectView = (x) -> assertEquals(TEXT_STRING, x);

    // Custom EventController implementation
    class CustomEventController extends UnicastRemoteObject implements EventController {
        EventView eventView;

        final private EventControllerUrl url;

        public CustomEventController(EventControllerUrl url) throws RemoteException {
            this.url = url;
        }

        public EventControllerUrl getUrl() {
            return url;
        }

        @Override
        public void handle(Event event) throws RemoteException {
            if(event.getEvent() == EventType.TEST)
                eventView.view(event.getObject());
        }

        public void register(EventView eventView) {
            this.eventView = eventView;
        }

        public void unregister(EventView eventView) {
            if(this.eventView == eventView) {
                this.eventView = null;
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        System.setSecurityManager( new SecurityManager());

        controller = new CustomEventController(new EventControllerUrl(HOST, PORT_NUMBER, SERVICE));

        LocateRegistry.createRegistry(PORT_NUMBER).rebind(SERVICE, controller);

        rmiController = (EventController) Naming.lookup(controller.getUrl().url());
    }

    @Test
    public void testEventAPI() throws Exception {
        controller.register(doubleView);
        rmiController.handle(new Event(EventType.TEST, TEST_NUMBER));
        controller.unregister(doubleView);
        controller.register(objectView);
        rmiController.handle(new Event(EventType.TEST, TEXT_STRING));
        controller.unregister(objectView);
    }
}