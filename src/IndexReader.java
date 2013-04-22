import java.util.Enumeration;
import java.util.Map;


public class IndexReader {
	/**
	* Returns the name of a node
	* Returns null if the node does not exist in the network
	*/
	public String getName(int nodeId) {
		// TODO: write this function
		return "";
		System.out.println("this is a test");
	}
	
	/**
	* Returns the privacy status of a node, i.e., true if the node
	* has privacy status public
	* Returns false if the node does not exist in the network (or if
	* its tweets are private)
	*/
	public boolean isPublic(int nodeId) {
		// TODO: write this function
		return false;
	}
	
	/**
	* Returns the ids of friends of a node
	* Given nodeId i, return all j such that (i,j) is in the network,
	* sorted by ascending order of j
	* Returns null if the node does not exist in the network
	*/
	public Enumeration<Integer> getFollowing(int nodeId) {
		// TODO: write this function
		return null;
	}
	
	/**
	* Returns the ids of nodes who consider the input node their friend
	* Given nodeId i, return all j such that (j,i) is in the network,
	* sorted by ascending order of j
	* Returns null if the node does not exist in the network
	*/
	public Enumeration<Integer> getFollowers(int nodeId) {
		// TODO: write this function
		return null;
	}
	
	/**
	* Returns the tweets of a given node, sorted by the order that they
	* appeared in the file tweets.txt
	* Word numbers in tweets should be translated into the corresponding words
	* Returns null if the node does not exist in the network
	*/
	public Enumeration<String> getTweets(int nodeId) {
		// TODO: write this function
		return null;
	}
	
	/**
	* Returns the number of nodes that would be returned by getFollowing
	* Returns -1 if the node does not exist in the network
	*/
	public int getOutDegree(int nodeId) {
		// TODO: write this function
		return -2;
	}
	
	/**
	* Returns the number of nodes that would be returned by getFollowers
	* Returns -1 if the node does not exist in the network
	*/
	public int getInDegree(int nodeId) {
		// TODO: write this function
		return -2;
	}
	
	/**
	* Returns the ids of nodes with a given name, sorted by
	* ascending node id
	* This is case sensitive
	* Returns null if the name does not exist in the network
	*/
	public Enumeration<Integer> getNodesByName(String name) {
		// TODO: write this function
		return null;
	}
	
	/**
	* Given a nodeId i and a word s, return pairs of
	* node ids and tweets (j,t) such that j tweeted t, t contains
	* the word s, and this tweet is visible to node i.
	* Word numbers in tweets should be translated into the corresponding words
	* This is case sensitive
	* Return value is sorted by node id (from smallest to largest), and within this
	* order, tweets should be returned in the order they appeared in tweets.txt
	* Returns an empty enumeration if the word does not appear in any tweet
	* visible to the user
	*/
	public Enumeration<Map.Entry<Integer,String>> getTweetsByWord(String word) {
		// TODO: write this function
		return null;
	}
	
	/**
	* Returns the frequency of the given word, i.e., the number of tweets
	* containing this word
	*/
	public int getWordFrequency(String word) {
		// TODO: write this function
		return -2;
	}
}
