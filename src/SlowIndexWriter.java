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

}
