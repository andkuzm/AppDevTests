| **Test Case 1** |    **Update product quantity** |
| --- |     --- |
| **Test ID** | tc1 |
| **Related Requirements** | AT-14 |
| **Prerequisite** | Existing StockItem 'Lays Chips' with quantity 5|
| **Input1** | (-1, 0, 1, MaxInteger-x+1) |
| **Input2** | (double, non-numerical) |
| | |
| **Step 1** | Open Sales system GUI |
| **Step 2** | Click on Warehouse tab |
| **Step 3** | Enter '1' to Bar Code field|
| **Step 4** | Press 'Tab'|
| **Step 5** | Add new quantity from Input|
| **Step 6** | Click 'Add product'|
| **Step 7** | Record new quantity from Warehouse status under 'Quantity' column|

| **Test Data** | **Expected result** | **Actual result** | **Outcome** |
| --- | --- | --- | --- |
| -1 | Error message - Negative quantity entered | Error message - Invalid quantity entered | PASSED |
| 0 | 5 | 5 | PASSED |
| 1 | 6 | 6 | PASSED |
| 2147483643 | Error message - quantity too large | -2147483647 | FAILED |
| 2.0 | Error message - Invalid quantity entered | Error message - Invalid quantity entered | PASSED |
| 'three' | Error message - invalid quantity entered | Error message - invalid quantity entered | PASSED |