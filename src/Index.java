import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

class Index {
	private static Index singleton;
	private WordsIDTree wordByIDTree;

	WordsIDTree getWordByIDTree() {
		return wordByIDTree;
	}

	private Index() {
		wordByIDTree = new WordsIDTree();
	}

	static Index getInstance() {
		if (singleton == null)
			singleton = new Index();
		return singleton;
	}

	private abstract class NodeEntry<K extends Comparable> {
		abstract K getKey();

		abstract NodeEntry<K> mergeEntry(NodeEntry<K> entry);
	}

	@SuppressWarnings("rawtypes")
	private class MyGenericTree<K extends Comparable, O extends NodeEntry<K>> {
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

	private class WordNode extends NodeEntry<String> {
		private int frequency;
		private Set<String> tweetList;
		private String name;
		private int id;

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

	private class NodeNode extends NodeEntry<Integer> {

		Integer id;
		String name;
		Set<NodeNode> followers;
		Set<NodeNode> Followed;
		Set<Integer> tweetId;
		boolean publicNode;

		@Override
		NodeEntry<Integer> mergeEntry(NodeEntry<Integer> entry) {
			throw new UnsupportedOperationException();
		}

		@Override
		Integer getKey() {
			return id;
		}

	}

	private class NameNode extends NodeEntry<String> {
		String name;
		Set<NodeNode> nodes;

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

	}

	class WordsTree extends MyGenericTree<String, WordNode> {

	}

	class WordsIDTree extends MyGenericTree<Integer, WordIDNode> {

	}

	private class NodesTree extends MyGenericTree<Integer, NodeNode> {
		private Set<NodeNode> publicList;

		@Override
		public NodeNode put(NodeNode value) {
			if (value.publicNode)
				publicList.add(value);
			return super.put(value);
		}
	}

	private class NamesTree extends MyGenericTree<String, NameNode> {

	}
}
