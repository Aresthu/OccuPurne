package tree;

import java.util.ArrayList;
import java.util.Collections;

public class Trie {
	private int SIZE;       //用于决定一个node的孩子节点的个数
    private TrieNode root;  //字典树的根
    public static database D;  //数据库
    public static int node_count;  //用于标记搜寻了多少个节点
    public static int utimin;   //决定一个transaction是否为supporting transaction的阈值
  
    public Trie() //初始化字典树
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
    {  //字典树节点
    	public supptrans nodeSupptrans;  //Supporting transaction of node
        public TrieNode[] son;   // 所有的儿子节点
        public ArrayList<Integer> itemset;  //node的itemset

        TrieNode() 
        {
        	nodeSupptrans = new supptrans();
            son = new TrieNode[SIZE];  
            itemset = new ArrayList<Integer>();
        }  
    }  
    
    //在字典树中插入一个itemset
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

        node.itemset = new ArrayList<Integer>(X);  //node的itemset
        node.nodeSupptrans = new supptrans(D,node.itemset,utimin); //get the supporting transaction of node
    } 
    
    //得到X的插入位置
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
	
	//前序遍历字典树
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
	
    //深度优先修剪字典树.  
    public static void prune(TrieNode node, int minsupport, float occumin, 
    		float utimin, float para, float qx_threshold)
    {  
        if(node!=null)
        {  
            //float qx = Calculate_Qx.run(D, node.itemset, allitem, minsupport, para, node.nodeSupptrans);
        	//TO DO NEXT 20150625
        	/*
        	 * 用vertical database 求出supporting transaction
        	 * 新的Explore模拟prune过程，接口：prev post加上已有的，去除node
        	 * 用prev取代node.itemset
        	 * prev用ArrayList<Integer>存储
        	 * 要把utility改成long
        	 * 找出high utility的itemset的绝对utility后除以database的utilitie
        	 */
        	
            float uti = Calculate_utility.run(D, node.itemset, node.nodeSupptrans);//D是数据库 
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
     * 参数说明：
     * uti_supportingthreshold: 当X在transaction中utility不小于阈值uti_supportingthreshold时，便认为该transaction是X的supporting transaction
     * minsupport：任意一个itemset X的supporting transaction的元素个数必须不小于minsupport
     * occumin: itemset X的occupancy阈值
     * utimin: itemset X的utility阈值，X在其supporting transaction中的总utility必须不小于utimin
     * para：没有用到
     * qx_threshold：没有用到
     */
    public static void main(String[] args) {
    	int DEBUG = 1;
    	database D1 = new database();
    	int uti_supportingthreshold = 3;   	//求一个itemset在某一个transaction里边的utility是否大于这个阈值，
											//才能决定该transaction是itemset的supporting transaction
    										
    	
        long trieTime = System.currentTimeMillis();
            	
        Trie tree = new Trie(D1, uti_supportingthreshold);  //初始化一棵树

        tree.root.nodeSupptrans = new supptrans(D1, tree.getRoot().itemset, utimin);  //初始化根节点的supporting transaction
//        //测试部分开始
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
///*      --测试部分结束--       */        
        
        if(1 == DEBUG){
        	System.out.println("Before 字典树构造完毕");
        }
        calculateSet calSet = new calculateSet();
        
        System.out.println(tree.D.allitem);
        
        
        ArrayList<ArrayList<Integer>> allitemset = calSet.allitemset(tree.D.allitem);
        for(int i=0; i<allitemset.size(); i++)
			tree.insert(allitemset.get(i));
        //字典树构造完毕
        if(1 == DEBUG){
        	System.out.println("字典树构造完毕");
        }
        
        trieTime = System.currentTimeMillis() - trieTime;
        System.out.println("Trie Time:" + trieTime +"ms");
        //开始剪枝
        float para = 0.1f;
        int minsupport = 10000;
        float occumin = 0.2f;
        float utimin = 10000f;
        float qx_threshold = 80000f;
        long totaltime = System.currentTimeMillis();
    	prune(tree.getRoot(), minsupport, occumin, utimin, para, qx_threshold);  
    	totaltime = System.currentTimeMillis()-totaltime;
    	System.out.println(node_count);//搜的节点数
    	System.out.println("Total time is:"+ totaltime+"ms");
    } 

}
