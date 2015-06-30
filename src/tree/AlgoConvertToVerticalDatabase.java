package tree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class AlgoConvertToVerticalDatabase {
	public long totaltime =0;
	public long totalUtility=0;
	
	public AlgoConvertToVerticalDatabase(){
		
	}
	
	public void run(String input, String vertical, String vertical2, String vertical3) throws IOException {
		totaltime = System.currentTimeMillis();
		
		// READ HORIZONTAL DATABASE
		BufferedReader reader = new BufferedReader(new FileReader(input));
		Map<Integer, ItemStructure> mapStructures = new HashMap<Integer, ItemStructure>();
		Map<Integer, Integer>  mapTidTU = new HashMap<Integer, Integer>();
		
		String line;
		int tid=0;
		while( ((line = reader.readLine())!= null)){
			String[] lineSplited = line.split(":");
			
			int transactionUtility = Integer.parseInt(lineSplited[1]);  			
			mapTidTU.put(tid, transactionUtility);
			
			String[] transactionItems = lineSplited[0].split(" ");
			String[] transactionItemsUtility = lineSplited[2].split(" ");
			
			for(int i=0; i< transactionItems.length; i++){
				int itemValue = Integer.parseInt(transactionItems[i]);
				
				// Add tid to tidset of item
				ItemStructure structure = mapStructures.get(itemValue);
				if(structure == null){
					structure = new ItemStructure();
					structure.item = itemValue;
					mapStructures.put(itemValue, structure);
				}
				structure.tidset.add(tid);
				structure.utilitiesForEachTid.add(Integer.parseInt(transactionItemsUtility[i]));
				
				totalUtility += Integer.parseInt(transactionItemsUtility[i]);
			}
			tid++;
		}
		reader.close();
		
		// sort list of items in lexical order
		List<Integer> listItems = new ArrayList<Integer>(mapStructures.keySet());
		Collections.sort(listItems);

		// WRITE VERTICAL DATABASE FILE 1
		int maxItem =0;
		BufferedWriter writer = new BufferedWriter(new FileWriter(vertical)); 
		for(Integer item : listItems){
			if(item > maxItem){
				maxItem = item;
			}
			ItemStructure structure = mapStructures.get(item);
			StringBuffer buffer = new StringBuffer();
			buffer.append(structure.item);
			buffer.append(":");
			// (1) SAVE TIDS
			Iterator<Integer> iterTIDS = structure.tidset.iterator();
			while(iterTIDS.hasNext()){
				buffer.append(iterTIDS.next());
				if(iterTIDS.hasNext()){
					buffer.append(' ');
				}else{
					break;
				}
			}
			buffer.append(":");
			
			// (2) SAVE UTILITY FOR EACH TID
			Iterator<Integer> iterTIDUtility = structure.utilitiesForEachTid.iterator();
			while(iterTIDUtility.hasNext()){
				buffer.append(iterTIDUtility.next());
				if(iterTIDUtility.hasNext()){
					buffer.append(' ');
				}else{
					break;
				}
			}
			writer.write(buffer.toString());
			writer.newLine();
		}
		
		writer.flush();
		writer.close();
		
		// WRITE VERTICAL DATABASE FILE 2
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(vertical2)); 
		
		Iterator<Entry<Integer, Integer>> iterEntries = mapTidTU.entrySet().iterator();
		while(iterEntries.hasNext()){
			Entry<Integer, Integer> entry = iterEntries.next();
			if(iterEntries.hasNext()){
				writer2.write(entry.getKey() + ":" + entry.getValue());
				writer2.newLine();
			}else{
				writer2.write(entry.getKey() + ":" + entry.getValue());
				break;
			}
		}
		writer2.close();
		
		// WRITE VERTICAL DATABASE FILE 3
		BufferedWriter writer3 = new BufferedWriter(new FileWriter(vertical3)); 
		// write the number of tids
		writer3.write("" +tid);
		writer3.newLine();
		
		// write the max item
		writer3.write("" +maxItem);
		writer3.newLine();
		
		// write the number of different items
		writer3.write("" + listItems.size());
		writer3.newLine();
		
		writer3.close();
		
		totaltime = System.currentTimeMillis() - totaltime;
		System.out.println("Total utility " + totalUtility);
	}

	// INNER CLASS
	class ItemStructure {
		public int item = 0;
		public List<Integer> tidset = new ArrayList<Integer>();
		public List<Integer> utilitiesForEachTid = new ArrayList<Integer>();
		
		public boolean equals(Object obj) {
			if(obj == this){
				return true;
			}
			ItemStructure itemS2 = (ItemStructure) obj;
			if(itemS2.item == item){
				return true;
			}
			return super.equals(obj);
		}
		
		public int hashCode() {
			String hash = "" + item; 
			return hash.hashCode();
		}
	}
	public static void main(String[] args) {
		/*AlgoConvertToVerticalDatabase converter = new AlgoConvertToVerticalDatabase();
		converter.run(dataset + ".txt", vertical, vertical2, vertical3);*/
	}
}