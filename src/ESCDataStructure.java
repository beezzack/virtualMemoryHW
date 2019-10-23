public class ESCDataStructure {
    int pages;
    int referenceBit;
    int modifyBit;

    ESCDataStructure(int thepage){
        pages = thepage;
        referenceBit = 1;
        int p = (int)(Math.random()*10+1);
        if(p<=3)modifyBit = 1;
        else modifyBit = 0;
    }
}
