import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;


public class SlowIndexWriter {
	/**
	* Given raw social network data, creates an on disk index
	*/
	public void createIndex() {}
	/**
	* Delete all index files created
	*/
	public void removeIndex() {}
	

	
	private class Index {
		private abstract class NodeEntry<K extends Comparable, V> {
			private NodeEntry<K, V> left;
			private NodeEntry<K, V> right;
			private K key;

			NodeEntry<K, V> getLeft() {
				return left;
			}
			
			NodeEntry<K, V> getRight() {
				return right;
			}
			
			int compareKey(K key) {
				return this.key.compareTo(key);
			}
			
			K getKey() {
				return key;
			}
			
			NodeEntry<K, V> getKeyElement(K element) {
				int res = compareKey(entry.getKey());
				if (res == 0) return this;
				if (res < 0) {
					if (right == null) return null;
					else return right.getKeyElement(element);
				}
				if (res > 0) {
					if (left == null) return null;
					else return left.getKeyElement(element);
				}
				return null
			}
			
			NodeEntry<K, V> addChild(NodeEntry<K, V> entry) {
				int res = compareKey(entry.getKey());
				int leftSize = (left == null)? 0 : left.size();
				int rightSize = (right == null)? 0 : right.size();
				if (res == 0) {
					return mergeEntry(entry);
				}
				if (res < 0) {
					if (right == null) {
						right = entry;
						
					} else {
						this.right.addChild(entry);
						if (rightSize > leftSize) {
							NodeEntry<K, V> tempRef = this;
							NodeEntry<K, V> replaceThis = removeKeyTree(right.minKey());
							if (replaceThis.right != null) right.addChild(replaceThis.right);
							replaceThis.right = right;
							replaceThis.left = left;
							right = null;
							left = null;
							this = replaceThis;
							left.addChild(tempRef);
						}
						
					}
				}
				if (res > 0) {
					if (left == null) {
						left = entry;
						
					} else {
						this.left.addChild(entry);
						if (rightSize < leftSize) {
							NodeEntry<K, V> tempRef = this;
							NodeEntry<K, V> replaceThis = removeKeyTree(left.maxKey());
							if (replaceThis.left != null) left.addChild(replaceThis.left);
							replaceThis.right = right;
							replaceThis.left = left;
							right = null;
							left = null;
							this = replaceThis;
							right.addChild(tempRef);
						}
					}
				}
				return this;
			}
			
			int size() {
				int finalSize = 1;
				if (left != null) finalSize += left.size();
				if (right != null) finalSize += right.size();
				return finalSize;
			}
			
			K minKey() {
				if (left == null) return key;
				else return left.minKey();
			}
			
			private NodeEntry<K, V> removeKeyTree(K value) {
				int res = compareKey(value);
				if (res == 0) return this;
				if (res < 0) {
					if (right == null) return null;
					else {
						NodeEntry<K, V> toRet = right.removeKeyTree(value);
						if (right == toRet) {
							right = null;
						}
						return toRet;
					}
				}
				if (res > 0) {
					if (left == null) return null;
					else {
						NodeEntry<K, V> toRet = left.removeKeyTree(value);
						if (left == toRet) {
							left = null;
						}
						return toRet;
					}
				}
				return null;
			}
			
			K maxKey() {
				if (right == null) return key;
				else return right.maxKey();
			}
			
			abstract NodeEntry<K, V> mergeEntry(NodeEntry<K, V> entry);
			
			abstract V getValue();
		}
		
		private abstract class MyGenericTree<K extends Comparable, O extends NodeEntry<K, V>> {
			private O root;
			
			public void clear() {
				root = null;
			}
			
			

			public boolean containsKey(Object key) {
				return (root.getKeyElement(key) != null);
			}

			
			public O get(Object key) {
				return root.getKeyElement(key);
			}

			public boolean isEmpty() {
				return (root == null);
			}

			public Set<K> keySet() {
				// TODO Auto-generated method stub
				return null;
			}

			public O put(O value) {
				root.addChild(value);
				return value;
			}

			public void putAll(Enumeration<? extends O> m) {
				while (m.hasMoreElements()) {
					root.addChild(m.nextElement());
				}
			}

			public O remove(Object key) {
				root 
				return null;
			}

			public int size() {
				return root.size();
			}

			public Collection<O> values() {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
		
		private class WordNode extends NodeEntry<String> {

			@Override
			NodeEntry<String> mergeEntry(NodeEntry<String> entry) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
		
		private class NodeNode extends NodeEntry<Integer> {

			@Override
			NodeEntry<Integer> mergeEntry(NodeEntry<Integer> entry) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
		
		private class NameNode extends Index.NodeEntry<String> {

			@Override
			NodeEntry<String> mergeEntry(NodeEntry<String> entry) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
		
		private class WordsTree extends MyGenericTree<String, WordNode> {
			
		}
		
		private class NodesTree extends MyGenericTree<Integer, NodeNode> {
			
		}
		
		private class NamesTree extends MyGenericTree<String, NameNode> {
			
		}
		
	}
	
}
