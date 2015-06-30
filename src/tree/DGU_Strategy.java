package tree;

import java.util.ArrayList;

/*
 * 实际上没有采用这个DGU策略
 */

public class DGU_Strategy 
{
	public database D;
	
	public DGU_Strategy(database D1, int removeitem, int uti_th)
	{
		D = D1;
		DGU(removeitem, uti_th);
	}
	
	public void DGU(int removeitem, int utimin)
	{
		if(getTWU(removeitem)<utimin)
			remove(removeitem);
	}
	
	public int getTWU(int item)
	{
		int sum = 0;
		ArrayList<Integer> removeitem = new ArrayList<Integer>();
		removeitem.add(item);
		supptrans suppt = new supptrans(D, removeitem);
		for(int i=0; i<suppt.getsize(); i++)
			sum+=suppt.get(i).transuti;
		System.out.println(sum);
		return sum;
	}
	
	/*
	public static int getTWU(database D1, String str)
	{
		trans t_temp = new trans(str);
		int sum = 0;
		supptrans suppt = new supptrans(D1,t_temp);
		for(int i=0; i<suppt.getsize(); i++)
			sum+=suppt.get(i).transuti;
		//System.out.println(sum);
		return sum;
	}*/
	
	public void remove(int removeitem) 
	{
		for(int i = 0;i < D.getsize(); i ++)
		{
			for(int j=0; j<D.get(i).t.size(); j++)
			{
				if(D.get(i).t.get(j).itemid == removeitem)
				{
					D.get(i).transuti -= D.get(i).t.get(j).itemuti;
					D.get(i).t.remove(j);
					break;
				}
			}
        }
	}
	
	public static void main(String[] args) 
	{
		database D1 = new database();
		
		for(int i = 0;i < D1.getsize(); i ++)
		{
			System.out.println(D1.get(i).transuti);
			for(int j=0; j<D1.get(i).t.size(); j++)
			{
				System.out.println(D1.get(i).t.get(j).itemid+" "+D1.get(i).t.get(j).itemuti);
			}
        }
		
		//int TWU = getTWU(D1,"ABCDE");
		//System.out.println(TWU);
		
		DGU_Strategy Dx = new DGU_Strategy(D1, 1, 102);
		
		for(int i = 0;i < Dx.D.getsize(); i ++)
		{
			System.out.println(Dx.D.get(i).transuti);
			for(int j=0; j<Dx.D.get(i).t.size(); j++)
			{
				System.out.println(Dx.D.get(i).t.get(j).itemid+" "+Dx.D.get(i).t.get(j).itemuti);
			}
        }
        
        
	}
}
