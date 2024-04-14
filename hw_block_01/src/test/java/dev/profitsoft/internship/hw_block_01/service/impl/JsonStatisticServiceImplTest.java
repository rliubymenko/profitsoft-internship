package dev.profitsoft.internship.hw_block_01.service.impl;

import dev.profitsoft.internship.hw_block_01.exception.JsonAttributeParseException;
import dev.profitsoft.internship.hw_block_01.service.JsonStatisticService;
import dev.profitsoft.internship.hw_block_01.service.XmlStatisticWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class JsonStatisticServiceImplTest {

    private static final String TEST_DATA_DIRECTORY = "src/test/resources";
    private static final String ATTRIBUTE_NAME = "courses";
    private static final int TREAD_COUNT = 8;

    private static final String RESULT_FILE_EXTENSION = ".xml";
    private static final String RESULT_FILE_PREFIX = "statistics_by_";
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(TREAD_COUNT);
    }

    @Test
    void shouldFailOnWrongDirectory() {
        Path path = Path.of(TEST_DATA_DIRECTORY, RESULT_FILE_PREFIX + ATTRIBUTE_NAME + RESULT_FILE_EXTENSION);
        XmlStatisticWriter xmlWriter = new XmlStatisticWriterImpl(path);
        JsonStatisticService statisticService = new JsonStatisticServiceImpl(executorService, xmlWriter);

        Assertions.assertThrows(JsonAttributeParseException.class,
                () -> statisticService.collectStatistics("src/test/resources/in", ATTRIBUTE_NAME),
                "Cannot parse files in directory: src/test/resources/in"
        );
    }
}