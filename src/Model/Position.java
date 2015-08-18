package Model;

/**
 * Created by Adam Fowles on 7/3/2015.
 */
public class Position
{
    public final int row;
    public final int col;

    public Position(int r, int c)
    {
        this.row = r;
        this.col = c;
    }

    /**
     * Construct a position from a string representation
     * 0-9 are represented as 00 - 09
     * @param s - the string representation in the form "## ##"
     */
    public Position(String s)
    {
        this.row = Integer.parseInt(s.substring(0,2));
        this.col = Integer.parseInt(s.substring(3,5));
    }

    @Override
    public boolean equals(Object p)
    {
        return row == ((Position)p).row && col == ((Position)p).col;
    }

    /**
     * Creates the string representation of a position to
     * write out in a message
     * @return
     */
    public String toString()
    {
        String s = "";
        if (row < 10)
        {
            s = "0" + row;
        }
        else
        {
            s = Integer.toString(row);
        }
        s += " ";
        if (col < 10)
        {
            s += "0" + col;
        }
        else
        {
            s += Integer.toString(col);
        }
        return s;
    }
}
