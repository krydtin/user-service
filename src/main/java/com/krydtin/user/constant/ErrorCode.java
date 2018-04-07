package com.krydtin.user.constant;

public class ErrorCode {

    public enum Registration {

        DUPLICATE_USERNAME("1000"),
        MINIMUM_INCOME("1001");
        private final String code;

        private Registration(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public enum Authentication {
        INVALID_TOKEN("2000"),
        INVALID_USERNAME_PASSWORD("2001");

        private final String code;

        private Authentication(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    
    public enum DataNotFound {
        DATA_NOT_FOUND("3000");

        private final String code;

        private DataNotFound(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    
    public enum Validation {
        VALIDATION_ERROR("4000");

        private final String code;

        private Validation(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
