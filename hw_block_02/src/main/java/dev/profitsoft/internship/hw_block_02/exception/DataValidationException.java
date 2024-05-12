package dev.profitsoft.internship.hw_block_02.exception;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Field validation exception
 */
@StandardException
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataValidationException extends RuntimeException {
}