package lld;

import java.util.*;

/*
    ref : https://leetcode.com/discuss/post/2312435/amazon-lld-notification-system-by-pravee-moxy/
    ref : https://chatgpt.com/c/6921b0bf-fed4-8320-8ac4-70617b7884d9

    Message
        - type ("sms", "email", "whatsapp")
        - content
        - to
        + getType()
        + getContent()
        + getTo()

    MessageHandler
        + send(message)

    EmailHandler
        + send(message)

    SMSHandler
        + send(message)

    WhatsAppHandler
        + send(message)


    Followup questions:
    1. If a new type comes, e.g., Push Notification — how to avoid changing NotificationService?
        - Use Dependency Injection + Auto-registration
    2. What if message content varies? Should Message be abstract?
        - Make Message an abstract base class, with subclasses
    3. Should MessageServiceImpl implement an interface?
        - Why?
        - To support plug-and-play implementations
        - To mock in tests
    4. User wants to receive messages via multiple channels. One message or two?
        - Multiple messages
    5. Do you need to abstract both Message and MessageHandler?
        - Message varies by channel → needs polymorphism
        - Handler varies by channel → needs polymorphism
    6. How to add urgency also here. High, Medium, Low ?
        - strategy to act differently based on urgency.
        - strategy.send(message)
    7. Fallback Channels
        - Chain of Responsibility
    8. Scaling to millions of messages
        - queue.send()
    9. tracking Status
        - high urgency messages, update status immediately
        - for low ones, we use async, so update from a webhook
    10. Scheduling Notifications (ex : at tomorrow 10 AM)
        - store in DB, background thread polls
*/

class Message {
    private final String notificationType;
    private final String content;
    private final String to;

    public Message(String type, String content, String to) {
        this.notificationType = type;
        this.content = content;
        this.to = to;
    }

    public String getType() { return notificationType; }
    public String getContent() { return content; }
    public String getTo() { return to; }
}

interface MessageHandler {
    void send(Message message);
}

class EmailHandler implements MessageHandler {
    @Override
    public void send(Message message) {
        System.out.println("EMail sent to " + message.getTo() + " : " + message.getContent());
    }
}

class SMSHandler implements MessageHandler {
    public void send(Message message) {
        System.out.println("SMS sent to " + message.getTo() + " : " + message.getContent());
    }
}

class WhatsAppHandler implements MessageHandler {
    public void send(Message message) {
        System.out.println("WhatsApp sent to " + message.getTo() + " : " + message.getContent());
    }
}

interface NotificationService {
    void sendNotification(Message message);
}

class NotificationServiceImpl implements NotificationService {
    private final Map<String, MessageHandler> handlerMap = new HashMap<>();

    public NotificationServiceImpl() {
        handlerMap.put("email", new EmailHandler());
        handlerMap.put("sms", new SMSHandler());
        handlerMap.put("whatsapp", new WhatsAppHandler());
    }

    @Override
    public void sendNotification(Message message) {
        MessageHandler handler = handlerMap.get(message.getType());

        handler.send(message);
    }
}

public class NotificationDemo {
    public static void main(String[] args) {
        NotificationService service = new NotificationServiceImpl();

        service.sendNotification(new Message("email", "Hi via Email", "abc@gmail.com"));
        service.sendNotification(new Message("sms", "Hi via SMS", "9000000000"));
        service.sendNotification(new Message("whatsapp", "Hi via WhatsApp", "9000000000"));
    }
}