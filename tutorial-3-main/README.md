Here is the list of files that I have been modified:
AccountController.java, TransactioalRecordController.java, AccountDTO.java, TransactionalRecordDTO.java, Account.java, TransactionalRecord.java, AccountService.java,
TransactionalRecordService.java

To run the code. First change the spring.datasource.username and spring.datasource.password in application.properties to the correspondence username and password.
And then, Create the database called banktransaction. Then run the spring boot project. To test the function. First click the http client. Then, we need to create customers with the following test code:
###
POST http://localhost:8080/api/customer
Content-Type: application/json

{
  "firstName": "abc",
  "lastName": "abc"
}
after entering the code, click the run button and we can check there is a user in PGadmin customer table. 
Then, we need to test the create account function, we can 3 account represent personal, business and bank with the following code:
###
POST http://localhost:8080/api/customer/{customerID}/account
Content-Type: application/json

{
  "accountName": "bus",
  "accountType": "BUSINESS",
  "merchantFeePercentage": 0.02
} 
###
POST http://localhost:8080/api/customer/{customerID}/account
Content-Type: application/json

{
  "accountName": "per",
  "accountType": "PERSONAL",
  "merchantFeePercentage": 0.00
} 
###
POST http://localhost:8080/api/customer/{customerID}/account
Content-Type: application/json

{
  "accountName": "ban",
  "accountType": "Bank",
  "merchantFeePercentage": 0.00
} 

Where customer ID is correspondent to the ID in customer table

Then we can add money to account with:
###
POST http://localhost:8080/api/customer/{fromCustomerId}/account/{accountID}/transaction_record/deposit
Content-Type: application/json

{
  "amount": 100.0
}

Finally we can test the transfer function with the code:
###
POST http://localhost:8080/api/customer/{{fromCustomerId}}/account/{{accountId}}/transaction_record/transfer
Content-Type: application/json

{
  "toCustomerId": #,
  "toAccountId": #,
  "amount": #,
  "bankAccountId": #
}

The # means you need to modify it to the corresponding number.

This code will support the new function because in the code, I added the code in transaction record section that will add the amount of merchant fee to the bank account by searching the correspondence bank account id. Moreover, by setting the normal merchant fee percentage to 0, we can make sure only the times when merchant fee percentage to non zero number, there will be a fee that send to bank.