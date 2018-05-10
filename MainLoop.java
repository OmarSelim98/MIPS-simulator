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
    String []inst = new String[inst_num];
    String current_inst = "";
    String [] current_inst_divided;

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

    void start(){
            /*String []inst = new String[8];
            String current_inst = "";
            String [] current_inst_divided;

            rf = new RegisterFile();
            assembler = new Assembler();
            instruction_process = new Instruction_memory(0);
            control = new ControlUnit();
            dataMemory = new DataMemory();

            inst[0]= "addi $t0 $t1 4" ;
            inst[1] = "addi $t1 $t2 9";
            inst[2] = "lb $t1 45($t0)";
            inst[3] = "lw $t1 0($t3)";
            inst[4] = "lw $t1 45($t0)";
            inst[5]= "sll $t3 $t2 3";
            inst[6]= "addi $t3 $t2 4";
            inst[7]= "add $t1 $t2 $t3";
            //Assemble Instructions
            String [] output = assembler.assemble(inst);

            for(int i = 0;i<output.length;i++){
                System.out.println("Inst["+i+"] : "+output[i]);
            }

            //Add to Inst Memory
            instruction_process.add(output);
            */

            //STAGE 1
            /* Here goes fetching inst , dividing it , jumping if possible and incrementing PC*/
            //fetch next inst

            current_inst = instruction_process.Fetch((Integer.parseInt(pc,2)));
            pc = PC_increment(pc,4);

            //divide current inst
            System.out.println("PC -> "+pc+" | inst : "+current_inst);
            current_inst_divided = instruction_process.Divide_Instruction(current_inst);
            //Fetch JUMP ADDRESS
            String jump_address = current_inst_divided[1]+current_inst_divided[2]+current_inst_divided[3]+current_inst_divided[4]+current_inst_divided[5];
            System.out.println("JUMP ADDRESS -> "+jump_address);

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
            control.set(current_inst_divided[0]);
            control.list();
            rf.setRead(current_inst_divided[1], 1); //set Rs as Register 1 to be read
            //Set Rt or Rd as Write Destination Depending on Imm Field (RegDst)
            try{
                if(ControlUnit.RegDst.equals("0")){
                    rf.setWrite(current_inst_divided[2]);
                }else if(ControlUnit.RegDst.equals("1")){
                    rf.setRead(current_inst_divided[2], 2);
                    rf.setWrite(current_inst_divided[3]);
                }}catch (Exception e){
                e.printStackTrace();
            }
            //Extend [15-0]
            String imm = current_inst_divided[3]+current_inst_divided[4]+current_inst_divided[5];
            String extended_32 = sign_extend(imm); //16 imm to 32 var
            String branch_address = shift_left_branch(extended_32);

            String funct = imm.substring(imm.length()-6,imm.length());
            String shamt  = imm.substring(imm.length()-11,imm.length() -6);
            //Get data from register file
            String reg_output1 = rf.read(1); //data 1 var
            String reg_output2 = rf.read(2); //data 2 var
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
            //check shamt src
            ALUControl alu_ctrl = new ALUControl();
            String shamt_src = alu_ctrl.getShamtSrc(funct);
            String alu_control_code = alu_ctrl.getALUCtrl(funct);
            String jumpR = alu_ctrl.getJumpR(funct);
            System.out.println("Alu Control : OUTPUT : Shamt Src -> "+shamt_src);
            System.out.println("Alu Control : OUTPUT :CODE-> "+alu_control_code);
            System.out.println("Alu Control : OUTPUT :JumpR-> "+jumpR);

            //BRANCH 1
            String full_branch_address = add_PC_Branch(pc,branch_address);
            System.out.println("Full Branch Address -> "+full_branch_address);

            //END BRANCH 1

            String alu_input1 = reg_output1;
            String alu_input2;
            //Mux
            if(shamt_src.equals("1")){
                alu_input2 = sign_extend_unsigned(shamt);
            }else{
                alu_input2 = not_shamt_output;
            }

            if(alu_input2 == null){
                alu_input2 = "000000000000000000000000000000000";
            }
            System.out.println("ALU : INPUT : DATA 1 -> "+alu_input1);
            System.out.println("ALU : INPUT : DATA 2 -> "+alu_input2);

            ALU alu = new ALU();
            alu.setInput1(alu_input1);
            alu.setInput2(alu_input2);
            alu.setControlCode(alu_control_code);
            alu.calculate();
            String alu_output = alu.getResult();
            String zero_flag = alu.getZeroFlag();

            System.out.println("ALU : OUTPUT : DATA -> "+alu_output);
            System.out.println("ALU : OUTPUT : ZERO FLAG -> "+zero_flag);


            //End Branch
            //STAGE 3 END

            //STAGE 4
            dataMemory.set_mem_read(ControlUnit.MemRead);
            dataMemory.set_mem_write(ControlUnit.MemWrite);
            dataMemory.set_address(alu_output);
            dataMemory.set_op_code(current_inst_divided[0]);
            String data_memory_output = dataMemory.Calculate();

            System.out.println("DATA MEMORY : OUTPUT -> "+data_memory_output);

            String register_write_input = "00000000000000000000000000000000";

            if(ControlUnit.MemtoReg.equals("1")){
                register_write_input = data_memory_output;
            }else{
                register_write_input = alu_output;
            }

            //Continue Branch
            String branch_mux_result= ""; // THIS GOES TO THE JUMP'S MUX
            String jump_mux_result="";
            if(ControlUnit.Branch.equals("1") && zero_flag.equals("1")){
                //Update PC to the calculated branch address
                branch_mux_result = full_branch_address;

            }else{
                //Don't update pc and wait for jump mux.
                branch_mux_result = pc;
            }

            if(ControlUnit.Jump.equals("1")){
                jump_mux_result = full_jump_address;
            }else{
                jump_mux_result = branch_mux_result;
            }

            if(alu_ctrl.getJumpR(funct).equals("1")){
                pc = reg_output1;
            }else{
                pc = jump_mux_result;
            }

            System.out.println(Integer.parseInt(pc,2));


            //STAGE 4 END

            //STAGE 5
            //Write to reg from (memory/ALU)
            System.out.println("REGISTER FILE : WRITTEN DATA -> "+register_write_input);
            try {
                rf.write(register_write_input);
            }catch (Exception e){
                //DONT SHOW ERROR
                //ONLY OCCURS IF USER HAS SET WRITE TO $0
                //SO IT ID NOT SET AND THE VARIABLE WILL BE = NULL
                //ARRAY WILL SEARCH FOR THE NULL INDEX , EXCEPTION WILL BE THROWN
            }
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
}
