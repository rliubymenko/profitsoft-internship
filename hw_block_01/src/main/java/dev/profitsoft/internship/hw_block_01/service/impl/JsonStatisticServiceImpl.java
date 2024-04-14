package dev.profitsoft.internship.hw_block_01.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import dev.profitsoft.internship.hw_block_01.dto.Item;
import dev.profitsoft.internship.hw_block_01.exception.JsonAttributeParseException;
import dev.profitsoft.internship.hw_block_01.service.JsonStatisticService;
import dev.profitsoft.internship.hw_block_01.service.XmlStatisticWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Class responsible for parsing JSON files and  gathering statistics
 */
public class JsonStatisticServiceImpl implements JsonStatisticService {

    private static final Logger log = Logger.getLogger(JsonStatisticServiceImpl.class.getName());

    private static final String COMMA = ",";
    private static final String JSON_EXTENSION = "*.json";
    private final JsonFactory jsonFactory;
    private final ExecutorService executorService;
    private final XmlStatisticWriter statisticWriter;

    public JsonStatisticServiceImpl(ExecutorService executorService, XmlStatisticWriter statisticWriter) {
        this.executorService = executorService;
        this.statisticWriter = statisticWriter;
        this.jsonFactory = new JsonFactory();
    }

    /**
     * Asynchronously parse and collect statistics from json files
     *
     * @param directoryPath path to the directory with json files
     * @param attribute     attribute, which we want to parse and gather statistics
     */
    public void collectStatistics(String directoryPath, String attribute) {
        Path path = Paths.get(directoryPath);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, JSON_EXTENSION)) {
            List<CompletableFuture<Map<String, Long>>> parseFileTasks = StreamSupport.stream(directoryStream.spliterator(), false)
                    .map(currentFileName -> parseFile(attribute, currentFileName))
                    .toList();

            CompletableFuture.allOf(parseFileTasks.toArray(new CompletableFuture[0]))
                    .thenApplyAsync(future -> parseFileTasks.stream()
                            .map(CompletableFuture::join)
                            .toList())
                    .thenAcceptAsync(itemMap -> {
                        List<Item> groupedItems = groupItems(itemMap);
                        statisticWriter.write(groupedItems);
                        executorService.shutdown();
                    })
                    .join();
        } catch (IOException e) {
            throw new JsonAttributeParseException("Cannot parse files in directory: " + directoryPath);
        }
    }

    /**
     * Group statistics by attribute name
     *
     * @param mapStream stream of Maps divided to each parsed file
     * @return the list of grouped items
     */
    private List<Item> groupItems(List<Map<String, Long>> mapStream) {
        return mapStream.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                .entrySet()
                .stream()
                .map(entry -> new Item(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(Item::count).reversed())
                .toList();
    }

    /**
     * Prepare CompletableFutures with parseAttribute task for each file
     *
     * @param attribute       attribute to parse
     * @param currentFileName current file name
     * @return CompletableFuture of Map grouped by attribute name
     */
    private CompletableFuture<Map<String, Long>> parseFile(String attribute, Path currentFileName) {
        return CompletableFuture.supplyAsync(() -> {
            try (BufferedReader reader = Files.newBufferedReader(currentFileName); JsonParser jsonParser = jsonFactory.createParser(reader)) {
                return parseAttribute(attribute, jsonParser);
            } catch (JsonAttributeParseException | IOException e) {
                throw new JsonAttributeParseException("Cannot parse file: " + currentFileName.getFileName());
            }
        }, executorService);
    }

    /**
     * Parse attribute from json file
     *
     * @param attribute  the attribute to parse
     * @param jsonParser the parser of the json file
     * @return Map grouped by attribute name
     */
    private Map<String, Long> parseAttribute(String attribute, JsonParser jsonParser) {
        try {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new JsonAttributeParseException("Expected json content to be an array");
            }

            List<String> attributes = new ArrayList<>();

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                if (attribute.equals(jsonParser.currentName())) {
                    jsonParser.nextToken();
                    String attributeValue = jsonParser.getText();
                    String[] splitAttributes = attributeValue.split(COMMA);
                    String[] strippedAttributes = StringUtils.stripAll(splitAttributes);
                    attributes.addAll(List.of(strippedAttributes));
                }
            }

            return attributes.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (IOException e) {
            throw new JsonAttributeParseException("Error while parsing json content");
        }
    }
}
