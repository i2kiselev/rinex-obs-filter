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
import java.util.stream.Collectors;

@Service
public class RinexService {

    private final RinexParser rinexParser;

    public RinexService(RinexParser rinexParser) {
        this.rinexParser = rinexParser;
    }

    public File completeRequest(String inputPath, String outputPath, TypeMatrix typeMatrix, FormatType formatType) {
        RinexObservation rinexObservation = processObservation(inputPath, typeMatrix);
        return FileUtils.writeOutputFile(outputPath, rinexObservation);
    }

    RinexObservation processObservation(String filePath, TypeMatrix typeMatrix) {
        RinexObservation rinexObservation = rinexParser.parseObservation(filePath);
        List<ObservationDataSet> observationDataSets = rinexObservation.getObservationDataSets();

        List<ObservationDataSet> filteredBySystems = observationDataSets.stream().filter(x -> typeMatrix.getUsedSystems().contains(x.getSatellite().getSystem())).collect(Collectors.toList());
        List<ObservationDataSet> updatedDataSets = filterObservationsByObs(filteredBySystems, typeMatrix);

        RinexObservationHeader sourceHeader = rinexObservation.getHeader();
        RinexObservation updatedObservation = new RinexObservation();
        RinexUtils.copyRinexHeader(sourceHeader, updatedObservation.getHeader());

        for (ObservationDataSet newDataSet : updatedDataSets) {
            updatedObservation.addObservationDataSet(newDataSet);
        }
        return updatedObservation;
    }


    private List<ObservationDataSet> filterObservationsByObs(List<ObservationDataSet> observationDataSets, TypeMatrix typeMatrix) {
        List<ObservationDataSet> newObservationDataSets = new ArrayList<>();
        for (ObservationDataSet dataSet : observationDataSets) {
            ObservationDataSet updatedDataSet;
            SatInSystem currentSatellite = dataSet.getSatellite();
            SatelliteSystem currentSatSystem = currentSatellite.getSystem();

            List<ObservationData> observationData = dataSet.getObservationData();
            List<ObservationData> filteredData = observationData.stream().filter(x -> typeMatrix.getUsedObs(currentSatSystem).contains(x.getObservationType().name())).collect(Collectors.toList());
            if (filteredData.isEmpty()) {
                continue;
            }
            updatedDataSet = new ObservationDataSet(dataSet.getSatellite(), dataSet.getDate(), dataSet.getEventFlag(), dataSet.getRcvrClkOffset(), filteredData);
            newObservationDataSets.add(updatedDataSet);
        }
        return newObservationDataSets;
    }


}
