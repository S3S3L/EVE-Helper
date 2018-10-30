/** 
 * Project Name:eve-helper 
 * File Name:Button.java 
 * Package Name:com.s3s3l.eve.component 
 * Date:Jun 1, 20176:38:48 PM 
 * Copyright (c) 2017, kehw.zwei@gmail.com All Rights Reserved. 
 * 
*/

package com.s3s3l.eve.component;

import java.io.IOException;

import com.s3s3l.eve.FileUtils;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * <p>
 * </p>
 * ClassName:Button <br>
 * Date: Jun 1, 2017 6:38:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class FlatButton extends HBox {

    @FXML
    private Label label;

    public FlatButton() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(
                    FileUtils.getFirstExistFile("component/FlatButton.fxml").toURI().toURL());
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
            this.getStylesheets()
                    .add(FileUtils.getFirstExistFile("component/css/flat-button.css").toURI().toURL().toExternalForm());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setText(String text) {
        label.setText(text);
    }

    public String getText() {
        return label.getText();
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }

}
