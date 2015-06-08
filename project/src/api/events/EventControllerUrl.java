package api.events;

import java.io.Serializable;

public class EventControllerUrl implements Serializable {

    final private String domain;
    final private int port;
    final private String service;

    public EventControllerUrl(String domain, int port, String service) {
        this.domain = domain;
        this.port = port;
        this.service = service;
    }

    public String url() { return "rmi://" + domain + ":" + port + "/" + service; }

    public String domain() { return domain; }

    public int port() { return port; }

    public String service() { return service; }

}
