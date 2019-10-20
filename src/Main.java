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

        for (int s:memReference.allRandom
                ) {
            if(thePage.pageSet.size()>=pageTableSize){//if page table is full
                int tmp = thePage.index.peek();
                if(!thePage.pageSet.contains(s)){//element 不在page table i.e. Trap
                    pagefault++;
                    thePage.pageSet.remove(tmp);//刪除最先放進去的
                    thePage.index.remove();

                    //get element in hardDrive; bring in the missing page
                    int tmpReference = thereference.reference[s-1];
                    thePage.pageSet.add(tmpReference);
                    thePage.index.add(tmpReference);
                }
                else {
                    //make tmp become first input,
                    thePage.index.remove(s);
                    thePage.index.add(s);
                }
            }
            else{//still have space put in a free frame refresh page table
                if(!thePage.pageSet.contains(s)){//element 不在page table i.e. Trap
                    pagefault++;
                    int diskReference = thereference.reference[s-1];
                    thePage.pageSet.add(diskReference);
                    thePage.index.add(diskReference);
                }
                else{
                    //make tmp become first input,
                    thePage.index.remove(s);
                    thePage.index.add(s);
                }
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

        for (int i = 0; i<memReference.allRandom.length; i++
             ) {
            int thisreference = memReference.allRandom[i];
/*            for (int a :thePage.pageSet
                 ) {
                System.out.print(a+" ");
            }
            System.out.println(": "  + thisreference);*/
            if(thePage.pageSet.size() >= pageTableSize){// page table full
                if(!thePage.pageSet.contains(thisreference)){ //page fault
                    pagefault++;
                    int predictResult = predict(pageTableSize,memReference,thePage.index,i);
                    //TO DO Disk write

                    //
                    thePage.pageSet.remove(thePage.index.get(predictResult));
                    thePage.index.remove(predictResult);


                    //get element in hardDrive; bring in the missing page
                    int diskReference = thereference.reference[thisreference-1];
                    thePage.index.add(diskReference);
                    thePage.pageSet.add(diskReference);
                }
            }
            else{
                if(!thePage.pageSet.contains(thisreference)){ //page fault
                    pagefault++;
                    //get element in hardDrive; bring in the missing page
                    int diskReference = thereference.reference[thisreference-1];
                    thePage.index.add(diskReference);
                    thePage.pageSet.add(diskReference);
                }
            }
        }
        System.out.println("pagefault: " + pagefault + ", harddriveW: " + harddriveW);
    }
    static private int predict(int pagetablesize, RandomReference memReference, ArrayList index,int startPoint){
        //回傳最久才用到的memmory reference
        int victimIndex = 0;
        //找最遠才用到的element當victim.
        int tmp,victim;
        victim = (int)(Math.random()*pagetablesize);
        int farestIndex = 0;
        for (int i = 0; i < index.size(); i++) {
            tmp = (Integer) index.get(i);
            victimIndex = 0;
            for(int j = startPoint; j< memReference.allRandom.length; j++){
                if(tmp == memReference.allRandom[j]){
                    victimIndex = j;
                    break; //stop, find first element
                }
            }
            if(victimIndex >= farestIndex){
                farestIndex = victimIndex ;
                victim = tmp;
            }
        }
        return index.indexOf(victim);
    }
    public static void main(String[] args) {
        int pageTableSize = 10;
        FIFOAlgo(pageTableSize);
        OptimalAlgo(pageTableSize);



    }
}
