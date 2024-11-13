## System-level functional testing plan for POS
<br>

|No|Functionality to be verified|Assignee|Usage frequency of the functionality|Possible damage for business on failure|Priority|Estimated test effort in personhours|
|---|:---:|---:|---:|---:|---:|---:|
|1|Test entering a product's name does select it from the warehouse for purchase.|Ahto|High|High|High|Low|
|2|Test entering the amount of a product being purchased for correct total sum calculation.|Andrey|High|High|High|Medium|
|3|Test confirming the current purchase will finish it successfully.|Darius|High|High|High|Low|
|4|Test canceling the current purchase will stop the current purchase.|Romet|High|High|High|Low|
|5|Test checking the current shopping cart will show all current items.|Ahto|Medium|High|Medium|Medium|
|6|Test receiving a notification when entering more of a product than available.|Andrey|High|High|High|Low|
|7|Test finished purchases updating the warehouse status.|Darius|High|High|High|Medium|
|8|Test viewing the warehouse status.|Romet|High|Medium|High|Low|
|9|Test entering a product's barcode.|Ahto|High|High|High|Low|
|10|Test entering a product's name.|Andrey|High|High|High|Low|
|11|Test entering a product's quantity.|Darius|High|High|High|Low|
|12|Test entering a product's price.|Romet|High|High|High|Low|
|13|Test refreshing the warehouse status page.|Ahto|High|Medium|High|Low|
|14|Test entering changing the quantity of the existing product.|Andrey|High|High|High|Medium|
|15|Test showing the total cost sum of every product in the shopping cart.|Darius|High|Medium|High|Medium|
|16|Test viewing the entire purchase history.|Romet|Low|Low|Low|High|
|17|Test viewing purchase history between a certain dates.|Ahto|Medium|Low|Low|High|
|18|Test viewing the last 10 purchases.|Andrey|Medium|Low|Medium|Medium|
|19|Test showing the warehouse contents in CLI.|Darius|Medium|Low|Medium|Low|
|20|Test showing the cart contents in CLI.|Romet|Low|Low|Low|Low|
|21|Test resetting the cart contents in CLI.|Ahto|Low|Low|Low|Low|
|22|Test the CLI displaying a message after completing a command.|Andrey|High|Low|Low|High|
|23|Test the GUI displaying error messages in the same position.|Darius|Medium|Low|Low|High|
|24|Test the GUI displaying the start date above the end date in the History tab.|Romet|High|Low|Low|Low|
|25|Test the row of the currently shown purchase being highlighted in the history tab.|Ahto|High|Low|Low|Low|
|26|Test entry fields being emptied automatically after a product is added to the cart.|Andrey|High|Low|Medium|Low|
|27|Test the current tab's state being saved after moving to a different tab.|Darius|High|Low|Medium|High|

**Estimation on personhours:**

|Category|Personhours|
|---|---|
|Low|0.5 - 1 h|
|Medium| 1 - 2 h|
|High|2 - 5 h|