package Chess;

import java.util.ArrayList;

/**
 * Desc: Second basic AI player, looks [n + 1] turns into the future and evaluates based on material, mode, positioning, check, checkmate, and draw. The initial variables set should be set for positioning mode, since this is the mode the AI starts in
 * Modes: 'D', 'A', 'P' : Defending, Attacking, Positioning
 * Date: 6/13/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class BetterAIPlayer extends Player
{
    //instance vars
    private char moveMode = 'P';
    private int centerThreshold;
    private int materialMultiplier;
    private int checkMultiplier;
    private int checkmateMultiplier;
    private int drawMultiplier;
    private int centerMultiplier;
    private int defenseMultiplier;
    private int developmentMultiplier;  
    private final int N = 1;//number of extra turns to look ahead
    private final boolean debug = true;//enables debug
    private final boolean extendedDebug = false;//enables additional debugging features
    private Move[] lastFourMoves = {new Move(), new Move(), new Move(), new Move()};//used for avoiding draw by repetition
    
    //constructor
    /**
     * Creates a counting player with the given color and name with the given multipliers
     * @param color color of the player
     * @param name name of the player
     * @param material
     * @param check
     * @param mate
     * @param draw 
     */
    public BetterAIPlayer(char color, String name, int material, int check, int mate, int draw, int center, int defense, int develop, int devThreshold)
    {
        super(color, name);
        materialMultiplier = material;
        checkMultiplier = check;
        checkmateMultiplier = mate;
        drawMultiplier = draw;
        centerMultiplier = center;
        defenseMultiplier = defense;
        developmentMultiplier = develop;
        centerThreshold = devThreshold;
        setMode('P');//begin game trying to control the center of the board
    }
    
    //misc
    public void setMode(char mode)
    {
        switch(mode)
        {
            case 'A'://attack
            {
                if(moveMode != 'A')
                {
                    materialMultiplier *= 2;
                    if(moveMode == 'P')
                    {
                        centerMultiplier /= 2;
                        checkMultiplier *= 4;
                        developmentMultiplier /= 6;
                    }
                    else if(mode == 'D')
                    {
                        checkMultiplier /= 2;
                    }
                }
                break;
            }
            case 'P'://Position
            {
                if(moveMode != 'P')
                {
                    developmentMultiplier *= 6;
                    centerMultiplier *= 2;
                    checkMultiplier /= 4;
                    if(moveMode == 'A')
                    {
                        materialMultiplier /= 2;
                    }
                    else if(mode == 'D')
                    {
                        checkMultiplier /= 2;
                    }
                }
                break;
            }
            case 'D'://defend
            {
                if(moveMode != 'D')
                {
                    checkMultiplier *= 2;
                    if(moveMode == 'P')
                    {
                        centerMultiplier /= 2;
                        checkMultiplier *= 4;
                        developmentMultiplier /= 6;
                    }
                    if(moveMode == 'A')
                    {
                        materialMultiplier /= 2;
                    }
                }
                break;
            }
            default: throw(new IllegalArgumentException("Mode " + mode + " is an invalid mode"));
        }
        moveMode = mode;
    }
    /**
     * Returns legal move with the best score
     * @param board board to get move on
     * @return player's move
     */
    public Move getMove(Board board)
    {
        //update mode based on the current state of the board
        updateMode(board);
        Move[] myMoves = getLegalMoves(board);
        
        //prints current score of the board as well as the move it is processing
        if(debug)System.out.println("Getting move. Starting score: " + scoreBoard(board));
        if(debug)System.out.println("Processing move " + (0 + 1) + " of " + myMoves.length);
        
        //
        Board boardClone = board.getClone();
        int repBonus = getStall(board, myMoves[0]);
        boardClone.move(myMoves[0]);
        int bestMoveIndex = 0;
        int bestScore = getMoveScore(board, myMoves[0], N) + getBonus(boardClone, this.getColor()) + repBonus;
        bestScore += getRepeatIncentive(myMoves[0]);
        //System.out.println("Score: " + bestScore);
        for(int index = 1; index < myMoves.length; index++)
        {
            //System.out.println("Processing move " + (index + 1) + " of " + myMoves.length);
            boardClone = board.getClone();
            repBonus = getStall(board, myMoves[index]);
            boardClone.move(myMoves[index]);
            int score = getMoveScore(board, myMoves[index], N) + getDefenseBonus(boardClone, this.getColor()) * defenseMultiplier + getDevBonus(boardClone, this.getColor()) * developmentMultiplier + repBonus;
            if(myMoves[index].matches(lastFourMoves[1]))//devalue repeating moves
            {
                score -= checkMultiplier * 3 / 2;
                if(myMoves[index].matches(lastFourMoves[3]))
                {
                    score -= checkmateMultiplier;
                }
            }
            //System.out.println("Score: " + score);
            if(score > bestScore)
            {
                bestScore = score;
                bestMoveIndex = index;
            }
        }
        
        //update lastmoves
        lastFourMoves[3] = lastFourMoves[2];
        lastFourMoves[2] = lastFourMoves[1];
        lastFourMoves[1] = lastFourMoves[0];
        lastFourMoves[0] = myMoves[bestMoveIndex];
        System.out.println("Move: " + myMoves[bestMoveIndex] + " Score: " + bestScore);
        return myMoves[bestMoveIndex];
    }
    /**
     * Recursive way of scoring a move. Works by making the given move, making the opponent's move, and then scoring or going through another round of new moves
     * @param board board to analyze
     * @param move move to make
     * @param lookAhead moves to look ahead
     * @return score of move
     */
    public int getMoveScore(Board board, Move move, int lookAhead)
    {
        if(lookAhead <= 0)//score now
        {
            int worstScore = 320000;//big value to overwrite
            Board boardCopy = board.getClone();
            boardCopy.move(move);
            Piece movedPiece = boardCopy.getPiece(move.getTarget());
            if(movedPiece.getIndex() == 1 && (move.getTarget().getRow() == 1 || move.getTarget().getRow() == 8))//pawn promo
            {
                boardCopy.setPiece(new Queen(movedPiece.getColor(), true, move.getTarget()), move.getTarget());
            }
            Player op;
            if(this.getColor() == 'W')
            {
                op = new RandomPlayer('B', "");
            }
            else
            {
                op = new RandomPlayer('W', "");
            }
            //check for draw or checkmate, if so, score
            if(boardCopy.getCheckmate(op.getColor()) || boardCopy.getDraw(op.getColor()))
            {
                return scoreBoard(boardCopy);
            }
            //else, continue down line & get op's moves
            Move[] opMoves = op.getLegalMoves(boardCopy);
            for(int opMoveIndex = 0; opMoveIndex < opMoves.length; opMoveIndex++)
            {
                //create a new board and make the op's move
                Board boardCopy2 = boardCopy.getClone();
                boardCopy2.move(opMoves[opMoveIndex]);
                
                //check promotion
                Piece movedPiece2 = boardCopy.getPiece(move.getTarget());
                if(movedPiece2.getIndex() == 1 && (move.getTarget().getRow() == 1 || move.getTarget().getRow() == 8))//pawn promo
                {
                    boardCopy.setPiece(new Queen(movedPiece2.getColor(), true, move.getTarget()), move.getTarget());
                }
                
                //score moves
                int boardScore = scoreBoard(boardCopy2);
                if(boardScore < worstScore)
                {
                    worstScore = boardScore;
                }
            }
            return worstScore;
        }
        else//look further ahead
        {
            int worstScore = 320000;//big score to overwrite
            
            //clone board and make move
            Board boardCopy = board.getClone();
            boardCopy.move(move);
            
            //check promotion
            Piece movedPiece = boardCopy.getPiece(move.getTarget());
            if(movedPiece.getIndex() == 1 && (move.getTarget().getRow() == 1 || move.getTarget().getRow() == 8))//pawn promo
            {
                boardCopy.setPiece(new Queen(movedPiece.getColor(), true, move.getTarget()), move.getTarget());
            }
            
            //get an opponent
            Player op;
            if(this.getColor() == 'W')
            {
                op = new RandomPlayer('B', "");
            }
            else
            {
                op = new RandomPlayer('W', "");
            }
            
            //check for draw or checkmate, if so, score
            if(boardCopy.getCheckmate(op.getColor()) || boardCopy.getDraw(op.getColor()))
            {
                return scoreBoard(boardCopy);
            }
            
            //else, continue down line & get op moves
            Move[] opMoves = op.getLegalMoves(boardCopy);
            int bestScore;
            for(int opMoveIndex = 0; opMoveIndex < opMoves.length; opMoveIndex++)//loop through all opponent moves
            {
                bestScore = -320000;//big negative to overwrite
                
                //create second board clone, get op piece & make op move
                Board boardCopy2 = boardCopy.getClone();
                Piece movedPiece2 = boardCopy.getPiece(opMoves[opMoveIndex].getOrigin());//get piece that was moved
                boardCopy2.move(opMoves[opMoveIndex]);
                
                //check for pawn promo
                if(movedPiece2.getIndex() == 1 && (opMoves[opMoveIndex].getTarget().getRow() == 1 || opMoves[opMoveIndex].getTarget().getRow() == 8))//pawn promo
                {
                    boardCopy.setPiece(new Queen(movedPiece2.getColor(), true, opMoves[opMoveIndex].getTarget()), opMoves[opMoveIndex].getTarget());
                }
                
                //check for mate or draw, if so pass that score
                if(boardCopy2.getCheckmate(this.getColor()) || boardCopy2.getDraw(this.getColor()))
                {
                    bestScore = scoreBoard(boardCopy2);
                }
                else//get my moves and score them
                {
                    Move[] myMoves = getLegalMoves(boardCopy2);
                    for(int myMoveIndex = 0; myMoveIndex < myMoves.length; myMoveIndex++)
                    {
                        int moveScore = getMoveScore(boardCopy2, myMoves[myMoveIndex], lookAhead - 1);//recursive call
                        if(moveScore > bestScore)
                        {
                            bestScore = moveScore;
                        }
                        if(bestScore >= worstScore)//if there is a move with a greater or equal score, there is no reason to keep going through these moves since it is not beneficial for the opponent to consider any of these moves
                        {
                            break;
                        }
                    }
                }
                
                //op chooses worst outcome
                if(bestScore < worstScore)
                {
                    worstScore = bestScore;
                }
            }
            return worstScore;
        }
    }
    /**
     * Scores a given board
     * @param board board to score
     * @return score
     */
    public int scoreBoard(Board board)
    {
        int score = 0;
        char opColor = 'W';
        if(this.getColor() == 'W')
        {
            opColor = 'B';
        }
        
        //score material
        score += materialMultiplier * getPiecesValue(board, 1, 3, 3, 5, 9, 0, this.getColor());
        score -= materialMultiplier * getPiecesValue(board, 1, 3, 3, 5, 9, 0, opColor);
        
        //score draw
        if(board.getDraw('W') || board.getDraw('B'))
        {
            score -= drawMultiplier;
        }
        
        //score mate
        if(board.getCheckmate(this.getColor()))
        {
            score -= checkmateMultiplier;
        }
        if(board.getCheckmate(opColor))
        {
            score += checkmateMultiplier;
        }
        
        //score check
        if(board.getCheck(this.getColor()))
        {
            score -= checkMultiplier;
        }
        if(board.getCheck(opColor))
        {
            score += checkMultiplier;
        }
        
        //score control if mode is positioning
        if(moveMode == 'P')
        {
            score += centerMultiplier * piecesInCenter(board, this.getColor());
            score -= centerMultiplier * piecesInCenter(board, opColor);
        }
        
        return score;
    }
    public String getPromotion(Board board, Position position)
    {
        return "Queen";
    }
    /**
     * Returns the number of pieces in the center of the board, the center squares are worth double, and non-pawns are also worth double
     * @param board board to review
     * @param color color to score
     * @return score for center control
     */
    public int piecesInCenter(Board board, char color)
    {
        int pieces = 0;
        for(int row = 3; row <= 6; row++)
        {
            for(int column = 3; column <= 6; column++)
            {
                Position querryPosition = new Position(Position.columnToFile(column), row);
                Piece querryPiece = board.getPiece(querryPosition);
                if(querryPiece != null)
                {
                    if(querryPiece.getColor() == color)
                    {
                        pieces++;
                        if(querryPiece.getIndex() != 1)//non-pawns are worth double
                        {
                            pieces++;
                        }
                    }
                }
            }
        }
        for(int row = 4; row <= 5; row++)//double value for pieces in the very center
        {
            for(int column = 4; column <= 5; column++)
            {
                Position querryPosition = new Position(Position.columnToFile(column), row);
                Piece querryPiece = board.getPiece(querryPosition);
                if(querryPiece != null)
                {
                    if(querryPiece.getColor() == color)
                    {
                        pieces++;
                        if(querryPiece.getIndex() != 1)//non-pawns are worth double
                        {
                            pieces++;
                        }
                    }
                }
            }
        }
        return pieces;
    }
    public void printState()
    {
        System.out.print(
        "Mode: " + moveMode + "\n"
      + "Material: " + materialMultiplier + "\n"
      + "Check: " + checkMultiplier + "\n"
      + "Mate: " + checkmateMultiplier + "\n"
      + "Draw: " + drawMultiplier + "\n"
      + "Center: " + centerMultiplier + "\n"
      + "Defense: " + defenseMultiplier + "\n"
      + "Dev: " + developmentMultiplier + "\n");
    }
    public int getStall(Board board, Move move)
    {
        if(Chess.getMovesSinceCapture() > 40)
        {
            if(board.getPiece(move.getTarget()) != null || board.getPiece(move.getOrigin()).getIndex() == 1)
            {
                return 10000;
            }
        }
        return 0;
    }
    /**
     * Returns the number of pieces defending other pieces, meant to favor moves that would otherwise be of equal value
     * @param board board to analize
     * @param color color to score
     * @return number of defenses
     */
    public int getDefenseBonus(Board board, char color)
    {
        //find pieces
        ArrayList<Piece> myPieces = new ArrayList<>();
        for(int column = 1; column <= 8; column++)
        {
            for(int row = 1; row <= 8; row++)
            {
                Position querryPosition = new Position(Position.columnToFile(column), row);
                Piece querryPiece = board.getPiece(querryPosition);
                if(querryPiece != null)
                {
                    if(querryPiece.getColor() == this.getColor())
                    {
                        myPieces.add(querryPiece);
                    }
                }
            }
        }
        int defenses = 0;
        for(int pieceIndex = 0; pieceIndex < myPieces.size(); pieceIndex++)
        {
            defenses += myPieces.get(pieceIndex).getDefendingMoves(board).length;
        }
        return defenses;
    }
    public int getDevBonus(Board board, char color)
    {
        int score = 0;
        double direction = (color == 'W' ? -3.5 : 3.5);
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces.add(board.getPiece(new Position('B', (int)(4.5 + direction))));
        pieces.add(board.getPiece(new Position('C', (int)(4.5 + direction))));
        pieces.add(board.getPiece(new Position('D', (int)(4.5 + direction))));
        pieces.add(board.getPiece(new Position('F', (int)(4.5 + direction))));
        pieces.add(board.getPiece(new Position('G', (int)(4.5 + direction))));
        for(int index = 0; index < pieces.size(); index++)
        {
            if(pieces.get(index) == null)
            {
                score += 2;
            }
            else if(pieces.get(index).getIndex() != 2 && pieces.get(index).getIndex() != 3)
            {
                score += 2;
            }
        }
        direction = (color == 'W' ? -2.5 : 2.5);
        if(board.getPiece(new Position('D', (int)(4.5 + direction))) == null)//encourage moving up center pawns
        {
            score += 3;
        }
        else if(board.getPiece(new Position('D', (int)(4.5 + direction))).getIndex() != 1)
        {
            score += 3;
        }
        if(board.getPiece(new Position('E', (int)(4.5 + direction))) == null)//encourage moving up center pawns
        {
            score += 3;
        }
        else if(board.getPiece(new Position('E', (int)(4.5 + direction))).getIndex() != 1)
        {
            score += 3;
        }
        return score;
    }
    /**
     * Updates what mode the AI is in for a given board
     * @param board board to set for
     */
    public void updateMode(Board board)
    {
        int centerControl = 0;
        
        //get opponent color
        char opColor = 'W';
        if(this.getColor() == 'W')
        {
            opColor = 'B';
        }
        
        //get center control
        centerControl += piecesInCenter(board, this.getColor());
        centerControl -= piecesInCenter(board, opColor);
        
        //figure out what mode to be in
        if(centerControl >= centerThreshold)
        {
            setMode('A');
        }
        else if(centerControl < -((centerThreshold + centerThreshold % 2) / 2))
        {
            setMode('P');
        }
        else
        {
            if(board.getCheck(this.getColor()))
            {
                setMode('D');
            }
            else
            {
                setMode('P');
            }
        }
        printState();
    }
    public int getBonus(Board board, char color)
    {
        return getDefenseBonus(board, color) * defenseMultiplier + getDevBonus(board, color) * developmentMultiplier;
    }
    public int getRepeatIncentive(Move move)
    {
        int modifier = 0;
        if(move.matches(lastFourMoves[1]))
        {
            modifier -= checkMultiplier * 3 / 2;
            if(move.matches(lastFourMoves[3]))
            {
                modifier -= checkmateMultiplier;
            }
        }
        return modifier;
    }
            
}