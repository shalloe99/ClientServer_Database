

Start the code by:

\--------------------

cd java

make

java Login

\----------------------

Enter username: root

Enter password: password

\-----------------------

Gui will pop up, and it is resizable. (click the edge and drag it outward)

Example :





Testing by clicking the dropdown table opinion:

jdb-show-related-tables <table-name>:

Testcase 1:

Customer

Then press send

+++++++++++++

Testcase 2:

address

Then press send

\------------------------------------------------------

jdb-show-all-primary-keys function :

Then press send

\------------------------------------------------------

jdb-find-column <column-name>:

Input:

customerID

\------------------------------------------------------

jdb-search-path <table1> <table2>:

Input:

First table: address

second table: customer





\------------------------------------------------------

jdb-search-and-join <table1> <table2>:

Input:

First table: address

second table: customer

(**this will run for a long time**)

\------------------------------------------------------

jdb-get-view <view-name> '(' < sql query > ')':

**\*\*\*\*\*\*This will not create a table, if no error, then it is successfully created.**

**If want to check if the view is created, please select see views \*\*\*\*\*\*\*\*\***

Testcase 1:

View name: CustomerView

Sql query : select \* from address;

+++++

Testcase 2:

View name: TestView

Sql query : select \* from productinventory

+++++

Testcase 3:

View name: JoinTestView4

Sql query : select productinventory.ProductID, productinventory.Quantity, productinventory.Bin,

product.Name, productNumber from productinventory left join product on

(productinventory.ProductID = product.ProductID);

\------------------------------------------------------

jdb-stat <table> (or <view-name>) <column\_name>:

Testcase 1:

Table or view name: TestView

Column name : Quantity

Testcase 2:

Table or view name: JoinTestView4

Column name : Quantity

\------------------------------------------------------

jdb-slice <tableName> <index1> <index2>:

Table name: customer

Index1: 10

Index 2: 20

\------------------------------------------------------

jdb-show-head <table name> <number of rows print out>:

Table: address

number of rows print out: 5

**(if did not show up in the end, try closing some tab in front. It is pushed to the end)**

\------------------------------------------------------

jdb-product-to-where <city>:

City: London





\------------------------------------------------------

jdb-info-of-subtotalitem <the first number of the ordering>:

the first number of the ordering: 5

\------------------------------------------------------

jdb-delete-view <viewName>:

**\*\*\*\*\*\*This will not create a table, if no error, then it is successfully deleted.**

**If want to check if the view is deleted, please select see views \*\*\*\*\*\*\*\*\***

viewName : TestView

\------------------------------------------------------

jdb-see-views:

Then press send

\------------------------------------------------------

jdb-show-tables:

Then press send

\------------------------------------------------------

jdb-draw:(bidirectional graph)

Then press send

\------------------------------------------------------

jdb-join-table:

Tables: salesreason, location, jobcandidate, department

\------------------------------------------------------

jdb-show-columns-of-a-table

Testcase 1:

Table name: address

Column name (for all ) : \*

+++++++++++++++++

Testcase 2:

Table name: address

Column name : AddressID, City, AddressLine1, PostalCode

\------------------------------------------------------

Custom sql command:

Sql: select \* from productinventory;

\------------------------------------------------------

Analytical Dahsboard:(xchart)

Press send.

