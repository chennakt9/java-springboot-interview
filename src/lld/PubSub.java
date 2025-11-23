package lld;

/*
    ref : https://chatgpt.com/share/6922f71b-acb0-800e-ad92-cf056ab23406

    Event
    - id
    - payload
    - type
    - createdAt
    + getId()
    + getPayload()
    + getType()

    EventQueue
    + push(event)
    + pull(batchSize)
    + ack(eventId)
    + retry(event)

    BatchProcessor
    - batchSize
    - eventHandler (EventHandler)
    + processBatch(events)

    EventHandler
    + handle(event)

    RetryPolicy
    - maxRetries
    - backoffStrategy
    + shouldRetry(event)
    + nextBackoffTime(event)


*/
public class PubSub {
}
