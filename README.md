# Bank Management System

The Bank Management System is a software system that allows users to use ATMs with basic banking functions. 
This system also provides administrators with tools to manage user accounts and generate reports.

## Features
- **ATM Functions**: Users can perform the following operations at the ATM:
  - Deposit funds into their accounts.
  - Withdraw cash.
  - Check their account balance.

- **Administrator Panel**: Administrators have additional capabilities:
  - Adding new user accounts.
  - Deactivating user accounts.
  - Searching for users based on specific criteria, such as name, last name, or JMBG (unique ID number).
  - Generating transaction reports and account balance reports.

## Technologies
- Java
- SQL Server

## How to Use
To configure the application to work with your SQL Server database, follow these steps:

1. Create a new file named `dbConfig.properties` in your project.
2. Open the `dbConfig.properties` file and add the following line, replacing the placeholders with your database connection details:
   db.url=jdbc:sqlserver://YourServerName:1433;databaseName=yourDatabaseName;user=yourUserName;password=yourPassword;;encrypt=true;trustServerCertificate=true

   Replace `YourServerName`, `yourDatabaseName`, `yourUserName`, and `yourPassword` with your actual database information.

4. Save the `dbConfig.properties` file.

Now, the application is configured to use your SQL Server database. You can customize the database connection settings according to your specific setup.

## Screenshot's of application
### ATM Interface
![ATM](BankManagementSystem(SS)/ATM.png)

### Admin Login
![Admin Login](BankManagementSystem(SS)/AdminLogin.png)

### Add New User
![Add New User](BankManagementSystem(SS)/AddNewUser.png)

### Search Users
![Search Users](BankManagementSystem(SS)/SearchUsers.png)

### Activate or Deactivate Users
![Activate or Deactivate Users](BankManagementSystem(SS)/Activate-Deactivate.png)

### Search Transactions
![Search Transactions](BankManagementSystem(SS)/SearchTransactions.png)

### Export to PDF 
![Export to PDF](BankManagementSystem(SS)/PDFExport.png)
