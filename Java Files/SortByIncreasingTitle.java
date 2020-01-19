import java.util.Comparator;

public class SortByIncreasingTitle implements Comparator<Map>{
	public int compare(Map o1, Map o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}
}
