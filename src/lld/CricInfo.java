package lld;

/*
    ref : https://github.com/pari1999tosh/low-level-design/blob/main/lld/advance_lld/crickinfo_low_level_design

    ==Requirements==
    The Cricinfo system should provide information about cricket matches, teams, players, and live scores.
    Users should be able to view the schedule of upcoming matches and the results of completed matches.
    The system should allow users to search for specific matches, teams, or players.
    Users should be able to view detailed information about a particular match, including the scorecard, commentary, and statistics.
    The system should support real-time updates of live scores and match information.
    The system should handle concurrent access to match data and ensure data consistency.
    The system should be scalable and able to handle a large volume of user requests.
    The system should be extensible to accommodate new features and enhancements in the future.


    ==Entities==
    User
    Teams
    Players
    Match
    ScoreBoard

    ==functionality==
    register()
    subscribeToMatch()
    viewMatch()
    seeUpcomingMatch
    searchMatch()
    getPlayerDetails()
    updateLiveScore()


    Interface NotificationStrategy
    + notifySubscribers(match : Match, eventType : String, eventData : String)

    Class ODINotificationStrategy implements NotificationStrategy
    Class T20NotificationStrategy implements NotificationStrategy
    Class TestNotificationStrategy implements NotificationStrategy


*/
public class CricInfo {
}
