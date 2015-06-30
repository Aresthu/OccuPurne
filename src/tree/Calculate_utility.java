package tree;

import java.util.ArrayList;

/*
 * This class is used to claculate the utility of any itemset X;
 * T is the supporting transaction of X;
 */


public class Calculate_utility {
	
	public static float run(database D1, ArrayList<Integer> X, supptrans T)
	{
		float X_uti = 0;
		if(T.getsize() > 0)
			X_uti = cal_uti(X,T);
		return X_uti;
	}
	
	//得到每个itemset在supporting transaction中的绝对utility
	public static float cal_uti(ArrayList<Integer> X, supptrans T)
	{
		int i=0;
		float Xuti = 0;
		if(X.size()>0)          //X is not an empty set
			for(trans trans_i: T.Tx)
			{
				for(item item_i: trans_i.t)
					if(item_i.itemid == X.get(i))   //找到每个transaction中与X对应的item utility并求和
					{
						Xuti += item_i.itemuti;
						i++;
						if(i == X.size())
						{
							i=0;
							break;
						}
					}
			}
		else                  // X is an empty set, and the supporting transaction of X is the database;
			for(trans trans_i: T.Tx)
				Xuti += trans_i.transuti;
		
		return Xuti;	
	}
	
	public static void main(String[] args)
	{
		database D1 = new database();
		ArrayList<Integer> Xitemset = new ArrayList<Integer>();
		//for(int i=0; i<4; i++)
			//Xitemset.add(i+1);
		
		supptrans T = new supptrans(D1,Xitemset);
		float utility = cal_uti(Xitemset,T);
		System.out.println(utility);
	}
}
