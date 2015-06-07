package api.events;

public class EventControllerUrl {
    final private String host;
    final private int port;
    final private String service;

    public EventControllerUrl(String host, int port, String service) {
        this.host = host;
        this.port = port;
        this.service = service;
    }

    public String url() {
        return "rmi://" + host + ":" + port + "/" + service;
    }


}
