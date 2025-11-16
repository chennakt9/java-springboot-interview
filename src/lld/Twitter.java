package lld;

/*
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
