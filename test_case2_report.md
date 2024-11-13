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


| **Test Data** | **Expected result** | **Actual result** | **Outcome** |
| --- | --- | --- | --- |
| -1 | Error message - 'Negative quanity entered' | Error message - Quantity can not be less than 0 | PASSED |
| 0 | 11.0 | 11.0 | PASSED |
| 1 | 22.0 | 22.0 | PASSED |
| DoubleMaxValue | Error message - 'There is currently not enough in stock' | 22.0 | FAILED |
| 1.0 | 11.0 | 22.0 | FAILED |
| 'three' | Error message - 'Invalid quantity entered' | 22.0 | FAILED |
