package com.artemn.treatment.repository;

import com.artemn.treatment.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TreatmentPlanRepository extends ListCrudRepository<TreatmentPlan, Long> {

    @Query("SELECT t FROM TreatmentPlan t WHERE (t.endTime >= :start OR t.endTime IS NULL) AND (t.scheduledUntil <= :end OR t.scheduledUntil IS NULL)")
    List<TreatmentPlan> findActive(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    default List<TreatmentPlan> findActive(LocalDateTime end) {
        return findActive(LocalDateTime.now(), end);
    }
}
