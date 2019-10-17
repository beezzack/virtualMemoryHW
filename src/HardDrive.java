public class HardDrive {
    public String[] reference = new String[500];


    HardDrive(){
        for (int i = 0; i < 500; i++){
            reference[i] = Integer.toString(i+1);
        }
    }
}
