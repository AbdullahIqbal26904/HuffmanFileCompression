# Huffman File Compression

> A lossless file compression system built entirely from scratch in Java — custom data structures, greedy algorithm, bit-level I/O, and a Swing GUI — developed as a Data Structures course project in 2024.

---

## Table of Contents

- [Overview](#overview)
- [How Huffman Coding Works](#how-huffman-coding-works)
- [Architecture](#architecture)
- [Data Structures Built from Scratch](#data-structures-built-from-scratch)
- [Project Structure](#project-structure)
- [Compression Results](#compression-results)
- [Getting Started](#getting-started)
- [GUI Walkthrough](#gui-walkthrough)
- [Technical Deep Dive](#technical-deep-dive)

---

## Overview

This project implements **Huffman Coding**, a foundational lossless data compression algorithm used in formats like JPEG, MP3, ZIP, and DEFLATE. Rather than using Java's built-in library data structures, every core component — the priority queue, hash map, and binary tree — was implemented manually to deeply understand the underlying mechanics.

**Key highlights:**

- Full compression and decompression pipeline operating at the **byte level**
- Custom **min-heap priority queue** implemented as a sorted linked list
- Custom **hash map** with chaining and dynamic rehashing
- **Tree serialization** to persist the Huffman tree between sessions
- **Java Swing GUI** for interactive file selection, compression, and decompression
- Tested on real-world data including *Oliver Twist* (the full novel — ~215 KB)

---

## How Huffman Coding Works

Huffman coding assigns **shorter binary codes to more frequent characters** and longer codes to rarer ones, guaranteeing no wasted bits through optimal prefix-free encoding.

### Step-by-step process

```
Input text: "AABBBCCCC"

1. Count frequencies:
   A → 2,  B → 3,  C → 4

2. Build a min-priority queue (sorted by frequency):
   [ A:2,  B:3,  C:4 ]

3. Repeatedly merge the two lowest-frequency nodes:

   Step 1:  Merge A(2) + B(3) → AB(5)
                 AB(5)
                /     \
              A(2)   B(3)

   Step 2:  Merge C(4) + AB(5) → root(9)
                  root(9)
                 /        \
               C(4)       AB(5)
                          /    \
                        A(2)  B(3)

4. Assign codes by traversing the tree (left = 0, right = 1):
   C → 0
   A → 10
   B → 11

5. Encode the original text:
   "AABBBCCCC" → 10 10 11 11 11 0 0 0 0
   Original:  9 chars × 8 bits = 72 bits
   Encoded:   18 bits  →  ~75% size reduction
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Java Swing GUI                       │
│   File → Open  │  [ZIP]  │  [UNZIP]  │  [EXIT]             │
│   Shows: original size, compressed size, % reduction        │
└────────────────────────┬────────────────────────────────────┘
                         │
          ┌──────────────┴──────────────┐
          ▼                             ▼
  ┌───────────────┐             ┌───────────────┐
  │   compress    │             │  decompress   │
  │    .java      │             │    .java      │
  └──────┬────────┘             └──────┬────────┘
         │                             │
         │  1. Read bytes              │  1. Deserialize tree
         │  2. Build freq map          │  2. Read binary file
         │  3. Build priority queue    │  3. Walk tree to decode
         │  4. Build Huffman tree      │  4. Write output file
         │  5. Generate codes          │
         │  6. Pack bits → write       │
         │  7. Serialize tree          │
         │                             │
         └──────────────┬──────────────┘
                        │
          ┌─────────────┴──────────────┐
          │                            │
   ┌──────▼──────┐            ┌────────▼───────┐
   │ priorityQueue│            │ Serialization  │
   │  (sorted    │            │     Util       │
   │  linked list)│            │ (tree ↔ disk) │
   └──────┬──────┘            └────────────────┘
          │
   ┌──────▼──────┐
   │ huffmanNode │
   │  (BST node) │
   └─────────────┘
```

---

## Data Structures Built from Scratch

### 1. Min-Priority Queue — `priorityQueue.java`

A **sorted singly linked list** that always keeps nodes ordered by frequency. This drives the Huffman tree construction.

```
Enqueue(C:4) → Enqueue(A:2) → Enqueue(B:3)

Front → [A:2] → [B:3] → [C:4] → null
         ↑ always dequeue from here (minimum frequency)
```

- `Enqueue(node)`: Inserts in sorted position — O(n)
- `Dequeue()`: Removes the front (minimum) — O(1)

### 2. Huffman Tree — `huffman.java` + `huffmanNode.java`

A **binary tree** where each leaf stores a byte value and its frequency. Internal nodes aggregate frequencies. The tree encodes the entire compression dictionary.

- `huffmanNode` implements `Serializable` so the tree can be saved to disk
- `huffmanNode` implements `Comparable<huffmanNode>` for priority ordering

### 3. Custom Hash Map — `hashmaps.java`

A **generic HashMap<K, V>** built from scratch with:
- **Separate chaining** via `LinkedList[]` buckets
- **Dynamic rehashing** when load factor exceeds 0.5
- Initial capacity: 4 buckets, doubles on rehash

```java
// Generic implementation — works for any key/value types
static class HashMap<K, V> {
    private LinkedList<Node>[] buck;
    public void put(K key, V value) { ... }
    public V get(K key) { ... }
    private void rehash() { ... }
}
```

---

## Project Structure

```
HuffmanFileCompression/
├── src/
│   ├── backEnd/
│   │   ├── huffmanNode.java      # Tree node (Serializable, Comparable)
│   │   ├── huffman.java          # Builds the Huffman tree from priority queue
│   │   ├── priorityQueue.java    # Min-priority queue (sorted linked list)
│   │   ├── Node.java             # Linked list node wrapper
│   │   ├── compress.java         # Full compression pipeline
│   │   ├── decompress.java       # Full decompression pipeline
│   │   ├── Serializationutil.java# Serialize/deserialize the Huffman tree
│   │   ├── hashmaps.java         # Custom generic HashMap implementation
│   │   └── driver.java           # CLI entry point
│   ├── FrontEnd/
│   │   └── driver.java           # Java Swing GUI entry point
│   ├── olivertwist.txt           # Test file: full novel (~215 KB)
│   ├── olivertwistzip.txt        # Compressed output
│   ├── shortFile.txt             # Test file: short paragraph
│   └── serialize2.txt            # Serialized Huffman tree (binary)
└── huffman.iml                   # IntelliJ IDEA module file
```

---

## Compression Results

Tested on two real text files:

| File | Original Size | Compressed Size | Reduction |
|------|--------------|-----------------|-----------|
| `shortFile.txt` (short paragraph) | 3,541 bytes | 2,128 bytes | **~40%** |
| `olivertwist.txt` (full novel) | 215,709 bytes | 203,706 bytes | **~5.6%** |

> The short file achieves a significantly higher compression ratio because its character distribution is more skewed (a small vocabulary), giving Huffman coding more room to assign ultra-short codes. The novel has a more uniform character distribution across a larger alphabet, which is a known characteristic of Huffman coding on natural language corpora.

---

## Getting Started

### Prerequisites

- Java 8 or higher
- IntelliJ IDEA (recommended) or any Java IDE

### Run the GUI

1. Clone the repository:
   ```bash
   git clone https://github.com/AbdullahIqbal26904/HuffmanFileCompression.git
   cd HuffmanFileCompression
   ```

2. Open the project in IntelliJ IDEA (it recognizes the `.iml` file automatically).

3. Run `src/FrontEnd/driver.java` — this launches the Swing window.

### Run the CLI

To compress a single file programmatically, run `src/backEnd/driver.java` and point it at your target file.

---

## GUI Walkthrough

```
┌─────────────────── File Compressor ───────────────────────┐
│ File ▾                                                     │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  Selected File Size:      215709 Bytes                     │
│  Percentage:              5.56% comp                       │
│  After zip/unzip size:    203706 Bytes                     │
│                                                            │
│      [ ZIP ]          [ UNZIP ]                            │
│                                                            │
│                    [ EXIT ]                                │
└────────────────────────────────────────────────────────────┘
```

**Workflow:**
1. `File → Open` — browse and select any `.txt` file
2. Click `ZIP` — compresses and writes `<filename>zip.txt`, displays % reduction
3. Click `UNZIP` — decompresses using the saved Huffman tree, writes `<filename>zipunzipped.txt`

---

## Technical Deep Dive

### Bit Packing (compress.java)

Characters are not written as full bytes. Instead, variable-length Huffman codes are packed **bit by bit** into a byte buffer, then flushed every 8 bits:

```java
for (int i = 0; i < code.length(); i++) {
    if (code.charAt(i) == '1') {
        bitBuffer = (bitBuffer << 1) | 1;
    } else {
        bitBuffer = (bitBuffer << 1);
    }
    bitCount++;
    if (bitCount == 8) {
        output.writeByte(bitBuffer);  // flush full byte
        bitBuffer = 0;
        bitCount = 0;
    }
}
```

Remaining bits are zero-padded to fill the final byte.

### Tree Serialization

Since decompression requires the same Huffman tree used during compression, the tree is **serialized to disk** (`serialize2.txt`) using Java's `ObjectOutputStream`. On decompression, it is deserialized via `ObjectInputStream`. This makes the compressed file and tree independently portable.

### Byte-Level Decoding (decompress.java)

Each byte of the compressed file is converted back to its **8-bit binary string** representation, accounting for Java's signed byte handling (values < 0 are treated as their unsigned equivalents via `256 + byte`). The bit string is then walked through the Huffman tree, emitting a character each time a leaf is reached.

---

## Concepts Demonstrated

| Concept | Where |
|---------|-------|
| Greedy algorithms | `huffman.java` — optimal substructure of Huffman tree |
| Binary trees | `huffmanNode.java` — recursive tree construction |
| Priority queues | `priorityQueue.java` — sorted linked list implementation |
| Hash maps | `hashmaps.java` — chaining + rehashing |
| Serialization | `Serializationutil.java` — Java object persistence |
| Bit manipulation | `compress.java` — bit packing with shift operators |
| GUI programming | `FrontEnd/driver.java` — Java Swing components |

---

## Author

**Abdullah Iqbal**  
Data Structures Course Project — 2024
