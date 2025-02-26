package Chess;

import java.util.ArrayList;

/**
 * Desc: The queen
 * Date: 6/5/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class Queen extends Piece
{
    //static vars
    private static final boolean[][] QUEEN_SPRITE_ARRAY = 
    {{true, true, true},
    {true, true, true},
    {false, true, false},
    {true, true, true}};
    private static final PieceSprite QUEEN_SPRITE = new PieceSprite(QUEEN_SPRITE_ARRAY);
    
    //constructors
    /**
     * Default constructor for queen, creates a white bishop at A1
     */
    public Queen()
    {
        this('W', false, new Position('A', 1));
    }
    /**
     * Standard constructor for queen, creates a queen of a given color at a given position
     * @param color color of the piece
     * @param hasMoved whether the piece has moved, should always be false unless promoting
     * @param position position the piece is being created in
     */
    public Queen(char color, boolean hasMoved, Position position)
    {
        super(color, hasMoved, position, QUEEN_SPRITE);
        innerPolyX = new int[]{45, 54, 62, 62, 56, 56, 61, 55, 57, 61, 58, 63, 68, 63, 57, 43, 36, 31, 36, 41, 38, 42, 44, 38, 43, 43, 37, 37};
        innerPolyY = new int[]{12, 12, 17, 20, 24, 28, 30, 33, 50, 54, 55, 80, 83, 86, 87, 87, 86, 83, 80, 55, 54, 50, 33, 30, 28, 24, 20, 17};
        outerPolyX = new int[]{45, 54, 62, 65, 65, 61, 59, 64, 66, 61, 58, 60, 65, 63, 62, 66, 71, 71, 67, 58, 42, 33, 28, 28, 33, 38, 36, 34, 39, 41, 38, 33, 35, 40, 38, 34, 34, 37};
        outerPolyY = new int[]{9, 9, 13, 17, 20, 25, 26, 28, 31, 34, 34, 50, 53, 56, 56, 78, 82, 85, 88, 90, 90, 88, 85, 82, 78, 56, 56, 53, 50, 34, 34, 31, 28, 26, 25, 20, 17, 13};
        pIndex = 5;
    }
    
    //accessor
    /**
     * Returns the queen's sprite
     * @return queen's sprite
     */
    public static PieceSprite getQueenSprite()
    {
        return QUEEN_SPRITE;
    }
    
    //misc
    /**
     * Returns a clone of this piece
     * @return new instance with the same color, hasMoved, and position
     */
    public Piece getClone()
    {
        return new Queen(this.getColor(), this.getMoved(), this.getPosition());
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
        distance = 1;
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
        distance = 1;
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
