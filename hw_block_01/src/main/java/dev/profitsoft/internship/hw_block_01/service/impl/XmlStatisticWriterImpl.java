package dev.profitsoft.internship.hw_block_01.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.profitsoft.internship.hw_block_01.dto.Item;
import dev.profitsoft.internship.hw_block_01.exception.XmlWriterException;
import dev.profitsoft.internship.hw_block_01.service.XmlStatisticWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Class responsible for writing statistics to XML file
 */
public class XmlStatisticWriterImpl implements XmlStatisticWriter {

    public static final String STATISTICS = "statistics";
    private final Path resultPath;
    private final XmlMapper xmlMapper;

    public XmlStatisticWriterImpl(Path resultPath) {
        this.resultPath = resultPath;
        this.xmlMapper = new XmlMapper();
    }

    /**
     * Write statistics about items to XML file
     *
     * @param items array of items containing statistic information about an attribute
     */
    @Override
    public void write(List<Item> items) {
        StandardOpenOption[] options = new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(resultPath, StandardCharsets.UTF_8, options)) {
            xmlMapper.writer().withRootName(STATISTICS).writeValue(bufferedWriter, items);
        } catch (IOException e) {
            throw new XmlWriterException("Unable to write items to XML file: " + resultPath);
        }
    }
}
