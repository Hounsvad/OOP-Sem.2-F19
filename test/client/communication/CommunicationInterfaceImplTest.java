/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nadinfariss
 */
public class CommunicationInterfaceImplTest {
    
    public CommunicationInterfaceImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendQuery method, of class CommunicationInterfaceImpl.
     */
    @Test
    public void testSendQuery() {
        System.out.println("sendQuery");
        String[] query = null;
        CommunicationInterfaceImpl instance = new CommunicationInterfaceImpl();
        List expResult = null;
        List result = instance.sendQuery(query);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkQuery method, of class CommunicationInterfaceImpl.
     */
    @Test
    public void testCheckQuery() {
        System.out.println("checkQuery");
        String[] query = null;
        CommunicationInterfaceImpl instance = new CommunicationInterfaceImpl();
        boolean expResult = false;
        boolean result = instance.checkQuery(query);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
