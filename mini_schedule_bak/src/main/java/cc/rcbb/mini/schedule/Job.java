package cc.rcbb.mini.schedule;

/**
 * <p>
 * Job
 * </p>
 *
 * @author rcbb.cc
 * @date 2025/2/19
 */
public class Job implements Comparable<Job> {

    private Runnable task;

    private long startTime;

    private long delay;

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public int compareTo(Job o) {
        return Long.compare(this.startTime, o.startTime);
    }
}
