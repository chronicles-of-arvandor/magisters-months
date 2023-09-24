package net.arvandor.magistersmonths.datetime;

public class MmMonth {

    private final String name;
    private final int startDay;
    private final int endDay;

    public MmMonth(String name, int startDay, int endDay) {
        this.name = name;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public String getName() {
        return name;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndDay() {
        return endDay;
    }

}
