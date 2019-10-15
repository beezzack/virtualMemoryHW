import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

    static private void FIFOAlgo(int pageTableSize){

        FIFOpage thePage = new FIFOpage();
        HardDrive thereference = new HardDrive();
        RandomReference memReference = new RandomReference(20);
        int pagefault = 0;
        int harddriveW = 0;

        for (String s:memReference.allRandom
                ) {
            if(thePage.pageSet.size()>=pageTableSize){//if page table is full
                String tmp = thePage.index.peek();
                if(!thePage.pageSet.contains(s)){//element 不在page table i.e. Trap
                    pagefault++;
                    thePage.pageSet.remove(tmp);//刪除最先放進去的
                    thePage.index.remove();

                    //get element in hardDrive; bring in the missing page, write++
                    String tmpReference = thereference.reference[Integer.parseInt(s)-1];
                    harddriveW++;
                    thePage.pageSet.add(tmpReference);
                    thePage.index.add(tmpReference);
                }
                else {
                    //make tmp become first input,
                    thePage.index.remove();
                    thePage.index.add(tmp);
                }
            }
            else{//still have space put in a free frame refresh page table
                String tmpReference = thereference.reference[Integer.parseInt(s)-1];
                thePage.pageSet.add(tmpReference);
                thePage.index.add(tmpReference);
            }
        }
        System.out.println("pagefault: " + pagefault + ", harddriveW: " + harddriveW);
    }
    static private void OptimalAlgo(int pageTableSize){
        Optimalpage thePage = new Optimalpage();
        HardDrive thereference = new HardDrive();
        RandomReference memReference = new RandomReference(20);
        int pagefault = 0;
        int harddriveW = 0;

        for (int i = 0; i<=memReference.allRandom.length; i++
             ) {
            if(thePage.pageSet.size()>=pagefault){// page table full
                if(!thePage.pageSet.contains(memReference.allRandom[i])){ //page fault
                    int predictResult = predict(memReference,thePage.index,i);
                    if(predictResult != 0){
                        String victim = thePage.index.get(predictResult);
                        thePage.pageSet.remove(victim);
                    }
                    else{
                        String victim = thePage.index.get(0);
                    }


                }
            }

        }
    }
    static private int predict(RandomReference memReference, ArrayList index,int startPoint){
        int victim = 0;
        String[] arr = new String[10];
        //store 10 data which will use in the future
        for(int i = 0; i < 10; i++ ){
            arr[i] = (String) index.get(startPoint+i);
        }




        return victim;
    }
    public static void main(String[] args) {
        int pageTableSize = 10;
        FIFOAlgo(pageTableSize);



    }
}
