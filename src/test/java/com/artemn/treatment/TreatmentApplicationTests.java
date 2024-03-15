package com.artemn.treatment;

import com.artemn.treatment.api.TreatmentResource;
import com.artemn.treatment.entity.TreatmentPlan;
import com.artemn.treatment.entity.TreatmentTask;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.artemn.treatment.enums.TaskStatus.ACTIVE;
import static com.artemn.treatment.enums.TreatmentAction.ActionA;
import static com.artemn.treatment.enums.TreatmentAction.ActionB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class TreatmentApplicationTests {

    @Autowired
    private TreatmentResource treatmentResource;

    @Test
    public void getPlansReturnsPlans() {
        List<TreatmentPlan> actual = treatmentResource.getPlans();

        assertThat(actual).hasSize(2);

        final TreatmentPlan johnPlan = actual.get(0);
        assertThat(johnPlan.getId()).isEqualTo(1L);
        assertThat(johnPlan.getPatient()).isEqualTo("John Doe");
        assertThat(johnPlan.getAction()).isEqualTo(ActionA);
        assertThat(johnPlan.getStartTime()).isEqualTo(LocalDateTime.parse("2024-01-01T09:00"));
        assertThat(johnPlan.getRecurrencePattern()).isEqualTo("every day at 08:00 and 16:00");

        final TreatmentPlan janePlan = actual.get(1);
        assertThat(janePlan.getId()).isEqualTo(2L);
        assertThat(janePlan.getPatient()).isEqualTo("Jane Smith");
        assertThat(janePlan.getAction()).isEqualTo(ActionB);
        assertThat(janePlan.getStartTime()).isEqualTo(LocalDateTime.parse("2024-01-02T11:00"));
        assertThat(janePlan.getRecurrencePattern()).isEqualTo("every Monday at 12:00");
    }

}
