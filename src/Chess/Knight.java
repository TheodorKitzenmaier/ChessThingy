package Chess;

import java.util.ArrayList;

/**
 * Desc: the knight
 * Date: 6/3/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class Knight extends Piece
{
    private static final boolean[][] knightSpriteArray = 
    {{true, true, true},
    {true, false, false},
    {true, true, false},
    {true, true, true}};
    private static final PieceSprite knightSprite = new PieceSprite(knightSpriteArray);
    
    //Constructors
    /**
     * Default constructor for knight, which creates a white king on A1
     */
    public Knight()
    {
        this('W', false, new Position('A', 1));
    }
    /**
     * Standard constructor for knight, which creates a knight of a given color at a given position
     * @param color color of the piece
     * @param hasMoved whether the piece has moved, should always be false unless promoting
     * @param position position the piece is being created in
     */
    public Knight(char color, boolean hasMoved, Position position)
    {
        super(color, hasMoved, position, knightSprite);
        innerPolyX = new int[]{57, 53, 62, 77, 75, 61, 53, 51, 64, 68, 63, 64, 73, 58, 41, 25, 32, 34, 27, 32, 26, 41};
        innerPolyY = new int[]{15, 26, 33, 36, 41, 43, 40, 46, 55, 64, 67, 79, 82, 86, 86, 81, 79, 73, 65, 53, 49, 20};
        outerPolyX = new int[]{62, 56, 65, 81, 78, 61, 68, 72, 66, 68, 77, 77, 59, 40, 22, 22, 29, 31, 22, 28, 22, 34, 42};
        outerPolyY = new int[]{13, 24, 30, 33, 44, 46, 54, 66, 70, 77, 80, 84, 89, 89, 84, 80, 77, 73, 66, 55, 51, 22, 14};
        pIndex = 3;
    }
    
    //accessor
    public static PieceSprite getKnightSprite()
    {
        return knightSprite;
    }
    
    //misc
    /**
     * Returns a clone of whatever piece was there
     * @return new instance with the same color, hasMoved, and position
     */
    public Piece getClone()
    {
        return new Knight(this.getColor(), this.getMoved(), this.getPosition());
    }
    /**
     * Returns the array of moves that are on the board and not capturing a friendly piece. Does not account for being in check
     * @param board board to get moves on
     * @return array of possible moves
     */
    public Move[] getPossibleMoves(Board board)
    {
        ArrayList<Move> moves = new ArrayList<>();
        //up
        if(this.getPosition().getRow() + 2 <= 8)
        {
            if(this.getPosition().getColumn() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getColumn() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //down
        if(this.getPosition().getRow() - 2 >= 1)
        {
            if(this.getPosition().getColumn() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getColumn() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //left
        if(this.getPosition().getColumn() - 2 >= 1)
        {
            if(this.getPosition().getRow() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 2), this.getPosition().getRow() + 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getRow() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 2), this.getPosition().getRow() - 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //right
        if(this.getPosition().getColumn() + 2 <= 8)
        {
            if(this.getPosition().getRow() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 2), this.getPosition().getRow() + 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getRow() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 2), this.getPosition().getRow() - 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //converts arraylist to array
        Move[] result = new Move[moves.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = moves.get(index);
        }
        return result;
    }
    public Move[] getDefendingMoves(Board board)
    {
        ArrayList<Move> moves = new ArrayList<>();
        //up
        if(this.getPosition().getRow() + 2 <= 8)
        {
            if(this.getPosition().getColumn() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getColumn() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //down
        if(this.getPosition().getRow() - 2 >= 1)
        {
            if(this.getPosition().getColumn() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getColumn() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 2);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //left
        if(this.getPosition().getColumn() - 2 >= 1)
        {
            if(this.getPosition().getRow() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 2), this.getPosition().getRow() + 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getRow() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 2), this.getPosition().getRow() - 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //right
        if(this.getPosition().getColumn() + 2 <= 8)
        {
            if(this.getPosition().getRow() + 1 <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 2), this.getPosition().getRow() + 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
            if(this.getPosition().getRow() - 1 >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 2), this.getPosition().getRow() - 1);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
            }
        }
        //converts arraylist to array
        Move[] result = new Move[moves.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = moves.get(index);
        }
        return result;
    }
}
