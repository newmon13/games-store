package dev.jlipka.recrutly.gamesstore.infra.error;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:error-codes.properties")
public class ErrorCodeResolver {

    private final Environment environment;

    public ErrorCodeResolver(Environment environment) {
        this.environment = environment;
    }

    public ErrorDetails resolve(ErrorCode errorCode) {
        int code = errorCode.getCode();
        String message = environment.getProperty(code + ".message");
        String cause = environment.getProperty(code + ".cause");
        String action = environment.getProperty(code + ".action");
        return new ErrorDetails(code, message, cause, action);
    }
}