package lld;

/*
    ref : https://chatgpt.com/share/6919ab91-081c-800e-8c0e-e3153eb18cad
    User
        - id
        - username
        + getId()
        + getUsername()

    Tweet
        - id
        - userId
        - text
        - timestamp
        - likes
        + getId()
        + getUserId()
        + getText()
        + getTimestamp()
        + getLikes()

    UserRepository
        - users
        + save()
        + findById()

    TweetRepository
        - tweets
        + save()
        + findById()
        + getTweetsByUserId()

    FollowRepository
        - following
        + follow()
        + getFollowing()

    UserService
        - CRUD operations on user

    TweetService
        - tweetRepo
        + postTweet()
        + likeTweet()

    FollowService
        - followRepo
        + follow()

    FeedService
        - tweetRepo
        - followRepo
        + getTimeLine()

*/

public class Twitter {
    public static void main(String[] args) {

    }
}
