package com.home.java_02.domain.task.repository;

import com.home.java_02.common.enums.TaskStatus;
import com.home.java_02.domain.task.entity.TaskQueue;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskQueueRepository extends JpaRepository<TaskQueue, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT tq FROM TaskQueue tq WHERE tq.id = :id")
  Optional<TaskQueue> findByIdForUpdate(@Param("id") Long id);

  List<TaskQueue> findAllByStatus(TaskStatus taskStatus);
}
