import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

class Index {
	private static Index singleton;
	private WordsIDTree wordByIDTree;
	private NamesIDTree nameByIDTree;
	private WordsTree wordsTree;
	private NamesTree namesTree;
	private NodesTree nodesTree;

	public NamesIDTree getNameByIDTree() {
		return nameByIDTree;
	}


	WordsIDTree getWordByIDTree() {
		return wordByIDTree;
	}
	

	public WordsTree getWordsTree() {
		return wordsTree;
	}


	public NamesTree getNamesTree() {
		return namesTree;
	}


	public NodesTree getNodesTree() {
		return nodesTree;
	}


	private Index() {
		wordByIDTree = new WordsIDTree();
		nameByIDTree = new NamesIDTree();
		namesTree = new NamesTree();
		wordsTree = new WordsTree();
		nodesTree = new NodesTree();
	}

	static Index getInstance() {
		if (singleton == null)
			singleton = new Index();
		return singleton;
	}

	private abstract class NodeEntry<K extends Comparable<K>> {
		abstract K getKey();

		abstract NodeEntry<K> mergeEntry(NodeEntry<K> entry);
	}
	private class MyGenericTree<K extends Comparable<K>, O extends NodeEntry<K>> {
		private O curNode;
		private MyGenericTree<K, O> left;
		private MyGenericTree<K, O> right;
		private K key;

		MyGenericTree() {
			curNode = null;
			left = null;
			right = null;
			key = null;
		}

		private MyGenericTree<K, O> getLeft() {
			return left;
		}

		private MyGenericTree<K, O> getRight() {
			return right;
		}

		private int compareKey(K key) {
			int compareTo = key.compareTo(key);
			return compareTo;
		}

		private O getNode() {
			return curNode;
		}

		private K getKey() {
			return key;
		}

		O getKeyElement(K element) {
			int res = compareKey(element);
			if (res == 0)
				return this.curNode;
			if (res < 0) {
				if (right == null)
					return null;
				else
					return right.getKeyElement(element);
			}
			if (res > 0) {
				if (left == null)
					return null;
				else
					return left.getKeyElement(element);
			}
			return null;
		}

		MyGenericTree<K, O> addSubTree(MyGenericTree<K, O> subTree) {
			if (subTree.right != null) {
				addSubTree(subTree.right);
				subTree.right = null;
			}
			if (subTree.left != null) {
				addSubTree(subTree.left);
				subTree.left = null;
			}
			addChild(curNode);
			return this;
		}

		MyGenericTree<K, O> addChild(O entry) {
			int res = compareKey(entry.getKey());
			int leftSize = (left == null) ? 0 : left.size();
			int rightSize = (right == null) ? 0 : right.size();
			if (res == 0) {
				curNode.mergeEntry(entry);
			}
			if (res < 0) {
				if (right == null) {
					right = new MyGenericTree<K, O>();
					right.curNode = entry;
				} else {
					right.addChild(entry);
					if (rightSize > leftSize) {
						MyGenericTree<K, O> replaceThis = removeKeyTree(right
								.minKey());
						if (replaceThis.getRight() != null)
							right.addChild(replaceThis.getRight().getNode());
						left.addChild(curNode);
						curNode = replaceThis.getNode();
						key = replaceThis.getKey();
					}

				}
			}
			if (res > 0) {
				if (left == null) {
					left = new MyGenericTree<K, O>();
					left.curNode = entry;
				} else {
					right.addChild(entry);
					if (rightSize > leftSize) {
						MyGenericTree<K, O> replaceThis = removeKeyTree(left
								.maxKey());
						if (replaceThis.getLeft() != null)
							left.addChild(replaceThis.getLeft().getNode());
						right.addChild(curNode);
						curNode = replaceThis.getNode();
						key = replaceThis.getKey();
					}
				}
			}
			return this;
		}

		int size() {
			int finalSize = 1;
			if (left != null)
				finalSize += left.size();
			if (right != null)
				finalSize += right.size();
			return finalSize;
		}

		K minKey() {
			if (left == null)
				return key;
			else
				return left.minKey();
		}

		Set<K> getSet() {
			Set<K> toRet = new HashSet<K>();
			toRet.add(key);
			if (right != null)
				toRet.addAll(right.getSet());
			if (left != null)
				toRet.addAll(left.getSet());
			return toRet;
		}

		private MyGenericTree<K, O> removeKeyTree(K value) {
			int res = compareKey(value);
			if (res == 0)
				return this;
			if (res < 0) {
				if (right == null)
					return null;
				else {
					MyGenericTree<K, O> toRet = right.removeKeyTree(value);
					if (right == toRet) {
						right = null;
					}
					return toRet;
				}
			}
			if (res > 0) {
				if (left == null)
					return null;
				else {
					MyGenericTree<K, O> toRet = left.removeKeyTree(value);
					if (left == toRet) {
						left = null;
					}
					return toRet;
				}
			}
			return null;
		}

		K maxKey() {
			if (right == null)
				return key;
			else
				return right.maxKey();
		}

		public void clear() {
			curNode = null;
			left = null;
			right = null;
		}

		public boolean containsKey(K key) {
			return (getKeyElement(key) != null);
		}

		public O get(K key) {
			return getKeyElement(key);
		}

		public boolean isEmpty() {
			return (curNode == null);
		}

		public O put(O value) {
			addChild(value);
			return value;
		}

		public void putAll(Enumeration<? extends O> m) {
			while (m.hasMoreElements()) {
				addChild(m.nextElement());
			}
		}

		public O remove(K key) {
			if (curNode != null) {
				MyGenericTree<K, O> subTree = removeKeyTree(key);
				if (subTree == null)
					return null;
				if (subTree.right != null)
					addSubTree(subTree.right);
				if (subTree.left != null)
					addSubTree(subTree.right);
				return subTree.getNode();
			}
			return null;
		}

		public Collection<O> values() {
			HashSet<O> toRet = new HashSet<O>();
			if (curNode != null)
				toRet.add(curNode);
			if (right != null)
				toRet.addAll(right.values());
			if (left != null)
				toRet.addAll(left.values());
			return toRet;
		}

	}

	class WordNode extends NodeEntry<String> {
		private int frequency;
		private TweetsList tweetList;
		private String name;
		private int id;

		public WordNode(Integer integer, String curWord, TweetNode curTweet) {
			frequency = 1;
			id = integer;
			name = curWord;
			tweetList = new TweetsList();
			tweetList.add(curTweet);
		}

		@Override
		NodeEntry<String> mergeEntry(NodeEntry<String> entry) {
			WordNode wordEntry = (WordNode) entry;
			frequency += wordEntry.frequency;
			tweetList.addAll(wordEntry.tweetList);
			return this;
		}

		@Override
		String getKey() {
			return name;
		}

		public int getFrequency() {
			return frequency;
		}

		public Enumeration<Entry<Integer, String>> getTweetsVisibleBy(
				NodeNode curNode) {
			return tweetList.getTweetsVisibleBy(curNode);
		}

	}

	class WordIDNode extends NodeEntry<Integer> {
		public WordIDNode(String name, Integer id) {
			super();
			this.name = name;
			this.id = id;
		}

		private String name;
		private Integer id;

		@Override
		NodeEntry<Integer> mergeEntry(NodeEntry<Integer> entry) {
			throw new UnsupportedOperationException();
		}

		@Override
		Integer getKey() {
			return id;
		}

		String getName() {
			return name;
		}

	}
	
	class TweetNode extends NodeEntry<Integer> {
		Integer[] tweetContent;
		NodeNode tweeter;
		Integer id;
		
		public TweetNode(int curTweetId, Integer[] wordsList, NodeNode tweeterNode) {
			id = curTweetId;
			tweetContent = wordsList;
			tweeter = tweeterNode;
		}

		String getText() {
			WordsIDTree wt = Index.getInstance().getWordByIDTree();
			String text = "";
			for (int i = 0; i < tweetContent.length; i++) {
				if (!text.equals("")) text += " ";
				String curWord = wt.get(tweetContent[i]).getName();
				text += curWord;
			}
			return text;
		}

		@Override
		Integer getKey() {
			return id;
		}

		@Override
		NodeEntry<Integer> mergeEntry(NodeEntry<Integer> entry) {
			throw new UnsupportedOperationException();
		}
	}
	
	class NodeNode extends NodeEntry<Integer> {

		private Integer id;
		private String name;
		private GenericSortedSet<Integer, NodeNode> follows;
		private GenericSortedSet<Integer, NodeNode> followedBy;
		private TweetsList tweetList;
		private boolean publicNode;

		public NodeNode(int curId, String name2, boolean isPublic) {
			id = curId;
			name = name2;
			publicNode = isPublic;
			follows = new GenericSortedSet<Integer, NodeNode>();
			followedBy = new GenericSortedSet<Integer, NodeNode>();
			tweetList = new TweetsList();
		}

		@Override
		NodeEntry<Integer> mergeEntry(NodeEntry<Integer> entry) {
			throw new UnsupportedOperationException();
		}

		@Override
		Integer getKey() {
			return id;
		}

		public String getName() {
			return name;
		}

		public GenericSortedSet<Integer, NodeNode> getFollowedBy() {
			return followedBy;
		}
		
		public GenericSortedSet<Integer, NodeNode> getFollows() {
			return follows;
		}

		public TweetsList getTweetList() {
			return tweetList;
		}

		public boolean isPublicNode() {
			return publicNode;
		}

	}

	class NameNode extends NodeEntry<String> {
		String name;
		GenericSortedSet<Integer, NodeNode> nodes;

		public NameNode(String name2, NodeNode nodeNode) {
			name = name2;
			nodes = new GenericSortedSet<Integer, NodeNode>();
			nodes.add(nodeNode);
		}

		@Override
		String getKey() {
			return name;
		}

		@Override
		NodeEntry<String> mergeEntry(NodeEntry<String> entry) {
			NameNode nameEntry = (NameNode) entry;
			nodes.addAll(nameEntry.nodes);
			return this;
		}

		public Enumeration<Integer> getNodesId() {
			return nodes.getKeyEnumeration();
		}

	}
	
	class NameIdNode extends NodeEntry<Integer> {
		public NameIdNode(String name, Integer id) {
			super();
			this.name = name;
			this.id = id;
		}

		String name;
		Integer id;

		@Override
		Integer getKey() {
			return id;
		}

		@Override
		NodeEntry<Integer> mergeEntry(NodeEntry<Integer> entry) {
			throw new UnsupportedOperationException();
		}

		public String getName() {
			return name;
		}
	}

	class WordsTree extends MyGenericTree<String, WordNode> {

	}

	class WordsIDTree extends MyGenericTree<Integer, WordIDNode> {

	}
	
	class NamesIDTree extends MyGenericTree<Integer, NameIdNode> {

	}

	class NodesTree extends MyGenericTree<Integer, NodeNode> {
		

		private GenericSortedSet<Integer, NodeNode> publicList;

		public NodesTree() {
			super();
			publicList = new GenericSortedSet<Integer, NodeNode>();
		}
		
		@Override
		public NodeNode put(NodeNode value) {
			if (value.publicNode)
				publicList.add(value);
			return super.put(value);
		}

		public Enumeration<Integer> getPublicList() {
			return publicList.getKeyEnumeration();
		}
	}

	class NamesTree extends MyGenericTree<String, NameNode> {

	}
	
	private static boolean testListNotEmpty(GenericSortedSet current) {
		return (current!=null)&&(!current.isEmpty());
	}
	
	class GenericSortedSet<K extends Comparable<K>, O extends NodeEntry<K>> implements Set<O> {

		private class GenericSortedKeyEnumeration implements Enumeration<K> {
			
			public GenericSortedKeyEnumeration(GenericSortedSet<K, O> current) {
				super();
				this.current = current;
			}

			GenericSortedSet<K, O> current;
			
			@Override
			public boolean hasMoreElements() {
				return testListNotEmpty(current);
			}

			@Override
			public K nextElement() {
				if (!hasMoreElements()) throw new NoSuchElementException();
				return current.curData.getKey();
			}
			
		}
		
		private class GenericSortedIterator implements Iterator<O> {
			
			GenericSortedSet<K, O> prev, current;
			boolean gotNext;
			
			public GenericSortedIterator(GenericSortedSet<K, O> current) {
				super();
				this.current = current;
				this.prev = null;
				this.gotNext = false;
			}
			
			@Override
			public boolean hasNext() {
				return testListNotEmpty(current);
			}

			@Override
			public O next() {
				if (!testListNotEmpty(current)) return null;
				gotNext = true;
				O curData = current.curData;
				prev = current;
				current = current.next;
				return curData;
			}

			@Override
			public void remove() {
				if (gotNext) {
					prev.curData = (current == null)? null : current.curData;
					prev.next = (current == null)? null : current.next;
					if (current != null) {
						current.curData = null;
						current.next = null;
					}
					current = prev;
					gotNext = false;
				}
				
			}
			
		}
		
		GenericSortedSet<K, O> next;
		O curData;
		
		@Override
		public boolean add(O arg0) {
			if (arg0 == null) return false;
			int res = curData.getKey().compareTo(arg0.getKey());
			if (res == 0) return false;
			if (res > 0) {
				GenericSortedSet<K, O> newNode = new GenericSortedSet<K, O>();
				newNode.next = next;
				newNode.curData = curData;
				curData = arg0;
				next = newNode;
				return true;
			}
			if (res < 0) {
				if (next == null) {
					GenericSortedSet<K, O> newNode = new GenericSortedSet<K, O>();
					newNode.next = null;
					newNode.curData = arg0;
					next = newNode;
					return true;
				} else return next.add(arg0);
			}
			return false;
		}
		
		

		@Override
		public boolean addAll(Collection<? extends O> arg0) {
			boolean changed = false;
			for (O o : arg0) {
				changed = add(o)||changed;
			}
			return changed;
		}

		@Override
		public void clear() {
			if (next != null) {
				next.clear();
			}
			next = null;
			curData = null;
		}

		@Override
		public boolean contains(Object arg0) {
			if (curData.equals(arg0)) return true;
			if (next != null) return next.contains(arg0);
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> arg0) {
			boolean doesContainAll = true;
			for (Object object : arg0) {
				doesContainAll = doesContainAll&&contains(object);
			}
			return doesContainAll;
		}

		@Override
		public boolean isEmpty() {
			return (curData == null);
		}

		@Override
		public Iterator<O> iterator() {
			return new GenericSortedIterator(this);
		}

		@Override
		public boolean remove(Object arg0) {
			if (isEmpty()) return false;
			if (curData.equals(arg0)) {
				if (next == null) {
					curData = null;
				} else {
					curData = next.curData;
					next = next.next;
				}
				return true;
			}
			if (next != null) return next.contains(arg0);
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> arg0) {
			boolean changed = false;
			for (Object object : arg0) {
				changed = remove(object)||changed;
			}
			return changed;
		}
		
		public Enumeration<K> getKeyEnumeration() {
			return new GenericSortedKeyEnumeration(this);
		}

		@Override
		public int size() {
			// TODO: Write this a bit cheaper? Maybe remember the number?
			int curSize = 1;
			if (next != null) curSize += next.size();
			return curSize;
		}

		@Override
		public boolean retainAll(Collection<?> arg0) {
			boolean changed = false;
			Iterator<?> iter;
			for(iter = iterator(); iter.hasNext(); iter.next()) {
				if(!arg0.contains(iter)) {
					remove(curData);
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public Object[] toArray() {
			Iterator<?> iter;
			Object[] arr = new Object[size()];
			int i = 0;
			for(iter = iterator(); iter.hasNext(); iter.next()) {
				arr[i++] = iter;
			}
			return arr;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		public <T> T[] toArray(T[] arg0) {
			Iterator<?> iter;
			T[] arr = null;
			if (arg0.length < size()) {
				arr = (T[]) Array.newInstance(arg0.getClass(), size());
			}
			int i = 0;
			for(iter = iterator(); iter.hasNext(); iter.next()) {
				if(arr != null) { arr[i++] = (T)iter; }
				else { arg0[i] = (T)iter; }
			}
			if(arr != null) { return arr; }
			else { return arg0; }
		}

		
	}
	
	class TweetsList extends GenericSortedSet<Integer, TweetNode> {
		
		class TweetsEnumeration implements Enumeration<String> {
			GenericSortedSet<Integer, TweetNode> current;
			Enumeration<Integer> followList;
			Enumeration<Integer> publicList;
			int curFollowListIndex;
			int curPublicListIndex;
			
			
			public TweetsEnumeration(TweetsList current) {
				super();
				this.current = current;
				this.followList = null;
				this.publicList = null;
				this.curFollowListIndex = 0;
				this.curPublicListIndex = 0;
			}
			
			public TweetsEnumeration(TweetsList current, NodeNode curNode) {
				super();
				this.current = current;
				this.followList = curNode.getFollows().getKeyEnumeration();
				this.publicList = Index.getInstance().getNodesTree().getPublicList();
				this.curFollowListIndex = followList.hasMoreElements()? followList.nextElement() : -1;
				this.curPublicListIndex = publicList.hasMoreElements()? publicList.nextElement() : -1;;
			}
			
			public boolean hasMoreElements() {
				return testListNotEmpty(current);
			}

			@Override
			public String nextElement() {
				if (!hasMoreElements()) throw new NoSuchElementException();
				// TODO: CHECK IF YOU CAN SEE THE TWEET SOMEHOW
				String text = current.curData.getText();
				current = current.next;
				return text;
			}
			
		}
		
		public Enumeration<String> getTweets() {
			return new TweetsEnumeration(this);
		}

		public Enumeration<Entry<Integer, String>> getTweetsVisibleBy(
				NodeNode curNode) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
