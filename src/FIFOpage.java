import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class FIFOpage{
    public HashSet<String> pageSet;
    public final Queue<String> index;

    FIFOpage(){
        pageSet = new HashSet<>();
        index = new LinkedList<>() ;
    }
}
