package Chess;

/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: medium for translating input of moves into components
 */
public class Move
{
    //instance vars
    private boolean valid;
    private Position origin;
    private Position target;
    
    //constructors
    /**
     * Default constructor for Move, creates a move with a target and origin of the default Position, and marks the move as invalid
     */
    public Move()
    {
        origin = new Position();
        target = new Position();
        valid = false;
    }
    /**
     * Constructor that takes two Positions as arguments, and origin and a target. If the origin is not the same as the target, the move is marked as valid
     * @param origin position to move from
     * @param target position to move to
     */
    public Move(Position origin, Position target)
    {
        this.origin = origin;
        this.target = target;
        valid = !this.origin.matches(target);
    }
    /**
     * Constructor that interprets a string to convert "chess notation" (F#-F#) to the move format. Creates a default move if the string can not be interpreted
     * @param stringMove string to interpret
     */
    public Move(String stringMove)
    {
        //checks if the string matches a chess move format, and creates a default move if not
        if(stringMove.matches("[ABCDEFGH][1234567890]-[ABCDEFGH][1234567890]"))
        {
            origin = new Position(stringMove.charAt(0), Character.getNumericValue(stringMove.charAt(1)));
            target = new Position(stringMove.charAt(3), Character.getNumericValue(stringMove.charAt(4)));
            valid = !origin.matches(target);
        }
        else
        {
            origin = new Position();
            target = new Position();
            valid = false;
        }
    }
    
    //accessors
    /**
     * Returns the origin
     * @return position to move from
     */
    public Position getOrigin()
    {
        return origin;
    }
    /**
     * Returns the target
     * @return position to move to
     */
    public Position getTarget()
    {
        return target;
    }
    /**
     * Returns whether the move was created in a valid way
     * @return validity of the move
     */
    public boolean isValid()
    {
        return valid;
    }
    /**
     * Checks if a given move has the same origin and target, returns true if they match
     * @param compare move to compare with
     * @return if the moves are the same
     */
    public boolean matches(Move compare)
    {
        if(origin.matches(compare.getOrigin()) && target.matches(compare.getTarget()))
        {
            return true;
        }
        return false;
    }
    /**
     * Converts to string in form F#-F#
     * @return move in string form
     */
    public String toString()
    {
        return "" + origin.getFile() + origin.getRow() + "-" + target.getFile() + target.getRow();
    }
}