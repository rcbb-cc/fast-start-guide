package cc.rcbb.mini.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * Main
 * </p>
 *
 * @author rcbb.cc
 * @date 2025/2/19
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
        ScheduleService scheduleService = new ScheduleService();
        scheduleService.schedule(() -> {
            System.out.println("A " + LocalDateTime.now().format(dateTimeFormatter));
        }, 1000);

        Thread.sleep(1000);

        scheduleService.schedule(() -> {
            System.out.println("B " + LocalDateTime.now().format(dateTimeFormatter));
        }, 2000);
    }

}
