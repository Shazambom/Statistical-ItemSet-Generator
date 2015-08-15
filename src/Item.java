/**
 * Created by Shazambom on 8/12/2015.
 */
public class Item implements Comparable{
    private String id;
    private double count;
    private int blockNum;
    private float percentage;
    private float percentageInBlock;
    private double totalCount;

    /**
     * Initializes the Item object
     * @param id the unique ID
     * @param blockNum the number block the Item is in
     */
    public Item(String id, int blockNum) {
        this.id = id;
        this.blockNum = blockNum;
        count = 1;
        percentage = 0;
        totalCount = 0;
    }

    /**
     * Gets the ID of the item
     * @return the string that identifies an item
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the number of times an item occurs in a block
     * @return the count instance variable
     */
    public double getCount() {
        return count;
    }

    /**
     * Increments the count variable by 1
     */
    public void increment() {
        count++;
    }

    /**
     * Gets the block number the item is in
     * @return the block number instance variable
     */
    public int getBlockNum() {
        return blockNum;
    }

    /**
     * Sets the percentage to a new float value
     * @param percentage the float value percentage is to be set to
     */
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    /**
     * Gets the percentage of the items occurance
     * @return the percentage instance variable
     */
    public float getPercentage() {
        return percentage;
    }

    /**
     * Sets the percentage of times the item shows up in a block
     * @param percentageInBlock the new percentage in block
     */
    public void setPercentageInBlock(float percentageInBlock) {
        this.percentageInBlock = percentageInBlock;
    }

    /**
     * Gets the percentage of times the item shows up in a block
     * @return the percentage in block instance variable
     */
    public float getPercentageInBlock() {
        return percentageInBlock;
    }

    /**
     * Sets the total count of the Item (how many times it occurs in total)
     * @param totalCount the new value of total count
     */
    public void setTotalCount(double totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * Gets the total times the item shows up in all the item sets
     * @return the total count instance variable
     */
    public double getTotalCount() {
        return totalCount;
    }

    @Override
    public int compareTo(Object other) {
        if (other == null) {
            return 0;
        }
        return (int) (count - ((Item) (other)).getCount());
    }

    @Override
    public String toString() {
        return "ID:" + id + " \t#Occurances:" + (int)totalCount + "\t PercentageTotal:" + percentage
                + "\t#InBlock:" + (int)count + "\tPercentageInBlock:"
                + percentageInBlock + "%\t BLOCK#:" + blockNum;
    }


}