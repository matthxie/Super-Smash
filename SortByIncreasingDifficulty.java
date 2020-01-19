import java.util.Comparator;

public class SortByIncreasingDifficulty implements Comparator<Map>{
	public int compare(Map o1, Map o2) {
		return o1.getDifficulty()- o2.getDifficulty();
	}
}
