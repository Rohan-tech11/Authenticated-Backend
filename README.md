---STEPS TO RUN BACKEND CODE---

 CREATE THE DB IN MYSQL NAME CALLED "swiftsend" (make sure its case sensitive ") 

 Run the Application using springstarter application

 Register the user using postman

 http://localhost:8080/register post req
  UserResgister json :
  {
    
  "firstName": "",
  "lastName": "",
  "email": "",
  "password": "",
  "mobileNumber": 1234567890
}

you should get the email to authenticate,click and verify the account



http://localhost:8080/login 
login using the credentials (try before the email verificationn and after the email verification)see the difference

http://localhost:8080/users
after login you should be able to see all the users in json format.


  
