package com.eversis.importer.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Recording {

    @Id
    @GeneratedValue
    private Long id;

    private String recordingName;

    /**
     * Data pobrania nagrania.
     */
    private Date downloadDate;

    private String type;

    /**
     * Data przygotowania nagrania do wysyłki. Data konwersji lub rozpakowania.
     */
    private Date preparationDate;

    private Date xmlGenerationDate;

    private Date consumptionDate;

}
