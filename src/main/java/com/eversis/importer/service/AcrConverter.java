package com.eversis.importer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class AcrConverter extends BaseConverter implements Runnable {

    private String SOURCE_PATH;
    private String DEST_TEMP_PATH;
    private String DEST_AUDIO_PATH;
    private String DEST_VERINT_PATH;

    private long finalI;

    @Override
    public void run() {
        log.debug(" ACR " + finalI + " " + LocalDate.now());
        try {
            Path sourceTar = Files.copy(Path.of(SOURCE_PATH + "pack.tar.gz"), Path.of(DEST_TEMP_PATH + "ACR_" + finalI + ".tar.gz"));
            Files.createDirectory(Path.of(DEST_TEMP_PATH + "ACR_" + finalI));
            String cmd = "tar xvzf " + DEST_TEMP_PATH + "ACR_" + finalI + ".tar.gz -C " + DEST_TEMP_PATH + "ACR_" + finalI;
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            Path copiedExtractedFile = Files.copy(Path.of(DEST_TEMP_PATH + "ACR_" + finalI + File.separator + "2018_11_02_16-00-15_653541.wav"), Path.of(DEST_AUDIO_PATH + "ACR_" + finalI + ".wav"));
            Path copiedExtractedXmlFile = Files.copy(Path.of(DEST_TEMP_PATH + "ACR_" + finalI + File.separator + "description.xml"), Path.of(DEST_VERINT_PATH + "ACR_" + finalI + ".xml"));
            Files.delete(sourceTar);
            FileUtils.deleteDirectory(new File(DEST_TEMP_PATH + "ACR_" + finalI));
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
