import java.util.Comparator;

public class SortByDecreasingTitle implements Comparator<Map>{
	public int compare(Map o1, Map o2) {
		return o2.getTitle().compareTo(o1.getTitle());
	}
}
