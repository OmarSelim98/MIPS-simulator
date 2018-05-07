
package org.architecture;

public class RegisterFile {
    static public String [] regData = new String[32];
    static public String [] regName = new String[32];
    private String reg1;
    private String reg2;
    private String regWriteBool; // Boolean string for knowing if we will write or not
    private String regWrite;
    
    public RegisterFile(){
       this.regName[0]="$0";
       this.regName[1]="$at";
       this.regName[2]="$v0";
       this.regName[3]="$v1";
       this.regName[4]="$a0";
       this.regName[5]="$a1";
       this.regName[6]="$a2";
       this.regName[7]="$a3";
       this.regName[8]="$t0";
       this.regName[9]="$t1";
       this.regName[10]="$t2";
       this.regName[11]="$t3";
       this.regName[12]="$t4";
       this.regName[13]="$t5";
       this.regName[14]="$t6";
       this.regName[15]="$t7";
       this.regName[16]="$s0";
       this.regName[17]="$s1";
       this.regName[18]="$s2";
       this.regName[19]="$s3";
       this.regName[20]="$s4";
       this.regName[21]="$s5";
       this.regName[22]="$s6";
       this.regName[23]="$s7";
       this.regName[24]="$t8";
       this.regName[25]="$t9";
       this.regName[26]="$k0";
       this.regName[27]="$k1";
       this.regName[28]="$gp";
       this.regName[29]="$sp";
       this.regName[30]="$fp";
       this.regName[31]="$ra";
       for(int i=0;i<regData.length;i++){
           regData[i]="00000000000000000000000000000000";
       }
       this.reg1 = "";
       this.reg2="";
    }
    /** Used to set the register that we will read from it in the next stag
     * @param regBinName The Binary code of the register.*/
    public void setRead(String regBinName,int regNum){
        if(regNum == 1){
        this.reg1 = ""+Integer.parseInt(regBinName,2);
        }else if(regNum == 2){
            this.reg2 = ""+Integer.parseInt(regBinName,2);
        }
    }
     /** takes an int refering to the register that we will read from (1 or 2)*/
    public String read (int regNum){
        //a string of binary reg name is passed
        int r;
        if(regNum == 1){
            r = Integer.parseInt(reg1);
            return regData[r];
        }else if(regNum == 2){
            if (reg2.equals("")){
               return null;
           }else{
           r = Integer.parseInt(reg2);
            return regData[r];
           }
        }
        return null;
    }
    /**Takes a String of Binary Register File name and Prepare it to write in it*/
    public void setWrite(String regName) throws Exception{
        if(Integer.parseInt(regName,2) != 0) {
            this.regWrite = "" + Integer.parseInt(regName, 2);
        }else{
            throw new Exception("You can't rewrite the zero register!");
        }
    }
    /**  */
    public void write(String data){
        int reg_index = Integer.parseInt(this.regWrite);
        this.regWriteBool = ControlUnit.RegWrite;
        if (regWriteBool.equals("1")){ //we check if we can write data
            //save data to register
            this.regData[reg_index] = data;
        }
        //do nothing
    }
    
   
    
    public static int getIndex(String regName){
        int ind = 0;
        for(int i=0;i<RegisterFile.regName.length;i++){
            if(regName.equals(RegisterFile.regName[i])){
                return i;
            }
        }
        return -1;
    }
    public static String getName(int ind){
        
        return regName[ind];
    }
    
}
