package ru.demo.exception;

public class ServiceException extends RuntimeException{
    public final static String INTERNAL_SERVER_ERROR = "Внутренняя ошибка сервера. %s. Код: %s";
    private final IErrorCode errorCode;

    public ServiceException(IErrorCode errorCode, String... args) {
        super(String.format(errorCode.getMessage(), args));
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return String.format(INTERNAL_SERVER_ERROR, super.getMessage(), errorCode.getCode());
    }
}
