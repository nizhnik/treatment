package com.artemn.treatment.service;

import com.artemn.treatment.entity.TreatmentPlan;
import com.artemn.treatment.entity.TreatmentTask;
import com.artemn.treatment.enums.TaskStatus;
import com.artemn.treatment.repository.TreatmentPlanRepository;
import com.artemn.treatment.repository.TreatmentTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.artemn.treatment.enums.TreatmentAction.ActionA;
import static com.artemn.treatment.enums.TreatmentAction.ActionB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceTest {

    @Mock
    TreatmentTaskRepository treatmentTaskRepository;
    @Mock
    TreatmentPlanRepository treatmentPlanRepository;

    @InjectMocks
    TreatmentService treatmentService;

    @Captor
    ArgumentCaptor<List<TreatmentTask>> tasksCaptor;

    @Test
    void scheduleTasks() {
        TreatmentTask[] expected = new TreatmentTask[]{
                new TreatmentTask()
                        .setAction(ActionA)
                        .setPatient("John Doe")
                        .setStartTime(LocalDateTime.of(2024, 1, 1, 16, 0))
                        .setStatus(TaskStatus.ACTIVE),
                new TreatmentTask()
                        .setAction(ActionA)
                        .setPatient("John Doe")
                        .setStartTime(LocalDateTime.of(2024, 1, 2, 8, 0))
                        .setStatus(TaskStatus.ACTIVE),
                new TreatmentTask()
                        .setAction(ActionB)
                        .setPatient("Jane Smith")
                        .setStartTime(LocalDateTime.of(2024, 1, 8, 12, 0))
                        .setStatus(TaskStatus.ACTIVE)
        };


        when(treatmentPlanRepository.findActive(any())).thenReturn(List.of(
                new TreatmentPlan()
                        .setId(1L)
                        .setAction(ActionA)
                        .setPatient("John Doe")
                        .setStartTime(LocalDateTime.parse("2024-01-01T09:00"))
                        .setEndTime(LocalDateTime.parse("2024-01-02T09:00"))
                        .setRecurrencePattern("every day at 08:00 and 16:00"),
                new TreatmentPlan()
                        .setId(2L)
                        .setAction(ActionB)
                        .setPatient("Jane Smith")
                        .setStartTime(LocalDateTime.parse("2024-01-02T11:00"))
                        .setEndTime(LocalDateTime.parse("2024-01-09T11:00"))
                        .setRecurrencePattern("every Monday at 12:00")
        ));

        treatmentService.scheduleTasks();

        verify(treatmentTaskRepository).saveAll(tasksCaptor.capture());

        final List<TreatmentTask> tasks = tasksCaptor.getValue();
        assertThat(tasks).containsExactly(expected);
    }
}
