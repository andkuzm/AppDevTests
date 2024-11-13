| **Test Case 3** |    **Test the GUI Warehouse tab displaying error messages in the same position** |
| --- |     --- |
| **Test ID** | tc3 |
| **Related Requirements** | AT-25 |
| **Prerequisite** | POS running, Warehouse tab|
| **Input** | (-1, 1.0, 2147483647, 'three') |
| | |
| **Step 1** | Open Sales systen GUI |
| **Step 2** | Click on 'Warehouse' tab |
| **Step 3** | Enter '1' to 'Bar Code' field|
| **Step 4** | Press 'Tab'|
| **Step 5** | Enter record from input1 to quantity field|
| **Step 6** | Click 'Add product'|
| **Step 7** | Record the position of error message (center of screen)|


| **Test Data** | **Expected result** |
| --- | --- |
| -1 | Error message - 'Negative quantity entered' (at predetermined position)|
| double | Error message - 'Invalid quantity entered' (at predetermined position)|
|2147483647| Error message - 'Invalid quantity entered' (at predetermined position) |
| three | Error message - 'Invalid quantity entered' (at predetermined position)|
