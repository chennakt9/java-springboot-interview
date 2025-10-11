### Singleton class
- bill-pugh (static holder) method
- double-checked lock
- enum singleton

### Immutable class
- make class final
- all class variables as private

### Q : Explain the internal working of ConcurrentHashMap in Java 8.
- In java 7
  - It was actually divided into multiple segments (like mini-HashMaps).
  - Each segment had its own -> Hash table (buckets)
  - So instead of locking the whole map for every update, only one segment was locked.
  - multiple threads can update different segments concurrently.
- In java 8
  - CAS (Compare-And-Swap), Used to insert into an empty bucket.
  - If a bucket already has entries, threads use synchronized on that bucket’s head node.

### Difference between synchronized, ReentrantLock and ReadWriteLock.
- synchronized → Built-in keyword that locks a method/block so only one thread at a time can run it.
- ReentrantLock → A flexible lock class that supports advanced features like tryLock, fairness, and interruptible waits. 
- ReadWriteLock → A lock that lets many readers access simultaneously but gives exclusive access to writers.

### Garbage collection
- Young (eden space, survivor from, survivor to) and old generation
- Minor gc - young generation (more frequently runs)
- Major gc - young + old
- Why do we need to divide heap in young and old ?
  - Most objects survive only for a short period of time (Weak generational hypothesis)
  - So we need a separate section where gc runs frequently, and clearing up the garbage. and longer surviving will go to old generation
- Algorithms:
  - Mark and Sweep: Mark reachable objects, sweep away the rest. (100MB)
  - Compact Mark and Sweep: After sweeping, compacts objects to avoid fragmentation. (2GB)
  - G1 GC (Garbage First) (>2GB)
    - Splits heap into regions, each region can be anything (eden, s1, old etc...)
    - collects priority regions first. 
- XX:MaxGCPauseMillis=<ms> — desired max pause; G1 will try to respect it.
- Common problems and solutions
  - Long or growing pauses : increase heap, raise MaxGCPauseMillis
  - Frequent Full GCs : increase heap size

### How does Java handle memory leaks and how would you detect them?
- Static references – Live as long as the class’s ClassLoader is active, usually until JVM shutdown.
- Unclosed resources – If streams, DB connections, or sockets aren’t closed, their memory and OS handles stay occupied.
- ThreadLocals – Stale values in ThreadLocal stay alive as long as the thread lives (often until thread pool ends).
- Collections that grow – Lists or Maps that keep accumulating objects prevent GC since they’re still referenced.
- Avoid string concatenation and use string builder
- How to Detect Which Type of Leak You Have
  - Heap Dump Analysis (Eclipse MAT / VisualVM)
  - Monitor Memory Trends (Constant increase in heap usage even after GC cycles.)
  - Analyze GC Logs (Frequent Full GCs, Increasing Old Gen occupancy)

### Explain Java memory model (heap, stack, metaspace) and garbage collection tuning.
- Heap
  - Created at JVM startup; shared by all threads.
  - Divided into young generation (new objects) and old generation (old objects)
  - Garbage Collector (GC) works mostly here.
- Stack
  - Each thread has its own stack.
  - Stores local variables, method calls, and partial results.
  - When a method is called → new stack frame.
  - When method ends → frame popped.
  - Objects are not stored here (only references to them).
  - Stack = fast, small, thread-local.
- Metaspace
  - Class metadata area
  - Stores class definitions, method metadata, and reflection info.
  - Grows dynamically (limited by system memory).
  - -XX:MaxMetaspaceSize=256m

### Difference between HashSet, LinkedHashSet, TreeSet
- HashSet : No guaranteed order; elements are stored based on their hash code.
- LinkedHashSet : Maintains insertion order (the order in which elements were added).
- TreeSet : Maintains natural ordering or custom ordering (via a Comparator).

### Difference between HashMap, HashTable, and ConcurrentHashMap
- HashMap → Not thread-safe, allows one null key and many null values.
- Hashtable → Thread-safe (synchronized), but doesn’t allow any nulls and is slower.
- ConcurrentHashMap → Thread-safe with fine-grained locking, no nulls, and much faster under concurrency.

### Explain volatile and synchronized in multithreading
- volatile : Keyword that ensures visibility of changes across threads.
- synchronized : Only one thread can execute a synchronized block/method at a time

### Deep copy vs Shallow copy
- Shallow Copy : Copies the object’s top-level structure only
- Deep Copy : Copies everything recursively

### Explain difference between abstract class and interface (Java 8 default/static methods).
- Abstract Class
  - Can have abstract + concrete methods
  - Can have instance variables (with any access modifiers)
  - can define constructors
- Interface
  - Can have abstract, default, and static methods
  - Can have only public static final constants
  - No constructors allowed
