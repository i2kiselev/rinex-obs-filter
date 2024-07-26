package com.i2kiselev.rinexprocessor.service;

import com.i2kiselev.rinexprocessor.record.FormatType;
import com.i2kiselev.rinexprocessor.record.TypeMatrix;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class RinexServiceTest {

    @TempDir
    File tempDir;

    public static final String FILE_PATH = "./src/test/resources/SEPT1151.23O";
    @Autowired
    RinexService rinexService;

    @Test
    void testFileWrite() throws IOException {
        List<String> input = new ArrayList<>();
        input.add("C,C1P,L1P,C5P,L5P,C2I");
        TypeMatrix typeMatrix = new TypeMatrix(input);
        File file = rinexService.completeRequest(FILE_PATH, Paths.get(tempDir.getPath(), "test-result.txt").toString(), typeMatrix, FormatType.REMOVE);
        assertTrue(file.exists());
        assertNotEquals(0, Files.size(file.toPath()));
    }
}
