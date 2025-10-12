Q : I was given two methods, one synchronized, one static synchronized - both with infinite loops and launched via separate threads.
    I was asked whether this setup could cause a 𝗱𝗲𝗮𝗱𝗹𝗼𝗰𝗸, and why. \
A : Instance (synchronized) methods lock on the object instance (this). Static (synchronized) methods lock on the Class object (ClassName.class).
    A deadlock happens when two or more threads are waiting for locks held by each other, creating a cycle where none can proceed.
     These locks are completely separate, Deadlock cannot occur in this setup.

### Explain CopyOnWriteArrayList → when to use, when not to use.
- What it is
    - CopyOnWriteArrayList is a thread-safe variant of ArrayList
    - Reads (like get()) are superfast and don’t require locks.
    - Writes (like add(), remove()) create a new copy of the underlying array.
- When to use
    - Read-heavy, write-light scenarios
- When NOT to use
    - Write-heavy scenarios

- Explain ThreadLocal – how it works internally, pitfalls.
- Pitfalls
    - If you use thread pools (like in ExecutorService) and don’t remove values
    - ThreadLocal values can stick around for the lifetime of the thread, which is potentially forever in a thread pool.
    - This happens because the thread never dies, so the ThreadLocalMap keeps the value.

- How does ForkJoinPool differ from a regular ThreadPoolExecutor?
    - ThreadPoolExecutor
        - General-purpose task execution (short or long tasks)
    - ForkJoinPool
        - Divide-and-conquer, recursive, fine-grained tasks (like parallel algorithms)
        - work-stealing

- Explain Deadlock, Livelock, Starvation → how to prevent them in Java.
  - Deadlock
  ```
    class DeadlockExample {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();
    
        public void method1() {
            synchronized (lock1) {
                System.out.println("Thread 1: Holding lock1...");
                synchronized (lock2) {
                    System.out.println("Thread 1: Holding lock2...");
                }
            }
        }
    
        public void method2() {
            synchronized (lock2) {
                System.out.println("Thread 2: Holding lock2...");
                synchronized (lock1) {
                    System.out.println("Thread 2: Holding lock1...");
                }
            }
        }
    }
  ```
  - How to Prevent Deadlocks
    - Consistent Lock Ordering
    - Try-Lock with Timeout (ReentrantLock)
  - Livelock
    - When we try to avoid deadlock, we can introduce a delay
    - after which it releases the currently holding lock.
    - both threads t1, and t2 released locks l1 and l2,
    - after sometimes they again try to aquire the locks at time same time
    - this cycle continues. which is known as livelock
  - How to Prevent Livelocks
    - Use backoff strategies (random delays before retrying).
    - Limit retries instead of spinning forever.
- Starvation
  - To avoid starvation, use fair lock like : ReentrantLock lock = new ReentrantLock(true);

- Explain how CompletableFuture improves async programming.
  - Future
    - runs on a separate thread, but when we call .get method it waits, so it is a blocking call
  - CompletableFuture
    - same runs on a separate thread, but it won't wait for result, we give callback which will execute after we get the result
- What is the difference between WeakReference, SoftReference, PhantomReference?
  - WeakReference → “I want it cached but it’s OK if it disappears.”
  - SoftReference → “Keep it as long as memory is healthy.” 
  - PhantomReference → “Tell me exactly when it’s gone so I can clean up.”

- Difference between process and thread
  - Process: Independent program with its own memory; heavier; isolated.
  - Thread: Lightweight unit within a process; shares memory; faster to create; crash can affect the whole process.

- How to create a thread in Java? (Thread vs Runnable vs Callable)
  - Thread: Extend Thread class and override run() to create a thread.
  - Runnable: Implement Runnable and pass it to Thread for execution.
  - Callable: Implement Callable, submit to ExecutorService, and get a result via Future.

- Java Thread Lifecycle
  - New (Created): Thread object is created but not started
  - Runnable: Thread is ready to run, waiting for CPU scheduling
  - Running: Thread is executing its run() method.
  - Blocked/Waiting: Thread is paused, waiting for a lock or another thread to notify.
  - Terminated (Dead): Thread has finished execution or stopped.

- Difference between sleep(), yield(), and join()
  - sleep : Pauses current thread for given time
  - yield : Suggests scheduler to give other threads a chance
  - join : Waits for another thread to finish

- Difference between wait() and sleep()
  - sleep : Pauses the current thread for given time
  - wait : Makes current thread wait until notified

- What is a Daemon thread?
  - A Daemon thread is a background thread that runs in the background to support user threads and automatically terminates when all user threads finish.
  - Example: Garbage Collector, Timer tasks.
  - Created by thread.setDaemon(true) before start().

- What is the ThreadGroup class?
  - The ThreadGroup class in Java is used to group multiple threads together so you can manage them as a single unit
  - Threads can belong to a group when created: new Thread(group, runnable).

- What is thread-safety?
  - Thread-safety means that a piece of code or data structure can be safely used by multiple threads at the same time without causing inconsistent data or unexpected behavior.

- Difference between synchronized method and block
  - Synchronized Method
    - Entire method
    - Locks the object (this) or class (if static)
  - Synchronized Block
    - Only a specific code block
    - Can lock any object explicitly

- ReentrantLock
  - A lock from java.util.concurrent.locks that allows a thread to acquire the same lock multiple times without deadlocking.
  - TL;DR: synchronized = easy & automatic, ReentrantLock = manual & flexible.

- Difference between volatile keyword and synchronized
  - volatile
    - Ensures visibility of changes to variables across threads
    - No locking, doesn’t block threads
  - synchronized
    - Acquires a lock, only one thread can enter

- What are atomic variables?
  - java.util.concurrent.atomic
  - AtomicInteger, AtomicLong, AtomicReference
  - They allow compound operations like incrementAndGet() or compareAndSet() to happen atomically, without using synchronized.

- How to detect and prevent deadlocks?
  - Detection
    - Use thread dumps (jstack) to see which threads are blocked and waiting for locks.
    - Deadlock detection tools: JVM profilers or thread analyzers can alert if deadlock occurs.
  - Prevention techniques:
    - Lock ordering: Always acquire multiple locks in the same order across threads.
    - TryLock with timeout: Use ReentrantLock.tryLock(timeout) to avoid waiting forever.
    - Avoid nested locks: Minimize locking multiple resources at once.

- Difference between ExecutorService and manual thread creation
  - manual thread creation
    - Expensive — new thread for each task → more overhead.
  - ExecutorService
    - Efficient — reuses threads from a pool.
  
- What are Future and CompletableFuture?
  - Future
    - get() blocks until result is ready
    - Usually used with ExecutorService.submit()
  - CompletableFuture
    - Can be non-blocking using callbacks like thenApply(), thenAccept()
    - uses ForkJoinPool by default
  
- What is ForkJoinPool?
  - You give it a task (usually a subclass of RecursiveTask or RecursiveAction).
  - The task recursively splits itself into smaller ones.
  - Each subtask runs on a worker thread.
  - When subtasks finish, results are joined together.

- Difference between parallelStream() and normal stream
  - normal stream
    - Main thread only
    - Better for small or I/O-bound tasks
    - .stream().forEach(System.out::println)
  - parallelStream
    - Uses ForkJoinPool.commonPool() internally
    - Better for small or I/O-bound tasks
    - .parallelStream().forEach(System.out::println)

- Difference between ConcurrentHashMap and Hashtable
  - Hashtable
    - Locks the entire map for every operation
  - ConcurrentHashMap
    - Locks only part of the map (bucket-level locking

- What are BlockingQueue and LinkedBlockingQueue?
  - BlockingQueue
    - Interface
  - LinkedBlockingQueue
    - Concrete class
    - A FIFO implementation of BlockingQueue backed by linked nodes. Can have optional capacity and uses separate locks for producers and consumers for better concurrency.

- What is ThreadLocal?
  - Each thread gets its own isolated copy of a variable
  - Used for things like per-thread contexts (e.g., request IDs, DB connections, formatters).

- What is the happens-before relationship in JVM memory model?
  - It defines when one action’s result is guaranteed to be visible to another thread.

- Difference between parallelism and concurrency
  - Concurrency: multiple tasks making progress at the same time (can be on a single core, interleaved).
  - Parallelism: multiple tasks literally running at the same time on multiple cores.
