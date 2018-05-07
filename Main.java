package org.architecture;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    MainLoop mainLoop;
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Mips Simulator");

        Button assembleBtn = new CustomButton(new Image("build.png"));
        Button runBtn = new CustomButton(new Image("run.png"));
        TextArea txtArea = new TextArea();

        runBtn.setDisable(true);

        GridPane topPane = new GridPane();
        Insets topPaneInset = new Insets(10,10,10,10);
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(topPaneInset);
        topPane.setHgap(40);
        topPane.add(assembleBtn,0,0);
        topPane.add(runBtn,1,0);
        topPane.setStyle("-fx-background-color:#2e3032");

        txtArea.getStylesheets().add(this.getClass().getResource("txtArea.css").toExternalForm());
        txtArea.addEventHandler(EventType.ROOT,e->{
            if(e.getEventType().toString().equals("KEY_PRESSED")) {
                runBtn.setDisable(true);
            }
        });

        BorderPane layout = new BorderPane();
        layout.setTop(topPane);
        layout.setCenter(txtArea);

        Scene scene = new Scene(layout,800,600);

        primaryStage.setScene(scene);
        primaryStage.show();

        assembleBtn.setOnAction(e->{

            //assembleBtn.setDisable(true);

            //INSERT ARRAY OF INSTRUCTIONS
            String[] inst = txtArea.getText().split("\n");
            int inst_num;
            if(txtArea.getText().trim().equals("")){
                inst_num = 0;

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("An Error Occurred While Building");
                errorAlert.setContentText("Empty text can't be built!");
                errorAlert.showAndWait();
            }else{
                runBtn.setDisable(false);
                inst_num = inst.length;
                mainLoop.setInstNum(inst_num);
                mainLoop = new MainLoop();
            }

            System.out.println("Number of instructions = "+inst_num);
        });
    }
}
