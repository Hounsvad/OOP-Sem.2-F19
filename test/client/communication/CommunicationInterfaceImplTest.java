/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.communication;

import org.junit.Assert;
import org.junit.Test;
import server.communication.ServerController;

/**
 *
 * @author Hounsvad
 */
public class CommunicationInterfaceImplTest {

    public CommunicationInterfaceImplTest() {
    }

    /**
     * Test of sendQuery method, of class CommunicationInterfaceImpl.
     */
    @Test
    public void testSendQuery() {
        CommunicationInterface testInstance = new CommunicationInterfaceImpl();
        ServerController serverC = new ServerController();
        Thread server = new Thread(serverC, "ServerThread");
        server.start();
        Assert.assertTrue("UltraAdmin".equals(testInstance.sendQuery("login", "olnor18", "0ba8e1e5a89330ed3f159a67fd6c80ad345048b8ce84242bd0cb08b5c4834550").get(0)[0]));
        serverC.stop();
    }

}
