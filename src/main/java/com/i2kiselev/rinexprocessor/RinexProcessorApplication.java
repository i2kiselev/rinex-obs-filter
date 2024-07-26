package com.i2kiselev.rinexprocessor;

import com.i2kiselev.rinexprocessor.exception.ApplicationException;
import com.i2kiselev.rinexprocessor.record.TypeMatrix;
import com.i2kiselev.rinexprocessor.service.RinexService;
import com.i2kiselev.rinexprocessor.util.CliUtils;
import com.i2kiselev.rinexprocessor.util.Const;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class RinexProcessorApplication implements CommandLineRunner {


    private final RinexService rinexService;

    public RinexProcessorApplication(RinexService rinexService) {
        this.rinexService = rinexService;
    }

    @Override
    public void run(String... args) {

        Options options = CliUtils.getOptions();
        CommandLineParser parser = new DefaultParser(false);
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException exception) {
            throw new ApplicationException("Exception when creating CLI parser, args : " + Arrays.toString(args), exception);
        }

        String inputStr = commandLine.getOptionValue(Const.INPUT_PATH);
        String input = StringUtils.replace(inputStr.substring(1, inputStr.length() - 1), "\\", "\\\\");
        String outputStr = commandLine.getOptionValue(Const.OUTPUT_PATH);
        String output = StringUtils.replace(outputStr.substring(1, outputStr.length() - 1), "\\", "\\\\");
        String format = commandLine.getOptionValue(Const.FORMAT_TYPE);
        TypeMatrix typeMatrix = new TypeMatrix(commandLine);
        rinexService.completeRequest(input, output, typeMatrix, CliUtils.getFormatByString(format));
    }

    public static void main(String[] args) {
        SpringApplication.run(RinexProcessorApplication.class, args);
    }

}
