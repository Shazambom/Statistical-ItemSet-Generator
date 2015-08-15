import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Shazambom on 8/11/2015.
 */
public class ItemSetGenerator {
    private  int maxItems = 35;
    private ArrayList<Item> items = new ArrayList<>();
    private String filePath;
    private String championName;

    /**
     * Inits the Item set generator
     * @param filePath The path where all of the .json files are and where the new files will be dumped
     * @param championName The name of the champion being analyzed
     */
    public ItemSetGenerator(String filePath, String championName, int maxItems) {
        this.filePath = filePath;
        SetAnalyzer in = new SetAnalyzer(filePath);
        items = in.analyzeData(in.generateData());
        this.championName = championName;
        this.maxItems = maxItems;
    }

    /**
     * Generates the Item set for the program
     * @param fileName The name of the .json file that is created by this program
     * @return the File that is generated
     */
    public File genItemSet(String fileName) {
        File itemSet = new File(filePath + fileName + ".json");
        try {
            PrintWriter out = new PrintWriter(filePath + fileName + ".json");
            out.print("{\n" +
                    "    \"map\": \"any\",\n" +
                    "    \"isGlobalForChampions\": false,\n" +
                    "    \"blocks\": [\n");
            ArrayList<ArrayList<Item>> buckets = new ArrayList<>();
            for (ArrayList<Item> list : genBlocks()) {
                if (list.size() != 0) {
                    buckets.add(list);
                }
            }
            for (int i = 0; i < buckets.size(); i++) {
                writeBlock(buckets.get(i), i + 1, out);
                if (i < buckets.size() - 1 && buckets.get(i).size() != 0) {
                    out.print(",\n");
                }
            }
            out.print("\n    ],\n" +
                    "    \"associatedChampions\": [],\n" +
                    "    \"title\": \"" + fileName.substring(1) + " \",\n" +
                    "    \"priority\": false,\n" +
                    "    \"mode\": \"any\",\n" +
                    "    \"isGlobalForMaps\": true,\n" +
                    "    \"associatedMaps\": [],\n" +
                    "    \"type\": \"custom\",\n" +
                    "    \"sortrank\": 1,\n" +
                    "    \"champion\": \"" + championName + "\"\n" +
                    "}");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemSet;
    }

    /**
     * Assists genItemSet() by creating blocks for items for each list of Items
     * @param items the list of items to go in the block
     * @param blockNum the block number
     * @param out the PrintWriter object that is writing to the file
     */
    private void writeBlock(ArrayList<Item> items, int blockNum, PrintWriter out) {
        if (items.size() > 0) {
            out.println("        {\n" +
                    "            \"items\": [");
            for (int i = 0; i < items.size(); i++) {
                out.print("                {\n" +
                        "                    \"count\": 1,\n" +
                        "                    \"id\": \""
                        + items.get(i).getId() + "\"\n" +
                        "                }");
                if (i != items.size() - 1) {
                    out.print(",\n");
                } else {
                    out.print("\n            ],\n" +
                            "            \"type\": \"Block#" + blockNum + "\"\n" +
                            "        }");
                }
            }
        }
    }

    /**
     * Generates the blocks for the Item sets based upon popularity of items
     * @return an array of ArrayLists of Items
     */
    public ArrayList<Item>[] genBlocks() {
        ArrayList<Item> filtered = filter();
        filtered.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return (int) (o2.getTotalCount() - o1.getTotalCount());
            }
        });
        ArrayList<Item>[] blocks = new ArrayList[getMaxBlocks(filtered)];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new ArrayList<>();
        }
        for (int i = 0; i < maxItems && i < filtered.size(); i++) {
            blocks[filtered.get(i).getBlockNum() - 1].add(filtered.get(i));
        }
        return blocks;
    }

    /**
     * Removes all of the unnessesary Items from the item list
     * @return the new list of useful items
     */
    public ArrayList<Item> filter() {
        ArrayList<Item> toReturn = new ArrayList<>();
        ArrayList<Item> dupes = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
                for (int j = i + 1; j < items.size(); j++) {
                    if (items.get(i).getId().equals(items.get(j).getId())) {
                        dupes.add(items.get(j));
                    }
                }
        }
        for (Item item: items) {
            if (!dupes.contains(item)) {
                toReturn.add(item);
            }
        }
        return toReturn;
    }

    /**
     * Gets the maximum ammount of blocks there could potentially be
     * @param getMax the list of items
     * @return the number of blocks
     */
    private int getMaxBlocks(ArrayList<Item> getMax) {
        int block = 1;
        for (Item item: getMax) {
            if (item.getBlockNum() > block) {
                block = item.getBlockNum();
            }
        }
        return block;
    }
}
