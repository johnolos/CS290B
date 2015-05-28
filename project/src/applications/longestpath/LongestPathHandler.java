package applications.longestpath;

import api.Event;
import api.EventHandler;
import api.JobRunner;

public class LongestPathHandler implements EventHandler {


    private JobRunner jobRunner = null;


    @Override
    public void handle(Event event) {
        switch (event.getEvent()) {
            case SHARED_UPDATED:
                break;
            default:
                break;
        }
    }

    @Override
    public void register(JobRunner jobRunner) {
        if(this.jobRunner == null) {
            this.jobRunner = jobRunner;
        }
    }

    @Override
    public void unregister(JobRunner jobRunner) {
        if(this.jobRunner == jobRunner) {
            this.jobRunner = null;
        }
    }
}
