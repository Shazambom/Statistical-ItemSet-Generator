import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shazambom on 8/11/2015.
 */
public class SetAnalyzer {
    private File parentDirectory;
    private String filePath;

    /**
     * Inits the file path and parent directory of the program
     * @param filePath the location of the files
     */
    public SetAnalyzer(String filePath) {
        this.parentDirectory = new File(filePath);
        this.filePath = filePath;
    }

    /**
     * Analyzes the data from the items list it is given
     * Also creates a file with all of the statistical analysis of the Items.
     * @param items the input of item objects it is given
     * @return a sorted and analyzed item list
     */
    public ArrayList<Item> analyzeData(ArrayList<Item> items) {
        items.sort((Item a, Item b) -> b.compareTo(a));
        File output = new File(filePath + "AnalyzedData.txt");
        float total = 0;
        for (Item item: items) {
            total += item.getCount();
        }
        ArrayList<String> totaledItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setPercentageInBlock(((float)items.get(i).getCount() * 100) / total);
            if (!totaledItems.contains(items.get(i).getId())) {
                ArrayList<Item> dupes = new ArrayList<>();
                for (int j = i + 1; j < items.size(); j++) {
                    if (items.get(i).getId().equals(items.get(j).getId())) {
                        dupes.add(items.get(j));
                    }
                }
                double totalItems = items.get(i).getCount();
                for (Item item : dupes) {
                    totalItems += item.getCount();
                }
                items.get(i).setPercentage(((float) totalItems * 100) / total);
                items.get(i).setTotalCount(totalItems);
                for (Item item : dupes) {
                    item.setPercentage(((float) totalItems * 100) / total);
                    item.setTotalCount(totalItems);
                }
                totaledItems.add(items.get(i).getId());
            }
        }
        try {
            PrintWriter out = new PrintWriter(output);
            for (Item item: items) {
                out.println(item.toString());
            }
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Reads through all of the folders in the given directory and generates all
     * of the information needed for analysis from items
     * @return a list of Items
     */
    public ArrayList<Item> generateData() {
        ArrayList<Item> toReturn = new ArrayList<Item>();
        File[] files = parentDirectory.listFiles();
        for (File file: files) {
            ArrayList<Item> itemIds = getItems(file);
            for (Item element: itemIds) {
                boolean notThere = true;
                for (Item item: toReturn) {
                    if (item.getId().equals(element.getId())
                            && item.getBlockNum() == element.getBlockNum()) {
                        item.increment();
                        notThere = false;
                        break;
                    }
                }
                if (notThere) {
                    toReturn.add(element);
                }
            }

        }
        return toReturn;
    }

    /**
     * Gets a list of items from an idividual item set
     * Only looks a .json files
     * Uses the Boyer Moore method of string searching for finding items
     * within item set files
     * @param file the file to be read
     * @return the list of items within the file
     */
    private ArrayList<Item> getItems(File file) {
        ArrayList<Item> items = new ArrayList<>();
        if (file.isFile() && file.getName().contains(".json")) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader in = new BufferedReader(fileReader);
                String line;
                int lineNum = 0;
                int blockNum = 0;
                boolean oneLine = false;
                while ((line = in.readLine()) != null) {
                    lineNum++;
                    String itemId = "";
                    ArrayList<Integer> idLocs = boyerMoore("\"id\":", line);
                    ArrayList<Integer> blockLocs = boyerMoore("\"items\":", line);
                    if (!oneLine && blockLocs.size() == 1) {
                        blockNum++;
                    } else if (blockLocs.size() > 1) {
                        oneLine = true;
                    }

                    for (Integer element : idLocs) {
                        int i = element + 6;
                        if (line.charAt(i) == '"') {
                            i++;
                        }
                        while (line.charAt(i) != '"') {
                            itemId += line.charAt(i);
                            i++;
                        }
                        if (oneLine && blockNum < blockLocs.size()
                                && element > blockLocs.get(blockNum)) {
                            blockNum++;
                        }
                        items.add(new Item(itemId, blockNum));

                        itemId = "";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return items;
    }

    /**
     * One of the more efficient string searching algorithms
     * @param pattern the string pattern we are searching for
     * @param text the string the pattern is contained
     * @return the locations in the text where the pattern shows up
     */
    private ArrayList<Integer> boyerMoore(CharSequence pattern, CharSequence text) {
        if (pattern == null || text == null || pattern.length() == 0) {
            throw new IllegalArgumentException("One of the "
                    + "arguments is invalid");
        }
        if (text.length() == 0 || text.length() < pattern.length()) {
            return new ArrayList<>();
        }
        ArrayList<Integer> toReturn = new ArrayList<>();
        int[] lastTable = buildLastTable(pattern);
        int i = pattern.length() - 1;
        int j = pattern.length() - 1;

        while (i < text.length()) {
            char current = text.charAt(i);
            if (current == pattern.charAt(j)) {
                if (j == 0) {
                    toReturn.add(i);
                    j = pattern.length() - 1;
                    i += (pattern.length() * 2) - 1;
                } else {
                    i--;
                    j--;
                }
            } else {
                int last = lastTable[current];
                if (j <= last + 1) {
                    i += pattern.length() - j;
                } else {
                    i += pattern.length() - (last + 1);
                }
                j = pattern.length() - 1;
            }
        }


        return toReturn;
    }

    /**
     * Creates the last table for Boyer Moore
     * It is a key part of the searching algorithm
     * @param pattern the pattern boyer moore is searching for
     * @return the last table
     */
    private int[] buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("One of the "
                    + "arguments is invalid");
        }
        int[] table = new int[Character.MAX_VALUE + 1];
        for (int i = 0; i < table.length; i++) {
            table[i] = -1;
        }
        for (int i = 0; i < pattern.length(); i++) {
            table[pattern.charAt(i)] = i;
        }

        return table;

    }

}
