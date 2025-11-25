package lld;

/*

ref : https://chatgpt.com/share/69246c87-810c-800e-ac01-c2af878e04d2

Task Creation: Users can create tasks with a title, description, priority, and assignee.
Task Assignment: Tasks can be assigned to users and reassigned as needed.
Task Status: Tasks can have statuses such as TODO, IN_PROGRESS, DONE, etc.
Task Priority: Tasks can have priorities such as LOW, MEDIUM, HIGH.
Comments: Users can add comments to tasks.
Task Updates: Tasks can be updated (status, priority, assignee, etc.).
Task Listing: List all tasks, or filter by status, priority, or assignee.
Extensibility: Easy to add new statuses, priorities, or features.

IssueStatus
    OPEN
    IN_PROGRESS
    RESOLVED
    CLOSED

IssuePriority
    LOW
    MEDIUM
    HIGH
    CRITICAL

==State pattern==
IssueState <Interface>
    + startProgress(issue)
    + resolveIssue(issue)
    + closeIssue(issue)
    + reopenIssue(issue)
    + getStatus()

OpenState
    + startProgress(issue)
    + resolveIssue(issue)       (not allowed)
    + closeIssue(issue)         (not allowed)
    + reopenIssue(issue)        (not allowed)
    + getStatus()

InProgressState
    + startProgress(issue)      (not allowed)
    + resolveIssue(issue)
    + closeIssue(issue)         (not allowed)
    + reopenIssue(issue)
    + getStatus()

ResolvedState
    + startProgress(issue)      (not allowed)
    + resolveIssue(issue)       (already resolved)
    + closeIssue(issue)
    + reopenIssue(issue)
    + getStatus()

ClosedState
    + startProgress(issue)      (not allowed)
    + resolveIssue(issue)       (not allowed)
    + closeIssue(issue)         (already closed)
    + reopenIssue(issue)
    + getStatus()

==Strategy pattern==
SearchStrategy
    + search(issues, filters)

BasicSearchStrategy
    + search(issues, filters)

AdvancedSearchStrategy
    + search(issues, filters)

FullTextSearchStrategy
    + search(issues, filters)

SearchService
    - strategy (SearchStrategy)
    + setStrategy(strategy)
    + search(filters)

Issue
    - id
    - title
    - description
    - priority (IssuePriority)
    - reporter (User)
    - assignee (User)
    - state (IssueState)
    - comments (List<Comment>)
    - history (List<IssueHistory>)
    + startProgress()
    + resolveIssue()
    + closeIssue() -> this.state.closeIssue(this);
    + reopenIssue()
    + addComment(comment)
    + addHistory(event)
    + getStatus()

*/
public class IssueManagement {
}
