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
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.gnome.gtk.Gtk;
import org.gnome.notify.Notification;
import org.gnome.notify.Notify;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.s3s3l.resource.JacksonHelper;
import com.s3s3l.resource.JacksonUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
    private static final Platform platform = Platform.parse(System.getProperty("os.name"));
    private static final Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
    private static final Queue<Notification> notificationQueue = new LinkedBlockingQueue<>();
    private static final LinkedBlockingQueue<String> searchQueue = new LinkedBlockingQueue<>();
    private static TrayIcon trayIcon;
    private static Notification currentNotification;
    private static String lastKeyWord;
    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("css/test.css");
        WebView webview = new WebView();
        WebEngine engine = webview.getEngine();
        engine.load(FileUtils.getFirstExistFile("index.html").toURI().toURL().toExternalForm());
        ((Group) scene.getRoot()).getChildren().add(webview);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        webview.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        webview.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws InterruptedException, IOException, AWTException {
        launch(args);
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
        notificationQueue.clear();
        String url = api.concat(keyWord);
        System.out.println(url);
        List<Item> items = json.convert(doGet(url), new TypeReference<List<Item>>() {
        });
        System.out.println(json.prettyPrinter().toJsonString(items));
        if (currentNotification != null) {
            currentNotification.close();
        }
        for (Item item : items) {
            buildNotify(platform, item);
        }
    }

    private static void buildNotify(Platform platform, Item item) throws AWTException {
        switch (platform) {
            case Windows:
                if (trayIcon == null) {
                    SystemTray tray = SystemTray.getSystemTray();
                    Image image = Toolkit.getDefaultToolkit().createImage("eve_icon.jpg");
                    trayIcon = new TrayIcon(image, "EVE Helper");
                    trayIcon.setImageAutoSize(true);
                    trayIcon.setToolTip("EVE Helper");
                    tray.add(trayIcon);
                }
                if (SystemTray.isSupported()) {
                    trayIcon.displayMessage(item.getTypename(),
                            String.format("买入价格:\t%s isk\n卖出价格:\t%s isk\n时间:\t%s\n描述:\t%s", df.format(item.getSell()),
                                    df.format(item.getBuy()), item.getTime() == null ? "" : item.getTime(),
                                    item.getDescription()),
                            MessageType.INFO);
                } else {
                    System.err.println("System tray not supported!");
                }
                break;
            case Linux:
                if (!Gtk.isInitialized()) {
                    Gtk.init(new String[] {});
                    Notify.init("EVE Helper");
                }
                Notification myNotification = new Notification(item.getTypename(),
                        String.format("买入价格:\t%s isk\n卖出价格:\t%s isk\n时间:\t%s\n描述:\t%s", df.format(item.getSell()),
                                df.format(item.getBuy()), item.getTime() == null ? "" : item.getTime(),
                                item.getDescription()),
                        null);
                // myNotification.connect(new Closed() {
                // @Override
                // public void onClosed(Notification paramNotification) {
                // System.out.println("Notification closed");
                // showNotify();
                // }
                // });
                notificationQueue.offer(myNotification);
                break;
            default:
                System.err.println("Operation System not support.");
                break;
        }
    }

    private static void showNotify() {
        Notification notification = notificationQueue.poll();
        if (notification != null) {
            currentNotification = notification;
            notification.show();
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
