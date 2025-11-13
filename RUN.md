# How to Run the Project

## Requirements

- Java 23+  
- Maven 3.8+

You can check your installation with:

```bash
java -version
mvn -version
```

---

## Option 1 — Run from IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Make sure `inputs.json` is located in the project root (the same folder as `pom.xml`).
3. In the Project panel open:
   ```text
   src/main/java/mst_edge_removal/Main.java
   ```
4. Right‑click `Main.java` and choose **Run 'Main.main()'**.
5. After running, you will get:
   - output in the **Run** console;
   - a generated `output.json` file in the project root with detailed results.

---

## Option 2 — Run from Terminal (Maven)

1. Open a terminal in the project root (folder with `pom.xml`).

2. Build the project:
   ```bash
   mvn package
   ```

3. Run the program:
   ```bash
   java -cp target/BonusTask-1.0-SNAPSHOT.jar mst_edge_removal.Main
   ```

4. After execution you will get:
   - console output with:
     - initial MST,
     - removed edge,
     - components after removal,
     - replacement edge,
     - new MST;
   - a file `output.json` in the project root containing the same data in JSON format.
