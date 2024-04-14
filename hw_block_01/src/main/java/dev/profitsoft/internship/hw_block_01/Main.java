package dev.profitsoft.internship.hw_block_01;

import dev.profitsoft.internship.hw_block_01.service.JsonStatisticService;
import dev.profitsoft.internship.hw_block_01.service.XmlStatisticWriter;
import dev.profitsoft.internship.hw_block_01.service.impl.JsonStatisticServiceImpl;
import dev.profitsoft.internship.hw_block_01.service.impl.XmlStatisticWriterImpl;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Main method needs to run the process of parsing jsons
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    private static final String RESULT_FILE_EXTENSION = ".xml";
    private static final String RESULT_FILE_PREFIX = "statistics_by_";
    private static final int DEFAULT_THREAD_COUNT = 8;

    /**
     * @param directoryPath path to the directory with json files
     * @param attribute     attribute, which we want to parse and gather statistics
     * @param threadCount   count of threads in change of parsing json files
     */
    public void runTask(String directoryPath, String attribute, int threadCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        Path path = Path.of(directoryPath, RESULT_FILE_PREFIX + attribute + RESULT_FILE_EXTENSION);
        XmlStatisticWriter xmlWriter = new XmlStatisticWriterImpl(path);
        JsonStatisticService statisticService = new JsonStatisticServiceImpl(executorService, xmlWriter);
        statisticService.collectStatistics(directoryPath, attribute);
    }

    /**
     * Start point of the program
     *
     * @param args array containing directory path and attribute name
     */
    public static void main(String[] args) {
        String directoryPath = args[0];
        String attributeName = args[1];

        if (StringUtils.isBlank(directoryPath)) {
            log.severe("Directory path cannot be empty or blank");
        } else if (StringUtils.isBlank(attributeName)) {
            log.severe("Attribute name cannot be empty or blank");
        } else {
            Main main = new Main();
            main.runTask(directoryPath, attributeName, DEFAULT_THREAD_COUNT);
        }
    }
}
