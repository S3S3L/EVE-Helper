/** 
 * Project Name:eve-helper 
 * File Name:Application.java 
 * Package Name:com.s3s3l.eve 
 * Date:May 11, 20173:41:23 PM 
 * Copyright (c) 2017, kehewei@hellobike.com All Rights Reserved. 
 * 
*/

package com.s3s3l.eve;

import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.s3s3l.resource.JacksonHelper;
import com.s3s3l.resource.JacksonUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <p>
 * </p>
 * ClassName:Application <br>
 * Date: May 11, 2017 3:41:23 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class EveHelper extends Application {
    private static final String api = "https://www.ceve-market.org/query/?search=";
    private static final JacksonHelper json = JacksonUtil.create();
    private static final DecimalFormat df = new DecimalFormat("#,###.00");
    private static final Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
    private static final LinkedBlockingQueue<String> searchQueue = new LinkedBlockingQueue<>();
    private static String lastKeyWord;
    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group(
                (Node) new FXMLLoader().load(new FileInputStream(FileUtils.getFirstExistFile("MarketSearch.fxml")))));
        scene.getStylesheets().add(FileUtils.getFirstExistFile("css/style.css").toURI().toURL().toExternalForm());
        ((Group) scene.getRoot()).getStyleClass().add("scene");
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.setOpacity(0.7);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) throws InterruptedException, IOException, AWTException {
        launch(args);
        // System.out.println(FileUtils.class.getResource("/"));
        // new Thread(() -> {
        // while (true) {
        // Transferable clipTf = sysClip.getContents(null);
        // if (clipTf != null) {
        // if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        // try {
        // String keyWord = (String)
        // clipTf.getTransferData(DataFlavor.stringFlavor);
        // if (keyWord.equals(lastKeyWord)) {
        // continue;
        // }
        // lastKeyWord = keyWord;
        // System.out.println(keyWord);
        // searchQueue.offer(keyWord);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
        // }).start();
        //
        // new Thread(() -> {
        // while (true) {
        // showNotify();
        // try {
        // Thread.sleep(5000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
        // }).start();
        //
        // while (true) {
        // doSearch(searchQueue.take());
        // }
    }

    private static void doSearch(String keyWord) throws IOException, AWTException {
        String url = api.concat(keyWord);
        System.out.println(url);
        List<Item> items = json.convert(doGet(url), new TypeReference<List<Item>>() {
        });
        System.out.println(json.prettyPrinter().toJsonString(items));
        for (Item item : items) {
        }
    }

    public static TreeNode doGet(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).addHeader("referer", "https://www.ceve-market.org/home/")
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            System.out.println(String.format("查询失败, %s", response.message()));
        }

        return json.toTreeNode(response.body().byteStream());
    }
}
