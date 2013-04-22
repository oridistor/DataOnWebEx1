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
		private abstract class NodeEntry<K extends Comparable> {
			private NodeEntry<K> left;
			private NodeEntry<K> right;
			private K key;

			NodeEntry<K> getLeft() {
				return left;
			}
			
			NodeEntry<K> getRight() {
				return right;
			}
			
			int compareKey(K key) {
				return this.key.compareTo(key);
			}
			
			K getKey() {
				return key;
			}
			
			NodeEntry<K> addChild(NodeEntry<K> entry) {
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
							K tempKey = key;
						}
						
					}
				}
				if (res > 0) {
					if (left == null) {
						left = entry;
						
					} else {
						this.left.addChild(entry);
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
			
			private NodeEntry<K> removeKeyTree(K value) {
				int res = compareKey(value);
				if (res == 0) return this;
				if (res < 0) {
					if (right == null) return null;
					else {
						NodeEntry<K> toRet = right.removeKeyTree(value);
						if (right == toRet) {
							right = null;
						}
						return toRet;
					}
				}
				if (res > 0) {
					if (left == null) return null;
					else {
						NodeEntry<K> toRet = left.removeKeyTree(value);
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
			
			abstract NodeEntry<K> mergeEntry(NodeEntry<K> entry);
		}
		
		private abstract class MyGenericTree<K extends Comparable, O extends NodeEntry<K>> {
			private O root;
			
			public void clear() {
				// TODO Auto-generated method stub
				
			}
			
			

			public boolean containsKey(Object key) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean containsValue(Object value) {
				// TODO Auto-generated method stub
				return false;
			}

			
			public O get(Object key) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
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
				// TODO Auto-generated method stub
				while (m.hasMoreElements()) {
					root.addChild(m.nextElement());
				}
			}

			public O remove(Object key) {
				// TODO Auto-generated method stub
				return null;
			}

			public int size() {
				// TODO Auto-generated method stub
				return 0;
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
