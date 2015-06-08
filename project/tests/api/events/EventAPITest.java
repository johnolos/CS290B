package api.events;

import org.junit.Before;
import org.junit.Test;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import static org.junit.Assert.*;

public class EventAPITest {

    /**
     * Setup configuration
     */
    CustomEventController   controller;
    EventListener           rmiListener;


    private String  DOMAIN = "localhost";
    private int     PORT = 2189;
    private String  SERVICE = "HAW328";

    /**
     * Test objects
     */
    final private double TEST_NUMBER = 100.0;
    final private String TEXT_STRING = "message";

    /**
     * Custom test views
     */
    EventView<Double> doubleView = new EventView<Double>() {
        @Override
        public void view(Double data) {
            assertEquals(TEST_NUMBER, data, 0.0);
        }

        @Override
        public void viewIfCapable(Object data) {
            try {
                view((Double)data);
            } catch (ClassCastException e) {
            }
        }
    };

    EventView<Object> objectView = new EventView<Object>() {
        @Override
        public void view(Object data) {
            assertEquals(TEXT_STRING, data);
        }

        @Override
        public void viewIfCapable(Object data) {
            view(data);
        }
    };

    // Custom EventController implementation
    class CustomEventController extends EventController {

        public CustomEventController(String domain, int port, String service) throws RemoteException {
            super(domain, port, service);
        }

        @Override
        public void handle(Event event) {
            if(event.getEventType() == Event.Type.TEST) {
                for(EventView eventView : eventViews) {
                    eventView.viewIfCapable(event.getObject());
                }
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        System.setSecurityManager( new SecurityManager());

        controller = new CustomEventController(DOMAIN, PORT, SERVICE);
        LocateRegistry.createRegistry(PORT).rebind(SERVICE, controller);

        rmiListener = (EventListener) Naming.lookup(controller.getUrl().url());
    }

    @Test
    public void testEventAPI() throws Exception {
        controller.register(doubleView);
        rmiListener.notify(new Event(Event.Type.TEST, TEST_NUMBER));
        controller.unregister(doubleView);
        controller.register(objectView);
        rmiListener.notify(new Event(Event.Type.TEST, TEXT_STRING));
        controller.unregister(objectView);
    }

}