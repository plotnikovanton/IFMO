public interface SimpleDate extends Comparable<SimpleDate> {
    enum Month { JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE,
        JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER }
    int getDay();
    Month getMonth();
    int getYear();
    int from(SimpleDate date);
}