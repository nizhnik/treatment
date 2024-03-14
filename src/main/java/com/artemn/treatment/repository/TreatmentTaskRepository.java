package com.artemn.treatment.repository;

import com.artemn.treatment.entity.TreatmentTask;
import org.springframework.data.repository.ListCrudRepository;

public interface TreatmentTaskRepository extends ListCrudRepository<TreatmentTask, Long> {
}
