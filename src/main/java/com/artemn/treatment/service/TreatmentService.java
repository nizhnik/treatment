package com.artemn.treatment.service;

import com.artemn.treatment.entity.TreatmentPlan;
import com.artemn.treatment.entity.TreatmentTask;
import com.artemn.treatment.enums.TaskStatus;
import com.artemn.treatment.repository.TreatmentPlanRepository;
import com.artemn.treatment.repository.TreatmentTaskRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.support.CronExpression;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TreatmentService {
    private final TreatmentTaskRepository treatmentTaskRepository;
    private final TreatmentPlanRepository treatmentPlanRepository;

    public List<TreatmentTask> findTasks() {
        return treatmentTaskRepository.findAll();
    }

    public List<TreatmentPlan> findPlans() {
        return treatmentPlanRepository.findAll();
    }

    @Transactional
    public List<TreatmentTask> scheduleTasks() {
        final LocalDateTime scheduleUntil = LocalDateTime.now().plusMonths(1);
        final List<TreatmentPlan> plans = treatmentPlanRepository.findActive(scheduleUntil);

        final List<TreatmentTask> tasks = plans.stream()
                .map(plan -> planToTasks(plan, scheduleUntil))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(TreatmentTask::getStartTime))
                .toList();

        treatmentTaskRepository.saveAll(tasks);

        plans.forEach(plan -> plan.setScheduledUntil(scheduleUntil));
        treatmentPlanRepository.saveAll(plans);

        return tasks;
    }

    private static List<TreatmentTask> planToTasks(TreatmentPlan plan, LocalDateTime scheduleUntil) {
        final LocalDateTime startTime = Optional.ofNullable(plan.getScheduledUntil()).orElse(plan.getStartTime());
        final LocalDateTime endTime = Optional.ofNullable(plan.getEndTime()).orElse(scheduleUntil);
        return recurrenceToDates(plan.getRecurrencePattern(), startTime, endTime).stream()
                .map(date -> new TreatmentTask()
                        .setAction(plan.getAction())
                        .setPatient(plan.getPatient())
                        .setStatus(TaskStatus.ACTIVE)
                        .setStartTime(date))
                .toList();
    }

    public static List<LocalDateTime> recurrenceToDates(String recurrence, LocalDateTime start, LocalDateTime end) {
        try {
            return toCrons(recurrence).stream()
                    .map(cron -> toDates(cron, start, end))
                    .flatMap(Collection::stream)
                    .toList();
        }  catch (Exception e) {
            throw new BadRequestException("Invalid recurrence: " + recurrence, e);
        }
    }

    private static List<LocalDateTime> toDates(String cron, LocalDateTime start, LocalDateTime end) {
        final List<LocalDateTime> dates = new ArrayList<>();

        final CronExpression cronExpression = CronExpression.parse(cron);
        LocalDateTime nextDate = start;
        while (nextDate.isBefore(end)) {
            nextDate = cronExpression.next(nextDate);

            if (nextDate == null) {
                break;
            }

            if (nextDate.isBefore(end) || nextDate.isEqual(end)) {
                dates.add(nextDate);
            }
        }

        return dates;
    }

    private static List<String> toCrons(String recurrence) {
        final String[] parts = recurrence.split(" at ");
        final String dayPart = parts[0].trim();
        final String timePart = parts[1].trim();
        final String dayOfWeekCron = toDayOfWeekCron(dayPart.split(" ")[1].toUpperCase());

        return Arrays.stream(timePart.split(" and "))
                .map(String::trim)
                .map(TreatmentService::toTimeCron)
                .map(timeCron -> timeCron + " ? * " + dayOfWeekCron)
                .toList();
    }

    private static String toTimeCron(String time) {
        try {
            final String minutes = time.split(":")[1];
            final String hours = time.split(":")[0];

            return "0 " + minutes + " " + hours;
        } catch (Exception e) {
            throw new BadRequestException("Invalid time: " + time, e);
        }
    }

    private static String toDayOfWeekCron(String dayOfWeek) {
        try {
            return "DAY".equals(dayOfWeek) ? "*" : String.valueOf(DayOfWeek.valueOf(dayOfWeek).getValue());
        } catch (Exception e) {
            throw new BadRequestException("Invalid day of the week: " + dayOfWeek, e);
        }
    }
}
