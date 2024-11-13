| **UC 2** |    **searching through history with "Show between dates" button** |
| --- |     --- |
| **Related Requirements** | AT-17 |
| **Initiating Actor** | Manager |
| **Actors Goal** | Look through history, by certain date till certain date |
| **Participating Actors** | "Show between dates" button, Purchase history list |
| **Preconditions** | None |
| **Postconditions** | all purchases between dates are showed in history tab |
**Flow of Events for Main Success Scenario**
| Direction | Event |
| --- | --- |
| **->** | 1. Manager insertas start date and end date for future search into appropriate fields |
| **->** | 2. After dates are inserted manager presses "Show between dates" button |
| **<-** | 3. System (a) searches through history and (b) shows all purchases between dates |
| **->** | 4. Manager looks through history he required |
**Flow of Events for Extensions (Alternate Scenarios)**
User enters invalid date
| Direction | Event |
| --- | --- |
| **->** | 1a. Manager inserts invalid start or end date |
| **->** | 2. After dates are inserted manager presses "Show between dates" button |
| **<-** | 3. System (a) detects error and (b) shows error message |
| **->** | 4. Manager provides valid dates |
| **<-** | 5. System (a) searches through history and (b) shows all purchases between dates |
| **->** | 6. Manager looks through history he required |