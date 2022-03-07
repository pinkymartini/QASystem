import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
	
	public static Scanner questionScanner;
	public static Scanner scriptScanner; 
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		File questions = new File("questions.txt");
		File scriptFile = new File("the_truman_show_script.txt");
		String script="";
		Stemmer stemmer = new Stemmer();
		
		ArrayList<String>Questions = new ArrayList<String>();
		
		try {
			 questionScanner = new Scanner(questions);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		try {
			 scriptScanner = new Scanner(scriptFile);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while ( questionScanner.hasNextLine())
		{
			String line = questionScanner.nextLine();
			Questions.add(line);
		}
		
		while ( scriptScanner.hasNextLine())
		{
			 script = scriptScanner.nextLine();
		}
		
		//seperate script by sentence by sentence. punctuations are in.
		String scriptSentencesWithPunct [] = script.split("(?<=[a-z])\\.\\s+");
		ArrayList<String> scriptSentencesList = new ArrayList( Arrays.asList( scriptSentencesWithPunct )); 
		
		//fulltextarraylist, keeps uppercase sentences, in which the words are seperated by commas. (a list where each item is a list (sentence))
		ArrayList<ArrayList<String>> FULLTEXTARRAYLIST = new ArrayList<ArrayList<String>>();
		ArrayList<String>Finalization = new ArrayList<String>();
		ArrayList<ArrayList<String>> unstemmedText = new ArrayList<ArrayList<String>>();
		List<String> newList = null;
		Set<String> stopwords = new HashSet<String>();
		stopwords.add("THE");
		stopwords.add("WHO");
		stopwords.add("WAS");
		stopwords.add("TO");
		stopwords.add("AT");
		stopwords.add("IS");
		stopwords.add("OF");
		stopwords.add("HOW");
		stopwords.add("MANY");
		stopwords.add("WHAT");
		stopwords.add("HIS");
		stopwords.add("WHERE");
		stopwords.add("A");
		stopwords.add("DOES");
		stopwords.add("COLOR");
		stopwords.add("MUCH");
		stopwords.add("SIZE");
		stopwords.add("WHY");
		stopwords.add("TIME");
		stopwords.add("BY");
		stopwords.add("WHEN");
		
		//remove stopwords from script
		
		for (int i = 0 ; i< scriptSentencesList.size(); i++)
		{
			String tempArray [] =scriptSentencesList.get(i).toUpperCase().replaceAll("\\p{Punct}", "").split(" ");
			ArrayList<String> tempList = new ArrayList( Arrays.asList( tempArray )); 
			
			for(String searchedword: tempArray)
			{
				String comparedWord = searchedword.toUpperCase();
				if(stopwords.contains(comparedWord))
				{
					tempList.remove(comparedWord);
					
				}
			}
			unstemmedText.add(tempList);	
			
			for(int j=0; j<tempList.size(); j++)
			{
				char [] chars = new char[tempList.get(j).length()];
				
				for(int k=0; k<tempList.get(j).length();k++)
				{	
					chars[k]= tempList.get(j).toLowerCase().charAt(k);
					
				}
				stemmer.add(chars, tempList.get(j).length());
				stemmer.stem();
				Finalization.add(stemmer.toString());
				newList = new ArrayList<String>(Finalization);
				
			}
			FULLTEXTARRAYLIST.add((ArrayList<String>) newList);
			Finalization.clear();

		}
		
		ArrayList<String> FinalizedQuestionList= new ArrayList<String>(); //will be replacing end after we stem that
		ArrayList<String>StopWordsRemovedQuestionList = new ArrayList<String>();
			
		
		//main part involving questions?? 
		for(int it = 0; it<Questions.size(); it++)
		{	
			System.out.println(Questions.get(it));
			
			String question = Questions.get(it).replaceAll("\\p{Punct}", "");
			
			String [] questionArray= question.toUpperCase().split(" ");
			
			//remove stopwords from question list.
			for(String searchedword: questionArray)//questionList
			{
				String comparedWord = searchedword.toUpperCase();
				if(!stopwords.contains(comparedWord))
				{
					StopWordsRemovedQuestionList.add(comparedWord);
				}
				
			}
			
			//stem the questions
			
			for(int i=0; i<StopWordsRemovedQuestionList.size(); i++)
			{
				char [] chars = new char[StopWordsRemovedQuestionList.get(i).length()];//string of first
					
				for(int k=0; k<StopWordsRemovedQuestionList.get(i).length();k++)
				{	
					
					chars[k]= StopWordsRemovedQuestionList.get(i).toLowerCase().charAt(k);
								
				}
				stemmer.add(chars, StopWordsRemovedQuestionList.get(i).length());
				stemmer.stem();
				
				FinalizedQuestionList.add(stemmer.toString());
			}
			
			for(int i = 0; i<FULLTEXTARRAYLIST.size(); i++)
			{
					
				if(FULLTEXTARRAYLIST.get(i).containsAll(FinalizedQuestionList))
				{
					FULLTEXTARRAYLIST.get(i).removeAll(FinalizedQuestionList);

					String ans = FULLTEXTARRAYLIST.get(i).get(0);
					
					boolean found = false;
					//check for no worry answer.
					for(int qq= 0; qq<unstemmedText.get(i).size();qq++)
					{
						if(unstemmedText.get(i).get(qq).toLowerCase().contains(ans))
						{
							System.err.println(unstemmedText.get(i).get(qq).toLowerCase());
							found=true;
							break;
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(found ==false)
					{
						System.err.println(FULLTEXTARRAYLIST.get(i).get(0).toLowerCase());
					}
					
					break;
				}
				else {
					continue;
				}
					
			}
						
			FinalizedQuestionList.clear();
			StopWordsRemovedQuestionList.clear();
			
		}
		
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		System.out.println(elapsedTime + " milliseconds");
		
	}

}
