package com.i2kiselev.rinexprocessor.service;

import com.i2kiselev.rinexprocessor.record.FormatType;
import com.i2kiselev.rinexprocessor.record.TypeMatrix;
import com.i2kiselev.rinexprocessor.util.FileUtils;
import com.i2kiselev.rinexprocessor.util.RinexUtils;
import org.orekit.files.rinex.observation.ObservationData;
import org.orekit.files.rinex.observation.ObservationDataSet;
import org.orekit.files.rinex.observation.RinexObservation;
import org.orekit.files.rinex.observation.RinexObservationHeader;
import org.orekit.gnss.SatInSystem;
import org.orekit.gnss.SatelliteSystem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RinexService {

    public static final int NULL_SIGNAL = 0;
    private final RinexParser rinexParser;

    public RinexService(RinexParser rinexParser) {
        this.rinexParser = rinexParser;
    }

    public File completeRequest(String inputPath, String outputPath, TypeMatrix typeMatrix, FormatType formatType) {
        RinexObservation rinexObservation = processObservation(inputPath, typeMatrix, formatType);
        return FileUtils.writeOutputFile(outputPath, rinexObservation);
    }

    RinexObservation processObservation(String filePath, TypeMatrix typeMatrix, FormatType format) {
        RinexObservation rinexObservation = rinexParser.parseObservation(filePath);
        List<ObservationDataSet> observationDataSets = rinexObservation.getObservationDataSets();

        List<ObservationDataSet> filteredBySystems = observationDataSets.stream().filter(x -> typeMatrix.getUsedSystems().contains(x.getSatellite().getSystem())).collect(Collectors.toList());
        List<ObservationDataSet> updatedDataSets = transformObservations(filteredBySystems, typeMatrix, format);

        RinexObservationHeader sourceHeader = rinexObservation.getHeader();
        RinexObservation updatedObservation = new RinexObservation();
        RinexUtils.copyRinexHeader(sourceHeader, updatedObservation.getHeader());

        for (ObservationDataSet newDataSet : updatedDataSets) {
            updatedObservation.addObservationDataSet(newDataSet);
        }
        return updatedObservation;
    }

    private List<ObservationDataSet> transformObservations(List<ObservationDataSet> observationDataSets, TypeMatrix typeMatrix, FormatType format) {
        List<ObservationDataSet> newObservationDataSets = new ArrayList<>();
        for (ObservationDataSet dataSet : observationDataSets) {
            ObservationDataSet updatedDataSet;
            SatInSystem currentSatellite = dataSet.getSatellite();
            SatelliteSystem currentSatSystem = currentSatellite.getSystem();

            List<ObservationData> observationData = dataSet.getObservationData();
            List<ObservationData> filteredData;
            if (format == FormatType.REMOVE) {
                filteredData = filterObservationsByObs(observationData, typeMatrix, currentSatSystem);
            } else {
                filteredData = nullifyObservationsByObs(observationData, typeMatrix, currentSatSystem);
            }

            if (filteredData.isEmpty()) {
                continue;
            }
            if (filteredData.stream().allMatch(x -> Double.isNaN(x.getValue()))) {
                continue;
            }

            updatedDataSet = new ObservationDataSet(dataSet.getSatellite(), dataSet.getDate(), dataSet.getEventFlag(), dataSet.getRcvrClkOffset(), filteredData);
            newObservationDataSets.add(updatedDataSet);
        }
        return newObservationDataSets;
    }

    private List<ObservationData> filterObservationsByObs(List<ObservationData> observationData, TypeMatrix typeMatrix, SatelliteSystem satelliteSystem) {
        return observationData.stream().filter(x -> typeMatrix.getUsedObs(satelliteSystem).contains(x.getObservationType().name())).collect(Collectors.toList());
    }

    private List<ObservationData> nullifyObservationsByObs(List<ObservationData> observationData, TypeMatrix typeMatrix, SatelliteSystem satelliteSystem) {
        return observationData.stream()
                .map(mapObservation(typeMatrix, satelliteSystem))
                .collect(Collectors.toList());
    }

    private static Function<ObservationData, ObservationData> mapObservation(TypeMatrix typeMatrix, SatelliteSystem currentSatSystem) {
        return x -> {
            if (typeMatrix.getUsedObs(currentSatSystem).contains(x.getObservationType().name())) {
                return x;
            } else {
                return new ObservationData(x.getObservationType(), Double.NaN, x.getLossOfLockIndicator(), NULL_SIGNAL);
            }
        };
    }

}
