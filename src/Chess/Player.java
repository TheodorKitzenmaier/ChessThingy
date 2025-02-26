package Chess;

import java.util.ArrayList;

/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: base for players
 */
public abstract class Player
{
    //instance vars
    private char color;
    private String name;
    
    //constructors
    /**
     * Default constructor for a player, creates a white player with an empty name
     */
    public Player()
    {
        color = 'W';
        name = "";
    }
    
    /**
     * Standard constructor for a player, creates a player with a given color and name
     * @param color color of the player
     * @param name name of the player
     */
    public Player(char color, String name)
    {
        this.color = color;
        this.name = name;
    }
    
    //accessors
    /**
     * Returns the color of the player
     * @return color of the player
     */
    public char getColor()
    {
        return color;
    }
    
    /**
     * Returns the name of the player
     * @return name of the player
     */
    public String getName()
    {
        return name;
    }
    
    
    //abstracts
    /**
     * Gets a player's move for a given board state
     * @param board board to get a move for
     * @return player's move
     */
    abstract Move getMove(Board board);
    /**
     * Gets a player's move for a given board state. By default this returns the
     * move a player would make with the normal getMove method, however it also
     * allows the JPanel extension to be passed to the player. This is currently
     * implemented to allow for mouse inputs
     * @param board board to get move on
     * @param chess chess instance
     * @return player's move
     */
    public Move getGMove(Board board, Chess chess)
    {
        return this.getMove(board);
    }
    /**
     * Returns the string name of the type of piece to promote the pawn at the
     * given position to
     * @param board board to promote on
     * @param position position of the pawn to promote
     * @return name of the piece to promote to
     */
    abstract String getPromotion(Board board, Position position);
    
    
    //utility methods
    /**
     * Returns the material of a given side on a given board based on the
     * material values provided as arguments
     * @param board board to analyze
     * @param pawn
     * @param bishop
     * @param knight
     * @param rook
     * @param queen
     * @param king
     * @param color color to get material for
     * @return the material
     */
    public int getPiecesValue(Board board, int pawn, int bishop, int knight, int rook, int queen, int king, char color)
    {
        //PieceSprite[] sprites = {Pawn.getPawnSprite(), Bishop.getBishopSprite(), Knight.getKnightSprite(), Rook.getRookSprite(), Queen.getQueenSprite(), King.getKingSprite()};
        //int[] values = {pawn, bishop, knight, rook, queen, king};
        int[] values = {king, pawn, bishop, knight, rook, queen};
        int score = 0;
        for(int row = 1; row <= 8; row++)
        {
            for(int column = 1; column <= 8; column++)
            {
                Piece checkingPiece = board.getPiece(new Position(Position.columnToFile(column), row));
                if(checkingPiece != null)
                {
                    if(checkingPiece.getColor() == color)
                    {
                        /*
                        for(int index = 0; index < 6; index++)
                        {
                            if(board.getPiece(new Position(Position.columnToFile(column), row)).getSprite().matches(sprites[index]))
                            {
                                score += values[index];
                            }
                        }
                        */
                        score += values[checkingPiece.getIndex()];
                    }
                }
            }
        }
        return score;
    }
    
    /**
     * Returns the combined value of the material of a given color on a given board with the standard chess values (1, 3, 3, 5, 9)
     * @param board
     * @param color
     * @return 
     */
    public int getPiecesValue(Board board, char color)
    {
        return getPiecesValue(board, 1, 3, 3, 5, 9, 0, color);
    }
    
    /**
     * Returns the array of all the pieces of a given color on a given board
     * @param board board to search
     * @param color color to find
     * @return array of pieces
     */
    public Piece[] getPieces(Board board, char color)
    {
        ArrayList<Piece> pieces = new ArrayList<>();
        for(int row = 1; row <= 8; row++)
        {
            for(int column = 1; column <= 8; column++)
            {
                Position querryPosition = new Position(Position.columnToFile(column), row);
                Piece querryPiece = board.getPiece(querryPosition);
                if(querryPiece != null);
                {
                    if(querryPiece.getColor() == color)
                    {
                        pieces.add(querryPiece);
                    }
                }
            }
        }
        //converts arraylist to array
        Piece[] result = new Piece[pieces.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = pieces.get(index);
        }
        return result;
    }
    
    /**
     * Returns array of all the moves the player can legally make on a given board
     * @param board board to get moves on
     * @return legal moves
     */
    public Move[] getLegalMoves(Board board)
    {
        ArrayList<Move> legalMoves = new ArrayList<>();
        //get all pieces and their moves
        for(int row = 1; row <= 8; row++)
        {
            for(int column = 1; column <= 8; column++)
            {
                Piece checkingPiece = board.getPiece(new Position(Position.columnToFile(column), row));
                if(checkingPiece != null)
                {
                    if(checkingPiece.getColor() == this.getColor())
                    {
                        //if it is a piece of the player's color, get that piece's legal moves and add them to the arraylist
                        Move[] moves = checkingPiece.getLegalMoves(board);
                        for(int index = 0; index < moves.length; index++)
                        {
                            legalMoves.add(moves[index]);
                        }
                    }
                }
            }
        }
        //converts arraylist to array
        Move[] result = new Move[legalMoves.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = legalMoves.get(index);
        }
        return result;
    }
}
