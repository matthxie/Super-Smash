import java.util.Comparator;

public class SortByDecreasingDiff implements Comparator<Map> {

	@Override
	public int compare(Map o1, Map o2) {
		// TODO Auto-generated method stub
		return o2.getDifficulty()-o1.getDifficulty();
	}

}
