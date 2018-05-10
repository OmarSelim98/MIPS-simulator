package org.architecture;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    MainLoop mainLoop;
    boolean is_built = false;
    static Stage stage;
    static Scene editor;
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("Mips Simulator");
        primaryStage.setResizable(false);
        Button assembleBtn = new CustomButton(new Image("org/architecture/build.png"));
        Button runBtn = new CustomButton(new Image("org/architecture/run.png"));
        Label inputLbl = new Label("Enter the memory location here : ");
        TextField txtField = new TextField();
        TextArea txtArea = new TextArea();

        inputLbl.setLabelFor(txtField);
        inputLbl.setTextFill(Color.WHITE);

        runBtn.setDisable(true);

        GridPane topPane = new GridPane();
        Insets topPaneInset = new Insets(10,10,10,10);
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(topPaneInset);
        topPane.setHgap(40);
        topPane.add(inputLbl,0,0);
        topPane.add(txtField,1,0);
        topPane.add(assembleBtn,3,0);
        topPane.add(runBtn,4,0);
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

        editor = new Scene(layout,800,600);

        primaryStage.setScene(editor);
        primaryStage.show();

        assembleBtn.setOnAction(e->{
            long last_time = System.currentTimeMillis();
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
            }else if(txtField.getText().trim().equals("")){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("An Error Occurred While Building");
                errorAlert.setContentText("You cannot input instructions in null address!");
                errorAlert.showAndWait();
            }
            else{
                inst_num = inst.length;
                System.out.println("Number of instructions = "+inst_num);
                MainLoop.inst_num = inst_num;
                MainLoop.pc = "000000000000000000000000000000000";
                mainLoop = new MainLoop();
                is_built = mainLoop.build(inst,Integer.parseInt(txtField.getText().trim()));

                if(is_built) {
                    runBtn.setDisable(false);

                    Alert builtAlert = new Alert(Alert.AlertType.INFORMATION);
                    builtAlert.setTitle("Success!");
                    builtAlert.setHeaderText("Successful operation!");
                    builtAlert.setContentText("Your Program has been built successsfully in "+(System.currentTimeMillis()-last_time)+"ms.");
                    builtAlert.showAndWait();
                }else{
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("An Error Occurred While Building");
                    errorAlert.setContentText("Compilation error!");
                    errorAlert.showAndWait();
                }
            }
        });
        runBtn.setOnAction(e->{
            //mainLoop.start();
            primaryStage.setScene(new DataScene(mainLoop).getScene());
        });
    }

    public static void SetScene(Scene scene){
        stage.setScene(scene);
    }
}
