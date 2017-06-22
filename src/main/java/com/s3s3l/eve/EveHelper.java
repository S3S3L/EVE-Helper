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
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.simplifiers.Simplifiers;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.s3s3l.resource.JacksonHelper;
import com.s3s3l.resource.JacksonUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

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
    private static double xOffset = 0;
    private static double yOffset = 0;
    private static double pinX = 0, pinY = 0;

    private static final AtomicBoolean control = new AtomicBoolean(false);
    private static final AtomicBoolean alt = new AtomicBoolean(false);
    private static final ITesseract tesseract = new Tesseract();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Scene scene = new Scene(new Group(
        // (Node) new FXMLLoader().load(new
        // FileInputStream(FileUtils.getFirstExistFile("MarketSearch.fxml")))));
        // scene.getStylesheets().add(FileUtils.getFirstExistFile("css/style.css").toURI().toURL().toExternalForm());
        // ((Group) scene.getRoot()).getStyleClass().add("scene");
        Rectangle cutedImg = new Rectangle(0, 0, 100, 100);
        Scene scene = new Scene(new Group(cutedImg));

        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case ALT:
                    alt.set(true);
                    break;
                case CONTROL:
                    control.set(true);
                    break;
                default:
                    break;
            }
        });
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
        // primaryStage.setOpacity(0.7);
        primaryStage.setResizable(true);
        primaryStage.show();

        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect.setFill(Color.WHITE);
        rect.setOpacity(0.8);

        Stage mask = new Stage(StageStyle.TRANSPARENT);
        mask.setResizable(false);
        // mask.setOpacity(0.3);
        mask.initModality(Modality.APPLICATION_MODAL);
        mask.setAlwaysOnTop(true);
        mask.initOwner(primaryStage);

        Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();

        Double screenWidth = Screen.getPrimary().getVisualBounds().getMaxX();
        Double screenHeight = Screen.getPrimary().getVisualBounds().getMaxY();

        System.out.println(screenWidth);
        System.out.println(screenHeight);
        System.out.println(monitor.getWidth());
        System.out.println(monitor.getHeight());

        Rectangle background = new Rectangle(screenWidth, screenHeight);
        BufferedImage screenShot = new Robot().createScreenCapture(new java.awt.Rectangle(monitor)).getSubimage(0, 0,
                screenWidth.intValue(), screenHeight.intValue());
        background.setFill(new ImagePattern(SwingFXUtils.toFXImage(screenShot, null)));

        Group root = new Group(background);
        root.getChildren().add(rect);

        Scene maskScene = new Scene(root, screenWidth, screenHeight);
        System.out.println(maskScene.getWidth());
        System.out.println(maskScene.getHeight());
        maskScene.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
            pinX = event.getSceneX();
            pinY = event.getSceneY();
            rect.setWidth(0);
            rect.setHeight(0);
        });
        maskScene.addEventFilter(MouseEvent.MOUSE_DRAGGED, (event) -> {
            rect.setLayoutX(Math.min(event.getSceneX(), pinX));
            rect.setLayoutY(Math.min(event.getSceneY(), pinY));
            rect.setWidth(Math.abs(event.getSceneX() - pinX));
            rect.setHeight(Math.abs(event.getSceneY() - pinY));
        });
        maskScene.addEventFilter(MouseEvent.MOUSE_RELEASED, (event) -> {
            System.out.println(doOCR(cutImage(screenShot, rect)));
            BufferedImage img = cutImage(screenShot, rect);
            cutedImg.setWidth(img.getWidth());
            cutedImg.setHeight(img.getHeight());
            primaryStage.setWidth(img.getWidth());
            primaryStage.setHeight(img.getHeight());
            cutedImg.setFill(new ImagePattern(SwingFXUtils.toFXImage(img, null)));

        });
        mask.setScene(maskScene);
        mask.show();
    }

    public static void main(String[] args) throws InterruptedException, IOException, AWTException {
        tesseract.setDatapath(FileUtils.getFullFilePath("classpath:/"));
        tesseract.setLanguage("eng");

        StringMetric stringMetric = StringMetricBuilder.with(StringMetrics.cosineSimilarity()).simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
                .simplify(Simplifiers.replaceNonWord()).build();
        launch(args);
    }

    private static BufferedImage cutImage(BufferedImage img, Rectangle rect) {
        System.out.println(rect.getLayoutX());
        System.out.println(rect.getLayoutY());
        System.out.println(rect.getWidth());
        System.out.println(rect.getHeight());
        BufferedImage cutedImg = img.getSubimage(new Double(Math.max(0, rect.getLayoutX())).intValue(),
                new Double(Math.max(0, rect.getLayoutY())).intValue(), new Double(rect.getWidth()).intValue(),
                new Double(rect.getHeight()).intValue());
        return cutedImg;
    }

    private static String doOCR(BufferedImage img) {
        try {
            return tesseract.doOCR(img);
        } catch (TesseractException e) {
            return "";
        }
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
