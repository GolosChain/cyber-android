package io.golos.commun4J.model;

public class GolosEosError {
    private int code;
    private String message;
    private Error error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GolosEosError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", error=" + error +
                '}';
    }

    static class Error {
        private int code;
        private String name;
        private String what;
        private Object details;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWhat() {
            return what;
        }

        public void setWhat(String what) {
            this.what = what;
        }

        public Object getDetails() {
            return details;
        }

        public void setDetails(Object details) {
            this.details = details;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "code=" + code +
                    ", name='" + name + '\'' +
                    ", what='" + what + '\'' +
                    ", details=" + details +
                    '}';
        }
    }
}
