package org.wahlzeit.model.gaming;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the {@link VideoGameManager} class.
 */
public class VideoGameManagerTest {

    private long index = 0L;

    // used to create unique type names because the video game manager is singleton and caches its types
    private long nextUniqueIndex() {
        return index++;
    }

    @Test
    public void testGetVideoGameType() {
        // given
        String nodeName = "node-" + nextUniqueIndex();
        String firstLeafName = "leaf1-" + nextUniqueIndex();
        String secondLeafName = "leaf2-" + nextUniqueIndex();

        String firstPath = nodeName + VideoGameManager.PATH_SEPARATOR + firstLeafName;
        String secondPath = nodeName + VideoGameManager.PATH_SEPARATOR + secondLeafName;

        VideoGameManager manager = VideoGameManager.getInstance();

        // when
        VideoGameType firstType = manager.getVideoGameType(firstPath);
        VideoGameType secondType = manager.getVideoGameType(secondPath);

        // then
        assertEquals(firstLeafName, firstType.getName());
        assertFalse(firstType.getDirectSubTypes().iterator().hasNext()); // no direct subtypes

        assertEquals(secondLeafName, secondType.getName());
        assertFalse(secondType.getDirectSubTypes().iterator().hasNext()); // no direct subtypes

        VideoGameType superType = firstType.getSuperType();
        assertSame(superType, secondType.getSuperType());

        assertEquals(nodeName, superType.getName());

        VideoGameType rootType = superType.getSuperType();
        assertEquals(VideoGameManager.ROOT_TYPE_NAME, rootType.getName());
        assertNull(rootType.getSuperType());
    }

    @Test(expected = NullPointerException.class)
    public void testGetVideoGameTypeThrowsNullPointerException() {
        // given
        VideoGameManager manager = VideoGameManager.getInstance();

        // when
        manager.getVideoGameType(null);
    }

    @Test
    public void testGetPathString() {
        // given
        String[] pathSegments = {"root-" + nextUniqueIndex(),
                "node-" + nextUniqueIndex(),
                "leaf-" + nextUniqueIndex()};
        String givenPath = String.join(VideoGameManager.PATH_SEPARATOR, pathSegments);

        VideoGameManager manager = VideoGameManager.getInstance();
        VideoGameType leafType = manager.getVideoGameType(givenPath);

        // when
        String resultingPath = manager.getPathString(leafType);

        // then
        assertEquals(givenPath, resultingPath);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPathStringThrowsNullPointerException() {
        // given
        VideoGameManager manager = VideoGameManager.getInstance();

        // when
        manager.getPathString(null);
    }
}
