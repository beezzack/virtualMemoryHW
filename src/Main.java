public class Main {

    static private void FIFOAlgo(int pageTableSize){

        FIFOpage thePage = new FIFOpage();
        HardDrive thereference = new HardDrive();
        RandomReference memReference = new RandomReference(20);
        int pagefault = 0;
        int harddriveW = 0;

        for (String s:memReference.allRandom
                ) {
            if(thePage.pageSet.size()>=pageTableSize){//if page table is full && element 不在page table 刪除最先放進去的
                pagefault++;
                String tmp = thePage.index.peek();
                if(!thePage.pageSet.contains(s)){
                    thePage.pageSet.remove(tmp);
                    thePage.index.remove();

                    //get element in hardDrive;
                    String tmpReference = thereference.reference[Integer.valueOf(s)-1];
                    harddriveW++;
                    thePage.pageSet.add(tmpReference);
                    thePage.index.add(tmpReference);
                }
                else{
                    //make tmp become first input,
                    thePage.index.remove();
                    thePage.index.add(tmp);
                }
                //System.out.println("pagefault occur, remove "+tmp+" add "+s);
            }
            else{
                String tmpReference = thereference.reference[Integer.valueOf(s)-1];
                thePage.pageSet.add(tmpReference);
                thePage.index.add(tmpReference);
            }
        }
        System.out.println("pagefault: " + pagefault + ", harddriveW: " + harddriveW);
    }
    static private void OptimalAlgo(int pageTableSize){
        
    }
    public static void main(String[] args) {
        int pageTableSize = 10;
        FIFOAlgo(pageTableSize);



    }
}
