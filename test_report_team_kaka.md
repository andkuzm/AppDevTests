# Homework 7

## Black box usability testing

### Prerequisite 1:    zipped CLI and GUI versions of POS-software
### Prerequisite 2:    Testing User Stories NF-1 and NF-2

<br>

### Test NF-1

|**Task**|**Expected output**|**Observed output (CLI)**|**Observed output (GUI)**|**Test result**|
|---|---|---|---|---|
|Measure loading time: purchase cart | < 2s| < 1s| < 1s| PASSED|
|Measure loading time: warehouse | < 2s| < 1s| < 1s| PASSED|

<br>
### Test NF-2
(continuous monitoring of software not feasible; testing general workflow)

|**Task**|**Expected output**|**Observed output (CLI)**|**Observed output (GUI)**|**Test result**|
|---|---|---|---|---|
|CLI-menu structure | aligned in two columns| unaligned| N/A| FAILED|
|menu command shortcuts|shortcuts cleary visible| no shortcuts| N/A| FAILED|
| 'help' | displays CLI menu| CLI menu displayed| N/A| PASSED|
| 'warehouse' | displays warehouse contents| warehouse contents displayed| warehouse contents displayed| PASSED|
| 'cart' (empty) | displays cart contents| cart contents displayed| cart contents displayed| PASSED|
| 'cart' (with product)| displays cart contents| cart contents displayed with "?" symbol| cart contents displayed| FAILED|
| 'add-cart' (3 pcs of Lays chips) | confirms purchase| items added, warning message displayed| items added| FAILED|
| 'team' | displays team info| error messages, no team info displayed| no team info displayed| FAILED|
| 'add-warehouse' (5 Beer NoFreeBeer 2 10) | add new product to warehouse contents| product added with errors (_5 beer 2.0Euro (10 items)_)| product added and the entry doubled| FAILED|
| 'add-warehouse' (1 Lauaviin viin 12 100) | overwrite an existing product| product not added| no such functionality| FAILED|
| 'remove-warehouse' (1) | product removed from warehouse contents| Lays Chips removed from warehouse| Lays Chips removed from warehouse| PASSED|
| 'history-all' | all purchases displayed| no purchased items displayed, total sum equals 0.0, needlessly precise  time(hh:mm:ss.sssss)| needlessly precise time(hh:mm:ss.sssss)| FAILED|
| 'history-10' | 10 last purchases displayed| no purchased items displayed, total sum equals 0.0, needlessly precise  time(hh:mm:ss.sssss)| needlessly precise time(hh:mm:ss.sssss)| FAILED|
| 'history-between' (2023-12-17 2023-12-18)| purchases from last two days displayed| no purchased items displayed, total sum equals 0.0, needlessly precise time(hh:mm:ss.sssss)| needlessly precise time(hh:mm:ss.sssss)| FAILED|
| 'purchase-cart' | cart emptied, items removed from warehouse| cart emptied, items removed from warehouse|cart emptied, items removed from warehouse| PASSED|
| 'reset-cart' | cart emptied, no items removed from warehouse| cart emptied, no items removed from warehouse|cart emptied, no items removed from warehouse| PASSED|
| 'team' | displays team info| error messages, no team info displayed| no team info displayed| FAILED|
| 'quit' | quits POS| POS terminated| N/A| PASSED|