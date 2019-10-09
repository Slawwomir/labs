package issue.model;


public enum IssueStatus {
    OPEN("Do zrobienia"),
    IN_PROGRESS("W trakcie"),
    DONE("Zrobione");

    private String name;

    IssueStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
