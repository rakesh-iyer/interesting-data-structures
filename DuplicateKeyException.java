class DuplicateKeyException extends Exception {
    String exceptionString;

    DuplicateKeyException(String exceptionString) {
        this.exceptionString = exceptionString;
    }

    public String toString() {
        return exceptionString;
    }
}

