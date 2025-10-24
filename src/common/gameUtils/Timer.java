package common.gameUtils;

/* Simple formatting utility for elapsed seconds. */
public class Timer {
    public static String formatTime(long totalSec) {
        long min = totalSec / 60;
        long sec = totalSec % 60;
        return (min == 0) ? (sec + "sec") : (min + "min " + sec + "sec");
    }
}
