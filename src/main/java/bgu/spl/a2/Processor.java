package bgu.spl.a2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;
    private Deque<Task> tasks;

    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
        tasks = new ArrayDeque<>();
    }

    @Override
    public void run() {
       while (true)
       {
           if (!tasks.isEmpty())
           {
               Task  t = tasks.pollFirst();
               t.start();
           }
           else
           {
               stealTasks();
           }
       }
    }

    private void stealTasks() {

        int nextToSteal = (id+1) % pool.getProcessors().size();
        boolean isFound = false;

        while (!isFound && nextToSteal != id)
        {
            Processor victim  = pool.getProcessors().get(nextToSteal);

            if (!victim.tasks.isEmpty())
            {
                isFound = true;
                for (int i=0; i<victim.tasks.size()/2;i++)
                    this.tasks.addFirst(victim.tasks.pollLast());
            }
            else
            {
                nextToSteal = (nextToSteal+1) % pool.getProcessors().size();
            }
        }
    }
}
