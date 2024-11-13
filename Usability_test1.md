| **User Story** | **AT-25** |
| --- | --- |
| Critical problem | Error message appears in inconsistent positions |
| Test applies to | GUI |
| Start Condition | Purchase tab opened, warehouse has at least one product with quantity 0 |
| Steps to take |  |
| 1 | Press "new purchase" button |
| 2 | Choose product with quantity equalling 0 |
| 3 | Press "confirm" button |
| 4 | Close the error message |
|5+| Loop 3-4|
| Condition when the test is considered passed | Error message appears in the same constant position on the screen |