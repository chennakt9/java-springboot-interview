package lld;

/*
    ref : https://chatgpt.com/share/69305234-d0c0-800e-b974-d9e825432130


    normalizedPrice = 1 - (hotel.price / maxPrice)
    normalizedRating = hotel.rating / 5
    normalizedDistance = 1 - (hotel.distance / maxDistance)

    score =
  (normalizePrice * weightPrice) +
  (normalizeRating * weightRating) +
  (normalizeDistance * weightDistance)

    Strategy Price Rating Distance
    Default	0.4	0.4	0.2
    Budget	0.7	0.2	0.1
    Luxury	0.1	0.6	0.3
    Small City	0.8	0.1	0.1



*/
public class HotelRankingSystem {
}
