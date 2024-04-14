package dev.profitsoft.internship.hw_block_01.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Main DTO class containing information with structure suitable to write to XML file as a result
 *
 * @param value value clause
 * @param count count clause
 */
@JsonAutoDetect
@JacksonXmlRootElement(localName = "item")
public record Item(@JsonProperty String value, @JsonProperty Long count) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;

        return new EqualsBuilder()
                .append(value, item.value)
                .append(count, item.count)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .append(count)
                .toHashCode();
    }
}
