package tree;

import java.util.ArrayList;
import java.util.TreeSet;

/*
 * �������Ҫ��д
 * ��д֮������ArrayList<Integer> allitem, ���allitem��ȫ���Ӽ�����ArrayList<ArrayList<Integer>> allitemset �洢
 * UPdateed 2015��6��22�� By Bilong Shen
 */

public class calculateSet {


/*	private static void showAllitem(ArrayList<Integer> allitem) {
		// TODO Auto-generated method stub
		for (int i = 0; i < allitem.size(); i++) {
			System.out.println(allitem.get(i));

		}
	}*/

	public  void showAllSet(ArrayList<ArrayList<Integer>> subset) {
		if (subset == null) {
			return;
		}
		for (int i = 0; i < subset.size(); i++)
			System.out.println(subset.get(i));
	}

	public  ArrayList<ArrayList<Integer>> allitemset(
			ArrayList<Integer> allitem) {
		
		
		ArrayList<ArrayList<Integer>> allSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> curSet = new ArrayList<Integer>();
		subSetHelper(allSet, curSet, allitem, 0);
		return allSet;
	}

	private  void subSetHelper(ArrayList<ArrayList<Integer>> allSet,
			ArrayList<Integer> curSet, ArrayList<Integer> allitem, int pos) {
		// ���������Ӽ��ĸ������������û��ݵķ���
		//addToResult(allSet, curSet);
		
		for (int i = pos; i < allitem.size(); i++) {
			curSet.add(allitem.get(i));
			subSetHelper(allSet, curSet, allitem, i + 1);
			curSet.remove(curSet.size() - 1);
		}

	}

	private  void addToResult(ArrayList<ArrayList<Integer>> allSet,
			ArrayList<Integer> curSet) {
/*		allSet.add(new ArrayList<Integer>(curSet));
*/	}

	public  static void main(String[] args) {
		for (int setNum = 0 ; setNum < 200; setNum++)
		{
			ArrayList<Integer> allitem = new ArrayList<Integer>();

			for (int i = 0; i <setNum; i++) {
				allitem.add(i + 1);
			}
			calculateSet  testCalset = new calculateSet();
	        long curT = System.currentTimeMillis();
			ArrayList<ArrayList<Integer>> subset = testCalset.allitemset(allitem);
			System.out.println("When set Number is "+setNum+"\tAll calculatetime :"+ (System.currentTimeMillis() - curT) +"ms");
		}
		
	
		//testCalset.showAllSet(subset);

	}
}
