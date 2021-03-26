package http;

public interface Response {

    public enum RESPONSE_RESULT {
        success, failed;
    }

    public enum STANDARD {
        results, responseData, responseData1, array, result, error, data, message, store, mall;
    }
}
