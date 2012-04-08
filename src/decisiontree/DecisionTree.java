package decisiontree;
import java.lang.reflect.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.lang.Math;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import frequency.HibernateUtil;

public class DecisionTree {
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	Field field[];
	Node root=null;
	
	class Node{
		Field f;
		
		Node child[];
		Node parent;
		String relationship;//the relation ship between child and its parent
		int noofchildern;
		Node(Field f1)
		{
			f=f1;
			noofchildern=0;
			//FINDING NO OF CHILDS FOR THIS NEW NODE
			Transaction tx= session.beginTransaction();
			String attribute=f1.getName();
			// FINDING all unique values for each attribute
	        String SQL_QUERY="select DISTINCT "+attribute+" from decisiontree.DecisionValuesPojo";
	        Query query=session.createQuery(SQL_QUERY);
	        //***************************************************
			
			child=new Node[query.list().size()];
		}
		void addChild(Node n)
		{
			this.child[noofchildern]=n;
			noofchildern++;
		}
		
		
	}
	
	void createDecisonTree() throws ClassNotFoundException
	{	
		//HashMap <Field,Boolean> h1=new HashMap<Field,Boolean>(); // Hash map to store whether an attribute has been used as a node in the decision tree or not
		// false means it has not been used ,true means it has been used		
		
		String resultAttribute="";
		//using Java Refelction to get all attribute names
        Class c=Class.forName("decisiontree.DecisionValuesPojo");
        field=c.getDeclaredFields(); //array of fields
        for(int i=1;i<field.length;i++)
        {
        	if(i==field.length-1)
        		resultAttribute=field[i].getName();
        	//else
        		//h1.put(field[i], false);
        	System.out.println(field[i].getName()+"  "+field[i].getType());
        }
        
		double entropyVal=entropy(null,resultAttribute);
		Field fieldt=informationGain(entropyVal,resultAttribute,"select count(*) from decisiontree.DecisionValuesPojo where ",null," ");
		root=new Node(fieldt);
		root.parent=null;
		
		entropy(root,resultAttribute);
		
	}
	
	
	Field informationGain(double entropyVal,String resultAttribute,String makequery,Node node,String relationship) throws ClassNotFoundException
	{
		System.out.println("-----------Information Gain---------------");
		double maxInformationGain=-0.1;
		double informationGain=0.0;
		Field maxField=null;
		String maxInformationGainAttribute="";
		
		//Finding all the column names for my TABLE , these attributes will be used to calculate information gain
		for(int i=1;i<field.length-1;i++) // Attribute
		{
				//THIS CODE IS USED JUST TO FIND OUT THOSE ATTRIBUTES WHICH HAVE ALREADY BEEN USED
				//*******************
				Node nodet=node;
				int flagusedfield=0;
				while(nodet!=null)
				{
					if(nodet.f==field[i]) // if the field has already been used ,we do not want to consider this attribute for information gain
						{
						System.out.println("the field is "+nodet.f.getName());
						flagusedfield=1;
						}
					
					if(nodet.parent!=null)
						nodet=nodet.parent;
					else
						nodet=null;
				}
				
				if(flagusedfield==1)
				{
					continue;
				}
				//*******************
				
				
				Transaction tx= session.beginTransaction();
				String attribute=field[i].getName();
				// FINDING all unique values for each attribute
		        String SQL_QUERY="select DISTINCT "+attribute+" from decisiontree.DecisionValuesPojo";
		        Query query=session.createQuery(SQL_QUERY);
		        //System.out.println(query.list());
		        
		        double entropy[]=new double[query.list().size()];
		        int count[]=new int[query.list().size()];
		        int totcount=0;
		        
		        for(int j=0;j<query.list().size();j++) // distinct values  of Attribute
		        {
		        	String avalue=query.list().get(j).toString();
		        	
		        	SQL_QUERY=makequery+attribute+"=:value and "+resultAttribute+"=:yes";
		        	Query query0=session.createQuery(SQL_QUERY);
		        	query0.setParameter("value",avalue);
		        	query0.setParameter("yes",1);
		        	//System.out.println("SQL_QUERY is "+SQL_QUERY);
		        	
		        	SQL_QUERY=makequery+attribute+"=:value and "+resultAttribute+"=:no";
		        	Query query1=session.createQuery(SQL_QUERY);
		        	query1.setParameter("value",avalue);
		        	query1.setParameter("no",0);
		        	
		        	int yes=Integer.parseInt(query0.list().get(0).toString());
		        	int no=Integer.parseInt(query1.list().get(0).toString());
		        	
		        	double pY=(double)yes/(yes+no);// Probability of yes
		            double pN=(double)no/(yes+no);// Probability of NO
		            if(yes+no==0)
		            {
		            	pY=0.0;
		            	pN=0.0;
		            }
		            entropy[j]=-pY*log2(pY)-pN*log2(pN);
		            
		            count[j]=yes+no;
		        	
		            totcount=totcount+count[j];
		        	
		        	System.out.println("For "+attribute+"="+avalue+" and true "+yes);
		        	System.out.println("For "+attribute+"="+avalue+" and false "+no);
		        } //end of for
		        
		        informationGain=entropyVal;
		        //System.out.println("information gain"+informationGain+"totcount"+totcount);
		        
		        for(int j=0;j<query.list().size();j++)
		        {
		        	//System.out.println(count[j]+"----"+totcount+"---------"+entropy[j]);
		        	informationGain=informationGain-(double)count[j]/(double)totcount*entropy[j];
		        	
		        }
		        
		        System.out.println("Information gain for attribute "+attribute+" is "+informationGain);
		        
		        if(informationGain>maxInformationGain)
				{
					maxInformationGainAttribute=attribute;
					maxInformationGain=informationGain;
					maxField=field[i];
				}
		        

		}//END OF outer FOR different fields
		
		
		if(maxField!=null)
		System.out.println("the maximum information gain is for attribute "+maxInformationGainAttribute+" with a value "+maxInformationGain+"--"+maxField.getName());
		
		if(node !=null && maxField!=null)
		{
			Node newnode=new Node(maxField);
			newnode.parent=node;
			node.addChild(newnode);
			newnode.relationship=relationship;
			System.out.println("Adding node "+newnode.f.getName()+" as child of "+node.f.getName()+".The relationship is "+relationship);
			entropy(newnode, resultAttribute);
		}
		
		return maxField;
	}
		
	
	
	double entropy(Node nodet,String resultAttribute) throws ClassNotFoundException
	{
		double entropy=0;
        int yes;
        int no;// Summation ( p(Xi)*I(Xi))
        System.out.println("-----------Calculating Entropy---------------");
        Node node=nodet;
        
        String makequery="select count(*) from decisiontree.DecisionValuesPojo where " ;
       
        //------------where condition
        
        if(node !=null)
        {
        	//Distinct values of the attribute
        	
        	Transaction tx= session.beginTransaction();
			String attribute=node.f.getName();
			// FINDING all unique values for each attribute
	        String SQL_QUERY="select DISTINCT "+attribute+" from decisiontree.DecisionValuesPojo";
	        Query query=session.createQuery(SQL_QUERY);
	        //System.out.println(query.list());
	        
	        String tmp=makequery;
	        for(int i=0;i<query.list().size();i++)
	        {
	        	makequery=tmp;
	        	makequery=makequery+attribute+"='"+query.list().get(i).toString()+"' and ";
	        	
			        	while (node.parent!=null) // making where condition
			        	{
			        		makequery=makequery+node.parent.f.getName()+"='"+node.relationship+"'";
			        		makequery=makequery+" and ";
			        		node=node.parent;
			        		//System.out.println("makequery is " +makequery);
			        	}
			        	System.out.println("-----------Calculating Entropy---------------");
			        	 double entropyVal=entropyforyesno(makequery, resultAttribute);
			        	 //System.out.println("makequery>>>>"+makequery);
			        	 informationGain(entropyVal,resultAttribute, makequery,nodet,query.list().get(i).toString());
	        }        	
        	
		}
        
        // for first time when it called only then node not equal to null
        //if(node==null)
        return entropyforyesno(makequery, resultAttribute);
        
	}
	
	
	 double entropyforyesno(String makequery,String resultAttribute)
	 {
		 double entropy=0;
	        int yes;
	        int no;// Summation ( p(Xi)*I(Xi))
	        	
		 //Finding the no of tuples that have value true to calculate entropy       
	        Transaction tx= session.beginTransaction();
	        String SQL_QUERY=makequery+resultAttribute+"=:play";
	        System.out.println(SQL_QUERY);
	        Query query=session.createQuery(SQL_QUERY);
	        query.setParameter("play",1);
	        yes=Integer.parseInt(query.list().get(0).toString());
	        System.out.println("play count true = "+yes);
	        
	        //Finding the no of tuples that have value false to calculate entropy
	        SQL_QUERY=makequery+resultAttribute+"=:donotplay";
	        query=session.createQuery(SQL_QUERY);
	        query.setParameter("donotplay",0);
	        System.out.println(SQL_QUERY);
	        no=Integer.parseInt(query.list().get(0).toString());
	        System.out.println("play count false = "+no);
	        
	        
	        //Calculating probabilities for Entropy
	        double pY=(double)yes/(yes+no);
	        double pN=(double)no/(yes+no);
	        
	        entropy=-pY*log2(pY)-pN*log2(pN);
	        System.out.println("The entropy value is "+entropy);
	        
			return entropy;
	 }
	
	
	
	double log2(double num)
	{
		if(num==0)
		{
			return 0.0;
		}
		return Math.log(num)/Math.log(2);
	}
	
	
	
	public static void main(String args[]) throws ClassNotFoundException
	{
		DecisionTree t=new DecisionTree();
		t.createDecisonTree();
	}
	
	   
}
