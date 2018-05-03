/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.architecture;

/**
 *
 * @author OmarSelim
 */
public class ALUControl {
    private String AluOp;
    public ALUControl(){
        this.AluOp = ControlUnit.AluOP;
    }
    public String getALUCtrl(String funct){
        //if aluop is 00 lw/sw , 01 beq , 10 R
        if(this.AluOp.equals("000")){ //lw/sw/sb/lb/lbu - add
            return "0010";
        }
        else if(this.AluOp.equals("001")){ //beq -sub
            return "0110";
        }else if(this.AluOp.equals("100")){//slti -setonlessthan
            return "0111";
        }
        else if(this.AluOp.equals("010")){ //R-TYPE
            if(funct.equals("100000")){//add
                return "0010";
            }else if(funct.equals("000000")){ //SLL
                return "0000";
            }else if(funct.equals("100111")){ //NOR
                return "1100";
            }else if(funct.equals("101010")){//SLT
                return "0111";
            }else if(funct.equals("001000")){//jr
                return "1001";
            }
        }
        return null;
    }
    public String getShamtSrc(String funct){
        if(ControlUnit.AluOP.equals("010")){
            if(funct.equals("000000")){
                return "1";
            }else{
                return "0";
            }
        }else{
            return "0";
        }
    }
}
