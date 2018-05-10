/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.architecture;
/*
inst [] -> assember -> output[]
*/


/**
 *
 * @author OmarSelim
 */

//This class assemble an instruction to binary code
//add rd rs rt
//sll rd rs value
//Binary : op - Rs - Rt - Rd - Shamt - Funct
//sll : op - rs -rd - 00000 - shamt funct
//j label
//j 26bit : rs - rd - rt

public class Assembler {
     String []branch = new String [1000];
     int inst_count = 0;
     /**/
    public  String[] assemble(String [] inst){
        String [] output = new String[inst.length];
        for(int i = 0;i<inst.length;i++){
            output[i] = this.assembleLine(inst[i],i);
        }
         prepare(output);
        return output;
    }
    private  String [] prepare(String [] output){
        String branches = "";
        for (int i=0;i<branch.length;i++){
            if(branch[i]!=null){
                branches+=i+":"+branch[i]+" ";
            }
        }
        branches = branches.trim();
        if(branches != "") {
            String[] branches_arr = branches.split(" ");
            String[][] branch_data = new String[branches_arr.length][];
            String[][] branch_line = new String[branches_arr.length][];
            for (int i = 0; i < branches_arr.length; i++) {
                branch_data[i] = branches_arr[i].split(":");
                System.out.println("(" + branch_data[i] + ")");
            }


                for (int i = 0; i < output.length; i++) {
                    String op = output[i].substring(0, 6);
                    for (int j = 0; j < branch_data.length; j++) {
                        System.out.println(output[i].indexOf(branch_data[j][1].charAt(0)));
                        if (output[i].indexOf(branch_data[j][1].charAt(0)) != -1) {
                            int last_int = output[i].indexOf(branch_data[j][1]);
                            String new_output = output[i].substring(0, last_int);
                            //Here we get the label and change it with the instruction position (for beq and j)
                            //new_output += to_16(Integer.toBinaryString(Integer.parseInt(branch_data[j][0])));
                            if (op.equals("000100")) {
                                //BEQ RELATIVE ADDRESS.
                                new_output += this.to_16(Integer.toBinaryString((Integer.parseInt(branch_data[j][0]) - 1) - i));
                            } else {
                                //J FULL ADDRESS.
                                new_output += this.to_26(Integer.toBinaryString(Integer.parseInt(branch_data[j][0])));
                            }
                            output[i] = new_output;
                        }
                    }

                }
        }
        return output;
       /*for(int i=0;i<output.length;i++){
           for(int j = 0)
       }*/
    }
    public  String assembleLine(String str,int position){
        String full_str = "";
        if(str.indexOf(":") > -1){
            String [] new_str = str.split(":");
            str = new_str[1].trim();
            branch[inst_count] = new_str[0].trim();
        }else{
            branch[inst_count] = null ;
        }
        inst_count ++;
        String opName = this.cut_first(str); //ex : add - li
        String type = this.get_type(str); 
        
        if(type.equals("R")){
            if(opName.equals("sll")){
                //op code - rs -rd - 000000 - shamt - funct
                String op_code = this.get_OpCode(str);
                
                full_str += op_code; // op
                full_str +=this.get_Rs(str);//rs
                full_str +=this.get_Rd(str);//rt(rd) second value
                full_str += "00000";
                full_str +=this.get_Shamt(str);
                full_str +=this.get_funct(opName);
                
                return full_str;
            }else if(opName.equals("jr")){
                //opcode - rs
                //System.out.print("lol");
                full_str += this.get_OpCode(str);
                full_str += this.get_Rd(str);
                full_str += "000000000000000";
                full_str += this.get_funct(opName);
                
                return full_str;
                
                //WAITING
            }else{
            // op code - rs - rt - rd - 00000 - funct
                full_str += this.get_OpCode(str);
                full_str += this.get_Rs(str);
                full_str += this.get_Rt(str);
                full_str += this.get_Rd(str);
                full_str += "00000";
                full_str += this.get_funct(opName);
                
                return full_str;
            }
        }else if(type.equals("I")){
            //OPCODE - RS -RD -16 BIT ADDRESS
            //check if it's not a word operator
            if(!this.is_word_operator(str)){
                //addi -slti - beq
                //addi : op code rs rt 16bits
                if(!opName.equals("beq")){ // for addi slti
                    full_str += this.get_OpCode(str);
                    full_str += this.get_Rs(str);
                    full_str += this.get_Rd(str);
                    full_str += to_16(this.get_Shamt(str));
                    return full_str;
                }else{
                    String new_pos;
                    if(this.get_label_address(str,"beq").charAt(0) == '0'){
                        new_pos=this.to_16(Integer.toBinaryString(Integer.parseInt(this.get_label_address(str,"beq"))-(position+1)));
                    }else{
                        new_pos=this.get_label_address(str,"beq");
                    }
                    //beq
                    full_str += this.get_OpCode(str);
                    full_str += this.get_Rs(str);//Rs
                    full_str += this.get_Rd(str);//Rt
                    full_str += new_pos;
                    
                    return full_str;
                }
            }else{
             //if a word operator
             //LW RT CONST(RS)
             //OP - RS - RT - CONST(16BIT)
             full_str += this.get_OpCode(str);
             full_str += this.get_Rs_I(str);//get rs
             full_str += this.get_Rd(str);//WARNING: RT
             full_str += this.get_imm(str);//get imm(string)->int->binary->16binary
             return full_str;
            }
            
        }else if(type.equals("J")){
            //If jump or jal
            if(opName.equals("j")){
                String new_pos;
                    if(this.get_label_address(str,"j").charAt(0) == '0'||this.get_label_address(str,"j").charAt(0) == '1'){
                        //BEQ RELATIVE ADDRESS.
                        new_pos=this.to_26(Integer.toBinaryString(Integer.parseInt(this.get_label_address(str,"j"))));
                    }else{
                        new_pos=this.get_label_address(str,"j");
                    }
                    //beq
                    //System.out.println(this.get_label_address(str,"j").charAt(0));
                    full_str += this.get_OpCode(str);
                    full_str += ""+new_pos;
                    return full_str;
            }
        }else{
            return null;
        }
        return str;
    }
    
    
    private  String  get_OpCode(String str){
        String sub_str=this.cut_first(str);
        switch(sub_str){
            case "add":
                str = "000000";
                break;
            case "sll":
                str = "000000";
                break;
            case "slt":
                str = "000000";
                break;
            case "nor":
                str = "000000";
                break;
            case "jr":
                str = "000000";
                break;
                
                
            case "addi":
                str = "001000";
                break;
            case "slti":
                str = "001010";
                break;
            case "lw":
                str = "100011";
                break;
            case "sw":
                str = "101011";
                break;    
            case "lb":
                str = "100000";
                break;
            case "sb":
                str = "101000";
                break;
            case "lbu":
                str = "100100";
                break;
            case "beq":
                str = "000100";
                break;
                
            case "j":
                str = "000010";
                break;
            case "jal":
                str = "000011";
                break;
                
            default:
                str = null;
            break;
        }
        return str;
    }
    //add rd rs rt
    //sll rd rs value
    //op - rs - rt -rd shamt funct
    //sll : op - rs -rd - 00000 - shamt funct
    private  String get_Rd(String str){
                int rd_int;
                int first_space =str.indexOf(" "); 
                int space = str.indexOf(" ",first_space+1);
                if(space <0) {
                    space = str.length();
                }
                String Rd = str.substring(first_space+1, space); //int space = str.indexOf(" ",4); 
                rd_int = RegisterFile.getIndex(Rd); // get integer code for the rigester.
                Rd = Integer.toBinaryString(rd_int);//Get Binary of the int
                return add_5(Rd);
    }
    private  String get_Rs(String str){
                int rs_int;
                int first_space =str.indexOf(" "); 
                first_space = str.indexOf(" ",first_space+1);
                int space = str.indexOf(" ",first_space+1);
                String Rs = str.substring(first_space+1, space); 
                rs_int = RegisterFile.getIndex(Rs); // get integer code for the rigester.
                Rs= Integer.toBinaryString(rs_int);//Get Binary of the int
                return add_5(Rs);
    }
    
    private  String get_Rt(String str){
        int rt_int;
        int first_space =str.indexOf(" ");
        int space = 0;
        for(int i = 0 ;i<2;i++){
            first_space = str.indexOf(" ",first_space+1);
            space = str.indexOf(" ",first_space+1);
            if(space == -1){
                space = str.length();
            }
        }
        String Rt = str.substring(first_space+1, space);
        rt_int = RegisterFile.getIndex(Rt); // get integer code for the rigester.
        Rt= Integer.toBinaryString(rt_int);//Get Binary of the int
        return add_5(Rt);
    }
    private  String get_Shamt(String str){
        int rt_int;
        int first_space =str.indexOf(" ");
        int space = 0;
        for(int i = 0 ;i<2;i++){
            first_space = str.indexOf(" ",first_space+1);
            space = str.indexOf(" ",first_space+1);
            if(space == -1){
                space = str.length();
            }
        }
        String shamt = Integer.toBinaryString(Integer.parseInt(str.substring(first_space+1, space)));
        if(shamt.length() >5)
            shamt = shamt.substring(shamt.length()-5, shamt.length());
        else if(shamt.length() < 5)
            shamt = this.add_5(shamt);
        return shamt;
    }
    /** Returns the function binary value ,R-TYPE ONLY!
        @param op operation Name for the instruction
        @return function binary code*/
    private  String get_funct(String op){
        String funct = "";
        switch(op){
            case "add":
                funct = "100000";
            break;
            case "sll":
                funct = "000000";
            break;
            case "slt":
                funct = "101010";
            break;
            case "nor":
                funct = "100111";
            break;
            case "jr":
                funct = "001000";
            break;
        }
        return funct;
    }
    private  String get_imm(String str){
        int space = str.indexOf(" ");
        space = str.indexOf(" ",space+1);
        int mark_index = str.indexOf("(");
        String imm = str.substring(space+1, mark_index);
        return to_16(Integer.toBinaryString(Integer.parseInt(imm)));
    }
    private  String get_Rs_I(String str){
        int rt_int;
        int first_mark = str.indexOf("(");
        int final_mark = str.indexOf(")");
        String Rs = str.substring(first_mark+1,final_mark);
        rt_int = RegisterFile.getIndex(Rs); // get integer code for the rigester.
        Rs= Integer.toBinaryString(rt_int);//Get Binary of the int
        return add_5(Rs);
    }
    /** Takes an instruction and checks its opcode and return if its a lw,sw,etc.. or not*/
    private  boolean is_word_operator(String str){
        boolean type;
        int space_ind = str.indexOf(" ");
        String sub_str = str.substring(0, space_ind);
        switch(sub_str){
            case "lw":
                type = true;
            break;
            case "sw":
                type = true;
            break;
            case "lb":
                type = true;
            break;
            case "lbu":
                type = true;
            break;
            case "sb":
                type = true;
            break;
            default:
                type = false;
            break;
        }
        return type;
    }
    public String get_28_imm(String str){
        int first_spc = str.trim().indexOf(" ");
        String lbl_name = str.substring(first_spc+1,str.length());
        for(int i =0 ;i<inst_count;i++){
            if(branch[i]!= null && branch[i].equals(lbl_name)){
             return to_16(""+i);
            }
        }
        return lbl_name;
        
    }
    private  String cut_first(String str){
        int first_ind = 0;
        int space_ind = str.indexOf(" ",first_ind);
        String sub_str=str.substring(first_ind,space_ind);
        return sub_str;
    }
    private  String add_5(String str){
        String new_str= "";
        if(str.length() < 5){
            for(int i = 0;i<(5-str.length());i++){
                new_str += "0";
            }
            new_str += str;
        }else{
            return str;
        }
        return new_str;
    }
    
    public  String to_16(String str){
        String new_str="";
        if(str.length() < 16){
            for(int i=0;i<16-str.length();i++){
                new_str += "0";
            }
            new_str+=str;
            return new_str;
        }else if(str.length() == 16){
            return str;
        }else{
            new_str += str.substring(16, str.length());
            return new_str;
        }
    }
    private  String get_type(String str){
        String opName = this.cut_first(str);
        String type = "";
        switch(opName){
            //R-type
            case "add":
                type = "R";
            break;
            case "sll":
                type = "R";
            break;
            case "slt":
                type = "R";
            break;
            case "nor":
                type = "R";
            break;
            case "jr":
                type = "R";
            break;
            
            //I-type
            case "addi":
                type = "I";
            break;
            case "lw":
                type = "I";
            break;
            case "sw":
                type = "I";
            break;
            case "lb":
                type = "I";
            break;
            case "lbu":
                type = "I";
            break;
            case "sb":
                type = "I";
            break;
            case "beq":
                type = "I";
            break;
            case "slti":
                type = "I";
            break;
            //j-Type
            case "j":
                type = "J";
            break;
            case "jal":
                type = "J";
            break;
        }
        return type;
    }
    private  String get_label_address(String str,String op){
        int first_spc = str.indexOf(" ");
        int spc = 0;
        if(op.equals("beq")){
            for(int i =0;i < 2;i++)
                first_spc = str.indexOf(" ",first_spc+1);
             spc = str.indexOf(" ",first_spc+1);
            if(spc == -1){
                spc = str.length();
            }
        }else if(op.equals("jal") || op.equals("j")){
            //System.out.println(str.substring(first_spc+1,spc).trim(););
            spc = str.length();
        }
        String lbl_name = str.substring(first_spc+1,spc).trim();
        for(int i =0 ;i<inst_count;i++){
            if(branch[i]!= null && branch[i].equals(lbl_name)){
                if(op.equals("beq")){
                    return to_16(""+i);
                }
                else if(op.equals("j") || op.equals("jal")){
                    return to_16(""+i);
                }
            }
        }
        return lbl_name;
    }
    public  String to_26(String str){
        String new_str="";
        if(str.length() < 26){
            for(int i=0;i<26-str.length();i++){
                new_str += "0";
            }
            new_str+=str;
            return new_str;
        }else if(str.length() == 26){
            return str;
        }else{
            new_str += str.substring(str.length()-26, str.length());
            return new_str;
        }
    }
}