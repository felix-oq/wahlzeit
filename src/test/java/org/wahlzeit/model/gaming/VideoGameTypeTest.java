package org.wahlzeit.model.gaming;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Test cases for the {@link VideoGameType} class.
 */
public class VideoGameTypeTest {

    @Test
    public void testHasSubType() {
        // given
        VideoGameType root = new VideoGameType("root");
        VideoGameType firstNode = new VideoGameType("node1", root);
        VideoGameType secondNode = new VideoGameType("node2", root);
        VideoGameType firstLeaf = new VideoGameType("leaf1", firstNode);

        List<VideoGameType> allTypes = Arrays.asList(root, firstNode, secondNode, firstLeaf);

        // when
        List<VideoGameType> rootSubTypes = allTypes.stream()
                .filter(root::hasSubType)
                .collect(Collectors.toList());

        List<VideoGameType> firstNodeSubTypes = allTypes.stream()
                .filter(firstNode::hasSubType)
                .collect(Collectors.toList());

        List<VideoGameType> secondNodeSubTypes = allTypes.stream()
                .filter(secondNode::hasSubType)
                .collect(Collectors.toList());

        List<VideoGameType> firstLeafSubTypes = allTypes.stream()
                .filter(firstLeaf::hasSubType)
                .collect(Collectors.toList());

        // then
        assertContainsExactly(rootSubTypes, root, firstNode, secondNode, firstLeaf);
        assertContainsExactly(firstNodeSubTypes, firstNode, firstLeaf);
        assertContainsExactly(secondNodeSubTypes, secondNode);
        assertContainsExactly(firstLeafSubTypes, firstLeaf);
    }

    private <T> void assertContainsExactly(Collection<T> collection, T... elementsToBeContained) {
        if (collection.size() != elementsToBeContained.length) {
            fail();
        }
        if (!collection.containsAll(Arrays.asList(elementsToBeContained))) {
            fail();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testHasSubTypeThrowsNullPointerException() {
        // given
        VideoGameType type = new VideoGameType("type");

        // when
        type.hasSubType(null);
    }

    @Test
    public void testGetDirectSubType() {
        // given
        String secondNodeName = "node2";

        VideoGameType root = new VideoGameType("root");
        VideoGameType firstNode = new VideoGameType("node1", root);
        VideoGameType secondNode = new VideoGameType(secondNodeName, root);

        // when
        Optional<VideoGameType> secondNodeResult = root.getDirectSubType(secondNodeName);
        Optional<VideoGameType> unknownNodeResult = root.getDirectSubType("unknown");

        // then
        assertTrue(secondNodeResult.isPresent());
        assertSame(secondNodeResult.get(), secondNode);

        assertFalse(unknownNodeResult.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testAddDirectSubTypeThrowsNullPointerException() {
        // given
        VideoGameType type = new VideoGameType("type");

        // when
        type.addDirectSubType(null);
    }
}
