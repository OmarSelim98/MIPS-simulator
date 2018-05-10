package org.architecture;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;

public class DataScene {
    private Group root = new Group();
    private TextField [] regsData = new TextField[32];
    private Label[] regsName = new Label[32];
    private BorderPane mainPane = new BorderPane();
    private GridPane regs = new GridPane();
    private BorderPane topPane = new BorderPane();
    private Button backBtn = new Button("Back");
    private Button nextBtn = new Button("Next Instruction");
    private Scene dataScene = new Scene(root, 1080 , 720);

    public DataScene(String [] regsNamesString,String [] regDataString){
        this.getScene();

        prepareRegsNames(regsNamesString);
        prepareRegsData(regDataString);

        topPane.setPrefWidth(this.getScene().getWidth());
        topPane.setStyle("-fx-background-color:#202020");
        topPane.setLeft(backBtn);
        topPane.setRight(nextBtn);
        topPane.setPadding(new Insets(10,10,10,10));

        Label RegistersMainLbl = new Label("Registers");
        RegistersMainLbl.setFont(Font.font("sans",22));
        RegistersMainLbl.setTextFill(Color.WHITE);
        ScrollPane regsScrollPanel = new ScrollPane();

        regs.setStyle("-fx-background-color: #2e3032;"+
                "-fx-border-style:solid outside;" +
                "-fx-border-width: 3;" +
                "-fx-border-color: #202020;");
        regs.setPrefHeight(this.getScene().getHeight());
        regs.addRow(0,RegistersMainLbl);
        for(int i  = 1;i<32;i++) {
            this.regsName[i].setTextFill(Color.WHITE);
            this.regsData[i].setEditable(false);
            this.regsData[i].setPrefColumnCount(20);
            this.regsData[i].getStylesheets().add(this.getClass().getResource("textField.css").toExternalForm());
            regs.addRow(i, this.regsName[i], this.regsData[i]);
        }
            //regsNamesBox.getChildren().add(regsName[i]);


        regsScrollPanel.setContent(regs);
        regsScrollPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        regsScrollPanel.setFitToHeight(true);
        regsScrollPanel.setFitToWidth(false);
        //regsScrollPanel.setMaxWidth(350);

        mainPane.setTop(topPane);
        mainPane.setCenter(regsScrollPanel);
        this.root.getChildren().add(mainPane);
    }
    public Scene getScene(){
        return this.dataScene;
    }
    public void prepareRegsNames(String [] names){
        for(int i = 0;i<32;i++){
            regsName[i] = new Label(names[i]);
        }
    }
    public void prepareRegsData(String [] data){
        for(int i = 0;i<32;i++){
            //regsData[i] = new TextField(""+Integer.parseInt(data[i],2));
            regsData[i] = new TextField(data[i]);
        }
    }
}
