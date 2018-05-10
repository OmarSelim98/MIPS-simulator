
package org.architecture;

import java.util.Scanner;

public class Instruction_memory {
    public static String [] IntMemory= new String[40000000];
    //the instruction memory has to be exactly a multiple of 4
    //this takes the position which the user wishes to insert his instructions at
    public int IntCounter=this.starting_address;
    private int starting_address;//the IntCounter is used for telling us the number of instructions we have
   public Instruction_memory(int starting_address){
       this.starting_address = starting_address*4;
   }

    public int CurrentInstruction=this.starting_address;
    // this at the beginning will tell us the position of the first instruction and will increase till it reaches the value of the IntCounter
    
    public void add(String[] Instructions)//adds all the instructions passed by the assembler to the instruction memory
    {int inst_count=0;
    // inst_count is used to tell us which instruction we are in the array of strings of size 32 passed by the assembler
    int begin =0;
    int end =8;
    
        for(int i=0;i<(Instructions.length*4);i++)
        {
            
            System.out.println("while adding : "+(starting_address+i));
            IntMemory[starting_address+i]=Instructions[inst_count++/4].substring(begin,end);
            
            if ((inst_count)%4==0)
            {
                begin=0;
                end=8;
            }
                else{
                begin=end;
                
                end+=8;
                }
            IntCounter++;
        }
        
       
    }
    public  String FetchNext()
            //this fetches the instruction and increments the CurrentInstruction by 4 which moves us to the next instruction
            //but it returns it in the form of a 32 sized string which is easier to divide later
    {
    String str="";
    for(int i=0;i<4;i++)
    {
        str+=Instruction_memory.IntMemory[this.CurrentInstruction+i];
    }
    this.CurrentInstruction+=4;
      return str;  
    }
    
    public String Fetch(int address){
        String str = "";
        for (int i=0;i<4;i++){
            System.out.println("WHile Fetching : "+(starting_address+address+i));
            str += Instruction_memory.IntMemory[starting_address+address+i];
        }
    this.CurrentInstruction += starting_address+address+4;
    return str;
    }
    public String[] Divide_Instruction(String Instruction)
            //divides the instruction to it's standard form (opcode rs rt rd shifamt funcode)
    {

        String [] str=new String[6];
        str[0]=Instruction.substring(0,6);
        str[1]=Instruction.substring(6,11);
        str[2]=Instruction.substring(11,16);
        str[3]=Instruction.substring(16,21);
        str[4]=Instruction.substring(21,26);
        str[5]=Instruction.substring(26,32);
        
        return str;
    }
    
    }