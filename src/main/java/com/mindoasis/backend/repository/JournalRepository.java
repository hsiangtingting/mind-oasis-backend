package com.mindoasis.backend.repository;

import com.mindoasis.backend.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long>{
    List<Journal> findByAppUserUuid(String uuid);
}
