package Chess;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;

/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: contains the pieces and handles moving logic
 */
public class Board
{
    //instance variables
    private Piece[][] pieces;
    private Move lastMove;
    
    //constructors
    /**
     * Default constructor for a board, creates a board in its starting position
     */
    public Board()
    {
        pieces = new Piece[8][];
        resetBoard();
    }
    /**
     * Copy constructor for a board, creates a board with the same pieces as in the matrix
     * @param board matrix of pieces to clone
     */
    public Board(Piece[][] board)
    {
        pieces = new Piece[8][];
        for(int rowIndex = 0; rowIndex < 8; rowIndex++)
        {
            pieces[rowIndex] = new Piece[8];
            for(int columnIndex = 0; columnIndex < 8; columnIndex++)
            {
                Piece pieceTemplate = board[rowIndex][columnIndex];
                if(pieceTemplate == null)
                {
                    pieces[rowIndex][columnIndex] = null;
                }
                else
                {
                    pieces[rowIndex][columnIndex] = pieceTemplate.getClone();
                }
            }
        }
    }
    
    //accessors
    /**
     * Returns the piece at a given position
     * @param position position to get piece at
     * @return piece at the given position
     */
    public Piece getPiece(Position position)
    {
        return pieces[position.getRowIndex()][position.getColumnIndex()];
    }
    /**
     * Returns the original matrix of pieces
     * @return matrix of pieces
     */
    public Piece[][] getPieces()
    {
        return pieces;
    }
    public Move getLastMove()
    {
        return lastMove;
    }
    
    //mutators
    /**
     * Allows for the setting of a piece on a board, used for promotions
     * @param piece piece to set
     * @param position position to put the piece
     */
    public void setPiece(Piece piece, Position position)
    {
        pieces[position.getRowIndex()][position.getColumnIndex()] = piece;
    }
    public void setMove(Move move)
    {
        lastMove = move;
    }
    
    //misc
    /**
     * Returns a new board that contains the pieces on the current board
     * @return new board instance with new piece instances
     */
    public Board getClone()
    {
        Board clone = new Board(pieces);
        clone.setMove(getLastMove());
        return clone;
    }
    /**
     * Returns if the given color is in check
     * @param color color to check for check
     * @return if the given color is in check
     */
    public boolean getCheck(char color)
    {
        //get enemy pieces to consider if they put the king in check
        Piece king = new King(color, false, new Position('A', -1));
        //ArrayList<Piece> enemyPieces = new ArrayList<>();
        for(int row = 0; row < 8; row++)//find the king
        {
            for(int column = 0; column < 8; column++)
            {
                if(pieces[row][column] != null)
                {
                    /*
                    if(pieces[row][column].getColor() != color)
                    {
                        enemyPieces.add(pieces[row][column]);
                    }
                    */
                    if(pieces[row][column].getColor() == color)
                    {
                        if(pieces[row][column].getIndex() == 0)//0 is the king's index
                        {
                            king = (King)pieces[row][column];
                        }
                    }
                }
            }
        }
        /*
        //checks if any of the pieces' possible moves take the king
        for(int pieceIndex = 0; pieceIndex < enemyPieces.size(); pieceIndex++)
        {
            Move[] possibleMoves = (enemyPieces.get(pieceIndex)).getPossibleMoves(this);
            for(int moveIndex = 0; moveIndex < possibleMoves.length; moveIndex++)
            {
                if(possibleMoves[moveIndex].getTarget().matches(king.getPosition()))
                {
                    return true;
                }
            }
        }
        return false;
        */
        
        //directions the king can be attacked from
        int[] xDirection = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] yDirection = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] xOffset = {-1, 1, 2, 2, 1, -1, -2, -2};
        int[] yOffset = {2, 2, 1, -1, -2, -2, -1, 1};
        Position kingPosition = king.getPosition();
        
        //increment through all the places the king could be attacked from
        for(int index = 0; index < 8; index++)
        {
            //pieces on the major axis
            for(int distance = 1; ((kingPosition.getColumn() + distance * xDirection[index] >= 1) && (kingPosition.getColumn() + distance * xDirection[index] <= 8)) && ((kingPosition.getRow() + distance * yDirection[index] >= 1) && (kingPosition.getRow() + distance * yDirection[index] <= 8)); distance++)//position is in bounds
            {
                Position querryPosition = new Position(Position.columnToFile(kingPosition.getColumn() + distance * xDirection[index]), kingPosition.getRow() + distance * yDirection[index]);//position to check
                Piece querryPiece = getPiece(querryPosition);//piece at that position
                if(querryPiece != null)
                {
                    if(querryPiece.getColor() != color && querryPiece.getIndex() != 3 && querryPiece.getIndex() != 0)//the piece is not friendly and not a knight, since knights can't attack along axis
                    {
                        Move[] querryMoves = querryPiece.getPossibleMoves(this);
                        for(int moveIndex = 0; moveIndex < querryMoves.length; moveIndex++)//check all of the piece's moves
                        {
                            if(querryMoves[moveIndex].getTarget().matches(kingPosition))
                            {
                                return true;
                            }
                        }
                    }
                    break;
                }
            }
            //possible knights
            if((kingPosition.getRow() + yOffset[index] <= 8 && kingPosition.getRow() + yOffset[index] >= 1) && (kingPosition.getColumn() + xOffset[index] <= 8 && kingPosition.getColumn() + xOffset[index] >= 1))//position is in bounds
            {
                Position querryPosition = new Position(Position.columnToFile(kingPosition.getColumn() + xOffset[index]), kingPosition.getRow() + yOffset[index]);//position to check
                Piece querryPiece = getPiece(querryPosition);//piece to check
                if(querryPiece != null)
                {
                    if(querryPiece.getColor() != color && querryPiece.getIndex() == 3)//piece is not frienly and is a knight
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Returns if the given color is in checkmate
     * @param color color to check for checkmate
     * @return if the given color is in checkmate
     */
    public boolean getCheckmate(char color)
    {
        //if king is in check and has no legal moves, checkmate
        if(getCheck(color))
        {
            //get my pieces
            ArrayList<Piece> myPieces = new ArrayList<>();
            for(int row = 0; row < 8; row++)
            {
                for(int column = 0; column < 8; column++)
                {
                    if(pieces[row][column] != null)
                    {
                        if(pieces[row][column].getColor() == color)
                        {
                            myPieces.add(pieces[row][column]);
                        }
                    }
                }
            }
            for(int pieceIndex = 0; pieceIndex < myPieces.size(); pieceIndex++)
            {
                if(myPieces.get(pieceIndex).getLegalMoves(this).length != 0)
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    /**
     * Returns if the given color has no moves
     * @param color color to check if it has no moves
     * @return if the given color has no moves
     */
    public boolean getDraw(char color)
    {
        //find pieces
        ArrayList myPieces = new ArrayList();
        for(int row = 0; row < 8; row++)
        {
            for(int column = 0; column < 8; column++)
            {
                if(pieces[row][column] != null)
                {
                    if(pieces[row][column].getColor() == color)
                    {
                        myPieces.add(pieces[row][column]);
                    }
                }
            }
        }
        //check if they all have no legal moves
        for(int pieceIndex = 0; pieceIndex < myPieces.size(); pieceIndex++)
        {
            Move[] pieceMoves = ((Piece)myPieces.get(pieceIndex)).getLegalMoves(this);
            if(pieceMoves.length != 0)
            {
                return false;
            }
        }
        return true;
    }
    /**
     * Returns if the given move is legal
     * @param move move to check
     * @return if the move is legal
     */
    public boolean moveIsLegal(Move move)
    {
        Piece movingPiece = this.getPiece(move.getOrigin());
        Move[] legalMoves = movingPiece.getLegalMoves(this);
        for(int moveIndex = 0; moveIndex < legalMoves.length; moveIndex++)
        {
            if(legalMoves[moveIndex].matches(move))
            {
                return true;
            }
        }
        return false;
    }
    /**
     * moves a piece
     * @param move move to make
     */
    public void move(Move move)
    {
        Position origin = move.getOrigin();
        Position target = move.getTarget();
        //check for enpassant
        if(this.getPiece(origin).getIndex() == 1)//if it is a pawn, continue, if it isn't, enpassan't
        {
            if(origin.getColumn() != target.getColumn())//the pawn moved as if taking
            {
                if(this.getPiece(target) == null)//this is an enpassant
                {
                    pieces[origin.getRowIndex()][target.getColumnIndex()] = null;
                }
            }
        }
        pieces[target.getRowIndex()][target.getColumnIndex()] = pieces[origin.getRowIndex()][origin.getColumnIndex()];
        pieces[origin.getRowIndex()][origin.getColumnIndex()] = null;
        pieces[target.getRowIndex()][target.getColumnIndex()].move(target);
        //check if it is a castle
        if(this.getPiece(target).getIndex() == 0)
        {
            Move[] rookMoves = 
            {
                new Move("A1-D1"),
                new Move("H1-F1"),
                new Move("A8-D8"),
                new Move("H8-F8")
            };
            Move[] kingMoves =
            {
                new Move("E1-C1"),
                new Move("E1-G1"),
                new Move("E8-C8"),
                new Move("E8-G8")
            };
            for(int index = 0; index < 4; index++)//if the king move was a castle move, move the rook
            {
                if(move.matches(kingMoves[index]))
                {
                    this.move(rookMoves[index]);
                    break;
                }
            }
        }
        lastMove = move;
    }
    /**
     * Sets the board to its starting position
     */
    public final void resetBoard()
    {
        for(int row = 0; row < 8; row++)//creates 8 empty rows
        {
            pieces[row] = new Piece[8];
            for(int column = 0; column < 8; column++)
            {
                pieces[row][column] = null;
            }
        }
        //creates pieces on the board
        //create kings
        pieces[7][4] = new King('W', false, new Position('E', 1));
        pieces[0][4] = new King('B', false, new Position('E', 8));
        //create knights
        pieces[7][1] = new Knight('W', false, new Position('B', 1));
        pieces[7][6] = new Knight('W', false, new Position('G', 1));
        pieces[0][1] = new Knight('B', false, new Position('B', 8));
        pieces[0][6] = new Knight('B', false, new Position('G', 8));
        //create rooks
        pieces[7][0] = new Rook('W', false, new Position('A', 1));
        pieces[7][7] = new Rook('W', false, new Position('H', 1));
        pieces[0][0] = new Rook('B', false, new Position('A', 8));
        pieces[0][7] = new Rook('B', false, new Position('H', 8));
        //create bishops
        pieces[7][2] = new Bishop('W', false, new Position('C', 1));
        pieces[7][5] = new Bishop('W', false, new Position('F', 1));
        pieces[0][2] = new Bishop('B', false, new Position('C', 8));
        pieces[0][5] = new Bishop('B', false, new Position('F', 8));
        //create queens
        pieces[7][3] = new Queen('W', false, new Position('D', 1));
        pieces[0][3] = new Queen('B', false, new Position('D', 8));
        //create pawns
        pieces[6][0] = new Pawn('W', false, new Position('A', 2));
        pieces[6][1] = new Pawn('W', false, new Position('B', 2));
        pieces[6][2] = new Pawn('W', false, new Position('C', 2));
        pieces[6][3] = new Pawn('W', false, new Position('D', 2));
        pieces[6][4] = new Pawn('W', false, new Position('E', 2));
        pieces[6][5] = new Pawn('W', false, new Position('F', 2));
        pieces[6][6] = new Pawn('W', false, new Position('G', 2));
        pieces[6][7] = new Pawn('W', false, new Position('H', 2));
        pieces[1][0] = new Pawn('B', false, new Position('A', 7));
        pieces[1][1] = new Pawn('B', false, new Position('B', 7));
        pieces[1][2] = new Pawn('B', false, new Position('C', 7));
        pieces[1][3] = new Pawn('B', false, new Position('D', 7));
        pieces[1][4] = new Pawn('B', false, new Position('E', 7));
        pieces[1][5] = new Pawn('B', false, new Position('F', 7));
        pieces[1][6] = new Pawn('B', false, new Position('G', 7));
        pieces[1][7] = new Pawn('B', false, new Position('H', 7));
    }
    /**
     * Draws the board
     * @param extraRowBreak adds an extra blank row for spacing
     */
    public void drawBoard(boolean extraRowBreak)
    {
        for(int printRow = 0; printRow < 8; printRow++)
        {
            for(int subRow = 0; subRow < 4; subRow++)
            {
                //print left boarder
                if(subRow == 1)
                {
                    System.out.print(8 - printRow);
                }
                else
                {
                    System.out.print(' ');
                }
                System.out.print(' ');
                
                //print pieces
                for(int column = 0; column < 8; column++)
                {
                    char fillChar = '*';
                    if((printRow + column) % 2 == 0)
                    {
                        fillChar = ' ';
                    }
                    
                    //print square
                    System.out.print(fillChar);
                    if(pieces[printRow][column] == null)//if no piece exists, fill with blank space
                    {
                        for(int i = 0; i < 3; i++)//piece sprites are 3 chars wide
                        {
                            System.out.print(fillChar);
                        }
                    }
                    else//use piece's sprite and color
                    {
                        char pieceColor = pieces[printRow][column].getColor();
                        boolean[] subSprite = pieces[printRow][column].getSprite().getRow(subRow);
                        for(int i = 0; i < 3; i++)
                        {
                            if(subSprite[i])
                            {
                                System.out.print(pieceColor);
                            }
                            else
                            {
                                System.out.print(fillChar);
                            }
                        }
                    }
                    System.out.print(fillChar);
                    System.out.print(' ');
                }
                System.out.print("\n");
            }
                if(extraRowBreak)
                {
                    System.out.print("\n");
                }
        }
        //print bottom row with files
        char[] files = {'A','B','C','D','E','F','G','H'};
        System.out.print("  ");
        for(int fileIndex = 0; fileIndex < 8; fileIndex++)
        {
            System.out.print("  " + files[fileIndex] + "   ");
        }
        System.out.print("\n");
    }
    /**
     * Draws a board using a given graphics, no clue how this is going to work (but it did :D)
     * @param frame frame to draw on?
     * @param chess the chess instance to draw for?
     */
    public void drawBoard(JFrame frame, Chess chess)
    {
        //some graphics stuff ig
        Graphics graphics = chess.getGraphics();
        //get size of board
        int boardSize = Math.min(chess.getWidth(), chess.getHeight());
        int tileSize = boardSize / 8;
        //draw board
        for(int row = 1; row <= 8; row++)
        {
            for(int column = 1; column <= 8; column++)
            {
                //draw background
                graphics.setColor(new Color(255, 238, 170));
                if((row + column) % 2 == 0)
                {
                    graphics.setColor(new Color(52, 28, 2));
                }
                graphics.fillRect((int)((column - 1) * tileSize), (int)((8 - row) * tileSize), (int)tileSize, (int)tileSize);
                //draw piece
                if(this.getPiece(new Position(Position.columnToFile(column), row)) != null)
                {
                    Piece currentPiece = this.getPiece(new Position(Position.columnToFile(column), row));
                    //scale and position polygons
                    int[] outerPolyX = new int[currentPiece.getOuterPolyX().length];
                    int[] outerPolyY = new int[currentPiece.getOuterPolyX().length];
                    int[] innerPolyX = new int[currentPiece.getInnerPolyX().length];
                    int[] innerPolyY = new int[currentPiece.getInnerPolyX().length];
                    for(int index = 0; index < outerPolyX.length; index++)
                    {
                        outerPolyX[index] = (int)(currentPiece.getOuterPolyX()[index] * tileSize / 100.0) + (int)(tileSize * (column - 1));
                        outerPolyY[index] = (int)(currentPiece.getOuterPolyY()[index] * tileSize / 100.0) + (int)(tileSize * (8 - row));
                    }
                    for(int index = 0; index < innerPolyX.length; index++)
                    {
                        innerPolyX[index] = (int)(currentPiece.getInnerPolyX()[index] * tileSize / 100.0) + (int)(tileSize * (column - 1));
                        innerPolyY[index] = (int)(currentPiece.getInnerPolyY()[index] * tileSize / 100.0) + (int)(tileSize * (8 - row));
                    }
                    //get correct piece color and display them
                    if(currentPiece.getColor() == 'W')
                    {
                        graphics.setColor(Color.BLACK);
                        graphics.fillPolygon(outerPolyX, outerPolyY, outerPolyX.length);
                        graphics.setColor(Color.WHITE);
                        graphics.fillPolygon(innerPolyX, innerPolyY, innerPolyX.length);
                    }
                    if(currentPiece.getColor() == 'B')
                    {
                        graphics.setColor(Color.WHITE);
                        graphics.fillPolygon(outerPolyX, outerPolyY, outerPolyX.length);
                        graphics.setColor(Color.BLACK);
                        graphics.fillPolygon(innerPolyX, innerPolyY, innerPolyX.length);
                    }
                }
            }
        }
        graphics.dispose();
    }
}