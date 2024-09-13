package in.mohit.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ApiResponse implements Serializable {
    private HttpStatus status;
    private String statusMessage;
    private String response;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ApiResponse(HttpStatus status, String statusMessage, String response) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.response = response;
    }
}
