/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess;

/**
 * Desc: First basic AI player, looks [n + 1] moves into the future and evaluates based on material, check, checkmate, and draw
 * Date: 6/9/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class CountingAIPlayer extends Player
{
    //instance vars
    private int materialMultiplier;
    private int checkMultiplier;
    private int checkmateMultiplier;
    private int drawMultiplier;
    private static final int N = 1;
    
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
    public CountingAIPlayer(char color, String name, int material, int check, int mate, int draw)
    {
        super(color, name);
        materialMultiplier = material;
        checkMultiplier = check;
        checkmateMultiplier = mate;
        drawMultiplier = draw;
    }
    
    //misc
    /**
     * Returns legal move with the best score
     * @param board board to get move on
     * @return player's move
     */
    public Move getMove(Board board)
    {
        System.out.println("Getting move. Starting score: " + scoreBoard(board));
        Move[] myMoves = getLegalMoves(board);
        System.out.println("Processing move " + 1 + " of " + myMoves.length);
        int bestMoveIndex = 0;
        int bestScore = getMoveScore(board, myMoves[0], N);
        System.out.println("Score: " + bestScore);
        for(int index = 1; index < myMoves.length; index++)
        {
            System.out.println("Processing move " + (index + 1) + " of " + myMoves.length);
            int score = getMoveScore(board, myMoves[index], N);
            System.out.println("Score: " + score);
            if(score > bestScore)
            {
                bestScore = score;
                bestMoveIndex = index;
            }
        }
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
        
        return score;
    }
    public String getPromotion(Board board, Position position)
    {
        return "Queen";
    }
}