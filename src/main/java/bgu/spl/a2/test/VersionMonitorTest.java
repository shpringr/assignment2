package bgu.spl.a2.test;

import bgu.spl.a2.VersionMonitor;
import org.junit.Before;
import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #testGetVersion()} once you have a version number, you can call
 * {@link #testAwait()} } with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #testInc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitorTest {


    public VersionMonitorTest(){

    }



    @Test public void testGetVersion() {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    @Before
    @Test public void testInc() {
        VersionMonitor v1 = new VersionMonitor();
        assertNull("The object are Null" ,v1);
        v1.inc();
        assertEquals("Failure - version not increment", 1 , v1.getVersion() );



        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    @Test public void testAwait() throws InterruptedException {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }
}
