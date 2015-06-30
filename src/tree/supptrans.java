package tree;

import java.util.ArrayList;

/*
 * ��ʾĳһ��itemset X��supporting transaction
 */

public class supptrans {
	public ArrayList<trans> Tx; // supporting transaction����ŵ�transaction

	// �ж�һ��transaction�Ƿ����ĳһ��item�ļ��ϣ���itemset��transaction��itemid�ǰ����������򣬿�����һ��ѭ��ʵ��
	public boolean contain(trans t1, ArrayList<Integer> itemset) {
		int count = 0;
		for (int i = 0; i < t1.t.size(); i++) {
			for (int j = 0; j < itemset.size(); j++)
				if (t1.t.get(i).itemid == Integer.parseInt(itemset.get(j)
						.toString())) // �ж�itemid�Ƿ����
				{
					count++;
					if (count == itemset.size()) // ���ȫ����ȣ����˳�
						return true;
				}
		}
		return false;
	}

	public supptrans() {
		Tx = new ArrayList<trans>();
	}

	// only used in DGU_Strategy
	public supptrans(database D, ArrayList<Integer> X) {
		if (X.size() > 0) {
			Tx = new ArrayList<trans>();
			for (int i = 0; i < D.getsize(); i++) {
				if (contain(D.tList.get(i), X))
					Tx.add(D.get(i));
			}
		} else
			Tx = new ArrayList<trans>(D.tList);
	}

	// used in other ways
	public supptrans(database D, ArrayList<Integer> X, int utimin) {

		// //��X���ǿռ�,�������ݿ⣬��ĳһ��transaction����X���е�item��X��transaction�е�utility������ֵutimin��
		// ��transaction��ӵ�supporting transaction��
		if (X.size() > 0) {
			Tx = new ArrayList<trans>();
			for (int i = 0; i < D.getsize(); i++) {
				if (contain(D.tList.get(i), X)
						&& ishighutility(D.tList.get(i), X, utimin))
					Tx.add(D.get(i));
			}
		} else
			// ��X�ǿռ���database����X��supporting transaction
			Tx = new ArrayList<trans>(D.tList);
	}

	public boolean ishighutility(trans trans_t, ArrayList<Integer> X, int utimin) {
		int i = 0;
		float Xuti = 0;
		for (item item_i : trans_t.t)
			// ����transaction�е�����item���ҵ���Ӧ��item����item utility���
			if (item_i.itemid == X.get(i)) {
				Xuti += item_i.itemuti;
				i++;
				if (i == X.size())
					break;
			}
		return Xuti >= utimin ? true : false;
	}

	public int getsize() {
		return Tx.size();
	}

	public trans get(int i) {
		return Tx.get(i);
	}

	public static void main(String[] args) {
		database D = new database();
		ArrayList<Integer> itemidset = new ArrayList<Integer>();
		itemidset.add(2);
		itemidset.add(3);
		int utimin = 3;
		supptrans tx = new supptrans(D, itemidset, utimin);
		for (int i = 0; i < tx.getsize(); i++) {
			System.out.println(tx.get(i).transuti);
			for (int j = 0; j < tx.get(i).t.size(); j++) {
				System.out.println(tx.get(i).t.get(j).itemid + " "
						+ tx.get(i).t.get(j).itemuti);
			}
		}
	}

}
