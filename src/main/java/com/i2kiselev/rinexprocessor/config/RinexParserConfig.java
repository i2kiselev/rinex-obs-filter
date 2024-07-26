package com.i2kiselev.rinexprocessor.config;

import org.orekit.data.ClasspathCrawler;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.files.rinex.observation.RinexObservationParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RinexParserConfig {

    private static final String OREKIT_DATA = "./orekit-data/tai-utc.dat";

    @Bean
    public RinexObservationParser getRinexParser() {
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new ClasspathCrawler(OREKIT_DATA));
        return new RinexObservationParser();
    }
}
