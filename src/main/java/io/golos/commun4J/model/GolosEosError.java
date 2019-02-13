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
        private ErrorMessage[] details;

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

        public void setDetails(ErrorMessage[] details) {
            this.details = details;
        }

        public Error(int code, String name, String what, ErrorMessage[] details) {
            this.code = code;
            this.name = name;
            this.what = what;
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

    static class ErrorMessage{
        private String message;
        private String file;
        private int line_number;
        private  String method;

        public ErrorMessage(String message, String file, int line_number, String method) {
            this.message = message;
            this.file = file;
            this.line_number = line_number;
            this.method = method;
        }

        public int getLine_number() {
            return line_number;
        }

        public void setLine_number(int line_number) {
            this.line_number = line_number;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return "ErrorMessage{" +
                    "message='" + message + '\'' +
                    ", file='" + file + '\'' +
                    '}';
        }
    }
}
