package ar.edu.itba.paw.lib;
import java.util.ArrayList;
import java.util.Collection;


public class UniqueElementList<T> extends ArrayList<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private ArrayList<T> list;

	public UniqueElementList() {
		super();
//		this.list = new ArrayList<T>();
	}

	public boolean add(T element) {
//		for (T item : list) {
//			if (item.equals(element)) {
//				return false;
//			}
//		}
//		list.add(element);
		if(contains(element)){
			return false;
		}else{
			super.add(element);
		}
		return true;
	}

//	public ArrayList<T> getList() {
//		return this.list;
//	}
	
	@Override
	public boolean addAll(Collection<? extends T> c){
		boolean ans = true;
		for(T elem: c){
			ans = ans && add(elem);
		}
		return ans;
	}
}