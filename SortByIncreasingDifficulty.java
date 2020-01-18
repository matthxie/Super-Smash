import java.util.Comparator;

public class SortByIncreasingDifficulty implements Comparator<Map>{

	@Override
	public int compare(Map o1, Map o2) {
		
		return o1.getDifficulty()- o2.getDifficulty();
	}

}
