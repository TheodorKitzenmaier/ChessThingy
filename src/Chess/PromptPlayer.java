package Chess;

import javax.swing.JOptionPane;

/**
 * name: Theodor Kitzenmaier
 * date: 6/8/22
 * desc: player that inputs moves through prompt windows
 */
public class PromptPlayer extends Player
{
    //constructors
    /**
     * uses default constructor for Player
     */
    public PromptPlayer()
    {
        super();
    }
    /**
     * Normal constructor for ConsolePlayer
     * @param color color of the player
     * @param name name of the player
     */
    public PromptPlayer(char color, String name)
    {
        super(color, name);
    }
    
    //misc
    /**
     * Gets a valid move from a JOptionPane from the player
     * @param board board to get the move on
     * @return move the player input
     */
    public Move getMove(Board board)//override of getMove for Player
    {
        Move playerMove;
        while(true)//runs until an input is valid
        {
            String moveInput = JOptionPane.showInputDialog(null, "Enter a move:", "Move", JOptionPane.QUESTION_MESSAGE);
            playerMove = new Move(moveInput);
            if(playerMove.isValid())//constructed in a way that did not cause an error
            {
                Piece movedPiece = board.getPiece(playerMove.getOrigin());
                if(movedPiece == null)
                {
                    JOptionPane.showMessageDialog(null, "There is no piece", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(movedPiece.getColor() == this.getColor())
                {
                    Move[] pieceValidMoves = movedPiece.getLegalMoves(board);
                    for(int moveIndex = 0; moveIndex < pieceValidMoves.length; moveIndex++)
                    {
                        if(playerMove.matches(pieceValidMoves[moveIndex]))
                        {
                            return playerMove;
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Piece is not capable of making that move", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "This is not your piece", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Move is improperly formatted. Use form: F#-F#", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public String getPromotion(Board board, Position position)
    {
        String choice = "";
        while(true)
        {
            choice = JOptionPane.showInputDialog(null, "Pawn promoted, enter piece to promote to:", "Promotion", JOptionPane.QUESTION_MESSAGE);
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
                JOptionPane.showMessageDialog(null, choice + " is not a valid piece type", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return choice;
    }
}
