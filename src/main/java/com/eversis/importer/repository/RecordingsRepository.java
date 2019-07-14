package com.eversis.importer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingsRepository extends JpaRepository<Recording, Long> {

    public List<Recording> findAllByXmlGenerationDateIsNotNull();

}
