| **UC 3** |    **Increasing the amount of an existing product.** |
| --- |     --- |
| **Related Requirements** | AT-14 |
| **Initiating Actor** | Warehouse staff member |
| **Actors Goal** | Increasing the amount of an existing product in the warehouse. |
| **Participating Actors** | The “add product” button, information fields, warehouse database/file.|
| **Preconditions** | An amount of the product already exists in the warehouse.  |
| **Postconditions** | The amount of the product has been increased by the added amount. |
**Flow of Events for Main Success Scenario**
| Direction | Event |
| --- | --- |
| **->** | 1. Warehouse staff members fill in the information fields to match with the chosen products information, differing only by the amount. |
| **<-** | 2. Warehouse staff member presses the “Add product” button. |
| **->** | 3. The amount of the product is updated. |
**Flow of Events for Extensions (Alternate Scenarios)**
The entered product doesn’t have an entry in the database.
| Direction | Event |
| --- | --- |
| **->** | 1a. Warehouse staff members fill in the information fields to match with the chosen products information. |
| **<-** | 2a (a) No existing entry for the product is found. (b) A new entry gets created. |
