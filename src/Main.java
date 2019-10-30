import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    static private void FIFOAlgo(int pageTableSize,int[] testcase){
        /*
        FIFO 演算法:
        1. page fault++ 條件: 當table中沒有要求的reference
        2. diskwrite++ 條件: <=30%
        3. 當page fault發生的時候：先進先出
        4. interrupt: trap一次，disk寫入一次 = page fault*2
        5. 如果已經在table內重新植入arraylist<>，當作新的
         */
        FIFOpage thePage = new FIFOpage();
        HardDrive thereference = new HardDrive();

        int pagefault = 0;
        int harddriveW = 0;

        for (int s:testcase
                ) {
            if(thePage.pageSet.size()>=pageTableSize){//if page table is full
                int tmp = thePage.index.peek();
                if(!thePage.pageSet.contains(s)){//element 不在page table i.e. Trap
                    pagefault++;
                    thePage.pageSet.remove(tmp);//刪除最先放進去的
                    thePage.index.remove();

                    //maybe the disk need to write first because the value in physical memory.
                    int randomNum = (int)(Math.random()*10+1);
                    if(randomNum<=3)harddriveW++;
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
    static private void OptimalAlgo(int pageTableSize,int[] testcase){
        /*
        Optimal 演算法:
        1. page fault++ 條件: 當table中沒有要求的reference
        2. diskwrite++ 條件: <=30%
        3. 當page fault發生的時候：進入predict() 得到應該替換的table index
        4. predict() 找下次出現時的位置，最遠的即是替換目標
        5. interrupt: trap一次，disk寫入phyical memory一次 = page fault*2
         */
        Optimalpage thePage = new Optimalpage();
        HardDrive thereference = new HardDrive();
        int pagefault = 0;
        int harddriveW = 0;

        for (int i = 0; i<testcase.length; i++
             ) {
            int thisreference = testcase[i];
/*            for (int a :thePage.pageSet
                 ) {
                System.out.print(a+" ");
            }
            System.out.println(": "  + thisreference);*/
            if(thePage.pageSet.size() >= pageTableSize){// page table full
                if(!thePage.pageSet.contains(thisreference)){ //page fault
                    pagefault++;
                    int predictResult = predict(pageTableSize,testcase,thePage.index,i);

                    //maybe the disk need to write first because the value in physical memory.
                    int randomNum = (int)(Math.random()*10+1);
                    if(randomNum<=3)harddriveW++;

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
    static private void ESCAlgo(int pageTableSize,int[]testcase){
        /*
        ESC 演算法:
        1. page fault++ 條件: 當table中沒有要求的reference
        2. diskwrite++ 條件: modifybit = 0(30%)
        3. 當page fault發生的時候：進入pick() 得到應該替換的table index
        4. pick() 預設reference bit = 1 首先先調查是否有(0,0) 再來調查(0,0)(0,1)如果都不是 referencebit = 0
        5. interrupt: trap一次，disk寫入phyical memory一次 = page fault*2
         */
        ESCpage thePage  = new ESCpage();
        HardDrive thereference = new HardDrive();
        int pagefault = 0;
        int harddriveW = 0;
        int pickResult = 0;

        for(int i = 0; i<testcase.length; i++){
            int thisreference = testcase[i];
            if(thePage.pageSet.size()>=pageTableSize){//pagetable full
                if(!thePage.pageSet.contains(thisreference)){
                    pagefault++;
                    pickResult = pick(pageTableSize,thePage,pickResult);
                    if(thePage.index.get(pickResult).modifyBit == 1){
                        harddriveW++;
                    }
                    thePage.pageSet.remove(thePage.index.get(pickResult).pages);



                    //get element in hardDrive; bring in the missing page
                    int diskReference = thereference.reference[thisreference-1];
                    thePage.index.get(pickResult).pages = diskReference;
                    int p = (int)(Math.random()*10+1);
                    if(p<=3)thePage.index.get(pickResult).modifyBit = 1;
                    else thePage.index.get(pickResult).modifyBit = 0;
                    thePage.index.get(pickResult).referenceBit = 1;
                    thePage.pageSet.add(diskReference);
                    pickResult = ++pickResult%pageTableSize;
                }
                else {
                    modify(thePage,thisreference);
                }
            }
            else{
                if(!thePage.pageSet.contains(thisreference)){ //page fault
                    pagefault++;
                    //get element in hardDrive; bring in the missing page
                    int diskReference = thereference.reference[thisreference-1];
                    ESCDataStructure tmpESC = new ESCDataStructure(diskReference);
                    thePage.index.add(tmpESC);
                    thePage.pageSet.add(diskReference);
                }
                else {
                    modify(thePage,thisreference);
                }
            }
        }
        System.out.println("pagefault: " + pagefault + ", harddriveW: " + harddriveW);
    }
    static private void CRSCAlgo(int pageTableSize,int[]testcase){
        /*
        CRSC 演算法:
        1. page fault++ 條件: 當table中沒有要求的reference
        2. diskwrite++ 條件: (30%)
        3. 當page fault發生的時候：進入CRSCpick() 得到應該替換的table index
        4. CRSCpick() 預設reference bit = 1 SCBit = 1, 首先先調查是否有(0,0) 再來調查(0,0)(0,1)如果都不是  如果這個table index page>28 reference bit == 0(換掉)

        5. interrupt: trap一次，disk寫入phyical memory一次 = page fault*2
         */
        CRSCpage thePage = new CRSCpage();
        HardDrive thereference = new HardDrive();
        int pagefault = 0;
        int harddriveW = 0;
        int pickResult = 0;
        int buffer = 0;
        int thePTSize = pageTableSize-3;
        for(int i = 0; i<testcase.length; i++){
            if(thePage.pageSet.size()>=thePTSize){
                if(!thePage.pageSet.contains(testcase[i])){
                    pagefault++;
                    pickResult = CRSCpick(thePage, pickResult,testcase[i],thePTSize);
                    thePage.pageSet.remove(thePage.index.get(pickResult).pages);

                    int randomNum = (int)(Math.random()*10+1);
                    if(randomNum<=3)buffer++;
                    if(buffer>=3){
                        harddriveW++;
                        buffer = 0;
                    }

                    //get element in hardDrive; bring in the missing page
                    int diskReference = thereference.reference[testcase[i]-1];
                    thePage.index.get(pickResult).pages = diskReference;
                    thePage.index.get(pickResult).referenceBit = 1;
                    thePage.pageSet.add(diskReference);
                    pickResult = ++pickResult%thePTSize;
                }
                else {
                    CRSCmodify(thePage,testcase[i]);
                }
            }
            else {
                if(!thePage.pageSet.contains(testcase[i])){ //page fault
                    pagefault++;
                    //get element in hardDrive; bring in the missing page
                    int diskReference = thereference.reference[testcase[i]-1];
                    CSRCDataStructure tmpESC = new CSRCDataStructure(diskReference);
                    thePage.index.add(tmpESC);
                    thePage.pageSet.add(diskReference);
                }
                else {
                    CRSCmodify(thePage,testcase[i]);
                }
            }
        }
        System.out.println("pagefault: " + pagefault + ", harddriveW: " + harddriveW);
    }
    static private void modify(ESCpage thePage,int thisreference){
        for (ESCDataStructure E:thePage.index
             ) {
            if(E.pages == thisreference){
                E.referenceBit = 1;
                int p = (int)(Math.random()*10+1);
                if(p<=3)E.modifyBit = 1;
                else E.modifyBit = 0;
                break;
            }
        }
    }
    static private void CRSCmodify(CRSCpage thePage,int thisreference){
        for (CSRCDataStructure E:thePage.index
                ) {
            if(E.pages == thisreference){
                E.referenceBit = 1;
                break;
            }
        }
    }
    static private int pick(int pagetablesize, ESCpage thePage,int startPoint){
        int j = startPoint;
        int count = 0;
        /*(0,0)*/
        for (int i = 0; i < pagetablesize; i++){
            boolean tmp1 = (thePage.index.get(j).referenceBit == 0)&&(thePage.index.get(j).modifyBit == 0);
            if(tmp1){
                return j;
            }
            j = (++j)%pagetablesize;
        }
        j = startPoint;
        /*(0,0)(0,1)*/
        while (count<(pagetablesize*4)){
            boolean tmp1 = (thePage.index.get(j).referenceBit == 0)&&(thePage.index.get(j).modifyBit == 0);//1 0比1 1先被挑中
            boolean tmp2 = (thePage.index.get(j).referenceBit == 0)&&(thePage.index.get(j).modifyBit == 1);//0 1比另外兩情況先被挑中
            if(tmp1)return j;
            else if(tmp2)return j;
            else {
                thePage.index.get(j).referenceBit = 0;
            }
            count++;j = (++j)%pagetablesize;
        }
        return startPoint;
    }
    static private int CRSCpick(CRSCpage thePage, int startpoint,int thisreference,int tableSize){
        int pickResult = startpoint;
        for (int i = 0; i < tableSize; i++){
            boolean tmp1 = (thePage.index.get(pickResult).referenceBit == 0)&&(thePage.index.get(pickResult).SCBit == 0);
            if(tmp1){
                return pickResult;
            }
            pickResult = (++pickResult)%tableSize;
        }
        pickResult = startpoint;
        for(int count = 0;count < tableSize*4; count++){
            boolean tmp1 = (thePage.index.get(pickResult).referenceBit == 0)&&(thePage.index.get(pickResult).SCBit == 0);//1 0比1 1先被挑中
            boolean tmp2 = (thePage.index.get(pickResult).referenceBit == 0)&&(thePage.index.get(pickResult).SCBit == 1);//0 1比另外兩情況先被挑中
            if(tmp1)return pickResult;
            else if(tmp2)return pickResult;
            else{
                if(Math.abs(thisreference-thePage.index.get(pickResult).pages)>=25){
                    thePage.index.get(pickResult).referenceBit = 0;
                }
                thePage.index.get(pickResult).SCBit = 0;
                pickResult = ++pickResult%tableSize;
            }
        }

        return pickResult;
    }
    static private int predict(int pagetablesize, int[] testcase, ArrayList index,int startPoint){
        //回傳最久才用到的memmory reference
        int victimIndex = 0;
        //找最遠才用到的element當victim.
        int tmp,victim;
        victim = (int)(Math.random()*pagetablesize);
        int farestIndex = 0;
        for (int i = 0; i < index.size(); i++) {
            tmp = (Integer) index.get(i);
            victimIndex = 0;
            for(int j = startPoint; j< testcase.length; j++){
                if(tmp == testcase[j]){
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
        RandomReference memReference = new RandomReference(20);
        int[] testcase = memReference.allRandom;
        Scanner key = new Scanner(System.in);
        System.out.println("1.All random");
        System.out.println("2.Locality");
        System.out.println("3.Mytestcase");
        int option = key.nextInt();
        switch (option){
            case 1: testcase = memReference.allRandom; break;
            case 2: testcase = memReference.Locality; break;
            case 3: testcase = memReference.mycase; break;
        }
        for (int pageTableSize = 10; pageTableSize <= 100; pageTableSize+=10){
            System.out.println("pageTableSize: "+ pageTableSize);
            System.out.print("  FIFO :  ");
            FIFOAlgo(pageTableSize,testcase);
            System.out.print("  OPT :   ");
            OptimalAlgo(pageTableSize,testcase);
            System.out.print("  ESC :   ");
            ESCAlgo(pageTableSize,testcase);
            System.out.print(" CRSC :   ");
            CRSCAlgo(pageTableSize,testcase);
        }




    }
}
