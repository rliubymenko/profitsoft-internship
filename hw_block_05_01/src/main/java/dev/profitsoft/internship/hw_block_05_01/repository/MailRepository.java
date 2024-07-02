package dev.profitsoft.internship.hw_block_05_01.repository;

import dev.profitsoft.internship.hw_block_05_01.data.MailData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface MailRepository extends ElasticsearchRepository<MailData, String> {

    Stream<MailData> findAllByStatus(String status);
}
