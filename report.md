# Report for the project #

## UI components ##

For UI components we have chosen to use Activity, for screens that manage all other screens (navigation destinations).
We chose to use Fragments for most of the screens, as that worked best with Navigation Component from Android Jetpack.

We have used ViewModels for fetching/working with data between external-services, our database and Activities/Fragments.
By using this we don't need to manage subscribing/un-subscribing in UI components, and gain the benefit of only fetching data when the "subscriber" is active (visible).

At the start of the project we tried to handle navigation between UI components ourselves, but encountered issues with handling "back-button"-behaviour in Fragments.  
To solve this and gain a more scalable solution for navigation, we chose to use Navigation Component included in Android Jetpack.

We also implemented date and time on a transaction in the database and on the Transactions-screen.


## Patterns ##

We tried to follow the MVVM pattern, with some exceptions. 


## Our choices ##

- Hiding wallets with a total USD value below 1 USD.
- Usage of extra "domain model"-class(es) for conversion from fetched data to "something we can work with". 
- Transformation of "wallet" and "transaction" into representational classes, (OwnedWallet, UserTransaction) to display in RecyclerView.
- Deactivate sell-button if you have less than 1 USD worth of the selected crypto-currency.
- Use of Navigation Component for navigation.
- We made the decision that you can only buy and sell at 1 dollar increment, and not buy or sell with cents
- We chose to have 2 tables for the database. One for the transactions and one for the Wallets of the crypto you have.
  We made these decision for more efficient fetching of the amount you have of one type, and not so you need to go
  through all transactions to get the correct amount of 1 crypto.
  

## Technologies / Frameworks ##

- `coreLibraryDesugaring` to use LocalDateTime with lower SDK then 26
- `room` to connect to Sql Lite Database and generate sql schema and tables
- `glide` to fetch photos async from CoinCap static assets
- `retrofit` to send http calls to CoinCap API in CoRoutines
- `moshi` to unMarshal API Json Response to kotlin objects with properties
- `androidx.navigation` to handle navigation in fragments
- `androidx.fragment` to use fragments in project
- `androidx.lifecycle` to use viewModel and livedata in project
- `viewBinding` to bind view to data
- `Coroutines` to run tasks async on a separate thread
- `Standard libraries` to run a kotlin project with the JVM


## Alternatives ##

- We could have handled buying/selling crypto-currency for less than 1 USD, but that might lead to potential issues.

- By not having a "domain template with the correct types" we would need to do additional typechecking, and manual conversion to the expected types in multiple places.  

- We could have done the needed "transformations" where needed, instead of having extra classes. 

- We could have handled all parts of navigation ourselves, but it could lead to extra management and potential issues.

- We could have used a single Activity/Fragment for buy and sell, as they display similar content and do similar actions.

- We could have saved USD as not a Long and made it soo a user can buy with cents to, but didn't because saving money as
  floating number is not advised.
  
- We could have made some of the fetching of data more efficient, both form the database and the API

- We could have made it soo a user can type in to both crypto amount or dollar amount, and it reflected back on what you
  typed

- We could have made the fetching more seamless with the first screen and started fetching before you went to screen 2

- We talked about adding a sell all of the selected crypto button.


## Reflection ##

- Hiding wallets with a total USD value below 1 USD.
    - Pros: 
        - Cuts down on extra complexity. 
        - Gives a clear perception of when/why a wallet is displayed in Portfolio.
    - Cons:
        - Makes the user unable to see potential "remainders" of crypto-currency in a wallet after selling.
                
- Usage of extra "domain model"-class(es) for conversion from fetched data to "something we can work with".
    - Pros: 
        - Gives consistent conversion into the expected form, no need to remember "what type a field should be".
    - Cons:
        - Extra class(es), extra functions for conversion.

- Deactivate sell-button if you have less than 1 USD worth of the selected crypto-currency.
    - The same pros and cons of "Hiding wallets with a total USD value below 1 USD" applies here.

- Use of Navigation Component for navigation.
    - Pros: 
        - Easier navigation between Fragments after initial-setup.
        - More scalable as the amount of Fragments increase.
    - Cons:
        - Can't be used with Activities only.
        - Requires more initial-setup than just using Intent/Bundle directly.

- We made the decision that you can only buy and sell at 1 dollar increment, and not buy or sell with cents
    - Pros: 
        - Avoid confusing the consumer with situations where he/she can sell, but the only displayed amount is "0.0" etc..
        - Cuts down on extra complexity.
    - Cons:
        - Gives the consumer a confusing/"cryptic" value when he/she has a minuscule amount of a the selected crypto-currency.

- We chose to have 2 tables for the database. One for the transactions and one for the Wallets of the crypto you have. 
    - Pros: 
        - More performant on lookup(1)
    - Cons:
        - Takes up more space on the device the app runs on.

---

1. More efficient fetching of the amount you have of one type, no need to go through all transactions to get the correct amount of 1 crypto.
 