import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class SlowIndexWriter {
	/**
	 * Given raw social network data, creates an on disk index
	 */
	public void createIndex() {

	}

	/**
	 * Delete all index files created
	 */
	public void removeIndex() {

	}

	private class DictParser {
		DictParser() {

		}

		void parseFile(File f) throws Exception {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			int curId = 0;
			while ((line = br.readLine()) != null) {
				Index.getInstance().getWordByIDTree().addChild(Index.getInstance().new WordIDNode(line, curId));
				curId++;
			}
			br.close();
		}
	}
	
	private class NamesParser {
		NamesParser() {

		}

		void parseFile(File f) throws Exception {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			int curId = 0;
			while ((line = br.readLine()) != null) {
				Index.getInstance().getNameByIDTree().addChild(Index.getInstance().new NameIdNode(line, curId));
				curId++;
			}
			br.close();
		}
	}
	
	private class NodesParser {
		NodesParser() {

		}

		void parseFile(File f) throws Exception {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				String[] intList = line.split(" ");
				int curId = Integer.parseInt(intList[0]);
				String name = Index.getInstance().getNameByIDTree().get(Integer.parseInt(intList[1])).getName();
				boolean isPublic = intList[2].equals("1");
				Index.NodeNode nodeNode = Index.getInstance().new NodeNode(curId, name, isPublic);
				Index.NameNode nameNode = Index.getInstance().new NameNode(name, nodeNode);
				Index.getInstance().getNamesTree().addChild(nameNode);
				Index.getInstance().getNodesTree().addChild(nodeNode);
			}
			br.close();
		}
	}

	private class EdgesParser {
		EdgesParser() {

		}

		void parseFile(File f) throws Exception {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			int curFollower = -1;
			Index.NodeNode curFollowerNode = null;
			while ((line = br.readLine()) != null) {
				String[] intList = line.split(" ");
				int followerId = Integer.parseInt(intList[0]);
				if (curFollower != followerId) {
					curFollower = followerId;
					curFollowerNode = Index.getInstance().getNodesTree().get(curFollower);
				}
				int followedId = Integer.parseInt(intList[1]);
				Index.NodeNode curFollowedNode = Index.getInstance().getNodesTree().get(followedId);
				curFollowerNode.getFollows().add(curFollowedNode);
				curFollowedNode.getFollowedBy().add(curFollowedNode);
			}
			br.close();
		}
	}
	
	private class TweetsParser {
		TweetsParser() {

		}

		void parseFile(File f) throws Exception {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			int curId = -1;
			int curTweetId = 0;
			Index.NodeNode curNode = null;
			while ((line = br.readLine()) != null) {
				String[] intList = line.split(" ");
				int nodeId = Integer.parseInt(intList[0]);
				if (curId != nodeId) {
					curId = nodeId;
					curNode = Index.getInstance().getNodesTree().get(curId);
				}
				Integer[] wordsList = new Integer[intList.length-1];
				for (int i = 0; i < wordsList.length; i++) {
					wordsList[i] = Integer.parseInt(intList[i+1]);
					
				}
				Index.TweetNode tweet = Index.getInstance().new TweetNode(curTweetId, wordsList, curNode);
				curNode.getTweetList().add(tweet);
				for (int i = 0; i < wordsList.length; i++) {
					String curWord = Index.getInstance().getWordByIDTree().get(wordsList[i+1]).getName();
					Index.WordNode wordNode = Index.getInstance().new WordNode(wordsList[i+1], curWord, tweet);
					Index.getInstance().getWordsTree().addChild(wordNode);
				}
				curTweetId++;
			}
			br.close();
		}
	}
}