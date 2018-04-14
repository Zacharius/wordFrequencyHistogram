import java.io.*;
import java.util.*;

public class WordHistogram{

    public static final String outFile = "output.txt";
    
    public static void main(String arg[]){

	if(arg.length != 1){
	    System.out.println("Wrong number of arguements");
	    System.out.println("Usage: WordHistogram wordFile");
	    System.exit(-1);
	}

	int maxLen =0;
	String inFile = arg[0];
	int freq;
	Set<Integer> keys;
	HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	TreeMap<Integer, ArrayList<String>> freqTree= new TreeMap<Integer, ArrayList<String>>();

	try{

	    //conect file to byte stream, then reader, and finally tokenizer
	    FileInputStream inFileStream = new FileInputStream(inFile);
	    InputStreamReader inFileReader = new InputStreamReader(inFileStream);
	    StreamTokenizer tokens = new StreamTokenizer(inFileReader);

	    //force all tokens to be lower case
	    tokens.lowerCaseMode(true); 
	    //add period to delimiters
	    tokens.quoteChar('.');

	    //iterate over all tokens until end of file
	    while (tokens.nextToken() != StreamTokenizer.TT_EOF){

		//only deal with words
		if(tokens.ttype == StreamTokenizer.TT_WORD){

		    //if word in hashmap, increase frequency by 1, else add it
		    if(wordMap.containsKey(tokens.sval)){
			freq = (int)wordMap.get(tokens.sval);
			wordMap.put(tokens.sval, ++freq);
		    }
		    else{
			wordMap.put(tokens.sval, 1);
		    }

		    //find the longest word
		    if(tokens.sval.length() > maxLen){
			maxLen = tokens.sval.length();
		    }
		}
			  
	    }
	    inFileReader.close();
	    
	}
	catch(IOException e){
	    e.printStackTrace();
	    System.out.println("IOEXception: Check that provided File Exists");
	    System.exit(-1);
	}

	//invert hashMap and convert to treeMap
	freqTree = hash2Tree(wordMap);

	keys = freqTree.descendingKeySet();

	//print all keys in treeMap
	for(int key : keys){
	    printList(maxLen, key, freqTree.get(key));
	}	
       
	
    }

    //invert hashmap and convert it to treemap
    private static TreeMap<Integer, ArrayList<String>>
	hash2Tree(HashMap<String,Integer> wordMap){

	int hashVal;
	int treeKey;
	ArrayList<String> treeVals;
	TreeMap<Integer, ArrayList<String>> freqTree = new TreeMap();

	//iterate through all keys in hash
	Set<String> keys = wordMap.keySet();
	for( String hashKey : keys){
	    //value of hash is now key to tree
	    treeKey = wordMap.get(hashKey);

	    //if key exists, get arraylist, else make new one
	    if(freqTree.containsKey(treeKey)){
		treeVals = freqTree.get(treeKey);
	    }
	    else{
		treeVals = new ArrayList();
	    }
		
	    //add the old hash key to the new tree value
	    treeVals.add(hashKey);
	    freqTree.put(treeKey, treeVals);
	}

	return freqTree;
    }

    //print a list, the words in the list are formatted according to frequency and maxLength
    private static void printList(int maxLen, int freq, ArrayList<String> list){
	int padding;
	File file;

	try{

	    //create outfile if it doesn't exist
	    file = new File(outFile);
	    if(!file.exists())
		file.createNewFile();
		
	    FileWriter writer = new FileWriter(file, true);
	    PrintWriter out = new PrintWriter(writer);

	    //iterate through words in list
	    for(String word : list){
		padding = maxLen- word.length();

		//print the number of spaces needed for padding
		for(int i=0; i<padding; i++){
		    out.print(" ");
		}

		out.print(word + " | ");

		//print number of '=' according to frequency
		for(int i=0; i<freq; i++){
		    out.print("=");
		}

		out.println(" (" + freq + ")");
	    }
	    out.close();
	}
	catch(IOException e){
	    e.printStackTrace();
	    System.exit(-1);
	}
    }
}

   
