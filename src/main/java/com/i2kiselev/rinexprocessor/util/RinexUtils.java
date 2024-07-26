package com.i2kiselev.rinexprocessor.util;

import com.i2kiselev.rinexprocessor.exception.ApplicationException;
import org.orekit.files.rinex.observation.RinexObservationHeader;
import org.orekit.gnss.ObservationType;
import org.orekit.gnss.SatInSystem;
import org.orekit.gnss.SatelliteSystem;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class RinexUtils {

    public static void copyRinexHeader(RinexObservationHeader source, RinexObservationHeader target) {
        target.setAgencyName(source.getAgencyName());
        target.setApproxPos(source.getApproxPos());

        target.setAntennaAzimuth(source.getAntennaAzimuth());
        target.setAntennaBSight(source.getAntennaBSight());
        target.setAntennaHeight(source.getAntennaHeight());
        target.setAntennaPhaseCenter(source.getAntennaPhaseCenter());
        target.setAntennaReferencePoint(source.getAntennaReferencePoint());
        target.setAntennaType(source.getAntennaType());
        target.setAntennaZeroDirection(source.getAntennaZeroDirection());
        target.setAntennaNumber(source.getAntennaNumber());


        target.setC1cCodePhaseBias(source.getC1cCodePhaseBias());
        target.setC1pCodePhaseBias(source.getC1pCodePhaseBias());
        target.setC2cCodePhaseBias(source.getC2cCodePhaseBias());
        target.setC2pCodePhaseBias(source.getC2pCodePhaseBias());

        target.setCenterMass(source.getCenterMass());
        target.setClockOffsetApplied(source.getClockOffsetApplied());

        target.setCreationDate(source.getCreationDate());
        target.setCreationDateComponents(source.getCreationDateComponents());
        target.setCreationTimeZone(source.getCreationTimeZone());

        target.setDoi(source.getDoi());
        target.setEccentricities(source.getEccentricities());
        target.setFormatVersion(source.getFormatVersion());
        target.setInterval(source.getInterval());

        target.setLeapSeconds(source.getLeapSeconds());
        target.setLeapSecondsFuture(source.getLeapSecondsFuture());
        target.setLeapSecondsDayNum(source.getLeapSecondsDayNum());
        target.setLeapSecondsWeekNum(source.getLeapSecondsWeekNum());
        target.setLicense(source.getLicense());

        target.setMarkerName(source.getMarkerName());
        target.setMarkerNumber(source.getMarkerNumber());
        target.setMarkerType(source.getMarkerType());

        target.setNbSat(source.getNbSat());
        Field obsMapField;
        try {
            obsMapField = target.getClass().getDeclaredField("nbObsPerSat");
        } catch (NoSuchFieldException e) {
            throw new ApplicationException(e.getMessage());
        }
        obsMapField.setAccessible(true);
        Map<SatInSystem, Map<ObservationType, Integer>> nbObsPerSat = source.getNbObsPerSat();
        try {
            obsMapField.set(target, nbObsPerSat);
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e.getMessage());
        }

        target.setObservationCode(source.getObservationCode());
        target.setObserverName(source.getObserverName());

        target.setPhaseCenterSystem(source.getPhaseCenterSystem());
        target.setProgramName(source.getProgramName());

        target.setReceiverNumber(source.getReceiverNumber());
        target.setReceiverType(source.getReceiverType());
        target.setReceiverVersion(source.getReceiverVersion());
        target.setRunByName(source.getRunByName());

        target.setSignalStrengthUnit(source.getSignalStrengthUnit());
        target.setSatelliteSystem(source.getSatelliteSystem());
        target.setStationInformation(source.getStationInformation());

        target.setTFirstObs(source.getTFirstObs());
        target.setTLastObs(source.getTLastObs());


        Field typeObsMapField;
        try {
            typeObsMapField = target.getClass().getDeclaredField("mapTypeObs");
        } catch (NoSuchFieldException e) {
            throw new ApplicationException(e.getMessage());
        }
        typeObsMapField.setAccessible(true);
        Map<SatelliteSystem, List<ObservationType>> typeObsMap = source.getTypeObs();
        try {
            typeObsMapField.set(target, typeObsMap);
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e.getMessage());
        }

    }
}
