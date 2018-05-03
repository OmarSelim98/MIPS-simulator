package org.architecture;
//Only register file write back method is needed to complete this class

public class DataMemory {
    private String mem_write;
    private String mem_read;
    private String address;
    private String write_data;
    private String op_code;

    public static String [] Memory = new String[4000000];
    // need access to the Register file to write back

    public void set_mem_write(String mem_write){
        this.mem_write = mem_write;
    }
    public void set_mem_read(String mem_read){
        this.mem_read = mem_read;
    }
    public void set_address(String address){
        this.address = mem_write;
    }
    public void set_data(String write_data){
        this.write_data = write_data;
    }
    public void set_op_code(String op_code){
        this.op_code = op_code;
    }

    public String Calculate(){
        String data = "";
        if(this.mem_write.equals("1")){
            ChooseMemoryOpeartion(this.address,this.write_data,this.op_code);
        }else if(this.mem_write.equals("0")){
            if(this.mem_read.equals("1")){
                data = ChooseMemoryOpeartion(this.address,this.write_data,this.op_code);
            }
        }
        return data;
    }

    public String LoadWord(String address)
    {
        int adress=  Integer.parseInt(address,2);
        String LoadedValue="";
        for(int i=adress;i<adress+4;i++)
        {
            if(Memory[i] != null) {
                LoadedValue += Memory[i];
            }else{
                LoadedValue += "00000000";
            }
        }

        return LoadedValue;
        //then we call the method which tells us where to store this value
    }
    public void StoreWord(String Address,String Value)
    {
        int begin =0,end=8;
        int address = Integer.parseInt(Address,2);
        for(int i= address;i<address+4;i++)
        {
            Memory[i]=Value.substring(begin,end);
            begin=end;
            end+=8;
        }


    }
    public void StoreByte(String Address,String value)//take least Significant 8 bits
    {
        int address = Integer.parseInt(Address,2);
        Memory[address]=value.substring(25,32);
    }
    public String LoadByte(String Address)
    // need to sign extend
    {
        String LoadedByte="";
        int address= Integer.parseInt(Address,2);
        if(Memory[address] != null) {
            if (Memory[address].charAt(0) == '0') {
                LoadedByte += "000000000000000000000000" + Memory[address];

            } else if (Memory[address].charAt(0) == '1') {
                LoadedByte += "111111111111111111111111" + Memory[address];
            }
        }else{
            LoadedByte+= "00000000000000000000000000000000";
        }
        return LoadedByte;
        // use accessor method from register file to set
    }
    public String LoadByteUnsigned(String address)
    {
        String LoadByte="";
        if(Memory[Integer.parseInt(address,2)] != null) {
            LoadByte += "000000000000000000000000" + Memory[Integer.parseInt(address, 2)];
        }else{
            LoadByte += "00000000000000000000000000000000";
        }
        return LoadByte;
        //use accessor method to load back into wanted register
    }
    public String ChooseMemoryOpeartion(String adress,String Value,String Opcode)
            //Picks the desired Operation based on the passed opcode
            //Takes all the parameters the data memory takes exactly like the data path

    {
        switch (Opcode){
            case ("100000"):{
                return this.LoadByte(adress);

            }
            case("100100"):{
                return this.LoadByteUnsigned(adress);

            }
            case("100011"):{
                return this.LoadWord(adress);
            }
            case("101000"):{
                 this.StoreByte(adress,Value);
            }
            case("101011"):{
                 this.StoreWord(adress,Value);
            }
        }
    return  null;
    }


}
