package uy.com.fing.ontoforms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiException extends RuntimeException {
    private int status;
    private String message;
}
