# Bonus Task — Edge Removal from an MST

Java + Maven implementation  
Repository: https://github.com/1B0-d/BonusTask

---

## Overview

This project implements the bonus task **“Edge Removal from an MST”**.

The program:

- Builds a **Minimum Spanning Tree (MST)** from a given weighted undirected graph.
- Displays all MST edges and their total weight.
- Removes one edge from the MST.
- Shows the connected components after the removal.
- Finds a **replacement edge** that reconnects the components while keeping the result a valid MST.
- Displays the new MST and its total weight.
- Saves all results to `output.json`.

All input graphs are loaded from `inputs.json`.

---

## Project Structure

```text
BonusTask/
│── pom.xml
│── README.md
│── inputs.json
│── output.json          (generated after running)
│
└── src/
    └── main/
        └── java/
            └── mst_edge_removal/
                └── Main.java
```

---

## Requirements

- Java 23+
- Maven 3.8+

Check installation:

```bash
java -version
mvn -version
```

---

## How to Run

### Option 1 — IntelliJ IDEA

1. Clone the repository:
   ```bash
   git clone https://github.com/1B0-d/BonusTask
   ```
2. Open the project in IntelliJ IDEA.
3. Make sure `inputs.json` is located in the project root (same folder as `pom.xml`).
4. Run:
   ```text
   src/main/java/mst_edge_removal/Main.java
   ```
5. Results will appear:
   - in the **Run** console;
   - in the generated file `output.json`.

---

### Option 2 — Maven (Terminal)

1. Clone the project:
   ```bash
   git clone https://github.com/1B0-d/BonusTask
   cd BonusTask
   ```

2. Build the project:
   ```bash
   mvn package
   ```

3. Run the program:
   ```bash
   java -cp target/BonusTask-1.0-SNAPSHOT.jar mst_edge_removal.Main
   ```

After execution:

- The console will show:
  - Initial MST edges and total weight  
  - Removed edge  
  - Components after removal  
  - Replacement edge  
  - New MST edges and total weight  

- A file `output.json` will be created or overwritten.

---

## Input Format (`inputs.json`)

The file contains multiple graphs in the following format:

```json
{
  "graphs": [
    {
      "id": 1,
      "n": 6,
      "edges": [
        { "u": 1, "v": 2, "w": 4 },
        { "u": 1, "v": 3, "w": 3 }
      ]
    },
    {
      "id": 2,
      "n": 5,
      "edges": [
        { "u": 1, "v": 2, "w": 3 },
        { "u": 2, "v": 3, "w": 1 }
      ]
    }
  ]
}
```

Fields:

- `id` — identifier of the graph (for reporting).
- `n` — number of vertices.
- `edges` — array of edges, each with:
  - `u` — first vertex (1-based index),
  - `v` — second vertex (1-based index),
  - `w` — edge weight (integer).

Currently, the program processes the **first graph in the list**:

```java
InputGraph input = wrapper.graphs.get(0);
```

To run the algorithm on another graph, change the index `0` to `1`, `2`, etc.

---

## Output Format (`output.json`)

The program generates a JSON report describing all important steps:

```json
{
  "graphId": 1,
  "initialMstEdges": [
    { "u": 2, "v": 3, "w": 1 },
    { "u": 1, "v": 3, "w": 3 }
  ],
  "initialMstWeight": 4,
  "removedEdge": { "u": 1, "v": 3, "w": 3 },
  "components": [
    [1],
    [2, 3]
  ],
  "replacementEdge": { "u": 1, "v": 2, "w": 4 },
    "newMstEdges": [
    { "u": 2, "v": 3, "w": 1 },
    { "u": 1, "v": 2, "w": 4 }
  ],
  "newMstWeight": 5
}
```

Fields:

- `graphId` — ID of the processed graph from `inputs.json`.
- `initialMstEdges` — edges of the MST before removal.
- `initialMstWeight` — total weight of the initial MST.
- `removedEdge` — edge removed from the MST.
- `components` — list of connected components (each is a list of vertex indices).
- `replacementEdge` — edge chosen to reconnect the components.
- `newMstEdges` — edges of the new MST after reconnection.
- `newMstWeight` — total weight of the new MST.

---

## Algorithm Summary

### 1. Building the MST (Kruskal’s Algorithm)

- All edges are sorted in non-decreasing order by weight.
- A Disjoint Set Union (DSU) structure is used to detect cycles.
- Edges are added in order if they connect two different DSU sets.
- The process stops when `n - 1` edges are added to the MST.

Result:  
A list of MST edges (`initialMstEdges`) and their total weight (`initialMstWeight`).

### 2. Removing One Edge

- From the MST, the **heaviest edge** is selected.
- This edge is removed from the MST.
- The removed edge is stored as `removedEdge`.

After this step, the MST becomes a forest with exactly **two connected components**.

### 3. Computing Components

- The remaining MST edges are used to build an adjacency list.
- A BFS is run to assign a component index to each vertex.
- Vertices are grouped into a list of components (`components`), using 1-based vertex indices.

### 4. Finding a Replacement Edge

Among all original edges in the input graph:

- The program finds all edges that connect **two different components**.
- Among these, it selects the edge with the minimum weight.
- This edge is stored as `replacementEdge`.

By the **cut property** of MSTs:

> For any cut of the vertices, the lightest edge crossing that cut is always part of some MST.

So the selected replacement edge guarantees that the resulting structure is still a valid MST.

### 5. Building the New MST

- The replacement edge is added to the remaining edges of the MST.
- The total weight of the new MST is recomputed as `newMstWeight`.
- The list of current MST edges is stored as `newMstEdges`.

Both the initial MST and the new MST are also printed to the console.

---

## Code Report

This implementation:

- Uses **Kruskal’s algorithm** with a **Disjoint Set Union (DSU)** to construct the MST.
- Correctly handles:
  - input parsing from JSON (`inputs.json`),
  - console output of all important intermediate steps,
  - JSON reporting into `output.json`.
- Demonstrates:
  - how removing a single edge from an MST splits the tree into two components,
  - how to detect these components using BFS,
  - how to find the lightest edge reconnecting them using the MST cut property.
- Follows the assignment requirements:
  - builds the MST,
  - displays the MST before removal,
  - removes one edge,
  - shows components after removal,
  - finds and outputs a replacement edge,
  - displays the new MST,
  - is provided in a GitHub repository with clear run instructions.


