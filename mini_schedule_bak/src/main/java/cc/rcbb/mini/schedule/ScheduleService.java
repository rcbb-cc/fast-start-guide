package cc.rcbb.mini.schedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * ScheduleService
 * </p>
 *
 * @author rcbb.cc
 * @date 2025/2/19
 */
public class ScheduleService {

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    Trigger trigger = new Trigger();

    public void schedule(Runnable task, long delay) {
        Job job = new Job();
        job.setTask(task);
        job.setDelay(delay);
        job.setStartTime(System.currentTimeMillis() + delay);
        trigger.queue.offer(job);
        trigger.wakeUp();
    }


    /**
     * 等待合适的时间，把对应的任务扔到线程池中
     */
    class Trigger {

        PriorityBlockingQueue<Job> queue = new PriorityBlockingQueue<>();

        Thread thread = new Thread(() -> {
            while (true) {
                // 无任务，一直睡眠
                while (queue.isEmpty()) {
                    LockSupport.park();
                }
                Job job = queue.peek();
                // 任务已经到期，执行任务
                if (job.getStartTime() <= System.currentTimeMillis()) {
                    job = queue.poll();
                    executorService.execute(job.getTask());
                    // 创建下一次的任务
                    Job nextJob = new Job();
                    nextJob.setTask(job.getTask());
                    nextJob.setDelay(job.getDelay());
                    nextJob.setStartTime(System.currentTimeMillis() + job.getDelay());
                    queue.offer(nextJob);
                } else {
                    // 睡眠到任务开始时间
                    LockSupport.parkUntil(job.getStartTime());
                }
            }
        });

        {
            thread.start();
        }

        void wakeUp() {
            LockSupport.unpark(thread);
        }
    }

}
