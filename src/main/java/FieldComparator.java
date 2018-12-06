import java.util.Comparator;
import java.util.List;

/**
 * Created by e080868 on 12/5/2018.
 */
public class FieldComparator implements java.util.Comparator<List<Field>> {

    private int index;


    public  FieldComparator(int index){
        this.index = index;
    }

    public int compare(List<Field> o1, List<Field> o2) {
        Field f1 = o1.get(index);
        Field f2 = o2.get(index);
        if(f1.getValue() instanceof Integer){
            return ((Integer) f1.getValue()).compareTo((Integer)f2.getValue());
        } else if(f1.getValue() instanceof Double){
            return ((Double) f1.getValue()).compareTo((Double)f2.getValue());
        } else if(f1.getValue() instanceof String) {
            return ((String) f1.getValue()).compareTo((String)f2.getValue());
        }
        return 0;    }
}
