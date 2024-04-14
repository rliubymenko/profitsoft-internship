package dev.profitsoft.internship.hw_block_01;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.logging.Logger;

class MainTest {

    private static final String TEST_DATA_DIRECTORY = "src/test/resources";
    private static final String ATTRIBUTE_NAME = "courses";

    private static final Logger log = Logger.getLogger(MainTest.class.getName());

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 8})
    void shouldMeasureParsingTimeOnDifferentThreadPollConfigurations(int threadCount) {
        Main main = new Main();
        long startTime = System.currentTimeMillis();
        main.runTask(TEST_DATA_DIRECTORY, ATTRIBUTE_NAME, threadCount);
        long finishTime = System.currentTimeMillis() - startTime;
        log.info(String.format("Parsing time with %d thread(s): %d millis", threadCount, finishTime));
    }
}