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
- 