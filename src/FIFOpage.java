import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class FIFOpage{
    public HashSet<Integer> pageSet;
    public final Queue<Integer> index;

    FIFOpage(){
        pageSet = new HashSet<>();
        index = new LinkedList<>();
    }
}
