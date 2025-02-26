package Chess;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Desc: uses mouse inputs
 * Date: 6/9/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class MousePlayer extends Player
{
    public MousePlayer()
    {
        super();
    }
    public MousePlayer(char color, String name)
    {
        super(color, name);
    }
    
    public Move getMove(Board board)
    {
        Player altPlayer = new PromptPlayer(this.getColor(), this.getName());
        return altPlayer.getMove(board);
    }
    public Move getGMove(Board board, Chess chess)
    {
        try
        {
            Thread.sleep(50L);
        }
        catch(InterruptedException ex)
        {
            Logger.getLogger(MousePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Position target = Chess.getTarget();
        Position origin = Chess.getOrigin();
        if(origin != null && target != null && board.getPiece(origin) != null)
        {
            if(board.getPiece(origin).getColor() == this.getColor())
            {
                Move theMove = new Move(origin, target);
                System.out.println("Origin and target are not null, testing move:");
                if(board.moveIsLegal(theMove))
                {
                    System.out.println("Move was valid");
                    Chess.resetMouse();
                    return theMove;
                }
                else
                {
                    System.out.println("Move was invalid");
                    Chess.resetMouse();
                }
            }
        }
        /*
        if(chess.getOrigin() == null)
        {
            System.out.println("Origin was null");
        }
        if(chess.getTarget() == null)
        {
            System.out.println("Target was null");
        }
        */
        return null;
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
