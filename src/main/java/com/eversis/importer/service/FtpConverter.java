package com.eversis.importer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class FtpConverter extends BaseConverter implements Runnable {

    private String SOURCE_PATH;
    private String DEST_TEMP_PATH;
    private String DEST_AUDIO_PATH;
    private String DEST_VERINT_PATH;

    private long finalI;

    @Override
    public void run() {
        System.out.println(" FTP " + finalI + " " + LocalDate.now());
        try {
            Path sourceMp3 = Files.copy(Path.of(SOURCE_PATH + "2018_11_02_16-00-15_653543.mp3"), Path.of(DEST_TEMP_PATH + "FTP_" + finalI + ".mp3"));
            String cmd = "/usr/local/bin/sox " + DEST_TEMP_PATH + "FTP_" + finalI + "mp3 -r 8000 -b 16 " + DEST_AUDIO_PATH + "FTP_" + finalI + ".wav";

            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            Path copiedExtractedXmlFile = Files.copy(Path.of(SOURCE_PATH + "description.xml"), Path.of(DEST_VERINT_PATH + "ACR_" + finalI + ".wav"));
            Files.delete(sourceMp3);
        } catch (IOException | InterruptedException e) {
            log.debug("issue with FTP conversion: " + e.getMessage());
        }
    }
}
