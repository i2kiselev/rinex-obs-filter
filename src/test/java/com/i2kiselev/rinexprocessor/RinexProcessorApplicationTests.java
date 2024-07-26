package com.i2kiselev.rinexprocessor;

import com.i2kiselev.rinexprocessor.service.RinexParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RinexProcessorApplicationTests {

    @Autowired
    RinexParser rinexParser;

    @Test
    void contextLoads() {
    }

}
