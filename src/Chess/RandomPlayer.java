package Chess;

import java.util.ArrayList;
import java.util.Random;

/**
 * Desc: player that makes a random legal move
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class RandomPlayer extends Player
{
    public RandomPlayer()
    {
        super();
    }
    public RandomPlayer(char color, String name)
    {
        super(color, name);
    }
    public Move getMove(Board board)//getLegalMoves was later added to Player class
    {
        Random generator = new Random();
        ArrayList<Move> legalMoves = new ArrayList<>();
        //get all pieces and their moves
        for(int row = 1; row <= 8; row++)
        {
            for(int column = 1; column <= 8; column++)
            {
                if(board.getPiece(new Position(Position.columnToFile(column), row)) != null)
                {
                    if(board.getPiece(new Position(Position.columnToFile(column), row)).getColor() == this.getColor())
                    {
                        //if it is a piece of the player's color, get that piece's legal moves and add them to the arraylist
                        Move[] moves = board.getPiece(new Position(Position.columnToFile(column), row)).getLegalMoves(board);
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
        //choose a random index and return that move
        int moveIndex = (int)(generator.nextDouble() * (double)(result.length - 1));
        return result[moveIndex];
    }
    public String getPromotion(Board board, Position position)
    {
        return "Queen";
    }
}
