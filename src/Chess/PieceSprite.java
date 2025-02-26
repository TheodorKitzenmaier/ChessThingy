package Chess;

/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: container for data on how to draw pieces
 */
public class PieceSprite
{
    //static vars
    private static boolean[][] defaultSprite =
    {{false, false, false},
    {false, false, false},
    {false, false, false},
    {false, false, false}};
    
    //instance variable
    private boolean[][] sprite;
    
    //constructors
    /**
     * Default constructor for piece sprite, creates a blank sprite
     */
    public PieceSprite()
    {
        this(defaultSprite);
    }
    /**
     * Standard constructor for a sprite, creates a sprite matching the given boolean matrix
     * @param sprite the sprite to create a sprite from
     */
    public PieceSprite(boolean[][] sprite)
    {
        this.sprite = new boolean[4][];
        //loops through rows and columns, copying data from original sprite
        for(int rowIndex = 0; rowIndex < 4; rowIndex++)
        {
            this.sprite[rowIndex] = new boolean[3];
            for(int colIndex = 0; colIndex < 3; colIndex++)
            {
                this.sprite[rowIndex][colIndex] = sprite[rowIndex][colIndex];
            }
        }
    }
        
    //misc
    /**
     * Returns the boolean row of a given index, with an index of 0 being the top row
     * @param rowIndex index to get the row from
     * @return row at the given index
     */
    public boolean[] getRow(int rowIndex)
    {
        return sprite[rowIndex];
    }
    /**
     * Compares two sprites
     * @param compare sprite to compare with
     * @return if the sprites match
     */
    public boolean matches(PieceSprite compare)
    {
        for(int row = 0; row < 4; row++)
        {
            boolean[] compareRow = compare.getRow(row);
            boolean[] thisRow = this.getRow(row);
            for(int column = 0; column < 3; column++)
            {
                if(compareRow[column] != thisRow[column])
                {
                    return false;
                }
            }
        }
        return true;
    }
}
