package com.i2kiselev.rinexprocessor.util;

import com.i2kiselev.rinexprocessor.record.FormatType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static com.i2kiselev.rinexprocessor.util.Const.*;

@Slf4j
public class CliUtils {


    public static final String NULLIFY = "nullify";
    public static final String REMOVE = "remove";

    public static Options getOptions() {
        Options options = new Options();

        Option inputFile = Option.builder("in")
                .longOpt(INPUT_PATH)
                .hasArg()
                .desc("Path of input Rinex file version 3.03")
                .required(true)
                .build();
        Option outputFile = Option.builder("out")
                .longOpt(OUTPUT_PATH)
                .hasArg()
                .desc("Path of output (filtered) Rinex file")
                .required(true)
                .build();

        Option formatType = Option.builder("f")
                .longOpt(FORMAT_TYPE)
                .hasArg()
                .desc("Format type of output file, 'nullify' option nullifies values of filtered observation, 'remove' deletes observation from dataset completely ")
                .required(false)
                .build();

        Option beidou = Option.builder("C")
                .longOpt(BEIDOU)
                .hasArg()
                .desc("OBS Components for Beidou satellites")
                .required(false)
                .build();
        Option galileo = Option.builder("E")
                .longOpt(GALILEO)
                .hasArg()
                .desc("OBS Components for Galileo satellites")
                .required(false)
                .build();
        Option gps = Option.builder("G")
                .longOpt(GPS)
                .hasArg()
                .desc("OBS Components for GPS satellites")
                .required(false)
                .build();
        Option irnss = Option.builder("I")
                .longOpt(IRNSS)
                .hasArg()
                .desc("OBS Components for IRNSS satellites")
                .required(false)
                .build();
        Option qzss = Option.builder("J")
                .longOpt(QZSS)
                .hasArg()
                .desc("OBS Components for QZSS satellites")
                .required(false)
                .build();
        Option mixed = Option.builder("M")
                .longOpt(MIXED)
                .hasArg()
                .desc("OBS Components for mixed satellites")
                .required(false)
                .build();
        Option glonass = Option.builder("R")
                .longOpt(GLONASS)
                .hasArg()
                .desc("OBS Components for Glonass satellites")
                .required(false)
                .build();
        Option sbas = Option.builder("S")
                .longOpt(SBAS)
                .hasArg()
                .desc("OBS Components for SBAS satellites")
                .required(false)
                .build();

        options.addOption(inputFile);
        options.addOption(outputFile);
        options.addOption(formatType);
        options.addOption(beidou);
        options.addOption(galileo);
        options.addOption(glonass);
        options.addOption(gps);
        options.addOption(irnss);
        options.addOption(qzss);
        options.addOption(mixed);
        options.addOption(sbas);
        return options;
    }

    public static FormatType getFormatByString(String format) {
        if (format == null) {
            return FormatType.NULLIFY;
        }
        if (format.equals(NULLIFY)) {
            return FormatType.NULLIFY;
        }
        if (format.equals(REMOVE)) {
            return FormatType.REMOVE;
        }
        log.warn("Unknown format type, using NULLIFY, passed value: {}", format);
        return FormatType.NULLIFY;
    }
}
