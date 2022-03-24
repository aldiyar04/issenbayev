package kz.iitu.itse1910.issenbayev.repository.search;

public enum SearchOperation {
    CONTAINS, EQUAL, GREATER_THAN, LESS_THAN;

    public static final char[] OPERATIONS = {':', '=', '>', '<'};

    public static SearchOperation get(char input) {
        switch (input) {
            case ':':
                return CONTAINS;
            case '=':
                return EQUAL;
            case '>':
                return GREATER_THAN;
            case '<':
                return LESS_THAN;
            default:
                throw new IllegalArgumentException("Invalid search operation: " + input);
        }
    }
}
