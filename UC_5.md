| **UC 5** |    **cancelling the current purchase** |
| --- |     --- |
| **Related Requirements** | AT-4 |
| **Initiating Actor** | Cashier |
| **Actors Goal** | Cancel current purchase |
| **Participating Actors** | SalesSystemUI, ShoppingCart, SalesSystemDAO (InMemorySalesSystemDAO) |
| **Preconditions** | Shopping cart is non-empty |
| **Postconditions** | Shopping cart is empty |
**Flow of Events for Main Success Scenario**
| Direction | Event |
| --- | --- |
| **->** | 1. Cashier triggers "Cancel purchase" button |
| **<-** | 2. System empties the shopping cart |
| **->** | 3. Shopping cart is empty and available for next purchase |
**Flow of Events for Extensions (Alternate Scenarios)**
No alternate scenarios due to the precondition