Design:
The design of the project was kept very simple. All controllers and repositories are being added to separate router and 
repository lists. Each Controller has its routes for GET, POST, PUT, ... and the repositories contain the SQL-statements
for the database. The database is running in a docker container and is a persistent postgres database.

Tests:
I tried to cover all possible test cases that I think are important. There are tests for the server as well as for the different 
controllers or repositories.

Zeit:
It took me about 40 hours to finish the project.

Reflexion:
In the future I will try to separate the codes more from each other by using service classess and 
multiple functions. 

Bonus Feature:
It is a lucky event with a designated probability. If met, the card of one of the players gets double 
the damage until the end of the round.

Github: https://github.com/RobertDulca/MTCG_BackendApp