import java.lang.reflect.Array;
import java.util.Random;

public class RandomReference {
    //假設每1/10到1/20 reference 會呼叫特定reference 位置 n n+1 .... n+20 這段
    int allRandom[] = new int[100000];
    int testcase[] = {90, 85, 55, 94, 77, 90 ,85 ,85, 48, 77, 94, 77, 90 ,85};
    int Locality[] = new int[100000];

    RandomReference(int n){
        for(int i = 0; i<100000; i++){
            allRandom[i] = (int)(Math.random()*500+1);
        }

    }
}
