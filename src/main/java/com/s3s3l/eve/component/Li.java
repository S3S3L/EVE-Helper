/** 
 * Project Name:eve-helper 
 * File Name:Li.java 
 * Package Name:com.s3s3l.eve.component 
 * Date:May 31, 20176:33:10 PM 
 * Copyright (c) 2017, kehewei@hellobike.com All Rights Reserved. 
 * 
*/

package com.s3s3l.eve.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/**
 * <p>
 * </p>
 * ClassName:Li <br>
 * Date: May 31, 2017 6:33:10 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class Li extends Label {

    private StringProperty value = new SimpleStringProperty(this, "value", "");

    public String getValue() {
        return valueProperty().get();
    }

    public void setValue(String value) {
        valueProperty().set(value);
    }

    public StringProperty valueProperty() {
        return value;
    }
}
