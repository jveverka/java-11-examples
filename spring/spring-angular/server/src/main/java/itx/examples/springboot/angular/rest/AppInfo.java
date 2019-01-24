package itx.examples.springboot.angular.rest;

public class AppInfo {

    private final String name;
    private final String version;
    private final long timeStamp;

    public AppInfo(String name, String version, long timeStamp) {
        this.name = name;
        this.version = version;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
