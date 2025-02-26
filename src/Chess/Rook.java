package Chess;

import java.util.ArrayList;

/**
 * Desc: the rook
 * Date: 6/3/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class Rook extends Piece
{
    //static vars
    private static final boolean[][] rookSpriteArray =
    {{true, false, true},
    {true, true, true},
    {false, true, false},    
    {true, true, true}};
    private static final PieceSprite rookSprite = new PieceSprite(rookSpriteArray);
    
    //constructors
    /**
     * Default constructor for rook, creates a white rook on A1
     */
    public Rook()
    {
        this('W', false, new Position('A', 1));
    }
    /**
     * Standard constructor for rook, creates a rook of a given color at a given position
     * @param color color of the piece
     * @param hasMoved whether the piece has moved, should always be false unless promoting
     * @param position position the piece is being created in
     */
    public Rook(char color, boolean hasMoved, Position position)
    {
        super(color, hasMoved, position, rookSprite);
        innerPolyX = new int[]{45, 54, 54, 61, 61, 67, 67, 61, 70, 57, 41, 29, 38, 32, 32, 38, 38, 45};
        innerPolyY = new int[]{12, 12, 23, 22, 13, 16, 43, 46, 83, 87, 87, 83, 46, 44, 16, 13, 22, 23};
        outerPolyX = new int[]{42, 57, 65, 70, 70, 65, 73, 77, 77, 70, 59, 40, 29, 22, 22, 26, 34, 29, 29, 34};
        outerPolyY = new int[]{8, 8, 10, 14, 44, 48, 81, 83, 85, 89, 91, 91, 89, 85, 83, 81, 48, 44, 14, 10};
        pIndex = 4;
    }
    
    //accessors
    /**
     * Returns the rook sprite
     * @return rook sprite
     */
    public static PieceSprite getRookSprite()
    {
        return rookSprite;
    }
    
    //misc
    /**
     * Returns a clone of this piece
     * @return returns a new instance with the same color, hasMoved, and position
     */
    public Piece getClone()
    {
        return new Rook(this.getColor(), this.getMoved(), this.getPosition());
    }
    /**
     * Returns the array of moves that are on the board and not capturing a friendly piece, does not account for check
     * @param board board to get possible moves on
     * @return array of possible moves
     */
    public Move[] getPossibleMoves(Board board)
    {
        ArrayList<Move> moves = new ArrayList<>();
        //up
        int distance = 1;
        while(true)
        {
            if(this.getPosition().getRow() + distance >8)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() + distance);
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
            distance++;
        }
        //right
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() + distance > 8)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + distance), this.getPosition().getRow());
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
            distance++;
        }
        //down
        distance = 1;
        while(true)
        {
            if(this.getPosition().getRow() - distance < 1)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() - distance);
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
            distance++;
        }
        //left
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() - distance < 1)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - distance), this.getPosition().getRow());
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
        //up
        int distance = 1;
        while(true)
        {
            if(this.getPosition().getRow() + distance >8)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() + distance);
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
            distance++;
        }
        //right
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() + distance > 8)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + distance), this.getPosition().getRow());
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
            distance++;
        }
        //down
        distance = 1;
        while(true)
        {
            if(this.getPosition().getRow() - distance < 1)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() - distance);
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
            distance++;
        }
        //left
        distance = 1;
        while(true)
        {
            if(this.getPosition().getColumn() - distance < 1)
            {
                break;
            }
            else
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - distance), this.getPosition().getRow());
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
