package bgu.spl.a2;

import org.junit.Test;
import static org.junit.Assert.*;

class VersionMonitorTest {

    VersionMonitor versionMonitor = new VersionMonitor();

    @Test
    public void testGetVersion() {

        int currVersion = versionMonitor.getVersion();
        versionMonitor.inc();
        assertNotSame(currVersion, versionMonitor.getVersion());
    }

    @Test
    public void testInc() {
        int currVersion = versionMonitor.getVersion();
        versionMonitor.inc();
        versionMonitor.inc();
        assertEquals(currVersion + 2, versionMonitor.getVersion());
    }

    @Test public void testAwait() throws InterruptedException {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }
}
