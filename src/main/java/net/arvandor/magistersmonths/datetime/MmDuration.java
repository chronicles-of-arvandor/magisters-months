package net.arvandor.magistersmonths.datetime;

public class MmDuration {

    private final long seconds;

    public MmDuration(long seconds) {
        this.seconds = seconds;
    }

    public long getSeconds() {
        return seconds;
    }

    public int asDays() {
        return (int) (seconds / 86400);
    }

}
