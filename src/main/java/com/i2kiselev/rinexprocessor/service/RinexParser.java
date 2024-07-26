package com.i2kiselev.rinexprocessor.service;

import org.orekit.data.DataSource;
import org.orekit.files.rinex.observation.RinexObservation;
import org.orekit.files.rinex.observation.RinexObservationParser;
import org.springframework.stereotype.Component;

@Component
public class RinexParser {

    private final RinexObservationParser rinexObservationParser;

    public RinexParser(RinexObservationParser rinexObservationParser) {
        this.rinexObservationParser = rinexObservationParser;
    }

    public RinexObservation parseObservation(String filePath) {
        DataSource fileDataSource = new DataSource(filePath);
        return rinexObservationParser.parse(fileDataSource);
    }
}
