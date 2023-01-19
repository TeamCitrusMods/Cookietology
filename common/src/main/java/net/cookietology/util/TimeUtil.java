package net.cookietology.util;

public class TimeUtil {
    public static String formatTicksToTime(long ticks) {
        long totalHours = (ticks / 72000);
        ticks = ticks % 72000;

        long totalMinutes = (ticks / 1200);
        ticks = ticks % 1200;

        long totalSeconds = (ticks / 20);

        return String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);
    }
}
