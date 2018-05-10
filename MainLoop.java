package org.architecture;

/*Main Loop will be divided to a number of stages , each stage has a number of operations that works
    in parallel to each other.
*/
public class MainLoop {
    public static String pc = "000000000000000000000000000000000";
    public static int inst_num = 0;
    RegisterFile rf;
    Assembler assembler;
    Instruction_memory instruction_process;
    ControlUnit control ;
    DataMemory dataMemory;
    String []inst;
    String current_inst = "";
    String [] current_inst_divided;

    String[]data_to_display = new String[48];

    public boolean build(String [] instFromTxtArea,int targetAddress){
        //the loop starts here
        try {
            inst = instFromTxtArea;
            current_inst = "";

            rf = new RegisterFile();
            assembler = new Assembler();
            instruction_process = new Instruction_memory(targetAddress);
            control = new ControlUnit();
            dataMemory = new DataMemory();

            String[] output = assembler.assemble(instFromTxtArea);

            instruction_process.add(output);

            for (int i = 0; i < output.length; i++) {
                System.out.println("Inst[" + i + "] : " + output[i]);
            }

            return true;
        }catch(Exception e){
            return false;
        }
    }

    String[] start(){
            //STAGE 1
            /* Here goes fetching inst , dividing it , jumping if possible and incrementing PC*/
            //fetch next inst

            current_inst = instruction_process.Fetch((Integer.parseInt(pc,2)));
            this.data_to_display[0] = pc; //DTD
            pc = PC_increment(pc,4);

            //divide current inst
            System.out.println("PC -> "+pc+" | inst : "+current_inst);

            current_inst_divided = instruction_process.Divide_Instruction(current_inst);
            //Fetch JUMP ADDRESS
            String jump_address = current_inst_divided[1]+current_inst_divided[2]+current_inst_divided[3]+current_inst_divided[4]+current_inst_divided[5];
            System.out.println("JUMP ADDRESS -> "+jump_address);
            this.data_to_display[1] = jump_address; //DTD
            //SHIFT JUMP ADDRESS BY 2
            String shifted_jump_address = shift_left_jump(jump_address);
            System.out.println("Shifted PC -> "+shifted_jump_address);

            //Calculate full jump address
            String full_jump_address = pc.substring(0,4)+shifted_jump_address;
            System.out.println("Full Jump Address -> "+full_jump_address);

            //STAGE 1 END
        /*
        0:op
        1:Rs
        2:Rt
        3:Rd
        4:shamt
        5:funct

        3+4+5 = 16 imm

        1+2+3+4+5 = 26 imm
        */
            //STAGE 2
            //Pass opcode to the Control Unit
            this.data_to_display[2] = current_inst_divided[0]; //DTD OPCODE
            this.data_to_display[3] = current_inst_divided[1]; //DTD RS
            this.data_to_display[4] = current_inst_divided[2]; //DTD RT
            this.data_to_display[5] = current_inst_divided[3]; //DTD RD

            control.set(current_inst_divided[0],pc);
            control.list();
            rf.setRead(current_inst_divided[1], 1); //set Rs as Register 1 to be read
            this.data_to_display[18] = current_inst_divided[1]; //DTD REG INPUT 1
            //Set Rt or Rd as Write Destination Depending on Imm Field (RegDst)
            try{
                if(ControlUnit.RegDst.equals("0")){
                    rf.setWrite(current_inst_divided[2]);
                    this.data_to_display[19] = null; //DTD REG INPUT 2
                    this.data_to_display[22] = current_inst_divided[2];  //DTD REGWrite
                }else if(ControlUnit.RegDst.equals("1")){
                    rf.setRead(current_inst_divided[2], 2);
                    rf.setWrite(current_inst_divided[3]);
                    this.data_to_display[19] = current_inst_divided[2];  //DTD REG INPUT 2
                    this.data_to_display[22] = current_inst_divided[3]; //DTD REGWrite
                }}catch (Exception e){

            }
            //Extend [15-0]
            String imm = current_inst_divided[3]+current_inst_divided[4]+current_inst_divided[5];
            this.data_to_display[6] = imm; //DTD IMM16
            String extended_32 = sign_extend(imm); //16 imm to 32 var
            String branch_address = shift_left_branch(extended_32);

            String funct = imm.substring(imm.length()-6,imm.length());
            String shamt  = imm.substring(imm.length()-11,imm.length() -6);

            this.data_to_display[7] = shamt; //DTD SHAMT
            this.data_to_display[8] = funct; //DTD FUNCT

            this.data_to_display[9] = ControlUnit.RegDst;
            this.data_to_display[10] = ControlUnit.Jump;
            this.data_to_display[11] = ControlUnit.Branch;
            this.data_to_display[12] = ControlUnit.MemRead;
            this.data_to_display[13] = ControlUnit.MemWrite;
            this.data_to_display[14] = ControlUnit.MemtoReg;
            this.data_to_display[15] = ControlUnit.AluOP;
            this.data_to_display[16] = ControlUnit.AluSrc;
            this.data_to_display[17] = ControlUnit.RegWrite;
            //Get data from register file
            String reg_output1 = rf.read(1); //data 1 var
            String reg_output2 = rf.read(2); //data 2 var

            this.data_to_display[20] = reg_output1; //DTD Reg out 1
            this.data_to_display[21] = reg_output2; //DTD Reg out 2
            this.data_to_display[24] = reg_output1; //DTD ALU OP MUX INPUT 1

            //DTD 23 TBA
            System.out.println("Branch Address -> "+branch_address);
            System.out.println("Shamt -> " + shamt);
            System.out.println("Funct -> "+funct);
            //STAGE 2 END

            //STAGE 3

            /* Here goes the alu control and alu*/
            String not_shamt_output = reg_output2;
            //check alu src (reg or imm to alu)
            if(ControlUnit.AluSrc.equals("1")){
                not_shamt_output=extended_32;
            }
            this.data_to_display[25] = extended_32; //DTD ALU OP MUX INPUT 2
            this.data_to_display[26] = not_shamt_output; //DTD ALU OP MUX OUTPUT
                //check shamt src
            ALUControl alu_ctrl = new ALUControl();
            String shamt_src = alu_ctrl.getShamtSrc(funct);
            String alu_control_code = alu_ctrl.getALUCtrl(funct);
            this.data_to_display[27] = alu_control_code; //DTD ALU CONTROL : OP
            this.data_to_display[28] = shamt_src;
            String jumpR = alu_ctrl.getJumpR(funct);
            this.data_to_display[29] = jumpR;
            System.out.println("Alu Control : OUTPUT : Shamt Src -> "+shamt_src);
            System.out.println("Alu Control : OUTPUT :CODE-> "+alu_control_code);
            System.out.println("Alu Control : OUTPUT :JumpR-> "+jumpR);

            //BRANCH 1
            String full_branch_address = add_PC_Branch(pc,branch_address);
            System.out.println("Full Branch Address -> "+full_branch_address);

            //END BRANCH 1

            String alu_input1 = reg_output1;
            String alu_input2;
            //Mux SHAMT
            if(shamt_src.equals("1")){
                alu_input2 = sign_extend_unsigned(shamt);
            }else{
                alu_input2 = not_shamt_output;
            }
            this.data_to_display[30] = alu_input2; //DTD SHAMT MUX OUTPUT
            if(alu_input2 == null){
                alu_input2 = "000000000000000000000000000000000";
            }
            System.out.println("ALU : INPUT : DATA 1 -> "+alu_input1);
            System.out.println("ALU : INPUT : DATA 2 -> "+alu_input2);

            ALU alu = new ALU();
            alu.setInput1(alu_input1);
            alu.setInput2(alu_input2);
            this.data_to_display[31] = alu_input1; // DTD ALU INPUT 1
            this.data_to_display[32] = alu_input2; // DTD ALU INPUT 2
            alu.setControlCode(alu_control_code);
            alu.calculate();
            String alu_output = alu.getResult();
            String zero_flag = alu.getZeroFlag();
            this.data_to_display[33] = zero_flag; // DTD ALU ZERO FLAG.
            this.data_to_display[34] = alu_output; // DTD ALU RESULT.

            System.out.println("ALU : OUTPUT : DATA -> "+alu_output);
            System.out.println("ALU : OUTPUT : ZERO FLAG -> "+zero_flag);


            //End Branch
            //STAGE 3 END

            //STAGE 4
            dataMemory.set_address(alu_output);
            dataMemory.set_data(reg_output2);
            this.data_to_display[35] = alu_output; // DTD TARGET ADDRESS.
            this.data_to_display[36] = reg_output2; // DTD DATA INPUT.
            dataMemory.set_op_code(current_inst_divided[0]);
            String data_memory_output = dataMemory.Calculate();
            if(ControlUnit.MemWrite.equals("1")){
                System.out.println("Location["+alu_output+"] = "+reg_output2);
            }
            this.data_to_display[37] = data_memory_output;

            System.out.println("DATA MEMORY : OUTPUT -> ");

            String register_write_input = "00000000000000000000000000000000";

            if(ControlUnit.MemtoReg.equals("1")){
                register_write_input = data_memory_output;
            }else{
                register_write_input = alu_output;
            }
            this.data_to_display[38] = register_write_input; // DTD MEM TO REG OUTPUT.

            this.data_to_display[39] = pc; // DTD BRANCH ALU INPUT 1
            this.data_to_display[40] = branch_address; // DTD BRANCH ALU INPUT 2
            this.data_to_display[41] = full_branch_address; // DTD BRANCH ALU OUTPUT
            //Continue Branch
            String branch_mux_result= ""; // THIS GOES TO THE JUMP'S MUX
            String jump_mux_result="";
            if(ControlUnit.Branch.equals("1") && zero_flag.equals("1")){
                //Update PC to the calculated branch address
                branch_mux_result = full_branch_address;
                this.data_to_display[42] = "1"; //DTD BRANCH ADN RES
            }else{
                //Don't update pc and wait for jump mux.
                branch_mux_result = pc;
                this.data_to_display[42] = "0"; //DTD BRANCH ADN RES
            }
            if(ControlUnit.Jump.equals("1")){
                jump_mux_result = full_jump_address;
            }else{
                jump_mux_result = branch_mux_result;
            }
            this.data_to_display[43] = jump_mux_result; //DTD JUMP MUX RES

            this.data_to_display[44] = reg_output1; //DTD JUMPR MUX INPUT 1
            this.data_to_display[45] = reg_output1; //DTD JUMPR MUX INPUT 2
            if(alu_ctrl.getJumpR(funct).equals("1")){
                pc = reg_output1;

            }else{
                pc = jump_mux_result;

            }
            this.data_to_display[46] = pc; //DTD JUMPR MUX Output
            System.out.println(Integer.parseInt(pc,2));


            //STAGE 4 END

            //STAGE 5
            //Write to reg from (memory/ALU)
            System.out.println("REGISTER FILE : WRITTEN DATA -> "+register_write_input);
            this.data_to_display[23] = register_write_input; //DTD Reg write back from MEM/ALU
            try {
                if(alu_ctrl.getJumpR(funct).equals("0")) {
                    rf.write(register_write_input);
                }
            }catch (Exception e){
                //e.printStackTrace();
                //DONT SHOW ERROR
                //ONLY OCCURS IF USER HAS SET WRITE TO $0
                //SO IT ID NOT SET AND THE VARIABLE WILL BE = NULL
                //ARRAY WILL SEARCH FOR THE NULL INDEX , EXCEPTION WILL BE THROWN
            }
            return  data_to_display;
            //STAGE 5 END

            //UPDATE GUI
            //this.gui.setRegs(rf.regName);
            //UPDATE END

        /*
        label - position = 0.
        j label - postion = 3.
        beq $0 $0 label - position = 4.

        j 000000 - full address
        beq - relative address

        */
        }

       public static String sign_extend(String str){
       String new_str="";
        if(str.length() < 32){
            String filler = str.charAt(0)+"";
            for(int i=0;i<32-str.length();i++){
                new_str += filler;
            }
            new_str+=str;
            return new_str;
        }else if(str.length() == 32){
            return str;
        }else {
            new_str += str.substring(str.length() - 32, str.length());
            return new_str;
        }
       }
    public static String sign_extend_unsigned(String str) {
        String new_str = "";
        if (str.length() < 32) {
            String filler = "0";
            for (int i = 0; i < 32 - str.length(); i++) {
                new_str += filler;
            }
            new_str += str;
            return new_str;
        } else if (str.length() == 32) {
            return str;
        } else {
            new_str += str.substring(str.length() - 32, str.length());
            return new_str;
        }
    }
    public String PC_increment(String pc,int num){
        int pc_num = Integer.parseInt(pc,2);
        pc_num += num;
        return sign_extend_unsigned(Integer.toBinaryString(pc_num));

    }
    public String shift_left_jump(String str){
        int num = Integer.parseInt(str,2);
        num = num*4;
        str =  sign_extend_unsigned(Integer.toBinaryString(num));
        return str.substring(str.length()-28,str.length());
    }
    public String shift_left_branch(String str){
        int num = (int)Long.parseLong(str,2);
        num = num*4;
        str =  sign_extend_unsigned(Integer.toBinaryString(num));
        return str;
    }

    public String add_PC_Branch(String pc, String branch){
        int pc_num = Integer.parseInt(pc,2);
        int branch_num =(int) Long.parseLong(branch,2);
        int res = pc_num+branch_num;
        System.out.println("Pc: "+pc_num+"branch: "+branch_num+" result = "+res);
        return sign_extend_unsigned(Integer.toBinaryString(res));
    }

    public String [] getDataToDisplay(){
        return this.data_to_display;
        /*try {
            return this.data_to_display;
        }catch(Exception e){
            for(int i  = 0;i<this.data_to_display.length;i++)
                this.data_to_display[i] = "not assigned yet";
            return this.data_to_display;
        }*/
    }

    /*
    * j main
sum: slti $t1 $a0 2
addi $t5 $0 1
beq $t1 $t5 exit
addi $sp $sp -4
sw $ra 0($sp)
addi $sp $sp -4
sw $a0 0($sp)
addi $a0 $a0 -1
jal sum
lw $a0 0($sp)
addi $sp $sp 4
add $v0 $v0 $a0
lw $ra 0($sp)
addi $sp $sp 4
jr $ra
exit: addi $v0 $0 1
jr $ra
main :addi $a0 $0 5
jal sum
addi $a0 $v0 0
    * */
}
