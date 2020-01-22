HTTP	Endpoints
GET all	/school/subject
POST	/school/subject 
Json body: { "subject": ""}
PUT	/school/subject
PATCH	/school/subject/{id}/student/{email} 
You can provide the id of the subject and the email of the student you want to assign the subject to.
DELETE	/school/subject/{id}
Methods for teacher

HTTP	Endpoints
GET all	/school/teacher
POST	/school/teacher 
Json body: { "forename": "", "lastName": "", "email": "" }
PUT	/school/teacher 
Query parameters: forename, lastname, email
DELETE	/school/teacher/{email}
