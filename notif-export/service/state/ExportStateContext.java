package com.example.notification_export_service.service.state;


import com.example.notification_export_service.domain.ExportJob;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExportStateContext {

    private final Map<String, ExportState> states = new ConcurrentHashMap<>();
    private final PendingState pendingState;
    private final ProcessingState processingState;
    private final CompletedState completedState;
    private final FailedState failedState;

    public ExportStateContext(PendingState pendingState,
                              ProcessingState processingState,
                              CompletedState completedState,
                              FailedState failedState) {
        this.pendingState = pendingState;
        this.processingState = processingState;
        this.completedState = completedState;
        this.failedState = failedState;
        initStates();
    }

    private void initStates() {
        states.put("PENDING", pendingState);
        states.put("PROCESSING", processingState);
        states.put("COMPLETED", completedState);
        states.put("FAILED", failedState);
    }

    public void transitionTo(ExportJob job, String newState) {
        ExportState state = states.get(newState);
        if (state != null) {
            state.handle(job);
        }
    }

    public ExportState getCurrentState(ExportJob job) {
        return states.get(job.getStatus());
    }
}
