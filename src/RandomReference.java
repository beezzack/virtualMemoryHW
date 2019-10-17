import java.util.Random;

public class RandomReference {
    //假設每1/10到1/20 reference 會呼叫特定reference 位置 n n+1 .... n+20 這段
    int allRandom[] = new int[10000];
    int Locality[] = new int[10000];

    RandomReference(int n){
        for(int i = 0; i<10000; i++){
            allRandom[i] = (int)(Math.random()*500+1);
        }

    }
}
