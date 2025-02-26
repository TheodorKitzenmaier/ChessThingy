package Chess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Successor to BetterAIPlayer / BetterCountingPlayer. Name was chosen due to
 * Prometheus being the Greek god whose name means foresight. This name is
 * doubly fitting since the AI is designed to look into the future, and is being
 * designed with more foresight than the previous AI, which became difficult to
 * manage due to unorganized code and lack of supporting methods. It is also
 * poorly documented. This AI is planned to be much easier to make modifications
 * to without causing major issues. It will also hopefully be move optimized to
 * allow for further look-ahead.<p>
 * Move modes:<p>
 * 'B' - book - follows set moves for certain positions, switches to development mode if there are no book moves, starting mode.<p>
 * 'D' - development - scores are weighted towards defending pieces and moving them into the middle of the board, comes after book moves are exhausted.<p>
 * 'A' - attack - scores are weighted towards material and attacking pieces, comes after development mode, chosen if development went well.<p>
 * 'P' - protect - scores are weighted towards material and defending pieces, comes after development mode, chosen if development went poorly<p>
 * 'C' - checkmate - scores are weighted towards not repeating and check, comes when the opponent has no material left.<p>
 * In the mid game the AI will switch between attack and protect modes based on
 * material advantage and the quantity of possible attacking moves the opponent
 * has. The thresholds in which the AI switches modes can be used to make the AI
 * more defensive of offensive.<p>
 * Created: 6/16/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class PrometheusAIPlayer extends Player
{
    //instance vars
    //mode variables
    private char moveMode = 'B';
    private char moveBias = 'A';
    private int attackThreshold;
    private int defenseThreshold;
    private int lookAhead;
    //multipliers
    private int centerMultiplier;
    private int defenseMultiplier;
    private int attackMultiplier;
    private int materialMultiplier;
    private int repeatMultiplier;
    private int drawMultiplier;
    private int checkMultiplier;
    private int checkmateMultiplier;
    private int blunderMultiplier;
    //other vars
    private static final int[] pieceValues = {0, 1, 3, 3, 5, 9};//values of pieces based of piece index
    private Move[] previousMoves = {new Move(), new Move(), new Move(), new Move(), new Move(), new Move()};//array of previous moves made, 0 is most recent
    private static boolean DEBUG = true;//enables displaying of all moves and scoring processes
    private static int MAX_SCORE = 2000000;
    
    //constructor
    /**
     * Creates an AI instance with the given color, name, and multipliers.
     * @param color color of the player
     * @param name name of the player
     * @param look number of turns to look ahead
     * @param bias play style preference, chosen when neither mode is majorly over its threshold 
     * @param attackT
     * @param defenseT
     * @param center
     * @param defense
     * @param attack
     * @param material
     * @param repeat
     * @param draw
     * @param check
     * @param checkmate 
     * @param blunder 
     */
    public PrometheusAIPlayer(char color, String name, int look, char bias, int attackT, int defenseT, int center, int defense, int attack, int material, int repeat, int draw, int check, int checkmate, int blunder)
    {
        super(color, name);
        lookAhead = look;
        attackThreshold = attackT;
        defenseThreshold = defenseT;
        centerMultiplier = center;
        defenseMultiplier = defense;
        attackMultiplier = attack;
        materialMultiplier = material;
        repeatMultiplier = repeat;
        drawMultiplier = draw;
        checkMultiplier = check;
        checkmateMultiplier = checkmate;
        blunderMultiplier = blunder;
    }
    
    //utility methods
    /**
     * Returns the legal moves a player of a given color can make that would
     * capture a piece.
     * @param board board to get moves on
     * @param color color to get moves for
     * @return moves that attack pieces
     */
    public Move[] getAttacks(Board board, char color)
    {
        ArrayList<Move> targetPositions = new ArrayList<>();
        Piece[] pieces = getPieces(board, color);
        //loop through all the pieces
        for(Piece piece : pieces)
        {
            Move[] pieceMoves = piece.getLegalMoves(board);
            //loop through the moves of the given piece, and add if there is a piece there
            for(Move pieceMove : pieceMoves)
            {
                Piece targetPiece = board.getPiece(pieceMove.getTarget());
                if(targetPiece != null)
                    targetPositions.add(pieceMove);
            }
        }
        //convert to array and return
        Move[] result = new Move[targetPositions.size()];
        for(int index = 0; index < targetPositions.size(); index++)
        {
            result[index] = targetPositions.get(index);
        }
        return result;
    }
    /**
     * Returns the pieces that attack a piece at a given position
     * @param board board to get pieces on
     * @param target position to get attackers for
     * @return array of attacking pieces
     */
    public Piece[] getAttackers(Board board, Position target)
    {
        ArrayList<Piece> attackers = new ArrayList<>();
        Piece[] pieces = getPieces(board, board.getPiece(target).getColor() == 'W' ? 'B' : 'W');
        for(Piece piece : pieces)//loop through all the pieces
        {
            Move[] pieceMoves = piece.getLegalMoves(board);
            for(Move pieceMove : pieceMoves)//loop through the moves of the given piece, and increment the counter if the target was not null
            {
                if(pieceMove.getTarget().matches(target))
                {
                    attackers.add(board.getPiece(pieceMove.getOrigin()));
                }
            }
        }
        //convert to array
        Piece[] result = new Piece[attackers.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = attackers.get(index);
        }
        return result;
    }
    /**
     * Returns the number of legal moves a player of a given color can make that
     * would capture a piece.
     * @param board board to get moves on
     * @param color color to get moves for
     * @return total number of moves that attack pieces
     */
    public int getAttackCount(Board board, char color)
    {
        int moveTotal = 0;
        Piece[] pieces = getPieces(board, color);
        for(Piece piece : pieces)//loop through all the pieces
        {
            Move[] pieceMoves = piece.getLegalMoves(board);
            for(Move pieceMove : pieceMoves)//loop through the moves of the given piece, and increment the counter if the target was not null
            {
                if(board.getPiece(pieceMove.getTarget()) != null)
                {
                    moveTotal++;
                }
            }
        }
        return moveTotal;
    }
    /**
     * Returns the moves that a player of a given color can make that would
     * defend a piece.
     * @param board board to get moves on
     * @param color color to get moves for
     * @return total number of moves that defend pieces
     */
    public Move[] getDefenses(Board board, char color)
    {
        ArrayList<Move> defendingMoves = new ArrayList<>();
        Piece[] pieces = getPieces(board, color);
        //loop through all the pieces and add their defending moves
        for(Piece piece : pieces)
        {
            Move[] pieceMoves = piece.getLegalDefenses(board);
            defendingMoves.addAll(Arrays.asList(pieceMoves));
        }
        //convert to array and return
        Move[] result = new Move[defendingMoves.size()];
        for(int index = 0; index < defendingMoves.size(); index++)
        {
            result[index] = defendingMoves.get(index);
        }
        return result;
    }
    /**
     * Returns the number of moves that a player of a given color can make that
     * would defend a piece.
     * @param board board to get moves on
     * @param color color to get moves for
     * @return total number of moves that defend pieces
     */
    public int getDefenseCount(Board board, char color)
    {
        int moveTotal = 0;
        Piece[] pieces = getPieces(board, color);
        for(Piece piece : pieces)//loop through all the pieces and add their defending moves
        {
            moveTotal += piece.getLegalDefenses(board).length;
        }
        return moveTotal;
    }
    /**
     * Returns an array of the pieces located in the center of the board. The
     * array contains the original instances from the board, so no mutators
     * should be used on the array elements. Doing so could cause desync
     * between the state of the board and the pieces' perception of where they
     * are, and by extension what moves they can make.
     * @param board board to get center pieces from
     * @param color color of pieces to get
     * @return array of center pieces
     */
    public Piece[] getCenterPieces(Board board, char color)
    {
        ArrayList<Piece> centerPieces = new ArrayList<>();
        //loop thorugh all the center spaces and get pieces
        for(int column = 3; column <= 6; column++)
        {
            for(int row = 3; row <= 6; row++)
            {
                Position querryPosition = new Position(Position.columnToFile(column), row);
                Piece querryPiece = board.getPiece(querryPosition);
                //check if the piece is not null and of the same color
                if(querryPiece != null)
                {
                    if(querryPiece.getColor() == color)
                    {
                        centerPieces.add(querryPiece);
                    }
                }
            }
        }
        //convery arraylist to array
        Piece[] result = new Piece[centerPieces.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = centerPieces.get(index);
        }
        return result;
    }
    /**
     * Updates the previous move array, setting the most recent move to the move
     * provided as an argument. Shifts all moves over, and discards the oldest
     * move in the array.
     * @param lastMove 
     */
    public void updateLastMove(Move lastMove)
    {
        previousMoves[5] = previousMoves[4];
        previousMoves[4] = previousMoves[3];
        previousMoves[3] = previousMoves[2];
        previousMoves[2] = previousMoves[1];
        previousMoves[1] = previousMoves[0];
        previousMoves[0] = lastMove;
    }
    
    //scoring methods
    /**
     * Returns an un-multiplied score for the pieces a given color has in the
     * middle of the board. Pawns are worth one point, while other pieces are
     * worth two, and all pieces have their value doubled if they are in one of
     * the four central squares.
     * @param board board to get score on
     * @param color color to score
     * @return total score from all pieces
     */
    public int scoreCenter(Board board, char color)
    {
        int score = 0;
        Piece[] centerPieces = getCenterPieces(board, color);
        //loop through all center pieces
        for(Piece piece: centerPieces)
        {
            //add score based on index of the piece, pawns are worth one and pieces are worth two
            score += piece.getIndex() == 1 ? 1 : 2;
            //double value for very center of the board
            if(piece.getPosition().getColumn() == 4 || piece.getPosition().getColumn() == 5 && piece.getPosition().getRow() == 4 || piece.getPosition().getRow() == 5)
                score += piece.getIndex() == 1 ? 1 : 2;
        }
        return score;
    }
    /**
     * Returns the score which reflects the number of previous moves. The
     * formula used is: 3 ^ #identical moves - 1.
     * @param move move to compare
     * @return score based on how many moves have repeated
     */
    public int scoreThreefold(Move move)
    {
        int score = 1;
        for(int moveIndex = 1; moveIndex <= 5; moveIndex ++)
        {
            if(move.matches(previousMoves[moveIndex]))
            {
                score *= 3;
            }
        }
        return score - 1;
    }
    /**
     * Returns 1 if the game is close to draw by the 50-move rule, and the piece
     * being moved is either a pawn or captures a piece. Otherwise returns 0,
     * since there is either no need to motivate this kind of move, or it is not
     * a move that would prolong the game.
     * @param board board to evaluate on
     * @param move move to evaluate
     * @return 1 if the move would receive an incentive to avoid 50-move draw
     */
    public int score50Move(Board board, Move move)
    {
        if(Chess.getMovesSinceCapture() > 40)
        {
            if(board.getPiece(move.getTarget()) != null || board.getPiece(move.getOrigin()).getIndex() == 1)
            {
                return 1;
            }
        }
        return 0;
    }
    /**
     * Returns 1 if the move is a blunder.
     * @param board
     * @param move
     * @return 
     */
    public int scoreBlunder(Board board, Move move)
    {
        /*the move is a blunder because it allows mate in one, should be redundant
          solve by making the move, then simulating all the opponent's moves and checking if any cause a checkmate*/
        {
            Board boardClone = board.getClone();
            boardClone.move(move);
            Player op = new RandomPlayer(this.getColor() == 'W' ? 'B' : 'W', "");
            Move[] opMoves = op.getLegalMoves(boardClone);
            for(Move opMove : opMoves)
            {
                Board secondClone = boardClone.getClone();
                secondClone.move(opMove);
                if(secondClone.getCheckmate(this.getColor()))
                    return 1;
            }
        }
        
        /*the move is a blunder because it sacrifices significant material
          solve by checking if the value of the piece captured is less than the piece capturing, and punishing the AI if:
          the cost of defending that piece is more than the cost of attacking it
          go through all the attackers, selecting the least valuable attacker
          then select the least valuable defender to retake
          if at the end, there are no remaining defenders and the attackers have not been exhausted
          OR the total value of the pieces taken is less than the value of the pieces lost
          the move is a blunder*/
        {
            Piece movingPiece = board.getPiece(move.getOrigin());
            Piece attackedPiece = board.getPiece(move.getTarget());
            if(pieceValues[attackedPiece.getIndex()] < pieceValues[movingPiece.getIndex()])
            {
                //get attacking pieces
                ArrayList<Piece> attackers = new ArrayList<>(Arrays.asList(getAttackers(board, move.getTarget())));
                for(int index = 0; index < attackers.size(); index++)
                {
                    if(attackers.get(index).getPosition().matches(move.getOrigin()))
                    {
                        attackers.remove(index);
                        break;
                    }            
                }
                
                //get defending pieces
                Move[] allDefenses = getDefenses(board, this.getColor() == 'W' ? 'B': 'W');
                ArrayList<Piece> defenders = new ArrayList<>();
                for(Move defense : allDefenses)
                {
                    if(defense.getTarget().matches(move.getTarget()))
                        defenders.add(board.getPiece(defense.getOrigin()));
                }
                
                //trade pieces
                int netTrade = 0;
                while(true)
                {
                    //AI captures
                    netTrade += pieceValues[attackedPiece.getIndex()];
                    attackedPiece = movingPiece;
                    
                    //defenders chooses how to recapture
                    if(!defenders.isEmpty())
                    {
                        movingPiece = defenders.get(0);
                        for(Piece defender : defenders)
                        {
                            if(pieceValues[defender.getIndex()] < pieceValues[movingPiece.getIndex()])
                                movingPiece = defender;
                        }
                        defenders.remove(attackedPiece);
                        
                        //defenders recapture
                        netTrade -= pieceValues[attackedPiece.getIndex()];
                        attackedPiece = movingPiece;
                    }
                    else
                        break;
                    
                    //AI chooses how to capture back
                    if(!attackers.isEmpty())
                    {
                        movingPiece = attackers.get(0);
                        for(Piece attacker : attackers)
                        {
                            if(pieceValues[attacker.getIndex()] < pieceValues[movingPiece.getIndex()])
                                movingPiece = attacker;
                        }
                        attackers.remove(movingPiece);
                    }
                    else
                        break;
                }
                if(netTrade < 0)
                    return 1;
            }
        }
        
        /*the move is a blunder because it is trying to delay a promotion (this is an old bug that caused the AI to sacrifice all its pieces to put the king in check, delaying a pawn move)
          solve by checking the the move puts the opponent in check, the moved piece can be taken back, and the moved piece has no defender*/
        {
            
        }
        
        return 0;
    }
    
    //bonus methods
    /**
     * Returns a positive value if the move is a blunder, 0 if not.
     * @param board board to check on
     * @param move move to check
     * @return 
     */
    public int getBlunderBonus(Board board, Move move)
    {
        return scoreBlunder(board, move) * blunderMultiplier;
    }
    /**
     * Returns a positive if the move repeats previous moves, increases if the
     * move matches multiple previous moves.
     * @param move move to score
     * @return 
     */
    public int getThreefoldBonus(Move move)
    {
        return scoreThreefold(move) * repeatMultiplier;
    }
    /**
     * Returns a positive if the move prevents/delays a draw by the 50 move rule.
     * @param board board to score on
     * @param move move to score
     * @return 
     */
    public int get50MoveBonus(Board board, Move move)
    {
        return score50Move(board, move) * drawMultiplier;
    }
    
    //dual side scoring methods
    /**
     * Scores the overall center control score of a given board, accounting for
     * both sides control over the center.
     * @param board board to score on
     * @return composite score of both sides' control of the center
     */
    public int scoreCenterControl(Board board)
    {
        int score = 0;
        score += scoreCenter(board, this.getColor()) * centerMultiplier;
        score -= scoreCenter(board, this.getColor() == 'W' ? 'B' : 'W') * centerMultiplier;
        return score;
    }
    /**
     * Scores the overall defensive strength of both sides.
     * @param board board to score on
     * @return composite score of both sides' defensive strength
     */
    public int scoreDefense(Board board)
    {
        int score = 0;
        score += getDefenseCount(board, this.getColor()) * defenseMultiplier;
        score -= getDefenseCount(board, this.getColor() == 'W' ? 'B' : 'W') * defenseMultiplier;
        return score;
    }
    /**
     * Scores the overall offensive strength of both sides.
     * @param board board to score on
     * @return composite score of both sides' offensive strength
     */
    public int scoreOffense(Board board)
    {
        int score = 0;
        score += getAttackCount(board, this.getColor()) * attackMultiplier;
        score -= getAttackCount(board, this.getColor() == 'W' ? 'B' : 'W') * attackMultiplier;
        return score;
    }
    /**
     * Scores the overall material strength of both sides.
     * @param board board to score on
     * @return composite score of both sides' material
     */
    public int scoreMaterial(Board board)
    {
        int score = 0;
        score += this.getPiecesValue(board, this.getColor()) * materialMultiplier;
        score -= this.getPiecesValue(board, this.getColor() == 'W' ? 'B' : 'W') * materialMultiplier;
        return score;
    }
    /**
     * Scores if either side is in check
     * @param board board to score on
     * @return composite score of both sides' being in check
     */
    public int scoreCheck(Board board)
    {
        int score = 0;
        if(board.getCheck(this.getColor()))
            score--;
        if(board.getCheck(this.getColor() == 'W' ? 'B' : 'W'))
            score++;
        score *= checkMultiplier;
        return score;
    }
    /**
     * Returns a score based on if either side is in checkmate
     * both sides control over the center.
     * @param board board to score on
     * @return composite score of both sides' being in checkmate
     */
    public int scoreCheckmate(Board board)
    {
        int score = 0;
        if(board.getCheckmate(this.getColor()))
            score--;
        if(board.getCheckmate(this.getColor() == 'W' ? 'B' : 'W'))
            score++;
        score *= checkmateMultiplier;
        return score;
    }
    /**
     * Returns a score based on if either side has no moves
     * both sides control over the center.
     * @param board board to score on
     * @return composite score of both sides' not having moves
     */
    public int scoreDraw(Board board)
    {
        int score = 0;
        if(board.getDraw(this.getColor()))
            score--;
        if(board.getDraw(this.getColor() == 'W' ? 'B' : 'W'))
            score--;
        score *= drawMultiplier;
        return score;
    }
    
    //final scoring
    /**
     * Returns the total score of a board based on the following factors: 
     * center control, 
     * defensive strength, 
     * offensive strength, 
     * material advantage, 
     * check, 
     * checkmate, 
     * and draw.
     * @param board board to score
     * @return total score
     */
    public int getBoardScore(Board board)
    {
        int score = 0;
        score += scoreCenterControl(board);
        score += scoreDefense(board);
        score += scoreOffense(board);
        score += scoreMaterial(board);
        score += scoreCheck(board);
        score += scoreCheckmate(board);
        score += scoreDraw(board);
        return score;
    }
    /**
     * Returns the bonuses for the move on the given board, should be SUBTRACTED
     * from the score of the move. 
     * @param move
     * @param board
     * @return 
     */
    public int getBonusScore(Move move, Board board)
    {
        int score = 0;
        score += getBlunderBonus(board, move);
        score += getThreefoldBonus(move);
        score -= get50MoveBonus(board, move);
        return score;
    }
    
    //move methods
    /**
     * Gets a player's move for a given board state
     * @param board board to get a move for
     * @return player's move
     */
    @Override
    public Move getMove(Board board)
    {
        //update move mode to reflect current board
        updateMode(board);
        
        //different ways of deciding moves
        switch(moveMode)
        {
            case 'B': //book moves
            {
                
            }
            default: //regular moves
            {
                //get legal moves
                Move[] legalMoves = getLegalMoves(board);
                int bestScore;
                Move bestMove;
                
                //score first move
                bestMove = legalMoves[0];
                bestScore = scoreMove(board, bestMove, -MAX_SCORE, lookAhead) - getBonusScore(bestMove, board);
                if(DEBUG)
                    System.out.println("Move 1: " + bestMove + " Score: " + bestScore);

                //score other moves, overwrite best move if a better one is found
                for(int index = 1; index < legalMoves.length; index++)
                {
                    //score move
                    int score;
                    score = scoreMove(board, legalMoves[index], -MAX_SCORE, lookAhead) - getBonusScore(legalMoves[index], board);
                    
                    //debug
                    if(DEBUG)
                        System.out.println("Move " + (index + 1) + ": " + legalMoves[index] + " Score: " + score);

                    //compare to old move
                    if(score > bestScore)
                    {
                        bestScore = score;
                        bestMove = legalMoves[index];
                    }
                }
                //return best move
                return bestMove;
            }
        }
    }
    /**
     * Scores the move based on the most likely board state nMoves in the future.
     * @param board board to advance
     * @param move move to score
     * @param highScore highest score in a branch, used for alpha beta
     * @param lookAhead how many turns to look ahead
     * @return move score
     */
    public int scoreMove(Board board, Move move, int highScore, int lookAhead)
    {
        //no more lookahead, score current state
        if(lookAhead == 0)
        {
            Board clone = board.getClone();
            clone.move(move);
            return getBoardScore(board);
        }
        //keep looking ahead
        else
        {
            //get some vars
            char opColor = this.getColor() == 'W' ? 'B' : 'W';
            
            //get clone and make move
            Board clone = board.getClone();
            clone.move(move);
            
            //check for promotion
            if(clone.getPiece(move.getTarget()).getIndex() == 1)
            {
                Piece movedPawn = clone.getPiece(move.getTarget());
                clone.setPiece(new Queen(movedPawn.getColor(), true, move.getTarget()), move.getTarget());
            }
            
            //check for end state
            if(clone.getCheckmate(opColor) || clone.getDraw(opColor))
                return getBoardScore(clone);
            
            //get op moves
            RandomPlayer op = new RandomPlayer(opColor, "");
            Move[] opMoves = op.getLegalMoves(clone);
            int lowestScore = MAX_SCORE;
            
            //loop through op moves
            for(Move opMove : opMoves)
            {
                int opMoveScore;
                
                //make op move on clone
                Board clone2 = clone.getClone();
                clone2.move(opMove);
                
                //check for promotion
                if(clone2.getPiece(opMove.getTarget()).getIndex() == 1)
                {
                    Piece movedPawn = clone2.getPiece(opMove.getTarget());
                    clone2.setPiece(new Queen(movedPawn.getColor(), true, opMove.getTarget()), opMove.getTarget());
                }
                
                //check for end state
                if(clone.getCheckmate(this.getColor()) || clone.getDraw(this.getColor()))
                    opMoveScore = getBoardScore(clone);
                else
                {
                    //get my moves
                    Move[] myMoves = getLegalMoves(clone2);
                    
                    //score first move
                    opMoveScore = scoreMove(clone2, myMoves[0], -MAX_SCORE, lookAhead - 1);
                    
                    for(int index = 1; index < myMoves.length; index++)
                    {
                        //if the score is greater than the lowest score, break since the opponent has a better move anyways
                        if(opMoveScore > lowestScore)
                            break;
                        
                        //recursive score
                        int myMoveScore = scoreMove(clone2, myMoves[index], opMoveScore, lookAhead - 1);
                        
                        //opponent's move is scored based on the highest score AI coult get to with that move
                        opMoveScore = myMoveScore > opMoveScore ? myMoveScore : opMoveScore;
                    }
                }
                lowestScore = opMoveScore < lowestScore ? opMoveScore : lowestScore;
                
                //return if it is lower than high score, since AI would not go down this path and additional computation is not needed
                if(lowestScore < highScore)
                    return lowestScore;
            }
            
            //return
            return lowestScore;
        }
    }
    public void updateMode(Board board)
    {
        if(moveMode != 'B')
        {
            
        }
    }
    public void setMode(char mode)
    {
        switch(mode)
        {
            case 'D':
            case 'A':
            case 'P':
            case 'C':
        }
        moveMode = mode;
    }
    @Override
    public String getPromotion(Board board, Position position)
    {
        return "Queen";
    }
}
