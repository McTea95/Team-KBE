package de.htw.ai.kbe.beleg1;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Utils utils = new Utils();
        CheckResult result = utils.loadClass("de.htw.ai.kbe.AppT");
        Assert.assertTrue(result.getMethodCount() == 5);
        Assert.assertTrue(result.getRunMeMethodCount() == 2);        
        Assert.assertTrue(result.getNotInvokeableMethodNames().contains("methodE"));
        Assert.assertTrue(result.getRunMeMethodNames().contains("methodA"));
        
        Assert.assertTrue(utils.createReportFile("Test.txt", result));
    }
}
