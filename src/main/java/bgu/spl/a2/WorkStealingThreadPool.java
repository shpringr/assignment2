package bgu.spl.a2;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ThreadLocalRandom;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {

    private List<Processor> processors;

    private List<Thread> threads;
    private List<ConcurrentLinkedDeque<Task>> queues;
    private VersionMonitor vm;

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */
    public WorkStealingThreadPool(int nthreads) {

        processors = new ArrayList<>();
        queues= new ArrayList<>();
        threads = new ArrayList<>();
        vm = new VersionMonitor();

        for (int i=0; i< nthreads; i++) {
            Processor currProcessor = new Processor(i, this);
            processors.add(i, currProcessor);
            queues.add(i, new ConcurrentLinkedDeque<>());
            threads.add(i, new Thread(currProcessor));
        }
    }

    VersionMonitor getVm() {
        return vm;
    }

    ConcurrentLinkedDeque<Task> getQueue(int id) {
        return queues.get(id);
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task)
    {
        int randomProcessor = ThreadLocalRandom.current().nextInt(0,processors.size());
        processors.get(randomProcessor).addTaskToQueue(task);
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException , UnsupportedOperationException{

        for (Thread thread : threads)
        {
            thread.interrupt();
        }
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start()
    {
        for (Thread thread : threads)
        {
            thread.start();
        }
    }

    List<Processor> getProcessors() {
        return processors;
    }
}