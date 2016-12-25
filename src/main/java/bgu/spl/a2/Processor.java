package bgu.spl.a2;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;

    /**
     * constructor for this class
     * <p>
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     * <p>
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id   - the processor id (every processor need to have its own unique
     *             id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
    }

    @Override
    public void run() {

        try {
            ConcurrentLinkedDeque<Task> tasks;
            while (!Thread.interrupted()) {
                tasks = pool.getQueue(id);

                if (!tasks.isEmpty()) {
                    Task t = tasks.pollFirst();
                    pool.printProcessorStates("run after submit processor :" + id);
                    t.handle(this);

                } else {
                    if (!stealTasks())
                        pool.getVm().await(pool.getVm().getVersion());
                }

            }
        }
        catch (InterruptedException e){}

        System.out.println("Processor " + id + " interrupted!!");
    }


    private boolean stealTasks() {

        int nextToSteal = (id + 1) % pool.getProcessors().size();
        boolean isFound = false;

        while (!isFound && nextToSteal != id)
        {
            ConcurrentLinkedDeque<Task> victimQueue = pool.getQueue(nextToSteal);

            if (!victimQueue.isEmpty()) {
                isFound = true;
                int size = victimQueue.size();
                for (int i = 0; i < size / 2 && victimQueue.size() > 0; i++)
                {
                    Task task = victimQueue.pollLast();
                    pool.getQueue(id).addFirst(task);
                    String msg = "Processor " + id + " stole from " + nextToSteal + " task " ;
                       if (task.check instanceof int[])
                                msg += Arrays.toString((int[]) task.check);

                    pool.printProcessorStates(msg);
                }
            } else {
                nextToSteal = (nextToSteal + 1) % pool.getProcessors().size();
            }
        }

        return isFound;
    }

    void addTaskToQueue(Task task){

        pool.getQueue(id).addFirst(task);
        pool.getVm().inc();
        pool.printProcessorStates("spwan processor :" + id);
    }

    int getId() {
        return id;
    }
}
