/** 
 * Project Name:eve-helper 
 * File Name:Selector.java 
 * Package Name:com.s3s3l.eve.component 
 * Date:May 31, 20172:22:43 PM 
 * Copyright (c) 2017, kehewei@hellobike.com All Rights Reserved. 
 * 
*/

package com.s3s3l.eve.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.s3s3l.eve.FileUtils;
import com.s3s3l.resource.JacksonUtil;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * <p>
 * </p>
 * ClassName:Selector <br>
 * Date: May 31, 2017 2:22:43 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class FlatSelector extends VBox {

    @FXML
    private TextField textField;
    @FXML
    private VBox dropList;

    private List<Object> data = new ArrayList<>();
    private String val;
    private String value = "value";
    private String name = "name";
    private final List<Li> items = new ArrayList<>();
    private AtomicBoolean dropListShowing = new AtomicBoolean(false);

    public FlatSelector() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(
                    FileUtils.getFirstExistFile("component/FlatSelector.fxml").toURI().toURL());
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
            this.getStylesheets()
                    .add(FileUtils.getFirstExistFile("component/css/flat-selector.css").toURI().toURL().toExternalForm());
            dropList.setVisible(false);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        Map<String, String> data1 = new HashMap<>();
        data1.put("name", "吉他");
        data1.put("value", "1");
        Map<String, String> data2 = new HashMap<>();
        data2.put("name", "OSY");
        data2.put("value", "2");
        Map<String, String> data3 = new HashMap<>();
        data3.put("name", "OSY3");
        data3.put("value", "3");
        Map<String, String> data4 = new HashMap<>();
        data4.put("name", "OSY4");
        data4.put("value", "4");
        Map<String, String> data5 = new HashMap<>();
        data5.put("name", "OSY5");
        data5.put("value", "5");
        Map<String, String> data6 = new HashMap<>();
        data6.put("name", "OSY6");
        data6.put("value", "6");
        List<Object> data = new ArrayList<>();
        data.add(data1);
        data.add(data2);
        data.add(data3);
        data.add(data4);
        data.add(data5);
        data.add(data2);

        setData(data);

        textField.setOnMouseClicked((event) -> {
            if (dropListShowing.get()) {
                hideDropList();
            } else {
                showDropList();
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            showDropList();
            if(!oldValue.equals(newValue)){
                buildItems(newValue);
            }
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || !newValue) {
                hideDropList();
            }
        });
    }

    public void showDropList() {
        if (!dropListShowing.getAndSet(true)) {
            dropList.setVisible(true);
        }
    }

    public void hideDropList() {
        if (dropListShowing.getAndSet(false)) {
            dropList.setVisible(false);
        }
    }
    
    public String getVal(){
        return val;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
        buildItems(null);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        buildItems(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        buildItems(null);
    }

    public void setItems(List<Li> items) {
        items.clear();
        items.addAll(items);
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }

    public String getPromptText() {
        return promptTextProperty().get();
    }

    public void setPromptText(String value) {
        promptTextProperty().set(value);
    }

    public StringProperty promptTextProperty() {
        return textField.promptTextProperty();
    }

    private void buildItems(String filter) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(name) || data.isEmpty()) {
            return;
        }
        items.clear();
        dropList.getChildren().clear();
        for (Object obj : data) {
            if (obj == null) {
                throw new RuntimeException("Li value can not be null.");
            }
            JsonNode node = JacksonUtil.create().valueToTree(obj);
            if (node.isArray()) {
                throw new RuntimeException("Li data can not be an array.");
            }

            if (!node.has(this.name)) {
                throw new RuntimeException(
                        String.format("Can not find field [%s] in type [%s]", this.name, obj.getClass().getName()));
            }
            if (!node.has(this.value)) {
                throw new RuntimeException(
                        String.format("Can not find field [%s] in type [%s]", this.value, obj.getClass().getName()));
            }

            String name = node.get(this.name).asText();
            String value = node.get(this.value).asText();

            if (!StringUtils.isEmpty(filter) && !name.contains(filter)) {
                continue;
            }

            Li li = new Li();
            li.setText(name);
            li.setValue(value);
            li.getStyleClass().addAll("li", "text");
            li.setOnMouseClicked((event) -> {
                textField.setText(li.getText());
                hideDropList();
                val = li.getValue();
            });
            items.add(li);
        }
        dropList.getChildren().addAll(items);
    }
}
