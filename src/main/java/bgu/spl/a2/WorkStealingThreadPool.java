package bgu.spl.a2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

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
    private List<Deque<Task>> queues;

    VersionMonitor getVm() {
        return vm;
    }

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
        queues = new ArrayList<>();
        vm = new VersionMonitor();

        for (int i=0; i< nthreads; i++) {
            processors.add(i, new Processor(i, this));
            queues.add(i, new ConcurrentLinkedDeque<>());
        }
    }

    List<Processor> getProcessors() {
        return processors;
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {

        //TODO: supposed to be random??
//        processors.get(0).addTask(task);
//        vm.inc();
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

    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

}
