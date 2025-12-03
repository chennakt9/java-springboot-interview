package lld;

/*
    ref : https://chatgpt.com/share/69303add-5968-800e-bfed-dcc754e6900a

    USERS
    -----
    - id (uuid)
    - name (string)
    - avatarUrl (string)
    - status (ONLINE | OFFLINE)
    - lastSeenAt (timestamp)


    CONVERSATIONS
    -------------
    - id (uuid)
    - type (DIRECT | GROUP)
    - title (string, only for group chats)
    - createdBy (userId)
    - createdAt (timestamp)


    CONVERSATION_PARTICIPANTS
    -------------------------
    - id (uuid)
    - conversationId (uuid)
    - userId (uuid)
    - role (ADMIN | MEMBER)
    - joinedAt (timestamp)
    - lastReadMessageId (uuid)   // for unread count & read receipts


    MESSAGES
    --------
    - id (uuid)
    - conversationId (uuid)
    - senderId (uuid)
    - content (string)
    - replyToMessageId (uuid, optional)
    - createdAt (timestamp)
    - editedAt (timestamp)


    ATTACHMENTS
    -----------
    - id (uuid)
    - messageId (uuid)
    - url (string)
    - mimeType (string)
    - sizeBytes (number)


    MESSAGE_STATUS
    --------------
    - id (uuid)
    - messageId (uuid)
    - userId (uuid)
    - status (SENT | DELIVERED | SEEN)
    - updatedAt (timestamp)

*/
public class ChatSystem {
}
