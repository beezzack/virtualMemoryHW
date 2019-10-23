import java.util.ArrayList;
import java.util.HashSet;

public class ESCpage {
    public HashSet<Integer> pageSet;
    public final ArrayList<ESCDataStructure> index;

    ESCpage(){
        pageSet = new HashSet<>();
        index = new ArrayList<ESCDataStructure>();
    }
}
