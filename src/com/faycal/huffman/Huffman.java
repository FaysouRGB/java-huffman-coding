package com.faycal.huffman;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Huffman {
    private static final Logger LOGGER = Logger.getLogger(Huffman.class.getName());

    // Helper class to store a node and its frequency.
    private static class NodeAndFrequency {
        Node node;
        int frequency;

        public NodeAndFrequency(Node node, int frequency) {
            this.node = node;
            this.frequency = frequency;
        }
    }

    private static Node createHuffmanTree(String input) {
        // Create a histogram of the input.
        Map<Character, Integer> histogram = Histogram.createHistogram(input);

        // Create a priority queue.
        PriorityQueue<NodeAndFrequency> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.frequency));
        for (Map.Entry<Character, Integer> entry : histogram.entrySet()) {
            queue.add(new NodeAndFrequency(new Node(entry.getKey()), entry.getValue()));
        }

        // Create the Huffman tree.
        while (queue.size() > 1) {
            NodeAndFrequency left = queue.poll();
            NodeAndFrequency right = queue.poll();
            Node parent = new Node(null);
            parent.setChildren(left.node, right.node);
            queue.add(new NodeAndFrequency(parent, left.frequency + right.frequency));
        }

        // Return the root of the Huffman tree.
        NodeAndFrequency root = queue.poll();
        return root.node;
    }

    private static String encodeHuffmanTree(Node node) {
        // Call the recursive function.
        StringBuilder sb = new StringBuilder();
        encodeHuffmanTreeRec(node, sb);

        // Add padding to the end.
        int padding = 0;
        while (sb.length() % 8 != 0) {
            sb.append("0");
            padding++;
        }

        // Add the padding to the beginning.
        sb.insert(0, String.format("%8s", Integer.toBinaryString(padding)).replace(' ', '0'));

        // Return the encoded Huffman tree.
        return sb.toString();
    }

    private static void encodeHuffmanTreeRec(Node node, StringBuilder sb) {
        // If node is a leaf, mark it with a 1 and append the character.
        if (node.isLeaf()) {
            sb.append("1");
            sb.append(String.format("%8s", Integer.toBinaryString(node.getCharacter())).replace(' ', '0'));
        } else {
            // Else mark it with a 0 and encode the left and right children.
            sb.append("0");
            encodeHuffmanTreeRec(node.getLeft(), sb);
            encodeHuffmanTreeRec(node.getRight(), sb);
        }
    }

    // Helper class to store a node and its index.
    private static class NodeAndIndex {
        Node node;
        int index;

        public NodeAndIndex(Node node, int index) {
            this.node = node;
            this.index = index;
        }
    }

    private static NodeAndIndex decodeHuffmanTree(String input) {
        // Call the recursive function.
        // Use an array to keep track of the current index.
        int[] indexAndPadding = new int[] { 8, Integer.parseInt(input.substring(0, 8), 2) };

        Node root = decodeHuffmanTreeRec(input.toCharArray(), indexAndPadding);
        indexAndPadding[0] += indexAndPadding[1];
        return new NodeAndIndex(root, indexAndPadding[0]);
    }

    private static Node decodeHuffmanTreeRec(char[] input, int[] indexAndPadding) {
        // If the current character is 1, create a leaf node.
        if (input[indexAndPadding[0]] == '1') {
            indexAndPadding[0]++;
            // Parse the 8 bits to a char.
            char character = (char) Integer.parseInt(new String(input, indexAndPadding[0], 8), 2);
            indexAndPadding[0] += 8;
            return new Node(character);
        } else {
            // Else create a parent node and set its children.
            indexAndPadding[0]++;
            Node left = decodeHuffmanTreeRec(input, indexAndPadding);
            Node right = decodeHuffmanTreeRec(input, indexAndPadding);
            Node parent = new Node(null);
            parent.setChildren(left, right);
            return parent;
        }
    }

    public static String readFile(String filename) {
        // Read the file content.
        StringBuilder result = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filename)) {
            int data;
            while ((data = fis.read()) != -1) {
                result.append((char) data);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + e.getMessage(), e);
        }
        return result.toString();
    }

    public static String compress(String input) {
        // Create the Huffman tree.
        Node root = createHuffmanTree(input);

        // Encode the Huffman tree.
        String tree = encodeHuffmanTree(root);

        // Encode the input.
        Map<Character, String> encoding = root.compressCharacters();
        StringBuilder sb = new StringBuilder();
        for (Character c : input.toCharArray()) {
            sb.append(encoding.get(c));
        }

        // Add padding to the end.
        int padding = 0;
        while (sb.length() % 8 != 0) {
            sb.append("0");
            padding++;
        }

        // Add the padding to the beginning.
        sb.insert(0, String.format("%8s", Integer.toBinaryString(padding)).replace(' ', '0'));

        // Return the Huffman tree and the encoded input.
        return tree + sb.toString();
    }

    public static String decompress(String input) {
        // Decode the Huffman tree.
        NodeAndIndex rootAndIndex = decodeHuffmanTree(input);

        // Get the input.
        char[] text = input.substring(rootAndIndex.index).toCharArray();

        // Read the padding.
        int padding = Integer.parseInt(new String(text, 0, 8), 2);
        text = new String(text, 8, text.length - 8).toCharArray();

        // Decode the input.
        StringBuilder sb = new StringBuilder();
        Node current = rootAndIndex.node;
        for (int i = 0; i < text.length; i++) {
            char c = text[i];
            if (i == text.length - padding - 1) {
                break;
            }
            if (c == '0') {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
            if (current.isLeaf()) {
                sb.append(current.getCharacter());
                current = rootAndIndex.node;
            }
        }

        // Return the decoded input.
        return sb.toString();
    }
}
