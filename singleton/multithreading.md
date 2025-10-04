Q : I was given two methods, one synchronized, one static synchronized - both with infinite loops and launched via separate threads.
    I was asked whether this setup could cause a 𝗱𝗲𝗮𝗱𝗹𝗼𝗰𝗸, and why. \
A : Instance (synchronized) methods lock on the object instance (this). Static (synchronized) methods lock on the Class object (ClassName.class).
    A deadlock happens when two or more threads are waiting for locks held by each other, creating a cycle where none can proceed.
     These locks are completely separate, Deadlock cannot occur in this setup.