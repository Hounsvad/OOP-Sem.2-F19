/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation;

import client.communication.CommunicationInterface;
import client.communication.CommunicationInterfaceImpl;
import client.presentation.utils.credentials.CredentialContainer;
import java.util.Arrays;
import java.util.List;

/**
 * A Singleton class
 *
 * @author Oliver
 */
public class CommunicationHandler {

    private MainFXMLController mainFXMLController;

    /**
     * The CommunicationHandler
     */
    private static CommunicationHandler communicationHandler;

    /**
     * The full name of the user currently signed in
     */
    private String name;

    /**
     * The CommunicationInterface
     */
    private CommunicationInterface communicationInterface;

    /**
     * Constructs the CommunicationHandler
     */
    private CommunicationHandler() {
        communicationInterface = new CommunicationInterfaceImpl();
    }

    /**
     * Creates an instance of the CommunicationHandler if none exists
     *
     * @return The instanceof the CommunicationHandler
     */
    public static CommunicationHandler getInstance() {
        if (communicationHandler == null) {
            communicationHandler = new CommunicationHandler();
        }
        return communicationHandler;
    }

    public void setMainFXMLController(MainFXMLController mainFXMLController) {
        this.mainFXMLController = mainFXMLController;
    }

    /**
     * Sends the query to the database
     *
     * @param input The query for the database
     * @return The data from the database
     */
    public List<String[]> sendQuery(String... input) {

        String[] query = Arrays.copyOf(input, input.length);
        List<String[]> returnVariable;
        if (input[0].equals("login")) {

            returnVariable = communicationInterface.sendQuery(query);
            if (!returnVariable.isEmpty()) {
                if (returnVariable.get(0)[0].equalsIgnoreCase("error")) {
                    {
                        System.err.println(returnVariable.get(0)[1]);
                        System.exit(0);
                    }
                }
                name = returnVariable.get(0)[0];
            } else {
                System.err.println("Return from login query was empty");
                System.exit(0);

            }
        } else {
            mainFXMLController.setSpinner(true);
            query = new String[input.length + 2];
            System.arraycopy(input, 1, query, 3, input.length - 1);
            query[0] = input[0];
            query[1] = CredentialContainer.getInstance().getUsername();
            query[2] = CredentialContainer.getInstance().getPassword();
            returnVariable = communicationInterface.sendQuery(query);
            mainFXMLController.setSpinner(false);
        }
        return returnVariable;
    }

    /**
     * @return The name of the user
     */
    public String getName() {
        return name;
    }
}
