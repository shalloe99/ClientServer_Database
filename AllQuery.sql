use adventureworks;
show tables;
select * from employee;
select * from address;
select * from salesorderdetail;
select * from salesorderheader;
select * from stateprovince;
#By week
select week(DueDate),month(DueDate),year(DueDate),SUM(TotalDue) from salesorderheader Group by YEAR(DueDate),month(DueDate),week(DueDate);
#By month
select month(DueDate),year(DueDate),SUM(TotalDue) from salesorderheader Group by YEAR(DueDate),month(DueDate);
#By year
select year(DueDate),SUM(TotalDue) from salesorderheader Group by YEAR(DueDate);
# Empoly marry status count
select MaritalStatus, count(*) from employee Group by MaritalStatus; 
# age 
select count(*),
  case
	when datediff(now(), BirthDate) / 365.25 -20> 50 then 'above 51'
    when datediff(now(), BirthDate) / 365.25 -20> 40 then '41 - 50'
    when datediff(now(), BirthDate) / 365.25-20>  30 then '31 - 40'
    when datediff(now(), BirthDate) / 365.25 -20> 18 then '18 - 30'
    else 'under 20'
  end as age_group
from employee
group by age_group;
# gender
select gender,count(*) from employee group by gender;
# Demographic sale by country 
select CountryRegionCode,year(DueDate) ,SUM(TotalDue) from salesorderheader 
	left join address On ShipToAddressID = AddressID 
    left join stateprovince on stateprovince.StateProvinceID = address.StateProvinceID
    Group by CountryRegionCode, YEAR(DueDate)
    ;