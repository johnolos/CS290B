package applications.longestpath;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PathTest {

    @Test
    public void testCompareTo() throws Exception {
        List<Path> paths = new ArrayList<>();

        paths.add(new Path(new ArrayList<>(), 10.0));
        paths.add(new Path(new ArrayList<>(), 15.0));
        paths.add(new Path(new ArrayList<>(), 20.0));

        assertEquals(10.0, paths.get(0).cost(), 0.0);
        assertEquals(15.0, paths.get(1).cost(), 0.0);
        assertEquals(20.0, paths.get(2).cost(), 0.0);

        paths.sort((path1, path2) -> path1.compareTo(path2));

        assertEquals(20.0, paths.get(0).cost(), 0.0);
        assertEquals(15.0, paths.get(1).cost(), 0.0);
        assertEquals(10.0, paths.get(2).cost(), 0.0);
    }
}