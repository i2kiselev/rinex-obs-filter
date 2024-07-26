package com.i2kiselev.rinexprocessor.record;

import com.i2kiselev.rinexprocessor.exception.ApplicationException;
import com.i2kiselev.rinexprocessor.util.Const;
import lombok.Data;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;
import org.orekit.gnss.SatelliteSystem;

import java.util.*;

@Data
public class TypeMatrix {

    private HashMap<SatelliteSystem, List<String>> satSystemConfigs;

    public TypeMatrix(List<String> input) {
        this.satSystemConfigs = parseInput(input);
    }

    public TypeMatrix(CommandLine commandLine) {
        this.satSystemConfigs = parseInputFromCLI(commandLine);
    }


    private HashMap<SatelliteSystem, List<String>> parseInput(List<String> input) {
        HashMap<SatelliteSystem, List<String>> systems = new HashMap<>();
        for (String system : input) {
            String[] split = StringUtils.split(system, ",");
            if (split.length == 0) {
                throw new ApplicationException("Incorrect config input format, input : " + system);
            }
            if (split.length == 1) {
                throw new ApplicationException("No columns selected for system, input : " + system);
            }

            String satSystemType = split[0];
            SatelliteSystem parsedSystemType = SatelliteSystem.parseSatelliteSystem(satSystemType);

            List<String> observationTypes = new ArrayList<>(Arrays.asList(split).subList(1, split.length));
            systems.put(parsedSystemType, observationTypes);
        }
        return systems;
    }

    private HashMap<SatelliteSystem, List<String>> parseInputFromCLI(CommandLine commandLine) {
        HashMap<SatelliteSystem, List<String>> systems = new HashMap<>();
        Option[] options = commandLine.getOptions();
        for (Option currentOption : options) {
            String optName = currentOption.getLongOpt();
            if (Const.INPUT_PATH.equals(optName)) {
                continue;
            }
            if (Const.OUTPUT_PATH.equals(optName)) {
                continue;
            }
            processOption(systems, currentOption);
        }

        return systems;
    }

    private void processOption(HashMap<SatelliteSystem, List<String>> map, Option option) {
        String satSystem = option.getOpt();
        SatelliteSystem parsedSystemType = SatelliteSystem.parseSatelliteSystem(satSystem);

        String types = option.getValue();
        types = types.substring(1, types.length() - 1);
        String[] typeList = StringUtils.split(types, ",");
        List<String> observationTypes = new ArrayList<>(Arrays.asList(typeList));
        map.put(parsedSystemType, observationTypes);
    }

    public List<String> getUsedObs(SatelliteSystem satelliteSystem) {
        return this.satSystemConfigs.get(satelliteSystem);
    }

    public Set<SatelliteSystem> getUsedSystems() {
        return this.satSystemConfigs.keySet();
    }

    @Override
    public String toString() {
        return "TypeMatrix{" +
                "satSystemConfigs=" + satSystemConfigs.toString() +
                '}';
    }
}
