| **UC 1** |    **Select product name** |
| --- |     --- |
| **Related Requirements** | AT-1 |
| **Initiating Actor** | Cashier |
| **Actors Goal** | Select a product from the stock by name and add it to the ‘POS Product’ submenu |
| **Participating Actors** | ShoppingCart, SalesSystemDAO |
| **Preconditions** | The SalesSystemDAO (InMemorySalesSystemDAO) is non-empty |
| **Postconditions** | 1. The product name is displayed in the ‘Name’ field of ‘POS Product’ submenu<br/>2. The barcode is displayed in the ‘Barcode’ field of ‘POS Product’ submenu<br/>3. The price is displayed in the ‘Price’ field of ‘POS Product’ submenu|



**Flow of Events for Main Success Scenario**

| Direction | Event |
| --- | --- |
| **->** | 1. Cashier selects a product from the list |
| **<-** | 2. Product name is displayed in the ‘Name’ field |
| **<-** | 3. Barcode is displayed in the ‘Barcode’ field |
| **<-** | 4. Price is displayed in the ‘Price’ field |

**Flow of Events for Extensions (Alternate Scenarios)**

| Direction | Event |
| --- | --- |
| **<-** | 1. The required item is not in the stock list |
| **->** | 2. New item is entered from the Warehouse submenu |