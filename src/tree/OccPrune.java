package tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OccPrune {
<<<<<<< HEAD
	//Add by MacBook 20150630
=======
	
	//added by PC
	
>>>>>>> refs/remotes/MacBook/master
	public int tidCount = 0; //tidCount the amount of the Transaction 
	public int maxItem = 0;
	public int itemCount = 0;

	Map<Integer, Set<Integer>> database = null; // the vertical database in
												// memory

	BufferedWriter writer = null;

	private int minUtility;
	private float minOccupancy;
	private boolean descendingOrderOfSupport;

	// statistics
	public int closedCount = 0;
	public long totaltime = 0;
	public double maxMemory = 0;
	
	public void runAlgorithm(int minUtility,float minOccu, String filePathInput1,
			String filePathInput2, String filePathInput3, String output1,
			final boolean descendingOrderOfSupport) throws IOException {
		this.minUtility = minUtility;
		this.minOccupancy = minOccu;
		this.descendingOrderOfSupport = descendingOrderOfSupport;
		totaltime = System.currentTimeMillis();
		maxMemory = 0;

		database = new HashMap<Integer, Set<Integer>>();
		// ------------------------------------------------
		// (A) FIRST WE READ THE INFORMATION OF FILE 3
		
		BufferedReader readerFile3 = new BufferedReader(new FileReader(
				filePathInput3));
		String line3 = readerFile3.readLine();
		tidCount = Integer.parseInt(line3); // number of transaction in database
		line3 = readerFile3.readLine();
		maxItem = Integer.parseInt(line3); // the maximum item id in the
											// database
		line3 = readerFile3.readLine();
		itemCount = Integer.parseInt(line3); // the number of different items in
												// the database

		// ---------------------------------------------------
		// (B) WE SCAN FILE 2 CONTAINING THE TID OF EACH TRANSACTION AND ITS
		// TRANSACTION UTILITY.
		// table: key= tid value = transaction utility
		int[] tableTU = new int[tidCount];
		BufferedReader readerFile2 = new BufferedReader(new FileReader(
				filePathInput2));
		String line;
		while (((line = readerFile2.readLine()) != null)) {
			String[] lineSplited = line.split(":");
			tableTU[Integer.parseInt(lineSplited[0])] = Integer
					.parseInt(lineSplited[1]);
		}

		// items appearing in Level1
		Set<Integer> allItems = new HashSet<Integer>(itemCount); // ////// <----
		Set<Integer> promisingItems = new HashSet<Integer>(itemCount); // //////
																		// <----

		int[] tableMin = new int[maxItem + 1];
		int[] tableMax = new int[maxItem + 1];

		// (C) WE SCAN THE VERTICAL DATABASE (FILE 1) to calculate the exact
		// utility of each item
		// and create each candidate of size 1 (with its tidset).
		String line2;
		BufferedReader readerFile1 = new BufferedReader(new FileReader(
				filePathInput1));
		while (((line2 = readerFile1.readLine()) != null)) {
			String[] lineSplited = line2.split(":"); // item: tids: list of
														// utilities
			int item = Integer.parseInt(lineSplited[0]);

			allItems.add(item); // ////// <----

			String[] tidsList = lineSplited[1].split(" ");
			String[] itemTidsUtilities = lineSplited[2].split(" ");
			int itemExactUtility = 0;
			int max = 0;
			int min = Integer.MAX_VALUE;
			for (String utilityString : itemTidsUtilities) {
				int utility = Integer.parseInt(utilityString);
				itemExactUtility += utility;
				if (utility > max) {
					max = utility;
				}
				if (utility < min) {
					min = utility;
				}
			}
			tableMax[item] = max;
			tableMin[item] = min;

			// calculate estimated utility
			int estimatedUtility = 0;
			for (String tidString : tidsList) {
				int tid = Integer.parseInt(tidString);
				estimatedUtility += tableTU[tid];
			}

			// if ESTIMATED utility is enough, note that this item is promising
			if (estimatedUtility >= minUtility) {
				Set<Integer> tidset = new HashSet<Integer>();
				for (String tid : tidsList) {
					tidset.add(Integer.parseInt(tid));
				}
				database.put(item, tidset);
				promisingItems.add(item);
			}
		}
		readerFile1.close();

		// (D) We recalculate TWU with only items of size 1 that have minimum
		// estimated utility
		// For each items that has disapeared from the level K2 but was in level
		// K1,
		// we remove its utility from the TWU of each transaction.
		recalculateTU(allItems, promisingItems, filePathInput1, tableTU);

		// --------------------------
		System.out.println("Running the DCI-Closed algorithm");

		writer = new BufferedWriter(new FileWriter(output1));

		// (E) INITIALIZE VARIABLES FOR THE FIRST CALL TO THE "DCI_CLOSED"
		// PROCEDURE
		List<Integer> closedset = new ArrayList<Integer>();
		Set<Integer> closedsetTIDs = new HashSet<Integer>();
		List<Integer> preset = new ArrayList<Integer>();

		// create postset and sort it by descending order or support.
		List<Integer> postset = new ArrayList<Integer>(promisingItems.size());
		for (Integer item : promisingItems) {
			postset.add(item);
		}

		// sort items by support ascending order. // TODO: ORDER BY UTILITY?
		Collections.sort(postset, new Comparator<Integer>() {
			public int compare(Integer item1, Integer item2) {
				int size1 = database.get(item1).size();
				int size2 = database.get(item2).size();
				if (size1 == size2) {
					return (item1 < item2) ? -1 : 1; // use lexicographical
														// order if support is
														// the same
				}

				if (descendingOrderOfSupport) {
					return size2 - size1;
				} else {
					return size1 - size2;
				}
			}
		});

		// (3) CALL THE "DCI_CLOSED" RECURSIVE PROCEDURE
		dci_closed(true, closedset, closedsetTIDs, postset, preset, tableTU,
				tableMin, tableMax, 0);

		printStatistics();
		// close the file
		writer.close();
	}

}
