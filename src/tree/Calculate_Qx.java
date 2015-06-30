package tree;

import java.util.ArrayList;

public class Calculate_Qx {
	public static float run(database D1, ArrayList<Integer> X,
			ArrayList<Integer> allitem, int freqmin, float para, supptrans T) 
	{
		float occu = Calculate_occupancy.run(D1, X, allitem, freqmin,T);
		float utility = Calculate_utility.run(D1, X, T);
		return occu+para*utility;
	}
	
	public static void main(String args[])
	{
		database D1 = new database();
		float para = 0.1f;
		int freqmin = 2;
        ArrayList<Integer> X = new ArrayList<Integer>();
        //X.add(1);
        //X.add(2);
        X.add(7);
        //X.add(4);
        ArrayList<Integer> allitem = new ArrayList<Integer>();
        for(int i=0; i<7; i++)
        	allitem.add(i+1);
		//float qx = Calculate_Qx.run(D1, X, allitem, freqmin, para);
		//System.out.println(qx);
	}
}
