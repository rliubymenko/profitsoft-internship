package dev.profitsoft.internship.hw_block_05_01.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Getter
@Setter
@Builder
@Document(indexName = "messages")
public class MailData {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Field(type = FieldType.Long)
    private Long attemptNumber;

    @Field(type = FieldType.Date)
    private Instant attemptDate;
}
