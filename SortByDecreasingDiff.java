import java.util.Comparator;

public class SortByDecreasingDiff implements Comparator <Map> {

	public int compare(Map o1, Map o2) {
		return o2.getDifficulty()-o1.getDifficulty();
	}
}
