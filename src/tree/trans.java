package tree;

import java.util.ArrayList;

/*
 * 表示没一个transaction
 */

public class trans
{
	public ArrayList<item> t;  //存放item
	public float transuti;     //transaction utility 
	
	trans()
	{
		t = new ArrayList<item>();
		transuti=0;
	}
	
	trans(ArrayList<Integer> itemidset)
	{
		t = new ArrayList<item>();
		transuti=0;
		for(int c1: itemidset)
		{
			item i_temp = new item(c1,0);
			t.add(i_temp);
		}
	}
	
	//计算transaction的utility
	public void cal_transuti()
	{
		for(int i=0; i<t.size(); i++)
			transuti += t.get(i).itemuti;
	}
}
