package Chess;

/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: converts between chess notation and array indexes
 */
public class Position
{
    //instance variables
    private char file;
    private int row;
    
    //static vars
    static char[] files = {'A','B','C','D','E','F','G','H'};
    
    //constructors
    /**
     * Default constructor for Position, creates and instance with parameters ('A',1)
     */
    public Position()
    {
        this('A',1);
    }
    /**
     * Normal constructor for Position
     * @param file file for the position
     * @param row row for the position
     */
    public Position(char file, int row)
    {
        this.file = file;
        this.row = row;
    }
    
    //accessors
    /**
     * Returns the file
     * @return file
     */
    public char getFile()
    {
        return file;
    }
    /**
     * Converts the file into a column, column is related to file as 1:A, 2:B, 3:C...
     * @return column
     */
    public int getColumn()//converts file to column as an int
    {
        for(int i = 0; i < 8; i++)
        {
            if(files[i] == file)
            {
                return i + 1;
            }
        }
        return -1;
    }
    /**
     * Converts the file to a column index to use with the board array, index is related to file as 0:A, 1:B, 2:C...
     * @return index of the column
     */
    public int getColumnIndex()
    {
        for(int i = 0; i < 8; i++)
        {
            if(files[i] == file)
            {
                return i;
            }
        }
        return -1;
    }
    /**
     * Returns the row
     * @return row
     */
    public int getRow()
    {
        return row;
    }
    /**
     * Converts the row to its index for use with the board array, index is related to row as 0:8, 1:7, 2:6...
     * @return index of the row
     */
    public int getRowIndex()
    {
        return 8 - row;
    }
    /**
     * Converts a numeric column (not index) to a file as a char
     * @param column column
     * @return equivalent file
     */
    public static char columnToFile(int column)
    {
        return files[column - 1];
    }
    /**
     * Checks if a given positions has the same row and file, returns true if they match
     * @param compare position to compare with
     * @return if the position matches
     */
    public boolean matches(Position compare)
    {
        return file == compare.getFile() && row == compare.getRow();
    }
    public String toString()
    {
        return "(" + file + "," + row + ")";
    }
}
