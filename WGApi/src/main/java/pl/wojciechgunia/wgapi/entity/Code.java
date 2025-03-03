package pl.wojciechgunia.wgapi.entity;

public enum Code
{
    SUCCESS("Operation completed successfully"),
    PERMIT("Access granted"),
    E1("Content not found"),
    E2("Operation failed"),
    E3("Not enough arguments"),
    E4("Element already exist"),
    A1("Log in failed"),
    A2("Data incorrect"),
    A3("Token is empty or invalid"),
    A4("User with name already exists"),
    A5("User with email already exists"),
    A6("User does not exist"),
    A7("Access denied");

    public final String label;
    Code(String label)
    {
        this.label = label;
    }
}
