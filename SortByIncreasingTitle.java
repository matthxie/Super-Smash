import java.util.Comparator;

public class SortByIncreasingTitle implements Comparator<Map>{

	@Override
	public int compare(Map o1, Map o2) {
		// TODO Auto-generated method stub
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
