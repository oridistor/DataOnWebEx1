import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		}
	}

}
