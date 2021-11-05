Instructions:

1.	A runnable system with instructions on how to build/run your application

    The application build is developed using Maven. 
    From the root folder of the application you may run the following maven command to build the runnable jar:
	mvn clean package
	
	Then you should run the following java command to start up the Spring boot application. It might take around 5 minutes to startup.
	java -jar -Djasypt.encryptor.password="Z8HPHxv6wSSPVb4K" -Dapplication.properties="application.yaml" -Djasypt.encryptor.algorithm="PBEWITHMD5ANDDES" target/theguildedrose-0.0.1-SNAPSHOT.jar

2.	A system that can process the two API requests via HTTP 

    You can see all the endpoints of the application documented on Swagger:
    http://localhost:8080/public/swagger/swagger-ui/index.html
    
    I've exposed a Postman collection and environment to consume the API.
    Click on the button below or copy the URL on your browser.
    
	[![Run in Postman](https://run.pstmn.io/button.svg)](https://god.gw.postman.com/run-collection/1116804-421e7f85-4139-4957-a742-dc5e3eab1a81?action=collection%2Ffork&collection-url=entityId%3D1116804-421e7f85-4139-4957-a742-dc5e3eab1a81%26entityType%3Dcollection%26workspaceId%3D36e01ff8-d6b3-44a1-a9af-77b48877769d#?env%5BThe%20Guilded%20Rose%20Expands%5D=W3sia2V5IjoiYmVhcmVyIiwidmFsdWUiOiIiLCJlbmFibGVkIjp0cnVlfV0=)

3.	Appropriate tests (unit, integration etc) 

4.	A quick explanation of: 
	a.	Your application, how you set it up, how it was built, how you designed the surge pricing and the type of architecture chosen.
		
		- The application uses Jasypt to encrypt secrets on the application. In this way we can keep encrypting keys out of the codebase. 
		- The enpoints are divided in three categories:
		  a) public: Public endpoints starts with /public. You don't need to be authenticated to consume public endpoints.
		  b) general: General endpoints require consumers to be authenticated to get a response from the api.
		  c) admin: Only authenticated users with the ADMIN role can execute admin endpoints.
		- The surge pricing was developed using an on memory Sliding Time Window that keeps all views within the past 1 hour.
		Whenever a user lists the inventory. The items returned are marked as viewed on the Sliding Time Window and if the amount
		of items viewed within 1 hour is 10 or more than a additional fee of 10% is added to the price.
		- Every order that's placed will go to the Warehouse table. It uses the Optimistic Lock to guarantee that multiple
		call to the same table won't override the last value inserted.
		- There are a bunch of endpoints intended to add items on the database, update the price of items, add units of items to the inventory.
		That makes this project pretty dynamic. I'd add the approval of users before they can start accessing the api.
		- I created the CustomGlobalExceptionHandler advice to centralize the exception handling for the application.
		- I'm using Spring Data, H2, Spring Boot, Open API 3, Postman, auth0, Spring Validation, Lombok, etc.
	
	b.	Choice of data format. Include one example of a request and response.
		The chosen data format is Json. It's human readable and pretty standard on frameworks.
		Any client should have built-in libraries that can generate or read json payloads.
		
		An example is the Order endpoint (Pass in the Bearer token):
		
		POST http://localhost:8080/user/order
		
		{
		  "itemId": 1,
		  "quantity": 1
		} 
	
	 
	c.	What authentication mechanism was chosen, and why?
	    The authentication used was JWT. Before a caller can access general and admin endpoints. A user must be created and logged in the application.
	    The /public/user endpoint is responsible to create a user. Inform USER or ADMIN on the the role field depending on your intent.
	    The /login endpoint is responsible to generate a JWT token for a user that will be used as the Bearer token across the api.
	    This decision means that the application should protect the user credentials and should always use HTTPS.
	    Using JWT has the advantage to only ask the user credential once. Subsequent calls to the api will only require the access token generated on the login endpoint.
		JWT allows that machines can generate access tokens without user interaction like OAuth.