![Banner](images/banner.png)

## Overview

This Java library implements Huffman coding for efficient text file compression and decompression. It analyzes character frequencies in ASCII text to build optimal binary encodings, reducing file sizes while maintaining lossless compression. The included command-line tool provides a simple interface for compressing and decompressing text files.

## Key Features

- **Huffman Tree Construction**: Builds a Huffman tree from the input text based on character frequencies.

- **Compression**: Encodes the input text using the Huffman tree to produce a compressed binary string.

- **Decompression**: Decodes the compressed binary string back to the original text using the Huffman tree.

- **File Handling**: Reads input text from files and writes compressed/decompressed output to files.

- **Logging**: Uses Java's logging framework to log errors and important events.

## Limitations and Known Issues

- **Fixed Character Set**: The current implementation assumes a fixed character set like ASCII and does not handle dynamic character sets like UNICODE.

- **No Error Correction**: The compressed output does not include error correction codes, so any corruption in the compressed data may lead to incorrect decompression (may add a checksum at the end of the compressed file to ensure data integrity in the future).

## Usage and Testing

You can find a CLI tool in the `src/Main.java` showcasing how easy the library is to use. The tool supports both compression and decompression of text files. Usage is: java Main [-d] <file> where if `-d` is set, the CLI will decompress the file instead of compressing it. The node structure can be printed in java and will be displayed the `.dot` format.

## Contributions and Feedback

If you have any suggestions or ideas for improving the library, or if you've discovered a bug, I would greatly appreciate your feedback. Please feel free to submit a pull request or open an issue in the project repository.

## Credits

This project was developed by Fayçal Beghalia. Any use of this code is authorized, provided that proper credit is given.

## Example

```sh
faycal@FaycalLaptop:~/faycal/projects/java-huffman$ cat input.txt
I love sushi.
faycal@FaycalLaptop:~/faycal/projects/java-huffman$ java Main input.txt > output.txt
faycal@FaycalLaptop:~/faycal/projects/java-huffman$ cat output.txt
000000010001011101101011010000010010111010100100101011101011011001010010111001101011011111011010010010110110010000101010010000000000011001011111100101000001111111000110100001101101001101000000
faycal@FaycalLaptop:~/faycal/projects/huffman$ java Main -d output.txt
I love sushi.
```
