| **Test Case 2** |    **Update shopping cart total sum** |
| --- |     --- |
| **Test ID** | tc2 |
| **Related Requirements** | AT-2 |
| **Prerequisite** | ShoppingCart open with existing item|
| **Input1** | (-1, 0, 1, DoubleMaxValue) |
| **Input2** | (double, non-numerical) |
| | |
| **Step 1** | Open Sales system GUI |
| **Step 2** | Click Point-of-sale tab|
| **Step 3** | Enter '1' to 'Bar Code' field|
| **Step 4** | Press 'Tab'|
| **Step 5** | Enter '1' to 'Amount' field|
| **Step 6** | Press 'Add to cart'|
| **Step 7** | Enter '1' to 'Bar Code' field|
| **Step 8** | Press 'Tab'|
| **Step 9** | Enter value from Input list|
| **Step 10** | Press 'Add to cart'|
| **Step 11** | Record new total from shopping cart - 'Total:' field, bottom left|


| **Test Data** | **Expected result** |
| --- | --- |
| -1 | Error message - 'Negative quanity entered' |
| 0 | 11.0 |
| 1 | 22.0 |
| DoubleMaxValue | Error message - 'There is currently not enough in stock' |
| 1.0 | 11.0 |
| 'three' | Error message - 'Invalid quantity entered' |
