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
 * 其他参数说明：
 * Y是X在全集中的补集，且Y不包括X之前的item
 * 例如全集={1,2,3,4,5,6,7}; X={2,3,4}; Y={5,6,7}
 * E是X在某一个transaction中的扩展集，等于Y和transaction的交集 
 * 若transaction = {1,2,3,4,5,7}; E={5,7} 
 */

public class Calculate_occupancy {
	
	public static float run(database D1, ArrayList<Integer> X
			, ArrayList<Integer> allitem, int minsupport, supptrans T)
	{
		ArrayList<Integer> Y = new ArrayList<Integer>();
		Y = cal_supplementaryset(allitem, X);    //求X在全集中的补集
		
		float occu = cal_occu(X,Y,D1,minsupport,T);  //计算occupancy
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
			ArrayList<Float> relative_Xuti = getutilityofX(T,X);   //在X的supporting transaction中计算X的相对utility
			ArrayList<Float> relative_Euti = getutilityofY(T,Y);   //在X的supporting transaction中计算Y的相对utility
			Collections.reverse(relative_Xuti);
			Collections.reverse(relative_Euti);
			if(relative_Euti.size() == 0)        //如果返回的扩展集为空，则补上0，relative_Xuti的大小和relative_Euti大小一致
				for(int i=0; i<relative_Xuti.size(); i++)
					relative_Euti.add(0f);
			
			for(int u=1; u<=minsupport; u++)
				sum += relative_Xuti.get(u-1)+relative_Euti.get(u-1);
			occu = sum/minsupport;
		}
		return occu;
	}
	
	//求补集，可以优化这个算法
	public static ArrayList<Integer> cal_supplementaryset(
			ArrayList<Integer> allitem, ArrayList<Integer> itemset) 
	{
		allitem.removeAll(itemset);  //先去除itemset中含有的元素
		
		while(allitem.size()!=0)
		{
			while(itemset.size()!=0)
				if(allitem.get(0) < itemset.get(0))  //allitem和itemset的元素都是按升序排序的，去除allitem中比itemset第一个元素小的元素
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
	
	//求交集
	public static ArrayList<Integer> cal_common(ArrayList<Integer> Y,
			ArrayList<item> T) 
	{
		ArrayList<Integer> common = new ArrayList<Integer>();
		for(item item_i: T)  //将T中所有item的id取出，构成一个ArrayList
		{
			common.add(item_i.itemid);
		}
		common.retainAll(Y);  //在common中取出包含Y的部分
		return common;
	}
	
	//get the relative utility of X in its supporting transaction
	public static ArrayList<Float> getutilityofX(supptrans T,
			ArrayList<Integer> X) {
		int i=0;
		float Xuti = 0;
		ArrayList<Float> re_uti = new ArrayList<Float>();
		if(X.size() > 0)      //X不为空
		{
			for(trans trans_i: T.Tx)  //遍历supporting transaction的所有transaction
			{
				Xuti = 0;
				for(item item_i: trans_i.t)  //计算Y与transaction的交集，即X在transaction中的扩展集
				{
					if(item_i.itemid == X.get(i))  //将transaction中包含E的item utility求和
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
		else       //X为空，则补1，这样可以从根节点(空集)往下开始搜索
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
			for(trans trans_i: T.Tx)  //遍历supporting transaction的所有transaction
			{
				Xuti = 0;
				E = cal_common(Y, trans_i.t);  //计算Y与transaction的交集，即X在transaction中的扩展集
				if(E.size() > 0)   //若X在transaction中的扩展集不为空
				{
					for(item item_i: trans_i.t)  //遍历transaction的所有item
					{
						if(item_i.itemid == E.get(i))  //将transaction中包含E的item utility求和
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
				else      //若为空，则补0
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
