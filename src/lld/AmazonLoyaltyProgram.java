package lld;

/*
    ref : https://chatgpt.com/share/6922e48d-02f0-800e-9e21-222d1ac5ec63

    Requirements
    - Award points for purchases
    - Maintain a wallet per customer
    - Support tier levels (Silver, Gold, Platinum)
    - Award tier benefits based on points / spend thresholds
    - Allow point redemption
    - Track transactions (earn + redeem)

    Customer
    id
    name
    tier (Tier)
    wallet (PointsWallet)
    getTier()
    getWallet()
    upgradeTier(newTier)

    Tier
    name ("Silver", "Gold", "Platinum")
    minPoints
    benefits (List<Benefit>)
    getBenefits()
    isEligible(totalPoints)

    Benefit
    id
    description
    discountPercentage
    getDescription()
    getDiscountPercentage()

    PointsWallet
    customerId
    totalPoints
    availablePoints
    addPoints(points)
    deductPoints(points)
    getAvailablePoints()
    getTotalPoints()

    PointsCalculator (Strategy pattern)
    calculatePoints(transaction)

    Redemption
    redemptionId
    customerId
    pointsRedeemed
    benefitApplied
    getPointsRedeemed()
    getBenefitApplied()

    ==Followups==
    1️⃣ What if tiers change dynamically?
    Introduce a TierConfigService to load tiers from DB.
    No need to hardcode Silver/Gold/Platinum.

    2️⃣ How do you prevent fraud or duplicate redemptions?
    Use idempotency tokens + transaction logs + atomic updates on wallet.

    3️⃣ How do you scale point allocation for millions of shoppers?
    Move allocation + tier evaluation to async events (Kafka / SNS-SQS).

    4️⃣ How to handle partial redemptions or cashback instead of discounts?
    Add new RedemptionStrategy.
    Strategy pattern → flexible, clean interviewer gold.

    5️⃣ How do you ensure consistency of wallet points?
    Use atomic DB operations or optimistic locking.

    6️⃣ Can customers downgrade tiers?
    Either:
    Time-based evaluation (e.g., last 12 months)
    Or business rule: no downgrades
    Make this configurable.

    7️⃣ How to store benefits?
    Benefits linked to tier using a table or config file.

    8️⃣ What if different categories give different points?
    Add CategoryBasedPointsCalculator
    Use Strategy pattern.

*/
public class AmazonLoyaltyProgram {
}
