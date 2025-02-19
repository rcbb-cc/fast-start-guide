# 一个简单的定时任务实现

B 站看到大佬的视频，自己过来实操学习一下，做个记录留存。

[学java的生生-【不背八股】70行代码实现定时任务](https://www.bilibili.com/video/BV19jffYVE3C/?vd_source=b13a31d63cf084a4bc7e9d71d9c78835#reply113889094272315)


## 思路

目标。
- 任务A，每隔1秒执行一次，输出当前的时间。
- 任务B，每隔2秒执行一次，输出当前的时间。

```java
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
```


核心代码：ScheduleService

```java
public class ScheduleService {

    public void schedule(Runnable runnable, long delay) {
        
    }

}
```


