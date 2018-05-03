package org.architecture;

public class ALU {

    private String input1;
    private String input2;
    private String code;
    private String result;
    private String zeroFlag = "0";

    /**Set the first input*/
    public void setInput1(String data){
        this.input1 = data;
    }
    /**Set the second input*/
    public void setInput2(String data){
        this.input2 = data;
    }
    /**Set the ALU control code*/
    public void setControlCode(String code){
        this.code = code;
    }
    /**Do The Calculation*/
    public void calculate(){
        try {
            int num1 = Integer.parseInt(input1, 2);
            int num2 = Integer.parseInt(input2, 2);
            int res;
            if (code.equals("0010")) { //ADD (add - addi - sw - lw - sb - lb - lbu)
                res = num1 + num2;
                this.result = this.convert_extend(res);
            } else if (code.equals("0110")) { //SUB(beq)
                res = num1 - num2;
                if (res == 0) {
                    this.zeroFlag = "1";
                }
            } else if (code.equals("1100")) {//nor
                this.result = "";
                for (int i = 0; i < this.input1.length(); i++) {
                    if (input1.charAt(i) == '0' && input1.charAt(i) == input2.charAt(i)) {
                        this.result += "1";
                    } else {
                        this.result += "0";
                    }
                    //NO NEED TO CONVERT NOR EXTEND
                }
            } else if (code.equals("0000")) {//sll
                res = num1 * (int) Math.pow(2, num2);
                System.out.println(res);
                this.result = this.convert_extend(res);
            } else if (code.equals("0111")) {//slt-slti
                //if num1 < num2 , res= 1
                if ((num1 < num2)) {
                    res = 1;
                } else {
                    res = 0;
                }
                this.result = this.convert_extend(res);
            } else if (code.equals("1001")) {//jr
                //WAITING
            }
        }catch (Exception e){
            this.input1 = "000000000000000000000000000000000";
            this.input2 = "000000000000000000000000000000000";
            this.result = "000000000000000000000000000000000";
        }
    }

    /**Get the Calculation's result*/
    public String getResult(){
        return this.result;
    }
    /**Get The Zero Flag*/
    public String getZeroFlag(){
        return this.zeroFlag;
    }

    private String convert_extend(int num){
        String num_str = Integer.toBinaryString(num);
        String new_str = "";
        if(num_str.length() < 32){
            String filler = "0";
            for(int i=0;i<32-num_str.length();i++){
                new_str += filler;
            }
            new_str+=num_str;
            return new_str;
        }else if(num_str.length() == 32){
            return num_str;
        }else{
            new_str += num_str.substring(num_str.length()-32, num_str.length());
            return new_str;
        }
    }
}
