package org.architecture;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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

    //for wires
    private GridPane wiresPane = new GridPane();
    private Label [] wiresLabels = new Label[14];
    private Label [][] wiresNames = new Label[14][];
    private TextField [][] wiresData = new TextField[6][9];
    private ScrollPane wiresScrollPane = new ScrollPane();
    private Label wireLabel = new Label("Wires");

    private Button backBtn = new CustomButton(new Image("org/architecture/return.png"));
    private Button nextBtn = new CustomButton(new Image("org/architecture/runNext.png"));
    private Scene dataScene = new Scene(root, 1080 , 720);

    private MainLoop mainLoop;

    private int counter = 0;

    public DataScene(MainLoop mainLoop){
        this.getScene();
        this.mainLoop = mainLoop;
        backBtn.setTooltip(new Tooltip("Return to Editor"));
        nextBtn.setTooltip(new Tooltip("Execute Next Instruction"));

        prepareRegsNames();
        prepareRegsData();
        prepareWiresNames();

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
        regs.setPrefHeight(this.getScene().getHeight()-63);
        wiresPane.setPrefHeight(this.getScene().getHeight()-63);
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

        wiresScrollPane.setContent(wiresPane);
        wiresScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        mainPane.setTop(topPane);
        mainPane.setLeft(regsScrollPanel);
        mainPane.setCenter(wiresScrollPane);
        this.root.getChildren().add(mainPane);

        //HANDLERS
        this.nextBtn.setOnAction(e->{
            if(counter<(mainLoop.inst.length-1)) {
                this.mainLoop.start();
                prepareRegsData();
                counter ++;
            }else{
                this.mainLoop.start();
                prepareRegsData();
                this.nextBtn.setDisable(true);
            }
        });
        this.backBtn.setOnAction(e->{
            Main.SetScene(Main.editor);
        });
    }
    public Scene getScene(){
        return this.dataScene;
    }
    public void prepareRegsNames() {
        for (int i = 0; i < 32; i++) {
            regsName[i] = new Label(RegisterFile.regName[i]);
        }
    }
    public void prepareRegsData(){
        if(regsData[0] != null){
            for (int i = 0; i < 32; i++) {
                regsData[i].setText(RegisterFile.regData[i]);
            }
        }else {
            for (int i = 0; i < 32; i++) {
                regsData[i] = new TextField(RegisterFile.regData[i]);
            }
        }
    }
    public void prepareWiresNames(){
        //PC [0][0]
        wiresLabels[0]= new Label("PC");
        wiresNames[0] = new Label[1];
        wiresNames[0][0] = new Label("Tareget Address");

        //Inst Mem [1][7]
        wiresLabels[1]= new Label("InstMem");
        wiresNames[1] = new Label[8];
        wiresNames[1][0] = new Label("JumpAddress");
        wiresNames[1][1] = new Label("OpCode");
        wiresNames[1][2] = new Label("Rs");
        wiresNames[1][3] = new Label("Rt");
        wiresNames[1][4] = new Label("Rd");
        wiresNames[1][5] = new Label("ImmValue16");
        wiresNames[1][6] = new Label("Shamt");
        wiresNames[1][7] = new Label("Funct");

        //Control [2][8]
        wiresLabels[2] = new Label("Control");
        wiresNames[2] = new Label[9];
        wiresNames[2][0] = new Label("RegDst");
        wiresNames[2][1] = new Label("Jump");
        wiresNames[2][2] = new Label("Branch");
        wiresNames[2][3] = new Label("MemRead");
        wiresNames[2][4] = new Label("MemWrite");
        wiresNames[2][5] = new Label("MemToReg");
        wiresNames[2][6] = new Label("ALUOP");
        wiresNames[2][7] = new Label("ALUSrc");
        wiresNames[2][8] = new Label("RegWrite");

        //RegisterFile [3][5]
        wiresLabels[3] = new Label("RegisterFile");
        wiresNames[3] = new Label[6];
        wiresNames[3][0] = new Label("Input1");
        wiresNames[3][1] = new Label("Input2");
        wiresNames[3][2] = new Label("Output1");
        wiresNames[3][3] = new Label("Output2");
        wiresNames[3][4] = new Label("WriteReg");
        wiresNames[3][5] = new Label("WriteData");

        //ALU OP MUX [4][2]
        wiresLabels[4] = new Label("ALU OP MUX");
        wiresNames[4] = new Label[3];
        wiresNames[4][0] = new Label("Input 1");
        wiresNames[4][1] = new Label("Input 2");
        wiresNames[4][2] = new Label("Output");

        //ALU CONTROL
        wiresLabels[5] = new Label("ALU CONTROL");
        wiresNames[5] = new Label[2];
        wiresNames[5][0]= new Label("ALU Control OP");
        wiresNames[5][1] = new Label("Shamt Src");

        //ShamtSrcMux
        wiresLabels[6] = new Label("MUX:ShamtSrc");
        wiresNames[6] = new Label[1];
        wiresNames[6][0] = new Label("output");

        //ALU
        wiresLabels[7] = new Label("ALU");
        wiresNames[7] = new Label[4];
        wiresNames[7][0] = new Label("Input 1");
        wiresNames[7][1] = new Label("Input 2");
        wiresNames[7][2] = new Label("Zero Flag");
        wiresNames[7][3] = new Label("ALU Result");

        //Memory
        wiresLabels[8] = new Label("Data Memory");
        wiresNames[8] = new Label[3];
        wiresNames[8][0] = new Label("address");
        wiresNames[8][1] = new Label("DataInput");
        wiresNames[8][2] = new Label("DataOutput");

        //MemToRegMux
        wiresLabels[9] = new Label("MUX:MemToReg");
        wiresNames[9] = new Label[1];
        wiresNames[9][0]= new Label("ToBeWrittenData");

        //BranchALU
        wiresLabels[10] = new Label("BranchALU");
        wiresNames[10] = new Label[3];
        wiresNames[10][0] = new Label("Input 1");
        wiresNames[10][1] = new Label("Input 2");
        wiresNames[10][2] = new Label("Output");

        //Branch AND
        wiresLabels[11] = new Label("BranchAND");
        wiresNames[11] = new Label[1];
        wiresNames[11][0] = new Label("Output");

        //Branch MUX
        wiresLabels[12] = new Label("MUX:Jump");
        wiresNames[12] = new Label[1];
        wiresNames[12][0] = new Label("Output");

        //JR MUX
        wiresLabels[13] = new Label("MUX:JR");
        wiresNames[13] = new Label[3];
        wiresNames [13][0] = new Label("Input 1");
        wiresNames [13][1] = new Label("Input 2");
        wiresNames [13][2] = new Label("Output");

        wireLabel.setFont(Font.font("sans",22));
        wireLabel.setTextFill(Color.WHITE);

        wiresPane.addRow(0,this.wireLabel);

        //PC
            wiresPane.add(wiresLabels[0],0,1);
            //names
            wiresPane.add(wiresNames[0][0],1,1);

            int end = 2;
            for(int i = 1;i< wiresLabels.length;i++){
                wiresPane.add(wiresLabels[i],0,end);
                for(int j = 0;j<wiresNames[i].length;j++){
                    wiresPane.add(wiresNames[i][j], 1, end + j);
                }
                end += wiresNames[i].length+1;
            }

        wiresPane.setPadding(new Insets(10,10,10,10));
        wiresPane.setHgap(75);
        wiresPane.setVgap(10);
        wiresPane.getStylesheets().add(this.getClass().getResource("wiresStyle.css").toExternalForm());
        wiresPane.getStyleClass().add("pane");
    }

    public void updateWiresData(){

    }
}
