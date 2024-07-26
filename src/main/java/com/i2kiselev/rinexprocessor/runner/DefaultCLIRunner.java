package com.i2kiselev.rinexprocessor.runner;

import com.i2kiselev.rinexprocessor.exception.ApplicationException;
import com.i2kiselev.rinexprocessor.record.FormatType;
import com.i2kiselev.rinexprocessor.record.TypeMatrix;
import com.i2kiselev.rinexprocessor.service.RinexService;
import com.i2kiselev.rinexprocessor.util.CliUtils;
import com.i2kiselev.rinexprocessor.util.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@ConditionalOnProperty(value = "app.cli.runner.stub", havingValue = "false", matchIfMissing = true)
@Slf4j
public class DefaultCLIRunner {

    private final RinexService rinexService;

    public DefaultCLIRunner(RinexService rinexService) {
        this.rinexService = rinexService;
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
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
            FormatType formatByString = CliUtils.getFormatByString(format);
            log.info("Starting processing, input file : {}", input);
            log.info("Type configuration : {}, format : {}", typeMatrix, formatByString);
            rinexService.completeRequest(input, output, typeMatrix, formatByString);
            log.info("File processed successfully, path : {}", output);
        };
    }
}
