package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class VersionMonitorTest {

    VersionMonitor versionMonitor;

    @Before
    public void setUp() {
        versionMonitor= new VersionMonitor();
    }

    @Test
    public void testGetVersion() {

        int currVersion = versionMonitor.getVersion();
        versionMonitor.inc();
        assertNotSame("get version is not true",currVersion, versionMonitor.getVersion());
        versionMonitor.setVersionNumber(3);
        assertSame("the get version should be 3",3,versionMonitor.getVersion());
    }

    @Test
    public void testInc() {
        int currVersion = versionMonitor.getVersion();
        versionMonitor.inc();
        versionMonitor.inc();
        assertEquals("version should be 2",currVersion + 2, versionMonitor.getVersion());
    }

    @Test public void testAwait()
    {
        int begin =  versionMonitor.getVersion();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    versionMonitor.await(begin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        versionMonitor.inc();
        try {
            thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertNotSame(begin, versionMonitor.getVersion());
    }

    @After
    public void tearDown() throws Exception {
        versionMonitor = null;

    }
}
