class KeyNotFoundException extends Exception {
    String exceptionString;

    KeyNotFoundException(String exceptionString) {
        this.exceptionString = exceptionString;
    }

    public String toString() {
        return exceptionString;
    }
}

