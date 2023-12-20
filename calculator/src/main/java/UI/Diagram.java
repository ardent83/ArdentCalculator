package UI;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

public class Diagram extends Application {
    private final BorderPane root = new BorderPane();
    @Override
    public void start(Stage stage) {
        root.setBackground(new Background(new BackgroundFill(Color.rgb(0, 171, 179,0.8), new CornerRadii(30), Insets.EMPTY)));
        Button textField = display();
        StringBuilder exp = new StringBuilder();
        root.setTop(new VBox(stackPane(textField),diagram(textField,exp,buttonX())));
        Scene scene = new Scene(root,408,820);
        root.setLeft(buttons(textField, exp, scene));
        scene.getStylesheets().add("style.css");
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }
    private final TextField left = new TextField("-1");
    private final TextField right = new TextField("1");
    private HBox buttonX(){
        HBox hBox = new HBox();
        Button buttonX = new Button("x");
        buttonX.setId("buX");
        buttonX.setMinWidth(82);
        buttonX.setPrefHeight(46);
        left.setId("leftRight");
        right.setId("leftRight");
        Button buttonL = new Button("Min");
        Button buttonR = new Button("Max");
        buttonL.setId("labelLR");
        buttonR.setId("labelLR");
        buttonL.setMinWidth(82);
        buttonL.setPrefHeight(46);
        buttonR.setMinWidth(82);
        buttonR.setPrefHeight(46);
        hBox.getChildren().addAll(buttonX, buttonL, left, buttonR, right);
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.setPadding(new Insets(0,0,0,2));
        return hBox;
    }
    private StackPane diagram(Button textField, StringBuilder exp, HBox buttonX){
        StackPane pane = new StackPane();
        buttonX.getChildren().get(0).setOnMouseClicked(mouseEvent -> {
            textField.setText(textField.getText() + 'x');
            exp.append('x');
        });
        pane.setAlignment(Pos.BOTTOM_LEFT);
        pane.getChildren().addAll(chart(exp.toString()),buttonX);
        return pane;
    }
    private ScatterChart<Number, Number> chart(String exp){
        //-----
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X");
        //-----
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y");
        //-----
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Y = " + exp);
        if (!exp.equals(""))
            for (double i = Double.parseDouble(left.getText()); i < Double.parseDouble(right.getText()); i+=0.01) {
                series.getData().add(new XYChart.Data<>(i, Calculator.computing(Controller.replaceX(exp, new BigDecimal(i).setScale(4, RoundingMode.DOWN).doubleValue()))));
            }
        //-----
        ScatterChart<Number,Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.getData().add(series);
        scatterChart.setId("Line");
//        scatterChart.setMinSize(400, 350);
        scatterChart.setPrefSize(380,360);
        return scatterChart;
    }
    private final AtomicInteger cont = new AtomicInteger(0);
    private Button display(){
        Button textField = new Button();
        textField.setId("textField");
        textField.setPrefHeight(100);
        textField.setPrefWidth(405);
        textField.setAlignment(Pos.CENTER_RIGHT);
        return textField;
    }
    private StackPane stackPane(Button textField){
        Button exit = new Button("⨯");
        exit.setId("exit");
        exit.setOnMouseClicked(mouseEvent -> {
            System.exit(0);
        });
        StackPane pane = new StackPane(textField, exit);
        pane.setAlignment(Pos.TOP_LEFT);
        return pane;
    }
    private VBox buttons(Button textField, StringBuilder exp, Scene scene){
        VBox vBox = new VBox();
        HBox hBox = new HBox(new Button("sin"), new Button("cos"),new Button("tan"), new Button("cot"),new Button("⥀"));
        HBox hBox1 = new HBox(new Button("%"), new Button("^"),new Button("√"),new Button("C"),new Button("/"));
        HBox hBox2 = new HBox(new Button("e"),new Button("1"),new Button("2"),new Button("3"),new Button("*"));
        HBox hBox3 = new HBox(new Button("π"),new Button("4"),new Button("5"),new Button("6"),new Button("-"));
        HBox hBox4 = new HBox(new Button("!"),new Button("7"),new Button("8"),new Button("9"),new Button("+"));
        HBox hBox5 = new HBox(new Button("("),new Button(")"),new Button("0"),new Button("."),new Button("="));
        vBox.getChildren().addAll(hBox, hBox1, hBox2, hBox3, hBox4, hBox5);
        vBox.setPadding(new Insets(2));
        vBox.setSpacing(2);
        hBox.setSpacing(1);
        for (Node h: vBox.getChildren()){
            if (h instanceof HBox){
                ((HBox) h).setSpacing(1);
            }
        }
        for (Node hBoxNode : vBox.getChildren()){
            if (hBoxNode instanceof HBox)
                if (!hBoxNode.equals(hBox))
                    for (Node buttonNode : ((HBox) hBoxNode).getChildren())
                        if (buttonNode instanceof Button) {
                            ((Button) buttonNode).setPrefWidth(80);
                            ((Button) buttonNode).setPrefHeight(60);
                            buttonNode.setOnMouseClicked(mouseEvent -> {
                                textField.setText(textField.getText() + ((Button) buttonNode).getText());
                                if (!(((Button) buttonNode).getText().equals("=") && ((Button) buttonNode).getText().equals("C")))
                                    exp.append(((Button) buttonNode).getText());
                            });
                            if (((Button) buttonNode).getText().equals("C")) {
                                buttonNode.setOnMouseClicked(mouseEvent -> {
                                    if (mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                                        textField.setText("");
                                        exp.delete(0,exp.length());
                                    } else if (!textField.getText().equals("")){
                                        textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                                        exp.delete(exp.length()-1,exp.length());
                                    }
                                });

                            }
                            if (((Button) buttonNode).getText().equals("=")) {
                                buttonNode.setId("equ");
                                buttonNode.setOnMouseClicked(mouseEvent -> {
                                    try {
                                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY )){
                                            textField.setText(String.valueOf(Calculator.computing(exp.toString())));
                                            exp.delete(0,exp.length());
                                            exp.append(textField.getText());
                                        } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                                            if (root.getTop() instanceof VBox){
                                                final StackPane diagram = diagram(textField,exp,buttonX());
                                                ((VBox) root.getTop()).getChildren().remove(1);
                                                ((VBox) root.getTop()).getChildren().add(diagram);
                                            }
                                        }
                                    } catch (Exception e) {
                                        textField.setText(e.getMessage());
                                        e.printStackTrace();
                                    }
                                });
                            }
                            if (Character.isDigit(((Button) buttonNode).getText().charAt(0))) {
                                buttonNode.setId("num");
                            }
                            if (((Button) buttonNode).getText().equals("("))
                                buttonNode.setId("rad");
                        }
        }
        for (Node node : hBox.getChildren()){
            if (node instanceof Button){
                ((Button) node).setPrefWidth(80);
                ((Button) node).setPrefHeight(60);
                node.setOnMouseClicked(mouseEvent -> {
                    switch (((Button) node).getText()){
                        case "sin" -> {
                            exp.append("s(");
                            textField.setText(textField.getText() + "sin(");
                        }
                        case "cos" -> {
                            exp.append("c(");
                            textField.setText(textField.getText() + "cos(");
                        }
                        case "tan" -> {
                            exp.append("t(");
                            textField.setText(textField.getText() + "tan(");
                        }
                        case "cot" -> {
                            exp.append("k(");
                            textField.setText(textField.getText() + "cot(");
                        }
                        case "⥀" -> {
                            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                                if (cont.getAndAdd(1)%2 == 0){
                                    scene.getStylesheets().add("style.css");
                                    scene.getStylesheets().remove("style1.css");
                                }
                                else{
                                    scene.getStylesheets().add("style1.css");
                                    scene.getStylesheets().remove("style.css");
                                }
                            }
//                            if (mouseEvent.getButton().equals(MouseButton.SECONDARY))
//                                if (root.getTop() instanceof VBox && ((VBox) root.getTop()).getChildren().size() > 1)
//                                    ((VBox) root.getTop()).getChildren().remove(1);
//                                else if (root.getTop() instanceof VBox)
//                                    ((VBox) root.getTop()).getChildren().add(diagram(textField, exp));
                        }
                    }
                });
            }

        }
        return vBox;
    }
}
