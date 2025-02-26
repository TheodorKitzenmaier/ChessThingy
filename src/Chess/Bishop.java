package Chess;

import java.util.ArrayList;

/**
 * Desc: The bishop
 * Date: 6/5/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class Bishop extends Piece
{
    //static vars
    private static final boolean[][] BISHOP_SPRITE_ARRAY = 
    {{false, true, false},
    {false, true, false},
    {false, true, false},
    {true, true, true}};
    private static final PieceSprite BISHOP_SPRITE = new PieceSprite(BISHOP_SPRITE_ARRAY);
    
    //constructors
    /**
     * Default constructor for bishop, creates a white bishop at A1
     */
    public Bishop()
    {
        this('W', false, new Position('A', 1));
    }
    /**
     * Standard constructor for bishop, creates a bishop of a given color at a given position
     * @param color color of the piece
     * @param hasMoved whether the piece has moved, should always be false unless promoting
     * @param position position the piece is being created in
     */
    public Bishop(char color, boolean hasMoved, Position position)
    {
        super(color, hasMoved, position, BISHOP_SPRITE);
        innerPolyX = new int[]{50, 55, 48, 57, 58, 56, 61, 56, 67, 59, 39, 31, 43, 38, 43, 41, 41, 46};
        innerPolyY = new int[]{11, 15, 36, 22, 31, 40, 42, 45, 80, 84, 84, 80, 45, 42, 40, 31, 23, 14};
        outerPolyX = new int[]{49, 52, 52, 57, 61, 61, 59, 64, 64, 59, 69, 72, 67, 56, 43, 32, 27, 30, 40, 35, 35, 40, 38, 38, 42, 48, 47};
        outerPolyY = new int[]{3, 5, 8, 11, 22, 32, 38, 41, 44, 47, 75, 79, 85, 88, 88, 85, 79, 74, 47, 45, 41, 38, 33, 22, 12, 8, 5};
        pIndex = 2;
    }
    
    //accessor
    /**
     * Returns the bishop's sprite
     * @return bishop's sprite
     */
    public static PieceSprite getBishopSprite()
    {
        return BISHOP_SPRITE;
    }
    
    //misc
    /**
     * Returns a clone of this piece
     * @return new instance with the same color, hasMoved, and position
     */
    public Piece getClone()
    {
        return new Bishop(this.getColor(), this.getMoved(), this.getPosition());
    }
    /**
     * Returns the array of moves that are on the board and not capturing a friendly piece
     * @param board board to get moves on
     * @return array of possible moves
     */
    public Move[] getPossibleMoves(Board board)
    {
        ArrayList<Move> moves = new ArrayList<>();
        //upRight
        int distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() + distance <= 8 && this.getPosition().getRow() + distance <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + distance), this.getPosition().getRow() + distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
        }
        //downRight
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() + distance <= 8 && this.getPosition().getRow() - distance >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + distance), this.getPosition().getRow() - distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
        }
        //downLeft
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() - distance >= 1 && this.getPosition().getRow() - distance >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - distance), this.getPosition().getRow() - distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
        }
        //upLeft
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() - distance >= 1 && this.getPosition().getRow() + distance <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - distance), this.getPosition().getRow() + distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() != this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
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
        //upRight
        int distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() + distance <= 8 && this.getPosition().getRow() + distance <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + distance), this.getPosition().getRow() + distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
        }
        //downRight
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() + distance <= 8 && this.getPosition().getRow() - distance >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + distance), this.getPosition().getRow() - distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
        }
        //downLeft
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() - distance >= 1 && this.getPosition().getRow() - distance >= 1)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - distance), this.getPosition().getRow() - distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
        }
        //upLeft
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() - distance >= 1 && this.getPosition().getRow() + distance <= 8)
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - distance), this.getPosition().getRow() + distance);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece == null)
                {
                    //moves.add(new Move(this.getPosition(), targetPosition));
                }
                else if(targetPiece.getColor() == this.getColor())
                {
                    moves.add(new Move(this.getPosition(), targetPosition));
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
            distance++;
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
