package bgu.spl.a2;

import static java.lang.Thread.*;

class SimpleRunnable implements Runnable {
    private String name;

    SimpleRunnable(String name) {
        this.name = name;
    }

    /**
     * Main lifecycle
     */
    public void run() {
        for (int i = 0; i < 50; i++) {
            System.out.println("RUNNABLE:" + this.name);
            try {
                sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}