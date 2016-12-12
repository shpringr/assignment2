package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VersionMonitorTest {

    VersionMonitor versionMonitor;

     public VersionMonitorTest()
     {
         versionMonitor =  new VersionMonitor();
     }

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

    @Test public void testAwait() throws InterruptedException {

        versionMonitor.await(2);
        assertSame(versionMonitor.getVersion(), 2);
    }

    @Test public void testAwaitSameVersion() throws InterruptedException {

        versionMonitor.inc();
        versionMonitor.inc();
        versionMonitor.await(2);
        assertSame(versionMonitor.getVersion(), 2);
    }

    @After
    public void tearDown() throws Exception {

    }
}
