# Run program

In order to run the program you need:
1. navigate cd .\hw_block_05_01\
2. create .env file with 2 constants: SPRING_MAIL_USERNAME and SPRING_MAIL_PASSWORD
3. run docker-compose up --build
4. navigate cd .\hw_block_02\ and run the application

After all these steps are done, you can go to the postman and send post request to http://localhost:8080/api/v1/students. 

```
{
    "username": "example",
    "email": "example@gmail.com",
    "firstName": "blasbla",
    "lastName": "bladbla",
    "birthDay": "1999-01-01",
    "courseId": 5
}
```

After doing this, you receive congratulation email on the pointed email.