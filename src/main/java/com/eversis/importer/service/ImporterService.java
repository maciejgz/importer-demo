package com.eversis.importer.service;


import com.eversis.importer.repository.Recording;
import com.eversis.importer.repository.RecordingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class ImporterService {

    private final RecordingsRepository recordingsRepository;
    @Value("${importer.source.path}")
    private String SOURCE_PATH;
    @Value("${importer.destination.temp.path}")
    private String DEST_TEMP_PATH;
    @Value("${importer.destination.audio.path}")
    private String DEST_AUDIO_PATH;
    @Value("${importer.destination.verint.path}")
    private String DEST_VERINT_PATH;


    @Autowired
    public ImporterService(RecordingsRepository recordingsRepository) {
        this.recordingsRepository = recordingsRepository;
    }

    public void importData(long probes) {
        // dwa scenariusze pobierania i rozpakowywania plików wykonywane losowo z założonym podziałem procentowym
        Random random = new Random();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        log.debug("job started for " + probes + " probes at: " + LocalDateTime.now());
        try {
            Files.createDirectory(Path.of(DEST_TEMP_PATH));
            Files.createDirectory(Path.of(DEST_AUDIO_PATH));
            Files.createDirectory(Path.of(DEST_VERINT_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (long i = 0; i < probes; i++) {
            int randomResult = random.nextInt(9);
            if (randomResult <= 4) {
                //skopiowanie pliku z dysku rozpakowanie
                executor.execute(new AcrConverter(SOURCE_PATH, DEST_TEMP_PATH, DEST_AUDIO_PATH, DEST_VERINT_PATH, i));
                saveRecordingInfo(i, "ACR");
            } else {
                //pobranie pliku z http i sox
                executor.execute(new FtpConverter(SOURCE_PATH, DEST_TEMP_PATH, DEST_AUDIO_PATH, DEST_VERINT_PATH, i));
                saveRecordingInfo(i, "FTP");
            }

        }
        log.debug("job finished at: " + LocalDateTime.now());
    }

    @Transactional
    protected void saveRecordingInfo(long id, String type) {
        Recording recording = new Recording();
        recording.setRecordingName(type + id);
        recording.setDownloadDate(new Date());
        recording.setPreparationDate(new Date());
        recording.setXmlGenerationDate(new Date());
        recording.setType(type);
        recordingsRepository.save(recording);
    }

    @Transactional
    @Scheduled(fixedRate = 30000)
    public void verifyIfRecordingAnalyzed() {
        //zadanie uruchamiane cyklicznie co 30s
        log.debug("verifyIfRecordingAnalyzed");

        //pobranie z bazy zapisanych i nieprzetworzonych nagrań
        List<Recording> recordings = recordingsRepository.findAllByXmlGenerationDateIsNotNull();
        //listowanie katalogu z nagraniami i weryfikacja czy plik dla każdego z nagrań istnieje. jeśli nie to oznacza, że został przetworzony.
        log.debug("number of not parsed files: " + recordings.size());
    }
}
