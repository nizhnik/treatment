package com.artemn.treatment.config;

import com.artemn.treatment.api.TreatmentResource;
import com.artemn.treatment.job.TreatmentTaskPlanner;
import com.artemn.treatment.repository.TreatmentPlanRepository;
import com.artemn.treatment.repository.TreatmentTaskRepository;
import com.artemn.treatment.service.TreatmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class TreatmentConfig {

    @Bean
    TreatmentService treatmentService(TreatmentTaskRepository treatmentTaskRepository,
                                      TreatmentPlanRepository treatmentPlanRepository) {
        return new TreatmentService(treatmentTaskRepository, treatmentPlanRepository);
    }

    @Bean
    TreatmentResource treatmentResource(TreatmentService treatmentService) {
        return new TreatmentResource(treatmentService);
    }

    @Bean
    TreatmentTaskPlanner treatmentTaskPlanner(TreatmentService treatmentService) {
        return new TreatmentTaskPlanner(treatmentService);
    }
}
