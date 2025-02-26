package Chess;

import java.util.ArrayList;

/**
 * Desc: The pawn
 * Date: 6/6/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class Pawn extends Piece
{
    //static vars
    private static final boolean[][] PAWN_SPRITE_ARRAY = 
    {{false, false, false},
    {false, true, false},
    {false, true, false},
    {true, true, true}};
    private static final PieceSprite PAWN_SPRITE = new PieceSprite(PAWN_SPRITE_ARRAY);
    
    //constructors
    /**
     * Default constructor for pawn, creates a white bishop at A1
     */
    public Pawn()
    {
        this('W', false, new Position('A', 1));
    }
    /**
     * Standard constructor for pawn, creates a pawn of a given color at a given position
     * @param color color of the piece
     * @param hasMoved whether the piece has moved, should always be false unless promoting
     * @param position position the piece is being created in
     */
    public Pawn(char color, boolean hasMoved, Position position)
    {
        super(color, hasMoved, position, PAWN_SPRITE);
        innerPolyX = new int[]{47, 52, 58, 61, 61, 58, 54, 56, 59, 57, 71, 76, 57, 43, 23, 28, 42, 40, 43, 45, 38, 38, 41};
        innerPolyY = new int[]{11, 11, 14, 20, 25, 31, 33, 45, 46, 48, 77, 79, 85, 85, 79, 77, 48, 46, 45, 33, 25, 20, 14};
        outerPolyX = new int[]{46, 53, 58, 62, 64, 64, 61, 57, 59, 64, 64, 60, 73, 76, 80, 78, 66, 58, 41, 33, 21, 19, 23, 26, 39, 35, 35, 40, 42, 38, 35, 35, 37, 41};
        outerPolyY = new int[]{8, 8, 10, 14, 20, 25, 32, 35, 43, 44, 47, 49, 74, 75, 79, 83, 88, 89, 89, 88, 83, 79, 75, 74, 49, 47, 44, 43, 35, 32, 26, 19, 14, 10};
        pIndex = 1;
    }
    
    //accessors
    public static PieceSprite getPawnSprite()
    {
        return PAWN_SPRITE;
    }
    
    //misc
    /**
     * Returns a clone of this piece
     * @return new instance with the same color, hasMoved, and position
     */
    public Piece getClone()
    {
        return new Pawn(this.getColor(), this.getMoved(), this.getPosition());
    }
    /**
     * Returns the array of moves that are on the board and not capturing a friendly piece, does not account for check
     * @param board board to get moves on
     * @return array of possible moves
     */
    public Move[] getPossibleMoves(Board board)
    {
        ArrayList<Move> moves = new ArrayList<>();
        int moveDirection = 1;
        if(this.getColor() == 'B')
        {
            moveDirection = -1;
        }
        //forward
        if(this.getPosition().getRow() + moveDirection <= 8 && this.getPosition().getRow() + moveDirection >= 1)
        {
            Position targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() + moveDirection);
            if(board.getPiece(targetPosition) == null)
            {
                moves.add(new Move(this.getPosition(), targetPosition));
                if(!this.getMoved())//double first move
                {
                    targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() + moveDirection + moveDirection);
                    if(board.getPiece(targetPosition) == null)
                    {
                        moves.add(new Move(this.getPosition(), targetPosition));
                    }
                }
            }
            //right
            if(this.getPosition().getColumn() + 1 <= 8)
            {
                targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece != null)
                {
                    if(targetPiece.getColor() != this.getColor())
                    {
                        moves.add(new Move(this.getPosition(), targetPosition));
                    }
                }
            }
            //left
            if(this.getPosition().getColumn() - 1 >= 1)
            {
                targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece != null)
                {
                    if(targetPiece.getColor() != this.getColor())
                    {
                        moves.add(new Move(this.getPosition(), targetPosition));
                    }
                }
            }
        }
        //French move
        if(this.getPosition().getRow() == (int)(4.5 + (double)moveDirection * .5))//5 or 4
        {
            if(this.getPosition().getColumn() - 1 >= 1)//left
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                Piece passedPiece = board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow()));
                if(passedPiece != null && targetPiece == null)
                {
                    if(passedPiece.getColor() != this.getColor() && passedPiece.getIndex() == 1)
                    {
                        if(passedPiece.getPosition().matches(board.getLastMove().getTarget()) && Math.abs(board.getLastMove().getTarget().getRow() - board.getLastMove().getOrigin().getRow()) == 2)
                        {
                            moves.add(new Move(this.getPosition(), targetPosition));
                        }
                    }
                }
            }
            if(this.getPosition().getColumn() + 1 <= 8)//right
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                Piece passedPiece = board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow()));
                if(passedPiece != null && targetPiece == null)
                {
                    if(passedPiece.getColor() != this.getColor() && passedPiece.getIndex() == 1)
                    {
                        if(passedPiece.getPosition().matches(board.getLastMove().getTarget()) && Math.abs(board.getLastMove().getTarget().getRow() - board.getLastMove().getOrigin().getRow()) == 2)
                        {
                            moves.add(new Move(this.getPosition(), targetPosition));
                        }
                    }
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
        int moveDirection = 1;
        if(this.getColor() == 'B')
        {
            moveDirection = -1;
        }
        //forward
        if(this.getPosition().getRow() + moveDirection <= 8 && this.getPosition().getRow() + moveDirection >= 1)
        {
            Position targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() + moveDirection);
            /*
            if(board.getPiece(targetPosition) == null)
            {
                moves.add(new Move(this.getPosition(), targetPosition));
                if(!this.getMoved())//double first move
                {
                    targetPosition = new Position(this.getPosition().getFile(), this.getPosition().getRow() + moveDirection + moveDirection);
                    if(board.getPiece(targetPosition) == null)
                    {
                        moves.add(new Move(this.getPosition(), targetPosition));
                    }
                }
            }
            */
            //right
            if(this.getPosition().getColumn() + 1 <= 8)
            {
                targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece != null)
                {
                    if(targetPiece.getColor() == this.getColor())
                    {
                        moves.add(new Move(this.getPosition(), targetPosition));
                    }
                }
            }
            //left
            if(this.getPosition().getColumn() - 1 >= 1)
            {
                targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                if(targetPiece != null)
                {
                    if(targetPiece.getColor() == this.getColor())
                    {
                        moves.add(new Move(this.getPosition(), targetPosition));
                    }
                }
            }
        }
        //French move
        /*
        if(this.getPosition().getRow() == (int)(4.5 + (double)moveDirection * .5))//5 or 4
        {
            if(this.getPosition().getColumn() - 1 >= 1)//left
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                Piece passedPiece = board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow()));
                if(passedPiece != null && targetPiece == null)
                {
                   if(passedPiece.getColor() != this.getColor() && passedPiece.getIndex() == 1)
                   {
                       moves.add(new Move(this.getPosition(), targetPosition));
                   }
                }
            }
            if(this.getPosition().getColumn() + 1 <= 8)//right
            {
                Position targetPosition = new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + moveDirection);
                Piece targetPiece = board.getPiece(targetPosition);
                Piece passedPiece = board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow()));
                if(passedPiece != null && targetPiece == null)
                {
                   if(passedPiece.getColor() != this.getColor() && passedPiece.getIndex() == 1)
                   {
                       moves.add(new Move(this.getPosition(), targetPosition));
                   }
                }
            }
        }
        */
        //converts arraylist to array
        Move[] result = new Move[moves.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = moves.get(index);
        }
        return result;
    }
}
