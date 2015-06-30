package tree;

import java.util.ArrayList;
import java.util.Collections;

public class Trie {
	private int SIZE;       //���ھ���һ��node�ĺ��ӽڵ�ĸ���
    private TrieNode root;  //�ֵ����ĸ�
    public static database D;  //���ݿ�
    public static int node_count;  //���ڱ����Ѱ�˶��ٸ��ڵ�
    public static int utimin;   //����һ��transaction�Ƿ�Ϊsupporting transaction����ֵ
  
    public Trie() //��ʼ���ֵ���
    {  
        root = new TrieNode();  
    }
        
    public Trie(database D1, int uti_threshold)
    {
    	D = D1;
    	node_count = 0;
    	SIZE = D.allitem.size();
    	utimin = uti_threshold;
    	root = new TrieNode();
    }
    
    class TrieNode 
    {  //�ֵ����ڵ�
    	public supptrans nodeSupptrans;  //Supporting transaction of node
        public TrieNode[] son;   // ���еĶ��ӽڵ�
        public ArrayList<Integer> itemset;  //node��itemset

        TrieNode() 
        {
        	nodeSupptrans = new supptrans();
            son = new TrieNode[SIZE];  
            itemset = new ArrayList<Integer>();
        }  
    }  
    
    //���ֵ����в���һ��itemset
    public void insert(ArrayList<Integer> X) 
    {  
        if (X == null || X.size() == 0) {  
            return;
        }  
        TrieNode node = root;
        Collections.sort(X);
        for (int i = 0; i < X.size(); i++) {  
        	
            int pos = getposition(X.get(i));  
            if (node.son[pos] == null) {  
                node.son[pos] = new TrieNode();  
            }   
            node = node.son[pos];  
        }

        node.itemset = new ArrayList<Integer>(X);  //node��itemset
        node.nodeSupptrans = new supptrans(D,node.itemset,utimin); //get the supporting transaction of node
    } 
    
    //�õ�X�Ĳ���λ��
	public static int getposition(Integer x) 
	{
		int i=0;
		for (; i < D.allitem.size(); i++) 
			if(D.allitem.get(i).equals(x))
				break;
		return i;
	}

	public TrieNode getRoot(){  
        return this.root;  
    }   
	
	//ǰ������ֵ���
	public void preTraverse(TrieNode node){  
        if(node!=null){  
            System.out.println(node.itemset); 
            for(int i = 0;i < node.nodeSupptrans.getsize(); i ++)
    		{
            	System.out.println("The transaction utility: " + node.nodeSupptrans.get(i).transuti);
//    			for(int j=0; j<node.nodeSupptrans.get(i).t.size(); j++)
//    			{
//    				System.out.println(node.nodeSupptrans.get(i).t.get(j).itemid+" "+node.nodeSupptrans.get(i).t.get(j).itemuti);
//    			}
    		}
            for(TrieNode child: node.son){  
                preTraverse(child);  
            }  
        }  
          
    }
	
    //��������޼��ֵ���.  
    public static void prune(TrieNode node, int minsupport, float occumin, 
    		float utimin, float para, float qx_threshold)
    {  
        if(node!=null)
        {  
            //float qx = Calculate_Qx.run(D, node.itemset, allitem, minsupport, para, node.nodeSupptrans);
        	//TO DO NEXT 20150625
        	/*
        	 * ��vertical database ���supporting transaction
        	 * �µ�Exploreģ��prune���̣��ӿڣ�prev post�������еģ�ȥ��node
        	 * ��prevȡ��node.itemset
        	 * prev��ArrayList<Integer>�洢
        	 * Ҫ��utility�ĳ�long
        	 * �ҳ�high utility��itemset�ľ���utility�����database��utilitie
        	 */
        	
            float uti = Calculate_utility.run(D, node.itemset, node.nodeSupptrans);//D�����ݿ� 
            float occu = Calculate_occupancy.run(D, node.itemset, D.allitem, minsupport, node.nodeSupptrans);
            //if(qx >= qx_threshold && uti>=utimin && occu>=occumin)
            if(uti>=utimin && occu>=occumin)
            {
            	node_count++;
            	for(TrieNode child: node.son)
            	{  
            		if(child != null)
            		{
            			prune(child, minsupport, occumin, utimin, para, qx_threshold); 
            		}
            	}
            }
        }  
          
    }
    
    /*
     * ����˵����
     * uti_supportingthreshold: ��X��transaction��utility��С����ֵuti_supportingthresholdʱ������Ϊ��transaction��X��supporting transaction
     * minsupport������һ��itemset X��supporting transaction��Ԫ�ظ������벻С��minsupport
     * occumin: itemset X��occupancy��ֵ
     * utimin: itemset X��utility��ֵ��X����supporting transaction�е���utility���벻С��utimin
     * para��û���õ�
     * qx_threshold��û���õ�
     */
    public static void main(String[] args) {
    	int DEBUG = 1;
    	database D1 = new database();
    	int uti_supportingthreshold = 3;   	//��һ��itemset��ĳһ��transaction��ߵ�utility�Ƿ���������ֵ��
											//���ܾ�����transaction��itemset��supporting transaction
    										
    	
        long trieTime = System.currentTimeMillis();
            	
        Trie tree = new Trie(D1, uti_supportingthreshold);  //��ʼ��һ����

        tree.root.nodeSupptrans = new supptrans(D1, tree.getRoot().itemset, utimin);  //��ʼ�����ڵ��supporting transaction
//        //���Բ��ֿ�ʼ
//        ArrayList<Integer> X1 = new ArrayList<Integer>();
//        ArrayList<Integer> X2 = new ArrayList<Integer>();
//        ArrayList<Integer> X3 = new ArrayList<Integer>();
//        ArrayList<Integer> X4 = new ArrayList<Integer>();
//        ArrayList<Integer> X5 = new ArrayList<Integer>();
//        ArrayList<Integer> X6 = new ArrayList<Integer>();
//        ArrayList<Integer> X7 = new ArrayList<Integer>();
//        X1.add(5);
//        X2.add(3);
//        X4.add(2);
//        X3.add(5);
//        X3.add(3);
//        X5.add(2);
//        X5.add(3);
//        X6.add(5);
//        X6.add(2);
//        X7.add(5);
//        X7.add(3);
//        X7.add(2);
//        tree.insert(X1);
//        tree.insert(X2);
//        tree.insert(X3);
//        tree.insert(X4);
//        tree.insert(X5);
//        tree.insert(X6);
//        tree.insert(X7);
//        tree.preTraverse(tree.getRoot());
///*      --���Բ��ֽ���--       */        
        
        if(1 == DEBUG){
        	System.out.println("Before �ֵ����������");
        }
        calculateSet calSet = new calculateSet();
        
        System.out.println(tree.D.allitem);
        
        
        ArrayList<ArrayList<Integer>> allitemset = calSet.allitemset(tree.D.allitem);
        for(int i=0; i<allitemset.size(); i++)
			tree.insert(allitemset.get(i));
        //�ֵ����������
        if(1 == DEBUG){
        	System.out.println("�ֵ����������");
        }
        
        trieTime = System.currentTimeMillis() - trieTime;
        System.out.println("Trie Time:" + trieTime +"ms");
        //��ʼ��֦
        float para = 0.1f;
        int minsupport = 10000;
        float occumin = 0.2f;
        float utimin = 10000f;
        float qx_threshold = 80000f;
        long totaltime = System.currentTimeMillis();
    	prune(tree.getRoot(), minsupport, occumin, utimin, para, qx_threshold);  
    	totaltime = System.currentTimeMillis()-totaltime;
    	System.out.println(node_count);//�ѵĽڵ���
    	System.out.println("Total time is:"+ totaltime+"ms");
    } 

}
