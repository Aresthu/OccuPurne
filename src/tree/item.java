package tree;

/*
 * 表示每一个item
 */

public class item
{
	public int itemid;      //item的id
	public float itemuti;   //item的utility
	
	item()
	{
		itemid = 0;
		itemuti = 0;
	}
	
	item(int num1, float num2)
	{
		itemid = num1;
		itemuti = num2;
	}
}
