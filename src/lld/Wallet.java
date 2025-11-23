package lld;

/*
    ref : https://chatgpt.com/share/692330d6-e3b8-800e-af1f-4befdaf73626

    ==Requirements==
    Create wallet for a user
    Add money / Load balance
    Deduct money
    Transfer funds (wallet → wallet)
    View balance
    View transaction history
    Prevent double spending (the wallet version of “bro, chill”)
    Transaction logging & idempotency

    ==APIs==
    POST /wallet

    Create wallet

    GET /wallet/{id}/balance
    POST /wallet/{id}/load

    Load money

    POST /transfer

    Body:

    {
      "from": "walletA",
      "to": "walletB",
      "amount": 500,
      "idempotencyKey": "abc-123"
    }

    GET /wallet/{id}/transactions


*/
public class Wallet {
}
