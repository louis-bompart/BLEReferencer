package fr.louisbompart.blereferencer.app;

/**
 * Created by louis on 29/01/2016.
 */
public class Chip {
    private String serialNumber;
    private String minor;
    private String major;


    public Chip(String serialNumber, String minor, String major) {

        this.serialNumber = serialNumber;
        this.minor = minor;
        this.major = major;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getMinor() {
        return minor;
    }

    public String getMajor() {
        return major;
    }

    @Override
    public String toString() {
        return serialNumber+","+major+","+minor;
    }
}
