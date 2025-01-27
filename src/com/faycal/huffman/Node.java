package com.faycal.huffman;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a node in the Huffman tree.
 */
public class Node {
    private final Character character;
    private Node left, right;

    /**
     * Constructs a Node with the given character.
     *
     * @param character the character this node represents
     */
    public Node(Character character) {
        this.character = character;
    }

    /**
     * Compresses the characters in the Huffman tree.
     *
     * @return an unmodifiable map where keys are characters and values are their
     *         Huffman codes
     */
    public Map<Character, String> compressCharacters() {
        Map<Character, String> encoding = new HashMap<>();
        compressCharactersRec(encoding, "");
        return Collections.unmodifiableMap(encoding);
    }

    private void compressCharactersRec(Map<Character, String> encoding, String prefix) {
        if (isLeaf()) {
            encoding.put(character, prefix);
        } else {
            if (left != null) {
                left.compressCharactersRec(encoding, prefix + "0");
            }
            if (right != null) {
                right.compressCharactersRec(encoding, prefix + "1");
            }
        }
    }

    /**
     * Gets the character this node represents.
     *
     * @return the character
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Checks if this node is a leaf node.
     *
     * @return true if this node is a leaf, false otherwise
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /**
     * Gets the left child of this node.
     *
     * @return the left child
     */
    public Node getLeft() {
        return left;
    }

    /**
     * Gets the right child of this node.
     *
     * @return the right child
     */
    public Node getRight() {
        return right;
    }

    /**
     * Sets the left and right children of this node.
     *
     * @param left  the left child
     * @param right the right child
     * @throws IllegalArgumentException if either child is null
     */
    public void setChildren(Node left, Node right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Children cannot be null");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {");
        printDotRec(sb);
        sb.append("}");
        return sb.toString();
    }

    private void printDotRec(StringBuilder sb) {
        if (isLeaf()) {
            sb.append("  \"").append(character).append("\";\n");
        } else {
            if (left != null) {
                sb.append("  \"").append(character).append("\" -> \"").append(left.character)
                        .append("\" [label=\"0\"];\n");
                left.printDotRec(sb);
            }
            if (right != null) {
                sb.append("  \"").append(character).append("\" -> \"").append(right.character)
                        .append("\" [label=\"1\"];\n");
                right.printDotRec(sb);
            }
        }
    }
}
