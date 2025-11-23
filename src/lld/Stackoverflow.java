package lld;

/*
    ref : https://chatgpt.com/share/6922a725-e628-800e-80c9-002cd031f895

    Requirements
    - A valid user  can post a question.
    - A valid user can answer a question.
    - Upvote/downvote questions and answers.
    - Add comment to an answer.
    - Search for a question (by title, tags, text).

    User
    - id
    - name
    - reputation
    - List<Question> questionsAsked
    - List<Answer> answersGiven

    Question
    - id
    - title
    - body
    - User author
    - List<Answer> answers
    - List<Tag> tags
    - int upvotesCount
    - int downvotesCount
    - List<Comment> comments

    Answer
    - id
    - body
    - User author
    - Question
    - int upvotes
    - int downvotes
    - List<Comment> comments

    Comment
    - id
    - body
    - User author

    Tag
    - id
    - name

    Vote
    - userId
    - entityId (question or answer)
    - VoteType type → UP/DOWN

    Followups:
    1. How do you avoid duplicate votes?
    Use a Vote table with (userId, entityId) as a composite key.

    2. How do you scale searching?
    Move to:
    ElasticSearch / Solr for full-text search
    Index title, body, tags
    Eventually consistent indexing pipeline

    3. How do you ensure ordering of answers?
    Sorted by:
    Upvotes (Hot score)
    Time
    Weighted scoring (StackOverflow uses Bayesian scoring)

    4. How to handle high concurrency on voting?
    Use atomic counters
    At scale: use Redis atomic increments
    Periodic sync to DB

    5. How to design reputation?
    Listeners:
    On upvote → +10
    On answer accepted → +15
    Maintain ReputationService

    6. Can a user delete a question?
    Answer:
    Only if no accepted answers (typical SO rule)
    Soft delete using a flag

    Write sharding
    - Questions sharded by question ID
    - Answers sharded by their question ID (hot key protection may be needed)

    Imagine sharding by tag:
    - All Java questions → same shard
    - All Python questions → same shard
    - That shard catches fire

    So it is better to choose questionId as shardKey (because it is evenly distributed

*/
public class Stackoverflow {
}
