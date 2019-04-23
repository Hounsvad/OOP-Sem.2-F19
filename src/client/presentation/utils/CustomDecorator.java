/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.utils;

import com.jfoenix.controls.JFXDecorator;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author Oliver
 */
public class CustomDecorator extends JFXDecorator {

    public CustomDecorator(Stage stage, Node node, boolean fullScreen, boolean max, boolean min) {
        super(stage, node, fullScreen, max, min);
        //make UnTraversable
        btnMin.setFocusTraversable(false);
        btnClose.setFocusTraversable(false);
        btnFull.setFocusTraversable(false);
        btnMax.setFocusTraversable(false);
        //Exit propperly
        btnClose.setOnAction((e) -> System.exit(0));
    }
}
