package ar.edu.itba.paw.lib;

import java.util.ArrayList;
import java.util.List;

public class RandomSublist {
	
	/**
	 * @param list - the {@link List} of elements
	 * @param n - the number of elements of the {@link List} to be returned
	 * @return - a {@link List} with n random elements from list
	 */
	public static <T> List<T> getRandomNFromList(List<T> list, int n){
		List<T> ans = new ArrayList<T>();
		if (list.size() <= n) {
			ans.addAll(list);
			return ans;
		}
		int rand;
		boolean flag = false;
		for (int i = 0; i < n; i++) {
			flag = true;
			while (flag) {
				rand = (int) (Math.random() * list.size());
				if (!ans.contains(list.get(rand))) {
					flag = false;
					ans.add(list.get(rand));
				}
			}
		}
		return ans;
	}

}
