package Chess;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;


/**
 * name: Theodor Kitzenmaier
 * date: 6/1/22
 * desc: contains main functionality of the game
 */
public class Chess extends JPanel
{
    //vars for mouse
    private static Position origin = null;
    private static Position target = null;
    private static int movesSinceCapture;
    
    /**
     * CHESS
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //get mode to run chess in
        String[] options = {"Console", "GUI"};
        int mode = JOptionPane.showOptionDialog(
                null,
                "Select graphics mode:",
                "Input",
                0,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                0);
        
        //create chess
        Chess theChess = new Chess();
        
        //get player names
        String whiteName;
        String blackName;
        whiteName = JOptionPane.showInputDialog(null, "Enter Player one (white)'s name:", "Player 1 selection", JOptionPane.QUESTION_MESSAGE);
        blackName = JOptionPane.showInputDialog(null, "Enter Player two (black)'s name:", "Player 2 selection", JOptionPane.QUESTION_MESSAGE);
        
        //declare & initialize players
        Player white = getPlayer(whiteName, 'W');
        Player black = getPlayer(blackName, 'B');
        
        //run chess in either mode
        switch(mode)
        {
        case 0://console mode
        {
            //play game
            Player winner = theChess.playGame(white, black, mode);
            System.out.print("\nWinner: " + winner.getName() + " " + winner.getColor() + "\n");
            break;
        }
        case 1://graphics mode
        {
            //play game
            Player winner = theChess.playGame(white, black, mode);
            JOptionPane.showMessageDialog(null, "Winner: " + winner.getName() + " " + winner.getColor(), "Winner", JOptionPane.INFORMATION_MESSAGE);
            break;
        }
        }
    }
    
    //constructor
    /**
     * Constructor ig, idk at this point
     */
    public Chess()
    {
        //target and origin had to be made static in chess to be accessed by the mouse listener. This SHOULDN'T be a problem, but was not the original intent. As a result there is some redundant code that will likely be cleaned up in the future
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if(e.getButton() == 1)//mouse one was pressed
                {
                    //get panel info
                    int sizeX = getSize().width;
                    int sizeY = getSize().height;
                    
                    //get position of mouse
                    Point originPoint = e.getPoint();
                    int pointX = originPoint.x;
                    int pointY = originPoint.y;
                    
                    //scale to get row & column
                    int panelSize = Math.min(sizeX, sizeY);
                    int tileSize = panelSize / 8;
                    int row = pointY / tileSize;
                    int column = pointX / tileSize;
                    
                    //row and column are in bounds
                    if((row >= 0 && row <= 7) && (column >= 0 && column <= 7))
                    {
                        origin = new Position(Position.columnToFile(column + 1), 8 - row);
                        Chess.setOrigin(origin);
                        target = null;
                        Chess.setTarget(target);
                        System.out.println("Clicked at " + e.getPoint() + " " + origin);
                    }
                    else
                    {
                        System.out.println("Clicked off the board");
                        origin = null;
                    }
                }
            }
            public void mouseReleased(MouseEvent e)
            {
                if(e.getButton() == 1)//mouse one was pressed
                {
                    //get panel info
                    int sizeX = getSize().width;
                    int sizeY = getSize().height;
                    
                    //get position of mouse
                    Point originPoint = e.getPoint();
                    int pointX = originPoint.x;
                    int pointY = originPoint.y;
                    
                    //scale to get row & column
                    int panelSize = Math.min(sizeX, sizeY);
                    int tileSize = panelSize / 8;
                    int row = pointY / tileSize;
                    int column = pointX / tileSize;
                    
                    //row and column are in bounds
                    if((row >= 0 && row <= 7) && (column >= 0 && column <= 7))
                    {
                        target = new Position(Position.columnToFile(column + 1), 8 - row);
                        Chess.setTarget(target);
                        System.out.println("Released at " + e.getPoint() + " " + target);
                    }
                    else
                    {
                        System.out.println("Released off the board");
                        target = null;
                        origin = null;
                        Chess.resetMouse();
                    }
                }
            }
        });
    }
    
    //accessors
    /**
     * Returns the click origin
     * @return the origin
     */
    public static Position getOrigin()
    {
        return origin;
    }
    /**
     * Returns the click target
     * @return the target
     */
    public static Position getTarget()
    {
        return target;
    }
    public static int getMovesSinceCapture()
    {
        return movesSinceCapture;
    }
    
    //mutators
    /**
     * Resets the origin and target to being null
     */
    public static void resetMouse()
    {
        System.out.println("Reset position");
        origin = null;
        target = null;
    }
    /**
     * Sets the click origin to a given position
     * @param p the position
     */
    public static void setOrigin(Position p)
    {
        origin = p;
        System.out.println("Set mouse origin to " + origin);
    }
    /**
     * Sets the click target to a given position
     * @param p the position
     */
    public static void setTarget(Position p)
    {
        target = p;
        System.out.println("Set mouse target to " + target);
    }
    
    //misc
    public static Player getPlayer(String name, char color)
    {
        String[] playerTypes = {
            "Console",
            "Prompt",
            "Mouse",
            "Random",
            "CountingAI",
            "CountingAI + ab",
            "Better CountingAI"};
        int choice = JOptionPane.showOptionDialog(null, "Choose player type", "Player selector", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, playerTypes, 0);
        switch(choice)
        {
            case 0: return new ConsolePlayer(color, name);
            case 1: return new PromptPlayer(color, name);
            case 2: return new MousePlayer(color, name);
            case 3: return new RandomPlayer(color, name);
            case 4: return new CountingAIPlayer(color, name, 8, 100, 1000, 250);
            case 5: return new CountingAIPlayerAB(color, name, 8, 100, 1000, 250);
            case 6: return new BetterAIPlayer(color, name, 64, 30, 4000, 1000, 8, 1, 24, 4);//this AI used to be my child, then I killed it by accident and I don't know how
            default: throw(new IllegalArgumentException("Illegal Player Choice, index " + choice + " is out of bounds"));
        }
    }
    /**
     * Plays a game with the given players for black and white, returns the player that won
     * @param white white player
     * @param black black player
     * @param mode the graphics mode to run in
     * @return winning player
     */
    public Player playGame(Player white, Player black, int mode)
    {
        switch(mode)
        {
        case 0:
        {
            Board board = new Board();
            movesSinceCapture = 0;
            while(true)
            {
                //white's move
                board.drawBoard(true);
                if(board.getDraw('W'))
                {
                    System.out.print("White has no moves");
                    System.exit(1);
                }
                Move whiteMove = white.getMove(board);
                if(board.getPiece(whiteMove.getOrigin()) == null)
                {
                    System.out.print("\nWhite made an illegal move: black wins by default.\n");
                    return black;
                }
                if(board.getPiece(whiteMove.getOrigin()).getColor() != 'W')
                {
                    System.out.print("\nWhite made an illegal move: black wins by default.\n");
                    return black;
                }
                if(board.moveIsLegal(whiteMove))
                {
                    if(board.getPiece(whiteMove.getTarget()) != null)
                    {
                        movesSinceCapture = 0;
                    }
                    else if(board.getPiece(whiteMove.getOrigin()).getIndex() == 1)
                    {
                        movesSinceCapture = 0;
                    }
                    else
                    {
                        movesSinceCapture++;
                        if(movesSinceCapture >= 50)
                        {
                            System.out.print("Draw by 50 moves without capture");
                            System.exit(1);
                        }
                    }
                    board.move(whiteMove);
                    //pawn promotion
                    if(whiteMove.getTarget().getRow() == 8)
                    {
                        //piece is pawn
                        if(board.getPiece(whiteMove.getTarget()).getIndex() == 1)
                        {
                            String choice = white.getPromotion(board, whiteMove.getTarget());
                            if(choice.length() == 0)
                            {
                                choice = white.getPromotion(board, whiteMove.getTarget());
                            }
                            if(choice.equalsIgnoreCase("Knight"))
                            {
                                board.setPiece(new Knight('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Rook"))
                            {
                                board.setPiece(new Rook('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Bishop"))
                            {
                                board.setPiece(new Bishop('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Queen"))
                            {
                                board.setPiece(new Queen('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else 
                            {
                                System.out.print("\n" + choice + " is not a valid piece type.\n");
                                System.out.print("\nWhite made an illegal move: black wins by default.\n");
                                return black;
                            }
                        }
                    }
                }
                else
                {
                    System.out.print("\nWhite made an illegal move: black wins by default.\n");
                    return black;
                }
                if(board.getCheckmate('B'))
                {
                    board.drawBoard(true);
                    return white;
                }

                //black's move
                board.drawBoard(true);
                if(board.getDraw('B'))
                {
                    System.out.print("Black has no moves");
                    System.exit(1);
                }
                Move blackMove = black.getMove(board);
                if(board.getPiece(blackMove.getOrigin()) == null)
                {
                    System.out.print("\nBlack made an illegal move: white wins by default.\n");
                    return white;
                }
                if(board.getPiece(blackMove.getOrigin()).getColor() != 'B')
                {
                    System.out.print("\nBlack made an illegal move: white wins by default.\n");
                    return white;
                }
                if(board.moveIsLegal(blackMove))
                {
                    if(board.getPiece(blackMove.getTarget()) != null)
                    {
                        movesSinceCapture = 0;
                    }
                    else if(board.getPiece(blackMove.getOrigin()).getIndex() == 1)
                    {
                        movesSinceCapture = 0;
                    }
                    else
                    {
                        movesSinceCapture++;
                        if(movesSinceCapture >= 50)
                        {
                            System.out.print("Draw by 50 moves without capture");
                            System.exit(1);
                        }
                    }
                    board.move(blackMove);
                    //pawn promotion
                    if(blackMove.getTarget().getRow() == 1)
                    {
                        //piece is pawn
                        if(board.getPiece(blackMove.getTarget()).getIndex() == 1)
                        {
                            String choice = black.getPromotion(board, blackMove.getTarget());
                            if(choice.length() == 0)
                            {
                                choice = black.getPromotion(board, blackMove.getTarget());
                            }
                            if(choice.equalsIgnoreCase("Knight"))
                            {
                                board.setPiece(new Knight('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Rook"))
                            {
                                board.setPiece(new Rook('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Bishop"))
                            {
                                board.setPiece(new Bishop('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Queen"))
                            {
                                board.setPiece(new Queen('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else
                            {
                                System.out.print("\n" + choice + " is not a valid piece type.\n");
                                System.out.print("\nBlack made an illegal move: white wins by default.\n");
                                return white;
                            }
                        }
                    }
                }
                else
                {
                    System.out.print("\nBlack made an illegal move: white wins by default.\n");
                    return white;
                }
                if(board.getCheckmate('W'))
                {
                    return black;
                }
            }
        }
        case 1://gui mode
        {
            //graphics nonsense
            Chess theChess = new Chess();
            JFrame frame = new JFrame("Chess");
            frame.getContentPane().add(theChess, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setVisible(true);
            
            //create board
            Board board = new Board();
            
            //draw board for testing
            board.drawBoard(frame, theChess);
            
            //play game
            movesSinceCapture = 0;
            while(true)
            {
                //white's move
                /*
                draw
                get draw
                get move
                check null piece moved
                check color of moved piece
                check move is legal
                make move
                check promotion
                check checkmate
                */
                board.drawBoard(frame, theChess);
                if(board.getDraw('W'))
                {
                    JOptionPane.showMessageDialog(null, "White has no legal moves", "Draw", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                }
                Move whiteMove = white.getGMove(board, this);
                while(whiteMove == null)
                {
                    whiteMove = white.getGMove(board, this);
                }
                if(board.getPiece(whiteMove.getOrigin()) == null)
                {
                    JOptionPane.showMessageDialog(null, "White made an illegal move: black wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                    return black;
                }
                if(board.getPiece(whiteMove.getOrigin()).getColor() != 'W')
                {
                    JOptionPane.showMessageDialog(null, "White made an illegal move: black wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                    return black;
                }
                if(board.moveIsLegal(whiteMove))
                {
                    if(board.getPiece(whiteMove.getTarget()) != null)
                    {
                        movesSinceCapture = 0;
                    }
                    else if(board.getPiece(whiteMove.getOrigin()).getIndex() == 1)
                    {
                        movesSinceCapture = 0;
                    }
                    else
                    {
                        movesSinceCapture++;
                        if(movesSinceCapture >= 50)
                        {
                            JOptionPane.showMessageDialog(null, "Draw by 50 moves without capture", "Draw", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(1);
                        }
                    }
                    board.move(whiteMove);
                    if(whiteMove.getTarget().getRow() == 8)
                    {
                        //piece is pawn
                        if(board.getPiece(whiteMove.getTarget()).getIndex() == 1)
                        {
                            String choice = white.getPromotion(board, whiteMove.getTarget());
                            if(choice.length() == 0)
                            {
                                choice = white.getPromotion(board, whiteMove.getTarget());
                            }
                            if(choice.equalsIgnoreCase("Knight"))
                            {
                                board.setPiece(new Knight('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Rook"))
                            {
                                board.setPiece(new Rook('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Bishop"))
                            {
                                board.setPiece(new Bishop('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Queen"))
                            {
                                board.setPiece(new Queen('W', true, whiteMove.getTarget()), whiteMove.getTarget());
                            }
                            else 
                            {
                                JOptionPane.showMessageDialog(null, choice + " is not a valid piece type.", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                                JOptionPane.showMessageDialog(null, "White made an illegal move: black wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                                return black;
                            }
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "White made an illegal move: black wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                    return black;
                }
                if(board.getCheckmate('B'))
                {
                    board.drawBoard(frame, theChess);
                    try
                    {
                        Thread.sleep(50L);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(Chess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return white;
                }
                
                //black's move
                board.drawBoard(frame, theChess);
                if(board.getDraw('B'))
                {
                    JOptionPane.showMessageDialog(null, "Black has no legal moves", "Draw", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                }
                Move blackMove = black.getGMove(board, this);
                while(blackMove == null)
                {
                    blackMove = black.getGMove(board, this);
                }
                if(board.getPiece(blackMove.getOrigin()) == null)
                {
                    JOptionPane.showMessageDialog(null, "Black made an illegal move: white wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                    return white;
                }
                if(board.getPiece(blackMove.getOrigin()).getColor() != 'B')
                {
                    JOptionPane.showMessageDialog(null, "Black made an illegal move: white wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                    return white;
                }
                if(board.moveIsLegal(blackMove))
                {
                    if(board.getPiece(blackMove.getTarget()) != null)
                    {
                        movesSinceCapture = 0;
                    }
                    else if(board.getPiece(blackMove.getOrigin()).getIndex() == 1)
                    {
                        movesSinceCapture = 0;
                    }
                    else
                    {
                        movesSinceCapture++;
                        if(movesSinceCapture >= 50)
                        {
                            JOptionPane.showMessageDialog(null, "Draw by 50 moves without capture", "Draw", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(1);
                        }
                    }
                    board.move(blackMove);
                    if(blackMove.getTarget().getRow() == 1)
                    {
                        //piece is pawn
                        if(board.getPiece(blackMove.getTarget()).getIndex() == 1)
                        {
                            String choice = black.getPromotion(board, blackMove.getTarget());
                            if(choice.length() == 0)
                            {
                                choice = black.getPromotion(board, blackMove.getTarget());
                            }
                            if(choice.equalsIgnoreCase("Knight"))
                            {
                                board.setPiece(new Knight('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Rook"))
                            {
                                board.setPiece(new Rook('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Bishop"))
                            {
                                board.setPiece(new Bishop('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else if(choice.equalsIgnoreCase("Queen"))
                            {
                                board.setPiece(new Queen('B', true, blackMove.getTarget()), blackMove.getTarget());
                            }
                            else 
                            {
                                JOptionPane.showMessageDialog(null, choice + " is not a valid piece type.", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                                JOptionPane.showMessageDialog(null, "Black made an illegal move: white wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                                return white;
                            }
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Black made an illegal move: white wins be default", "Illegal move", JOptionPane.INFORMATION_MESSAGE);
                    return white;
                }
                if(board.getCheckmate('W'))
                {
                    board.drawBoard(frame, theChess);
                    try//try catch since the compiler insisted one was needed
                    {
                        Thread.sleep(50L);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(Chess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return black;
                }
            }
        }
        }
        System.out.print("Something went wrong");
        return white;
    }
    
}
