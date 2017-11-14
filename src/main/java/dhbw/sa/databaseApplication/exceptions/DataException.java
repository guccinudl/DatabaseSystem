package dhbw.sa.databaseApplication.exceptions;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class DataException extends RuntimeException{

    public DataException(String message) {
        super(message);
    }
}