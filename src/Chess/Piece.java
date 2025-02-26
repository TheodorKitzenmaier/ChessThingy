package Chess;

import java.util.ArrayList;

/**
 * Abstract base class for chess pieces. Contains variables for position and
 * color, as well as whether the piece has moved and its type index. The type
 * index is used to quickly check what type a piece is, and the current
 * convention is:<p>
 * King     0<p>
 * Pawn     1<p>
 * Bishop   2<p>
 * Knight   3<p>
 * Rook     4<p>
 * Queen    5<p>
 * Pieces have both 3x4 boolean matrix sprites for use in the console mode, as
 * well as polygon definitions for a 100x100 sprite for use in graphics mode.<p>
 * Child classes must override the getPossibleMoves(), getClone(), and
 * getDefendingMoves() methods to define the behavior of the piece.<p>
 * Something to note is that the position of the pieces are not bound to the
 * board they are on, so issues with either the board not updating the positions
 * or the positions being modified outside of the board can lead to desync
 * between where the piece thinks it is and its position on the board. This
 * leads to best practice being that methods that return pieces should call
 * getClone() instead. This prevents any desync which could cause pieces to
 * misrepresent what moves they are capable of.<p>
 * Created: 6/1/22
 * @author Theodor Kitzenmaier
 */
public abstract class Piece
{
    //instance variables, names are mostly self explanitory
    protected byte pIndex;//index of the piece, related to its type. replaced using sprite to identify pieces
    private char color;
    private boolean hasMoved;
    private Position position;
    private PieceSprite sprite;
    protected int[] innerPolyX;
    protected int[] outerPolyX;
    protected int[] innerPolyY;
    protected int[] outerPolyY;
    
    //constructors
    /**
     * Default constructor for Piece, creates an instance with parameters ('W', false, new Position('A', 1), new PieceSprite())
     */
    public Piece()
    {
        this('W', false, new Position('A', 1), new PieceSprite());
    }
    /**
     * Normal constructor for Piece
     * @param color color of the piece
     * @param hasMoved whether the piece has moved, should always be false unless promoting
     * @param position position the piece is being created in
     * @param sprite sprite for the piece to use
     */
    public Piece(char color, boolean hasMoved, Position position, PieceSprite sprite)
    {
        this.color = color;
        this.hasMoved = hasMoved;
        this.position = position;
        this.sprite = sprite;
    }
    
    //accessors
    /**
     * Returns the type of the piece
     * @return type as an index
     */
    public byte getIndex()
    {
        return pIndex;
    }
    /**
     * Returns the color of the piece
     * @return color of the piece
     */
    public char getColor()
    {
        return color;
    }
    /**
     * Returns whether the piece has moved
     * @return if the piece has moved
     */
    public boolean getMoved()
    {
        return hasMoved;
    }
    /**
     * Returns the position of the piece
     * @return position of the piece
     */
    public Position getPosition()
    {
        return position;
    }
    /**
     * Returns the sprite of the piece
     * @return sprite of the piece
     */
    public PieceSprite getSprite()
    {
        return sprite;
    }
    /**
     * Returns inner polygonX
     * @return inner polygonX
     */
    public int[] getInnerPolyX()
    {
        return innerPolyX;
    }
    /**
     * Returns outer polygonX
     * @return outer polygonX
     */
    public int[] getOuterPolyX()
    {
        return outerPolyX;
    }
    /**
     * Returns inner polygonY
     * @return inner polygonY
     */
    public int[] getInnerPolyY()
    {
        return innerPolyY;
    }
    /**
     * Returns outer polygonY
     * @return outer polygonY
     */
    public int[] getOuterPolyY()
    {
        return outerPolyY;
    }
    
    //mutators
    /**
     * Sets the position of the piece to the new position, and updates hasMoved
     * @param newPosition position to move the piece to
     */
    public void move(Position newPosition)
    {
        position = newPosition;
        hasMoved = true;
    }
    
    //utility
    /**
     * Returns the legal moves that this piece can make on a given board
     * @param board board to get legal moves on
     * @return array of legal moves
     */
    public Move[] getLegalMoves(Board board)
    {
        ArrayList<Move> legalMoves = new ArrayList<>();
        Move[] possibleMoves = this.getPossibleMoves(board);
        for(int moveIndex = 0; moveIndex < possibleMoves.length; moveIndex++)
        {
            Board boardClone = board.getClone();
            //boardClone.drawBoard();
            //System.out.println(possibleMoves[moveIndex]);
            boardClone.move(possibleMoves[moveIndex]);
            if(!boardClone.getCheck(this.getColor()))
            {
                legalMoves.add(possibleMoves[moveIndex]);
            }
            //this.move(possibleMoves[moveIndex].getOrigin());
            /*added this line of code to prevent issues with the piece not knowing
            where it actually is. this does mean that hasMoved will not properly
            display its state, but I can fix that later. maybe a form of move that
            marks the move as a testing move?*/
            //was fixed by making the test moves no longer act on the original instances of the pieces
        }
        Move[] result = new Move[legalMoves.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = legalMoves.get(index);
        }
        return result;
    }
    /**
     * Returns the defending moves that this piece can make on a given board,
     * accounts for if the move would cause the piece's king to be put in check.
     * @param board board to get legal moves on
     * @return array of legal moves
     */
    public Move[] getLegalDefenses(Board board)
    {
        ArrayList<Move> defendingMoves = new ArrayList<>();
        Move[] possibleMoves = this.getDefendingMoves(board);
        for(int moveIndex = 0; moveIndex < possibleMoves.length; moveIndex++)
        {
            Board boardClone = board.getClone();
            boardClone.move(possibleMoves[moveIndex]);
            if(!boardClone.getCheck(this.getColor()))
            {
                defendingMoves.add(possibleMoves[moveIndex]);
            }
        }
        Move[] result = new Move[defendingMoves.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = defendingMoves.get(index);
        }
        return result;
    }
    
    //abstracts
    /**
     * Returns the moves that lead to spaces on the board not occupied by an friendly piece, does not account for check
     * @param board board to get moves on
     * @return array of possible moves
     */
    public abstract Move[] getPossibleMoves(Board board);
    /**
     * Returns all the moves that would capture a piece of the same color
     * @param board board to find moves on
     * @return array of moves
     */
    public abstract Move[] getDefendingMoves(Board board);
    /**
     * Returns a clone of whatever piece was there.
     * @return new instance with the same color, hasMoved, sprite, and position
     */
    public abstract Piece getClone();
}
