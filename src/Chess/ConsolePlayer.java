package Chess;

import java.util.Scanner;

/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: container for data on how to draw pieces
 */
public class ConsolePlayer extends Player
{
    //constructors
    /**
     * uses default constructor for Player
     */
    public ConsolePlayer()
    {
        super();
    }
    /**
     * Normal constructor for ConsolePlayer
     * @param color color of the player
     * @param name name of the player
     */
    public ConsolePlayer(char color, String name)
    {
        super(color, name);
    }
    
    //misc
    /**
     * Gets a valid move from the console given a board
     * @param board board to get the move on
     * @return move the player input
     */
    public Move getMove(Board board)//override of getMove for Player
    {
        Scanner input = new Scanner(System.in);
        Move playerMove;
        while(true)//runs until an input is valid
        {
            System.out.print("\nEnter move: ");
            String moveInput = input.nextLine();
            playerMove = new Move(moveInput);
            if(playerMove.isValid())//constructed in a way that did not cause an error
            {
                Piece movedPiece = board.getPiece(playerMove.getOrigin());
                if(movedPiece == null)
                {
                    System.out.print("\nThere is no piece\n");
                }
                else if(movedPiece.getColor() == this.getColor())
                {
                    Move[] pieceValidMoves = movedPiece.getLegalMoves(board);
                    for(int moveIndex = 0; moveIndex < pieceValidMoves.length; moveIndex++)
                    {
                        if(playerMove.matches(pieceValidMoves[moveIndex]))
                        {
                            input.close();
                            return playerMove;
                        }
                    }
                    System.out.print("\nPiece is not capable of making that move\n");
                }
                else
                {
                    System.out.print("\nThis is not your piece\n");
                }
            }
            else
            {
                System.out.print("\nMove is improperly formatted. Use form: F#-F#\n");
            }
            board.drawBoard(true);
        }
    }
    public String getPromotion(Board board, Position position)
    {
        Scanner input = new Scanner(System.in);
        String choice = "";
        while(true)
        {
            System.out.print("\nPawn promoted, enter piece to promote to:");
            choice = input.nextLine();
            if(choice.length() == 0)
            {
                choice = input.nextLine();
            }
            if(choice.equalsIgnoreCase("Knight"))
            {
                break;
            }
            else if(choice.equalsIgnoreCase("Rook"))
            {
                break;
            }
            else if(choice.equalsIgnoreCase("Bishop"))
            {
                break;
            }
            else if(choice.equalsIgnoreCase("Queen"))
            {
                break;
            }
            else 
            {
                System.out.print("\n" + choice + " is not a valid piece type.\n");
            }
        }
        input.close();
        return choice;
    }
}
