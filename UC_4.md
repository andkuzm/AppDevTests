| **UC 4** |    **searching through history with "Show between dates" button** |
| --- |     --- |
| **Related Requirements** | AT-19 |
| **Initiating Actor** | Manager |
| **Actors Goal** | Look at the last 10 purchases |
| **Participating Actors** | "Show last 10" button, Purchase history list |
| **Preconditions** | None |
| **Postconditions** | last 10 purchases are showed in history tab |
**Flow of Events for Main Success Scenario**
| Direction | Event |
| --- | --- |
| **->** | 1. Manager triggers "Show last 10" button |
| **<-** | 2. System (a) searches through history until it finds enough material and (b) shows last 10 purchases |
| **->** | 3. Manager looks through history he required |
**Flow of Events for Extensions (Alternate Scenarios)**
There is less than 10 purchases
| Direction | Event |
| --- | --- |
| **->** | 1a. Manager triggers "Show last 10" button |
| **<-** | 2. System (a) finds that there were less than 10 purchases and (b) shows all purchases presented in the list |
| **->** | 3. Manager looks through history he got |