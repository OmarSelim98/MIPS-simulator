
package org.architecture;

public class ControlUnit {
    public static String RegDst; //0 to write in Rt 1 to write in Rd
    public static String RegWrite; //1 for write
    public static String AluSrc; //0 for non imm
    public static String AluOP; // 00  add , 01 sub , 10 R-type
    public static String MemWrite;
    public static String MemtoReg;
    public static String MemRead;
    public static String Branch;
    public static String Jump;
    public static String JumpR;
    public ControlUnit(){
        ControlUnit.RegDst = "0";
        ControlUnit.Branch = "0";
        ControlUnit.MemRead = "0";
        ControlUnit.MemtoReg = "0";
        ControlUnit.MemWrite = "0";
        ControlUnit.AluSrc = "0";
        ControlUnit.RegWrite = "0";
        ControlUnit.AluOP = "000";
        ControlUnit.Jump = "0";
        ControlUnit.JumpR = "0";
    }
    public void set(String opCode){
        ControlUnit.JumpR = "0";
        /*R-type Control unit outputs*/
        if(opCode.equals("000000")){
            ControlUnit.RegDst = "1";
            ControlUnit.Branch = "0";
            ControlUnit.MemRead = "0";
            ControlUnit.MemtoReg = "0";
            ControlUnit.MemWrite = "0";
            ControlUnit.AluSrc = "0";
            ControlUnit.RegWrite = "1";
            ControlUnit.AluOP = "010";
            ControlUnit.Jump = "0";

        }else if(opCode.equals("000010") || opCode.equals("000011")){ // J & JAL inst
            ControlUnit.RegDst = "0";
            ControlUnit.Branch = "0";
            ControlUnit.MemRead = "0";
            ControlUnit.MemtoReg = "0";
            ControlUnit.MemWrite = "0";
            ControlUnit.AluSrc = "0";
            ControlUnit.RegWrite = "0";
            ControlUnit.AluOP = "000";
            ControlUnit.Jump = "1";
            if(opCode.equals("000011")){
                //if JAL set ra to sth.

            }
        }else if(opCode.equals("001000")){ //addi
            ControlUnit.RegDst = "0";
            ControlUnit.Branch = "0";
            ControlUnit.MemRead = "0";
            ControlUnit.MemtoReg = "0";
            ControlUnit.MemWrite = "0";
            ControlUnit.AluSrc = "1";
            ControlUnit.RegWrite = "1";
            ControlUnit.AluOP = "000";
            ControlUnit.Jump = "0";
        }else if(opCode.equals("100011") || opCode.equals("100000") || opCode.equals("100100")){ //lw | lb || lbu
            ControlUnit.RegDst = "0";
            ControlUnit.Branch = "0";
            ControlUnit.MemRead = "1";
            ControlUnit.MemtoReg = "1";
            ControlUnit.MemWrite = "0";
            ControlUnit.AluSrc = "1"; //imm-field
            ControlUnit.RegWrite = "1";
            ControlUnit.AluOP = "000";
            ControlUnit.Jump = "0";
        }else if(opCode.equals("101011") || opCode.equals("101000")){ //sw || sb
            ControlUnit.RegDst = "0";
            ControlUnit.Branch = "0";
            ControlUnit.MemRead = "0";
            ControlUnit.MemtoReg = "0";
            ControlUnit.MemWrite = "1";
            ControlUnit.AluSrc = "1"; //imm-field
            ControlUnit.RegWrite = "0";
            ControlUnit.AluOP = "000";
            ControlUnit.Jump = "0";
        }else if(opCode.equals("000100")){ //beq
            ControlUnit.RegDst = "1";
            ControlUnit.Branch = "1";
            ControlUnit.MemRead = "0";
            ControlUnit.MemtoReg = "0";
            ControlUnit.MemWrite = "1";
            ControlUnit.AluSrc = "0"; //imm-field
            ControlUnit.RegWrite = "0";
            ControlUnit.AluOP = "001"; //subtract to branch if 0
            ControlUnit.Jump = "0";
        }else if(opCode.equals("001010")){ //slti
            ControlUnit.RegDst = "0";
            ControlUnit.Branch = "0";
            ControlUnit.MemRead = "0";
            ControlUnit.MemtoReg = "0";
            ControlUnit.MemWrite = "0";
            ControlUnit.AluSrc = "1"; //imm-field
            ControlUnit.RegWrite = "1";
            ControlUnit.AluOP = "100"; //subtract to branch if 0
            ControlUnit.Jump = "0";
        }
    }
    public void list(){
        System.out.println("RegDst : "+ControlUnit.RegDst);
        System.out.println("Branch : "+ControlUnit.Branch);
        System.out.println("MemRead : "+ControlUnit.MemRead);
        System.out.println("MemtoReg : "+ControlUnit.MemtoReg);
        System.out.println("MemWrite : "+ControlUnit.MemWrite);
        System.out.println("AluSrc : "+ControlUnit.AluSrc);
        System.out.println("RegWrite : "+ControlUnit.RegWrite);
        System.out.println("AluOP : "+ControlUnit.AluOP);
        System.out.println("Jump : "+ControlUnit.Jump);
    }
}
