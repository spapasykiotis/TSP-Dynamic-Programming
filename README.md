
# 🧭 Parallel Travelling Salesman Problem Solver in Java

This project provides a multithreaded solution to the classic Travelling Salesman Problem (TSP) using Java. It implements a dynamic programming algorithm with memoization and divides the computational workload across multiple threads for faster execution on multicore systems.

## 🧩 Problem Statement

Given a set of cities and their coordinates, the Travelling Salesman Problem aims to find the shortest route that visits each city exactly once and returns to the starting city.

## 🚀 Features

- Parallelized using Java `Thread` class for faster computation.
- Utilizes memoization with a `ConcurrentHashMap` to avoid redundant subproblem evaluations.
- Accepts a flexible number of threads from the user.
- Accepts a dataset of cities from a file.
- Interactive or dataset-based input.

## 📦 How It Works

1. **Input Collection**
   - Number of threads is provided by the user.
   - Cities (name, latitude, longitude) are loaded from a text dataset file.
   - The user selects the starting city from the list.

2. **Distance Calculation**
   - Distance is computed using the Euclidean formula on the coordinates of each city.
   - This approximation assumes a 2D environment.

3. **Thread Assignment**
   - Each thread processes a subset of possible paths based on its range of city indices.

4. **Path Calculation**
   - Uses recursive dynamic programming with memoization to avoid recomputation.

5. **Result Selection**
   - Once all threads complete, the shortest tour is selected and printed.

## 📌 Usage

Make sure `greek_cities_dataset.csv` is in the same directory or update the code with the correct path.

### 📝 Example Interaction

```text
Enter number of threads to run in parallel:
4
Loading 25 cities from dataset...
Available starting cities:
Athens, Thessaloniki, Patras, Heraklion, ...
Enter the name of the starting city:
Athens
```

## 📁 Dataset Format

Each line in the dataset file must contain:

```
<CityName> <Latitude> <Longitude>
```

Example line:

```
Athens 37.9838 23.7275
```

## 🔧 Dependencies

- Java 8 or higher
- No external libraries required

## 🧩 Utility Classes

- **`TSP` Class**: Handles the multithreaded execution of the Travelling Salesman Problem. It manages the parallel processing of path calculations using dynamic programming with memoization.
- **`Point` Class**: Represents a city with its name and geographical coordinates, along with a thread flag array to track which threads have processed each city.

## 📊 Performance Note

- Execution time benefits significantly from multithreading, especially for larger numbers of cities.
- Memoization ensures no subproblem is solved more than once per thread.

## 📃 License

This project is open-source and licensed under the MIT License.
