package LazyLoading;
 
import dominio.Decreto;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

public class LazySorter implements Comparator<Decreto> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(Decreto decreto1, Decreto decreto2) {
        try {
            Object value1 = Decreto.class.getField(this.sortField).get(decreto1);
            Object value2 = Decreto.class.getField(this.sortField).get(decreto2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}