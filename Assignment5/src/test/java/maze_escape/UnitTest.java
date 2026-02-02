package maze_escape;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UnitTest {

    @Test
    public void getsAllVertices() {
        AbstractGraph<Integer> abstractGraph = new AbstractGraph<>() {
            @Override
            public Set<Integer> getNeighbours(Integer vertexFrom) {
                return Set.of(2, 3, 4);
            }
        };

        Set<Integer> allVert = abstractGraph.getAllVertices(1);
        Set<Integer> expectedVert = Set.of(1, 2, 3, 4);

        Assertions.assertEquals(expectedVert, allVert);

        allVert = abstractGraph.getAllVertices(3);
        expectedVert = Set.of(3, 2, 4);

        Assertions.assertEquals(expectedVert, allVert);
    }
}