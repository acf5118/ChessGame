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

    public static Position convertToMultiArray(int single)
    {
        int r, c;
        c = single % 8;
        r = (single - c) / 8;
        return new Position(r, c);
    }

    public static int convertToSingleArray(Position p)
    {
        return (p.row * 8) + p.col;
    }

    @Override
    public boolean equals(Object p)
    {
        return row == ((Position)p).row && col == ((Position)p).col;
    }
}
