package tree;
import java.util.ArrayList;
import java.util.Collections;

/* 
 * This class is used to calculate the occupancy of any itemset X
 * input: 
 *   T is the supporting transaction of X
 *   minsupport is the minimum support of any itemset
 * output: the occupancy upper bound of itemset X
 * 
 * ��������˵����
 * Y��X��ȫ���еĲ�������Y������X֮ǰ��item
 * ����ȫ��={1,2,3,4,5,6,7}; X={2,3,4}; Y={5,6,7}
 * E��X��ĳһ��transaction�е���չ��������Y��transaction�Ľ��� 
 * ��transaction = {1,2,3,4,5,7}; E={5,7} 
 */

public class Calculate_occupancy {
	
	public static float run(database D1, ArrayList<Integer> X
			, ArrayList<Integer> allitem, int minsupport, supptrans T)
	{
		ArrayList<Integer> Y = new ArrayList<Integer>();
		Y = cal_supplementaryset(allitem, X);    //��X��ȫ���еĲ���
		
		float occu = cal_occu(X,Y,D1,minsupport,T);  //����occupancy
		return occu;
	}
	
	//calculate occupancy
	public static float cal_occu(ArrayList<Integer> X, ArrayList<Integer> Y
			, database D, int minsupport, supptrans T)
	{
		float occu = 0;
		float sum = 0;
		if(T.getsize() >= minsupport)  //supporting transaction exists
		{
			ArrayList<Float> relative_Xuti = getutilityofX(T,X);   //��X��supporting transaction�м���X�����utility
			ArrayList<Float> relative_Euti = getutilityofY(T,Y);   //��X��supporting transaction�м���Y�����utility
			Collections.reverse(relative_Xuti);
			Collections.reverse(relative_Euti);
			if(relative_Euti.size() == 0)        //������ص���չ��Ϊ�գ�����0��relative_Xuti�Ĵ�С��relative_Euti��Сһ��
				for(int i=0; i<relative_Xuti.size(); i++)
					relative_Euti.add(0f);
			
			for(int u=1; u<=minsupport; u++)
				sum += relative_Xuti.get(u-1)+relative_Euti.get(u-1);
			occu = sum/minsupport;
		}
		return occu;
	}
	
	//�󲹼��������Ż�����㷨
	public static ArrayList<Integer> cal_supplementaryset(
			ArrayList<Integer> allitem, ArrayList<Integer> itemset) 
	{
		allitem.removeAll(itemset);  //��ȥ��itemset�к��е�Ԫ��
		
		while(allitem.size()!=0)
		{
			while(itemset.size()!=0)
				if(allitem.get(0) < itemset.get(0))  //allitem��itemset��Ԫ�ض��ǰ���������ģ�ȥ��allitem�б�itemset��һ��Ԫ��С��Ԫ��
				{
					allitem.remove(0);
					if(allitem.size() == 0)
						break;
				}
				else
					break;
			break;
		}
		return allitem;
	}
	
	//�󽻼�
	public static ArrayList<Integer> cal_common(ArrayList<Integer> Y,
			ArrayList<item> T) 
	{
		ArrayList<Integer> common = new ArrayList<Integer>();
		for(item item_i: T)  //��T������item��idȡ��������һ��ArrayList
		{
			common.add(item_i.itemid);
		}
		common.retainAll(Y);  //��common��ȡ������Y�Ĳ���
		return common;
	}
	
	//get the relative utility of X in its supporting transaction
	public static ArrayList<Float> getutilityofX(supptrans T,
			ArrayList<Integer> X) {
		int i=0;
		float Xuti = 0;
		ArrayList<Float> re_uti = new ArrayList<Float>();
		if(X.size() > 0)      //X��Ϊ��
		{
			for(trans trans_i: T.Tx)  //����supporting transaction������transaction
			{
				Xuti = 0;
				for(item item_i: trans_i.t)  //����Y��transaction�Ľ�������X��transaction�е���չ��
				{
					if(item_i.itemid == X.get(i))  //��transaction�а���E��item utility���
					{
						Xuti += item_i.itemuti;
						i++;
						if(i == X.size())
						{
							i=0;
							re_uti.add(Xuti/trans_i.transuti);
							break;
						}
					}
				}
			}
		}
		else       //XΪ�գ���1���������ԴӸ��ڵ�(�ռ�)���¿�ʼ����
		{
			for(int j=0; j<T.getsize(); j++)
				re_uti.add(1f);
		}
		
		return re_uti;
	}
	
	//get the relative utility of Y in the supporting transaction of X
	public static ArrayList<Float> getutilityofY(supptrans T,
			ArrayList<Integer> Y) {
		int i=0;
		float Xuti = 0;
		ArrayList<Float> re_uti = new ArrayList<Float>();
		ArrayList<Integer> E = new ArrayList<Integer>();
		if(Y.size() > 0)
		{
			for(trans trans_i: T.Tx)  //����supporting transaction������transaction
			{
				Xuti = 0;
				E = cal_common(Y, trans_i.t);  //����Y��transaction�Ľ�������X��transaction�е���չ��
				if(E.size() > 0)   //��X��transaction�е���չ����Ϊ��
				{
					for(item item_i: trans_i.t)  //����transaction������item
					{
						if(item_i.itemid == E.get(i))  //��transaction�а���E��item utility���
						{
							Xuti += item_i.itemuti;
							i++;
							if(i == E.size())
							{
								i=0;
								re_uti.add(Xuti/trans_i.transuti);
								break;
							}
						}
					}
				}
				else      //��Ϊ�գ���0
					re_uti.add(0f);
			}
		}
		return re_uti;
	}
	
	public static void main(String[] args)
	{
		database D1 = new database();
		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<Integer> allitem = new ArrayList<Integer>();
		int utimin = 8;
		for(int i=0; i<7; i++)
			allitem.add(i+1);
		ArrayList<Integer> Y = new ArrayList<Integer>();
		for(int k=1;k<=7; k++)
		{
			X.add(k);
			Y = cal_supplementaryset(allitem, X);
			supptrans T = new supptrans(D1,X,utimin);
			for(int i = 0;i < T.getsize(); i ++)
			{
				System.out.println(T.get(i).transuti);
				for(int j=0; j<T.get(i).t.size(); j++)
				{
					System.out.println(T.get(i).t.get(j).itemid+" "+T.get(i).t.get(j).itemuti);
				}
	        }
			int minsupport = 1;
			float occu = cal_occu(X,Y,D1,minsupport,T);
			System.out.println(occu);
			X.remove(0);
		}
	}

}
