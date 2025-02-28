package com.i2kiselev.rinexprocessor.util;

import com.i2kiselev.rinexprocessor.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.orekit.files.rinex.observation.RinexObservation;
import org.orekit.files.rinex.observation.RinexObservationWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FileUtils {

    public static FileWriter getFileWriter(File file) {

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException exception) {
            throw new ApplicationException("Exception when creating fileWriter, path : " + file.getPath(), exception);
        }
        return fileWriter;
    }

    public static File getOutputFile(String pathOfResult) {
        log.info("Creating output file, path : {}", pathOfResult);
        File newOutputFile = new File(pathOfResult);
        try {
            boolean newFile = newOutputFile.createNewFile();
            if (!newFile) {
                throw new ApplicationException("File already exists, path : " + pathOfResult);
            }
        } catch (IOException exception) {
            throw new ApplicationException("Exception when creating output file, path : " + pathOfResult, exception);
        }
        return newOutputFile;
    }

    public static File writeOutputFile(String path, RinexObservation rinexObservation) {
        log.info("Starting writing output file, path : {}", path);
        File outputFile = getOutputFile(path);
        try (FileWriter fileWriter = getFileWriter(outputFile);
             RinexObservationWriter obsWriter = new RinexObservationWriter(fileWriter, "output-file")) {
            obsWriter.writeCompleteFile(rinexObservation);
        } catch (IOException exception) {
            throw new ApplicationException("Exception when writing output file, path : " + path, exception);
        }
        return outputFile;
    }
}
