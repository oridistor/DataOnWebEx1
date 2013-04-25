import java.util.Enumeration;
import java.util.Map;

public class IndexReader {
	/**
	 * Returns the name of a node Returns null if the node does not exist in the
	 * network
	 */
	public String getName(int nodeId) {
		return fetchNodeById(nodeId).getName();
	}
	
	private Index.NodeNode fetchNodeById(int nodeId) {
		return Index.getInstance().getNodesTree().get(nodeId);
	}

	/**
	 * Returns the privacy status of a node, i.e., true if the node has privacy
	 * status public Returns false if the node does not exist in the network (or
	 * if its tweets are private)
	 */
	public boolean isPublic(int nodeId) {
		Index.NodeNode NodeNode = fetchNodeById(nodeId);
		if (NodeNode == null) return false;
		else return NodeNode.isPublicNode();
	}

	/**
	 * Returns the ids of friends of a node Given nodeId i, return all j such
	 * that (i,j) is in the network, sorted by ascending order of j Returns null
	 * if the node does not exist in the network
	 */
	public Enumeration<Integer> getFollowing(int nodeId) {
		Index.NodeNode curNode = fetchNodeById(nodeId);
		if (curNode == null) return null;
		return curNode.getFollows().getKeyEnumeration();
	}

	/**
	 * Returns the ids of nodes who consider the input node their friend Given
	 * nodeId i, return all j such that (j,i) is in the network, sorted by
	 * ascending order of j Returns null if the node does not exist in the
	 * network
	 */
	public Enumeration<Integer> getFollowers(int nodeId) {
		Index.NodeNode curNode = fetchNodeById(nodeId);
		if (curNode == null) return null;
		return curNode.getFollowedBy().getKeyEnumeration();
	}

	/**
	 * Returns the tweets of a given node, sorted by the order that they
	 * appeared in the file tweets.txt Word numbers in tweets should be
	 * translated into the corresponding words Returns null if the node does not
	 * exist in the network
	 */
	public Enumeration<String> getTweets(int nodeId) {
		Index.NodeNode curNode = fetchNodeById(nodeId);
		if (curNode == null) return null;
		return curNode.getTweetList().getTweets();
	}

	/**
	 * Returns the number of nodes that would be returned by getFollowing
	 * Returns -1 if the node does not exist in the network
	 */
	public int getOutDegree(int nodeId) {
		Index.NodeNode curNode = fetchNodeById(nodeId);
		if (curNode == null) return -1;
		else return curNode.getFollows().size();
	}

	/**
	 * Returns the number of nodes that would be returned by getFollowers
	 * Returns -1 if the node does not exist in the network
	 */
	public int getInDegree(int nodeId) {
		Index.NodeNode curNode = fetchNodeById(nodeId);
		if (curNode == null) return -1;
		else return curNode.getFollowedBy().size();
	}

	/**
	 * Returns the ids of nodes with a given name, sorted by ascending node id
	 * This is case sensitive Returns null if the name does not exist in the
	 * network
	 */
	public Enumeration<Integer> getNodesByName(String name) {
		Index.NameNode curName = Index.getInstance().getNamesTree().get(name);
		if (curName == null) return null;
		else return curName.getNodesId();
	}

	/**
	 * Given a nodeId i and a word s, return pairs of node ids and tweets (j,t)
	 * such that j tweeted t, t contains the word s, and this tweet is visible
	 * to node i. Word numbers in tweets should be translated into the
	 * corresponding words This is case sensitive Return value is sorted by node
	 * id (from smallest to largest), and within this order, tweets should be
	 * returned in the order they appeared in tweets.txt Returns an empty
	 * enumeration if the word does not appear in any tweet visible to the user
	 */
	public Enumeration<Map.Entry<Integer, String>> getTweetsByWord(int nodeId, String word) {
		Index.NodeNode curNode = fetchNodeById(nodeId);
		Index.WordNode curWord = Index.getInstance().getWordsTree().get(word);
		if (curWord == null) return null; // TODO - write an empty enumeration definition
		return curWord.getTweetsVisibleBy(curNode);
	}

	/**
	 * Returns the frequency of the given word, i.e., the number of tweets
	 * containing this word
	 */
	public int getWordFrequency(String word) {
		Index.WordNode curWord = Index.getInstance().getWordsTree().get(word);
		if (curWord == null) return 0;
		else return curWord.getFrequency();
	}
}
