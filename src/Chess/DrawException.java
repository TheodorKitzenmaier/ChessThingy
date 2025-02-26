package Chess;

/**
 * Desc: Error thrown in case of a draw
 * Date: 6/4/22
 * @author Theodor Kitzenmaier <tkitzenm at asu.edu>
 */
public class DrawException extends Exception
{
    public DrawException(String errorMessage)
    {
        super(errorMessage);
    }
}
