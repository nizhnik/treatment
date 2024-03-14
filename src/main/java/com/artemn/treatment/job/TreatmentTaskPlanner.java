package com.artemn.treatment.job;

import com.artemn.treatment.entity.TreatmentTask;
import com.artemn.treatment.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class TreatmentTaskPlanner {

    private final TreatmentService treatmentService;

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void scheduleTasks() {
        log.info("Planning task...");
        final List<TreatmentTask> treatmentTasks = treatmentService.scheduleTasks();
        log.info("Planned {} tasks", treatmentTasks.size());
    }
}
