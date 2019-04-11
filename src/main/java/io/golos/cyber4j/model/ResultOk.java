package io.golos.cyber4j.model;

public class ResultOk {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResultOk{" +
                "status='" + status + '\'' +
                '}';
    }
}
