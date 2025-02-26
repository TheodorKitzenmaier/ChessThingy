package Chess;

import java.util.ArrayList;

/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: the king
 */
public class King extends Piece
{
    //static vars
    private static final boolean[][] kingSpriteMatrix = 
    {{false, true, false},
    {true, true, true},
    {false, true, false},
    {false, true, false}};
    private static final PieceSprite kingSprite = new PieceSprite(kingSpriteMatrix);
    
    //constructors
    /**
     * Default constructor for king, creates a white king on A1
     */
    public King()
    {
        this('W', false, new Position('A', 1));
    }
    /**
     * Standard constructor for king, creates a king of a given color at a given position
     * @param color color of the piece
     * @param hasMoved whether the piece has moved, should always be false unless promoting
     * @param position position the piece is being created in
     */
    public King(char color, boolean hasMoved, Position position)
    {
        super(color, hasMoved, position, kingSprite);
        innerPolyX = new int[]{48, 51, 51, 58, 58, 51, 51, 58, 55, 63, 54, 45, 36, 44, 41, 48, 48, 41, 41, 48};
        innerPolyY = new int[]{7, 7, 14, 14, 17, 17, 35, 38, 41, 81, 83, 83, 81, 41, 38, 35, 17, 17, 14, 14};
        outerPolyX = new int[]{46, 53, 53, 60, 60, 53, 53, 59, 61, 58, 66, 69, 69, 57, 42, 30, 30, 33, 41, 38, 40, 46, 46, 39, 39, 46};
        outerPolyY = new int[]{5, 5, 12, 12, 19, 19, 34, 36, 39, 42, 78, 80, 82, 86, 86, 82, 80, 78, 42, 39, 36, 34, 19, 19, 12, 12};
        pIndex = 0;
    }
    
    //accessors
    /**
     * Returns the default king sprite
     * @return king's sprite
     */
    public static PieceSprite getKingSprite()
    {
        return kingSprite;
    }
    
    //misc
    /**
     * Returns a clone of whatever piece was there
     * @return new instance with the same color, hasMoved, and position
     */
    public Piece getClone()
    {
        return new King(this.getColor(), this.getMoved(), this.getPosition());
    }
    /**
     * Returns the array of moves that are on the board and not capturing a friendly piece, does not account for check
     * @param board board to get moves on
     * @return array of possible moves
     */
    public Move[] getPossibleMoves(Board board)//adds moves to an arraylist if the given spot is empty or contains an opponent's piece
    {
        ArrayList<Move> moves = new ArrayList<>();
        //up
        if(this.getPosition().getRow() + 1 <= 8)//checks if the move is on the board
        {
            if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)) == null)//checks if the space is empty
            {
                moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)));//adds move
            }
            else if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)).getColor() != this.getColor())//checks if the space does not contain a piece of the same color
            {
                moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)));//adds move
            }
        }
        //upright
        if(this.getPosition().getRow() + 1 <= 8 && this.getPosition().getColumn() + 1 <= 8)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)) == null)
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)).getColor() != this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)));
            }
        }
        //right
        if(this.getPosition().getColumn() + 1 <= 8)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())) == null)
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())).getColor() != this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())));
            }
        }
        //downright
        if(this.getPosition().getRow() - 1 >= 1 && this.getPosition().getColumn() + 1 <= 8)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)) == null)
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)).getColor() != this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)));
            }
        }
        //down
        if(this.getPosition().getRow() - 1 >= 1)
        {
            if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)) == null)
            {
                moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)));
            }
            else if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)).getColor() != this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)));
            }
        }
        //downleft
        if(this.getPosition().getRow() - 1 >= 1 && this.getPosition().getColumn() - 1 >= 1)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)) == null)
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)).getColor() != this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)));
            }
        }
        //left
        if(this.getPosition().getColumn() - 1 >= 1)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())) == null)
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())).getColor() != this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())));
            }
        }
        //upleft
        if(this.getPosition().getRow() + 1 <= 8 && this.getPosition().getColumn() - 1 >= 1)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)) == null)
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)).getColor() != this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)));
            }
        }
        //castle
        if(!this.getMoved() && !board.getCheck(this.getColor()))
        {
            //right
            if(board.getPiece(new Position('H', this.getPosition().getRow())) != null)
            {
                if(board.getPiece(new Position('H', this.getPosition().getRow())).getIndex() == 4 && !board.getPiece(new Position('H', this.getPosition().getRow())).getMoved())//is a rook and has not moved
                {
                    if(board.getPiece(new Position('F', this.getPosition().getRow())) == null && board.getPiece(new Position('G', this.getPosition().getRow())) == null)//path is clear
                    {
                        Board clone = board.getClone();
                        clone.move(new Move(new Position('E', this.getPosition().getRow()), new Position('F', this.getPosition().getRow())));
                        if(!clone.getCheck(this.getColor()))
                            moves.add(new Move(this.getPosition(), new Position('G', this.getPosition().getRow())));
                    }
                }
            }
            //left
            if(board.getPiece(new Position('A', this.getPosition().getRow())) != null)
            {
                if(board.getPiece(new Position('A', this.getPosition().getRow())).getIndex() == 4 && !board.getPiece(new Position('A', this.getPosition().getRow())).getMoved())//is a rook and has not moved
                {
                    if(board.getPiece(new Position('B', this.getPosition().getRow())) == null && board.getPiece(new Position('C', this.getPosition().getRow())) == null && board.getPiece(new Position('D', this.getPosition().getRow())) == null)//path is clear
                    {
                        Board clone = board.getClone();
                        clone.move(new Move(new Position('E', this.getPosition().getRow()), new Position('D', this.getPosition().getRow())));
                        if(!clone.getCheck(this.getColor()))
                            moves.add(new Move(this.getPosition(), new Position('C', this.getPosition().getRow())));
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
    public Move[] getDefendingMoves(Board board)//adds moves to an arraylist if the given spot is empty or contains an opponent's piece
    {
        ArrayList<Move> moves = new ArrayList<>();
        //up
        if(this.getPosition().getRow() + 1 <= 8)//checks if the move is on the board
        {
            if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)) == null)//checks if the space is empty
            {
                //moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)));//adds move
            }
            else if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)).getColor() == this.getColor())//checks if the space does not contain a piece of the same color
            {
                moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() + 1)));//adds move
            }
        }
        //upright
        if(this.getPosition().getRow() + 1 <= 8 && this.getPosition().getColumn() + 1 <= 8)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)) == null)
            {
                //moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)).getColor() == this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() + 1)));
            }
        }
        //right
        if(this.getPosition().getColumn() + 1 <= 8)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())) == null)
            {
                //moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())).getColor() == this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow())));
            }
        }
        //downright
        if(this.getPosition().getRow() - 1 >= 1 && this.getPosition().getColumn() + 1 <= 8)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)) == null)
            {
                //moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)).getColor() == this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() + 1), this.getPosition().getRow() - 1)));
            }
        }
        //down
        if(this.getPosition().getRow() - 1 >= 1)
        {
            if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)) == null)
            {
                //moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)));
            }
            else if(board.getPiece(new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)).getColor() == this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(this.getPosition().getFile(), this.getPosition().getRow() - 1)));
            }
        }
        //downleft
        if(this.getPosition().getRow() - 1 >= 1 && this.getPosition().getColumn() - 1 >= 1)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)) == null)
            {
                //moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)).getColor() == this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() - 1)));
            }
        }
        //left
        if(this.getPosition().getColumn() - 1 >= 1)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())) == null)
            {
                //moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())).getColor() == this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow())));
            }
        }
        //upleft
        if(this.getPosition().getRow() + 1 <= 8 && this.getPosition().getColumn() - 1 >= 1)
        {
            if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)) == null)
            {
                //moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)));
            }
            else if(board.getPiece(new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)).getColor() == this.getColor())
            {
                moves.add(new Move(this.getPosition(), new Position(Position.columnToFile(this.getPosition().getColumn() - 1), this.getPosition().getRow() + 1)));
            }
        }
        //castle
        /*
        if(!this.getMoved())
        {
            //right
            if(board.getPiece(new Position('H', this.getPosition().getRow())) != null)
            {
                if(board.getPiece(new Position('H', this.getPosition().getRow())).getSprite().matches(Rook.getRookSprite()) && !board.getPiece(new Position('H', this.getPosition().getRow())).getMoved())//is a rook and has not moved
                {
                    if(board.getPiece(new Position('F', this.getPosition().getRow())) == null && board.getPiece(new Position('G', this.getPosition().getRow())) == null)//path is clear
                    {
                        moves.add(new Move(this.getPosition(), new Position('G', this.getPosition().getRow())));
                    }
                }
            }
            //left
            if(board.getPiece(new Position('A', this.getPosition().getRow())) != null)
            {
                if(board.getPiece(new Position('A', this.getPosition().getRow())).getSprite().matches(Rook.getRookSprite()) && !board.getPiece(new Position('A', this.getPosition().getRow())).getMoved())//is a rook and has not moved
                {
                    if(board.getPiece(new Position('B', this.getPosition().getRow())) == null && board.getPiece(new Position('C', this.getPosition().getRow())) == null && board.getPiece(new Position('D', this.getPosition().getRow())) == null)//path is clear
                    {
                        moves.add(new Move(this.getPosition(), new Position('C', this.getPosition().getRow())));
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
