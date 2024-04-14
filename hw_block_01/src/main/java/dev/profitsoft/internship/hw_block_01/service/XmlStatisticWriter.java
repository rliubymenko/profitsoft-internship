package dev.profitsoft.internship.hw_block_01.service;

import dev.profitsoft.internship.hw_block_01.dto.Item;

import java.util.List;

public interface XmlStatisticWriter {
    void write(List<Item> items);
}
