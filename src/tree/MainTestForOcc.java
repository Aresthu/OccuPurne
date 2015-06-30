package tree;


import AlgoDCI_Closed_Util;

import java.io.File;
import java.io.IOException;

public class MainTestForOcc {
	static String dataset = "E:\\workplace\\OccuExp\\Data\\Ginput";  
	static String vertical = dataset + "_vertical.txt";
	static String vertical2 = dataset + "_.txt";
	static String vertical3 = dataset + "_vertical3.txt";
	static String output1 = "C:\\Datasets\\phase1_output.txt";
	static String output2 = "C:\\Datasets\\";

	static boolean descendingOrderOfSupport= true;
	
	// fpv06_tBMV1_Q10   // fpv6_Chess_Q10  // fpv6_T10I6D100K_Q5 //up_Mushroom_Q5
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		File out1 = new File(output1);
		out1.delete();
		for(int i=1; i<100; i++){
			File out2 = new File(output2 + "L" + i + ".txt");
			if(out2.exists() == false){
				break;
			}
			out2.delete();
		}
		
		// Convert to vertical database

		File verticalFile = new File(vertical);
		if(verticalFile.exists()){
			System.out.println("-------------------------");
			System.out.println("VERTICAL DATABASE ALREADY EXIST.... SO WE DON'T NEED TO CONVERT IT AGAIN.");
			System.out.println("-------------------------");
		}else{
			AlgoConvertToVerticalDatabase converter = new AlgoConvertToVerticalDatabase();
			converter.run(dataset + ".txt", vertical, vertical2, vertical3);

			System.out.println("FINISHED CONVERTING DATABASE TO VERTICAL FORMAT");
			System.out.println("Time conversion: " + converter.totaltime/1000 + "s   (" +  converter.totaltime + " ms)");
			System.out.println("-------------------------");
		}
		
		
		//	PHASE 1
		OccPrune phase1 = new OccPrune();
		phase1.runAlgorithm(minUtility, vertical, vertical2, vertical3, output1, descendingOrderOfSupport);
		
		System.out.println("FINISHED PHASE 1");
		System.out.println("Time phase1: " + phase1.totaltime/1000 + "s   (" +  phase1.totaltime + " ms)");
		System.out.println("Closed candidates : " + phase1.closedCount); 
		System.out.println("Max memory : " + phase1.maxMemory);
		System.out.println("-------------------------");
	}

}
