package tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/*
 * This class is used to build the database from the document;
 */

public class database
{
	public ArrayList<trans> tList;   //存放transaction
	public ArrayList<Integer> allitem;   //all items of the database

	public int getsize() 
	{
		return tList.size();
	}

	public trans get(int i) 
	{
		return tList.get(i);
	}
	
	//构造函数，只需要修改路径
	public database()
	{
		tList= new ArrayList<trans>();
		allitem = new ArrayList<Integer>();
		String fileName = new String("E:/workplace/OccuExp/Data/Mushroom_Q10.txt");
		
		File file = new File(fileName);
		//File file = new File("F:/wenky/Intern/小米项目/data/Gnew.txt");      
		//File file = new File("E:/2015.05.03-清华实习/小米项目/data/Gnew.txt");
		BufferedReader reader = null;
		String str = null;
		
		try
		{
			reader = new BufferedReader(new FileReader(file));
			while((str = reader.readLine()) != null)
			{
				trans t1 = new trans();
				String[] split1 = str.split(" ");       //开始拆字，具体查看数据集
				for(int j=0;j<split1.length;j++)
				{
					String[] split2 = split1[j].split("\\(");
					if(allitem.contains(Integer.parseInt(split2[0])) == false)
						allitem.add(Integer.parseInt(split2[0]));
					split2[1] = split2[1].replace(")", "");
					item i1 = new item(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));  //构造item
					t1.t.add(i1);   //将item存在transaction t1中
				}
				t1.cal_transuti();  //计算t1的transaction utility
				tList.add(t1);      //将transaction t1添加到database中
			}
			//System.out.println("Dictionary Finished");
			reader.close();
		}
		catch (IOException e) 
		{
            e.printStackTrace();
        }
		Collections.sort(allitem);    //对所有的item做升序排序
	}
	
	public static void main(String[] args) 
	{
		database D = new database();
		int sum = 0;
		System.out.println(D.allitem);
		for(int i = 0;i < D.getsize(); i ++)
		{
			System.out.println(D.get(i).transuti);
			sum+=D.get(i).transuti;
			for(int j=0; j<D.get(i).t.size(); j++)
			{
				System.out.println(D.get(i).t.get(j).itemid+" "+D.get(i).t.get(j).itemuti);
			}
        }
		System.out.println(sum);
	}
	
}