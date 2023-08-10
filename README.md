# ***Group: Free Wifi***
# ***App Name: Free Wifi***
# **README for Phase 3**
* **Members**
	1. Ryan Morris(rcm320@lehigh.edu)
	2. Mark Mitri(mame23@lehigh.edu)
	3. Penelope Shaw (pss324@lehigh.edu)
	4. Matt Silverman(mrs224@lehigh.edu)
	5. Simon Gajewski(sig224@lehigh.edu)
* **Grader**
	1. Ethan Santalone(els623@lehigh.edu)
_______________________________________________________________________
* **Project Goal Phase 1: The goal is to start small a cloud-hosted web system, plus a mobile app, so that employees can post short ideas (messages), and other employees can like (and remove a like from) that idea**
* **Roles include:**
	1. Project Manager (Ryan Morris)
	2. Backend Developer (Penelope Shaw)
	3. Mobile Developer (Mark Mitri)
	4. Admin Developer (Simon Gajewski)
	5. Web Developer (Matt Silverman)	 	 	
* Trello Link for Phase 1: https://trello.com/b/WiSQJmlp/phase-1
_______________________________________________________________________
* **Project Goal Phase 2: The goal is to build upon the cload-hosted web system and mobile app to add authenticated users, likes & dislikes, comments, edited posts and comments**
* **Roles include:**
    1. Project Manager (Mark Mitri)
    2. Backend Developer (Simon Gajewski)
    3. Mobile Developer (Ryan Morris)
    4. Admin Developer (Matt Silverman)
    5. Web Developer (Penelope Shaw)
* Trello Link for Phase 2: https://trello.com/b/CmgiHAEM/phase-2
_______________________________________________________________________
* **Project Goal Phase 3: The goal is to start small a cloud-hosted web system and mobile app that allows for file and picture upload that hosts the files in a google drive. This functionality is added on and done so that it does not impact the other phases work**
* **Roles include:**
    1. Project Manager (Simon Gajewski)
    2. Backend Developer (Matt Silverman)
    3. Mobile Developer (Penelope Shaw)
    4. Admin Developer (Ryan Morris)
    5. Web Developer (Mark Mitri)
* Trello Link for Phase 3: https://trello.com/b/i1mCSnR5/phase-3
_________________________________________________________________________
# **Build Instructions**
* **Back-end**
	1. mvn clean
	2. mvn package 
	3. mvn exec:java
* **Web**
	1. npm start
* **Mobile**
	1. Run Anroid Simulator through VSCode
* **Admin**
    1. mvn clean
    2. mvn package
    3. mvn exec:java
__________________________________________________________________________
# **Phase 3 Deliverables:**

**User Stories:**
Authenticated Users →

1. Login
   As an authenticated user, I want to be able to log into the app because I want to be able to access other peoples posts/accounts as well as my own
   Tests:
   Enter your information into the login page

2. See your profile
   As an authenticated user, I want to be able to see my profile page because I want to make changes, or just see the information stored there
   Tests:
   Clicking on the profile page to see if we get sent there

3. See others profile
   As an authenticated user, I want to be able to see other people's profile page because I want to access the information stored or learn more about a person
   Tests:
   Clicking on the profile page of another user to see if we get sent there
4.  Be able to go to Home page
	As an authenticated user I want to be able to go the home page because I want to see posts other people have made
	Tests:
	Clicking the home page button and checking if we get sent to the home page


5. Add comment
   As an authenticated user I want to be able to add a comment to an existing post because I want to show my support/disapproval of the idea presented by the user
   Tests:
   Finding a post, writing a comment and then hitting the add comment button

6. Edit Comment
   As an authenticated user I want to be able to edit a comment because I may have changed my mind, or didn’t explain my point well enough.
   Tests:
   Hitting the edit comment button, changing the message and hitting submit to see if it changed my comment
7. Post a message
   As an authenticated user I want to post a titled message because I want to share my ideas with other users and see if others like my ideas
   Tests
   Typing a message, adding it to the table and seeing it it the app

8. Like other posts
   As an authenticated user I want to be able to like other posts because I want to show my support for the posts I like
   Tests
   Hitting like on a post and seeing if the number of likes increase

9. Remove my like on a post
   As an authenticated user I want to be able to remove a like from a post because I want to show I changed my mind on the post
   Tests:
   Hitting like button, after having liking it, and seeing if the number goes down

10. Use both devices to access the app
	As an authenticated user I want to be able to use both my devices to access the app because I want to use my phone and sometimes my computer to access the app
	Tests:
	Opening the app on both your PC and smart device to see if it opens

11. Be able to upload pictures to enterprise-owned repository
	As an authenticated user (engineer) I want to be able to post pictures because I want to share diagrams.
	Tests
	(Manual) Successfully uploading a picture and observing the picture being posted.

12. Be able to upload videos to enterprise-owned repository
	As an authenticated user I want to be able to post videos because I want to share a larger variety of information.
	Tests
	(Manual) Successfully uploading a video and seeing it be added to repository

13. Be able to upload audio files to enterprise-owned repository
	As an authenticated user I want to be able to post audio files because I want to share a larger variety of information.
	Tests
	(Manual) Successfully uploading an audio file and seeing it be added to repository

14. Be able to share links to various media (pictures, audio files, videos) in ideas or comments
	As an authenticated user I want to be able to share links because I want to share a larger variety of information.
	Tests
	(Manual) Successfully sharing a link and clicking on the link to see that it displays the correct information.

15. Be able to link to another message or comment
	As an authenticated user I want to be able to reply to an existing comment or post to share my opinion on it.
	Tests
	(Manual) Successfully being able to reply to an existing comment or post

Admin →

1. Add and Remove Tables
   As an admin I want to be able to add and remove tables because I want to be able to update and moderate the app
   Tests
   Manual: Physically adding or removing a table


2. Add and Remove Messages
   As an admin I want to be able to add and remove messages because I want to be able to update and moderate the app
   Tests
   Manual: Physically adding or removing a message
   Automatic: Adding a message to the table and returning a query to see if its there

3. Remove Posts
   As an admin I want to remove posts because I want to be able to remove content that should not be available and/or is outdated.

4. Remove Comments
   As an admin I want to able to
   Remove Comments
   Because
   I want to be able to remove offensive/unacceptable content
5. Remove Users
   As and admin I want to be able to
   Remove Users
   Because
   I want to be able to remove users who post offensive/unacceptable content

Tests for 3, 4, and 5:
Manual: Physically removing post, comment, or user
Automatic: Removing a post, comment, or user and returning a query to verify that it has been deleted
Unit Testing:

Mobile:

Automatic
Testing Android Camera and Gallery API
Testing to see that picture has been posted to the Buzz by posting a test photos

Manual
Mindful that app is intuitive for Android users

Admin (Additional Unit Tests)

Automatic:
Purposely posting an invalid link/image to test input field verification
Manual:
Removing the least recently accessed Google Drive file from the interface and reloading the service to make sure it got deleted from the records

Backend (Additional Tests)
Manual:
Ensure the link/url to a file is properly stored in google drive
Automatic:
Check CRUD operations

**Routes**

![Scheme](Sprint10Deliverables/RoutesProfile.png)

![Scheme](Sprint10Deliverables/RoutesComments.png)

![Scheme](Sprint10Deliverables/RoutesAllPosts.png)

![Scheme](Sprint10Deliverables/RoutesAuth.png)

**ER Diagram**

![Scheme](Sprint10Deliverables/ERDiagram.png)

**Mock Web UI**

![Scheme](Sprint10Deliverables/Mock_UI_Web_Sprint10.jpg)

**Mock Mobile UI Drawings**

![Scheme](Sprint10Deliverables/MobileUIDrawing.png)

**State Machine Idea**

![Scheme](Sprint10Deliverables/State%20Machine%20Idea.png)

**State Machine (User)**

![Scheme](Sprint10Deliverables/State%20Machine%20(User).png)

**System Drawing Phase 3**

![Scheme](Sprint10Deliverables/State%20Machine%20Phase%203.png)


__________________________________________________________________________
# **Phase 2 Description of the Project**
* add authentication and authorization, improve upon and expand the “like/remove a like” functionality, add a comment mechanism, and add a user 
* profile.
-------------------------------------------
* Authenticated Users → 
* 
* **1. Login**
* **As an authenticated user, I want to be able to**
*	log into the app 
* **because** 
*	I want to be able to access other peoples posts/accounts as well as my own
* **Tests:**
*	Enter your information into the login page
____________________________________________________________________________________________
* **2. See your profile**
* **As an authenticated user, I want to be able to**
*	see my profile page because I want to make changes, or 
*	just see the information stored there
* **Tests:**
* 	Clicking on the profile page to see if we get sent there
____________________________________________________________________________________________
* **3. See others profile**
* **As an authenticated user, I want to be able to**
*	see other people's profile page 
* **because**
*	I want to access the information stored or learn more about a person
* **Tests:**
*	Clicking on the profile page of another user to see if we get sent there
____________________________________________________________________________________________
* **4.  Be able to go to Home page**
* **As an authenticated user, I want to be able to**
*	go the home page 
* **because**
*	I want to see posts other people have made
* **Tests:**
* 	Clicking the home page button and checking if we get sent to the home page
____________________________________________________________________________________________
* **5. Add comment**
* **As an authenticated user, I want to be able to**
*	add a comment to an existing post 
* **because**
*	I want to show my support/disapproval of the idea presented by the user
* **Tests:**
* 	Finding a post, writing a comment and then hitting the add comment button
____________________________________________________________________________________________
* **6. Edit Comment** 
* **As an authenticated user, I want to be able to**
*	edit a comment 
* **because**
*	I may have changed my mind, or 
*	didn’t explain my point well enough.
* **Tests:**
*	Hitting the edit comment button
*	Changing the message and hitting submit to see if it changed my comment
____________________________________________________________________________________________
* **7. Post a message** 
* **As an authenticated user, I want to**
*	post a titled message 
* **because**
*	I want to share my ideas with other users and see if others like my ideas
* **Tests**
*	Typing a message, adding it to the table and seeing it it the app
____________________________________________________________________________________________
* **8. Like other posts**
* **As an authenticated user, I want to be able to**
*	like other posts 
* **because**
*	I want to show my support for the posts I like
* **Tests:**
*	Hitting like on a post and seeing if the number of likes increase
____________________________________________________________________________________________
* **9. Remove my like on a post**
* **As an authenticated user, I want to be able to**
*	remove a like from a post 
* **because**
*	I want to show I changed my mind on the post
* **Tests:**
*	Hitting like button, after having liking it, and seeing if the number goes down
____________________________________________________________________________________________
* **10. Use both devices to access the app**
* **As an authenticated user, I want to be able to**
*	use both my devices to access the app 
* **because**
*	I want to use my phone and sometimes my computer to access the app
* **Tests:**
* 	Opening the app on both your PC and smart device to see if it opens
____________________________________________________________________________________________
* Admin →
* 
* **1. Add and Remove Tables**
* **As an admin, I want to be able to**
*	add and remove tables 
* **because**
*	I want to be able to update and moderate the app
* **Tests**
*	Manual: Physically adding or removing a table
____________________________________________________________________________________________
* **2. Add and Remove Messages**
* **As an admin, I want to be able to**
*	add and remove messages 
* **because** 
*	I want to be able to update and moderate the app
* **Tests**
*	Manual: Physically adding or removing a message
*	Automatic: Adding a message to the table and returning a query to see if its there
____________________________________________________________________________________________
* **3. Remove Posts**
* **As an admin, I want to be able to** 
*	(“Manage new tables and new columns”)
* 	Remove Posts
* **Because**
* 	I want to be able to remove offensive/unacceptable content
____________________________________________________________________________________________
* **4. Remove Comments**
* **As an admin, I want to able to**
* 	Remove Comments
* **Because**
*	I want to be able to remove offensive/unacceptable content
____________________________________________________________________________________________
* **5. Remove Users**
* **As and admin I want to be able to** 
* 	Remove Users
* **Because**
* 	I want to be able to remove users who post offensive/unacceptable content
* 
* **Tests for 3, 4, and 5:**
* 	Manual: Physically removing post, comment, or user
* 	Automatic: Removing a post, comment, or user and returning a query to verify that it has been deleted
-------------------------------------------------------------------------------
![Scheme](Sprint6Deliverables/StateMachineDrawing(User%20Perspective).jpg)
![Scheme](Sprint6Deliverables/image%20(1).png)
![Scheme](Sprint6Deliverables/sprint6er.pptx.jpg)
![Scheme](artifacts/routes_updated.png)
![Scheme](artifacts/Mock_UI_Updated_Drawings.jpg)
__________________________________________________________________________
# **Phase 1 Desciption of Project**
* Currently we have a cloud-hosted web system, plus a mobile app, so that employees can post short ideas (messages), and other employees can like (and remove a like from) that idea. 
* Our backend connects to heroku and can use all HTTP requests to affect likes and messages on the created dynamic web server
* Our admin code can create a table, drop/delete a table, and remove an entry from a table
* The mobile app has a scrolling list of messages, a like button for each message, a functionality where the like button saves a message and saved Messages on a different screen. We can add a message on a different part of the screen screen and the added message shows on the homepage. 
*  Our website (using React) performs GET and POST requests to the server, in order to show a list of ideas, allow the creation of new ideas, and allow a user to “like” ideas. It connects to the backend and is "live"
Listing of user stories for the admin and “anonymous user” personas:
-----------------------------------------------
Automatic: Done through code
Manual: Done through app
-----------------------------------------------
-----------------------------------------------

![Scheme](artifacts/ERD.jpg)
![Scheme](artifacts/webdrawing.jpg)
![Scheme](artifacts/adminmobile.jpg)
![Scheme](artifacts/routes.jpg)
![Scheme](artifacts/statemachine.jpg)
![Scheme](artifacts/User Stories.png)


# Backend Notes

* Testing
* Manual

* DBeaver.io
	1. Serves as substitute for “admin-cli”, great for testing to ensure that HTTP requests are successful
* Curl Commands
	2. Serve as substitute for front-end, a method to make HTTP requests
	3. Going through the “mvn clean”, “mvn package” and “mvn exec:java” allows you to test using the localhost
* Automatic
* Use Print Statements to make sure that various methods and variables are being accessed properly


* Sample Curl Commands Used During Testing
	1. curl -s http://localhost:4567/messages/1 -X PUT -d "{'mTitle':'did it work’', ‘mMessage’:’Yes it did!’
	2. curl -s http://localhost:4567/messages/5 -X PUT -d "{'mTitle':'Movie Time','mMessage':'I DO NOT plan to rent Napolean Dynamite this weekend.', 'mLikes':1}"
	3. curl -s http://localhost:4567/messages/5 -X PUT -d "{'mTitle':'Movie Time', 'mMessage':'I plan to rent Napolean Dynamite SO MUCH this weekend.'}"
	4. curl -s http://localhost:4567/messages/35 -X DELETE
	5. curl -s http://localhost:4567/messages/34/like -X PUT -d "{'mLikes':100}"
	6. curl -s http://localhost:4567/messages -X GET 
	7. curl -s http://localhost:4567/messages -X POST -d "{'mTitle':'MY POST TEST', 'mMessage':'I want this to work.', 'mLikes':8}"

* Heroku Database Credentials
* ADMINISTRATION
* Database Credentials
* Host
* ec2-3-213-228-206.compute-1.amazonaws.com
* Database
* dfoihhup8lug1n
* User
* qcvplotheamxrd
* Port
* 5432
* Password
* 2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907
* URI
* postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n
* Heroku CLI
* heroku pg:psql postgresql-convex-35170 --app gentle-bastion-60274

* REST Verbs (from Tutorial)
* GET
* Reading resources is done through the GET verb. Conventions that we discuss later will determine whether the read will return one resource, or many. The application will also decide whether the GET returns the entire resource, or just part of it.
 Usually the request will not have any extra data in it, so if any parameters need to be sent, they should be sent using a "Query String". For example a GET of /~spear/?reason=tutorial&amp;deadline=yesterday requests /~spear, but passes in two parameters:
reason, which has a value of tutorial, and
 deadline, with a value of yesterday.
 Note that there are no "official" parameters, which is why we typically provide both the parameter name and parameter value.
* POST
 The way to create new resources is with POST. Typically a POST will not specify the resource name that it is creating, but will instead use a resource name that can be thought of as a set.
 The data that the server should use to create the new resource must be provided in the request (after the newline), and the return of a POST will usually provide a unique identifier for the newly created resource.

* Environment Variable Usage
* `1DATABASE_URL=postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n mvn heroku:deploy

* Mobile Testing
	1. Testing that entries are live
	2. Clicking like button to see if a change in count was made
	3. Adding a message
* Admin Testing
	1. Testing that tables can be created, dropped or deleted
	2. Testing that database entries can be removed 
* Web Testing
	1. Clicking like button to see if a change in like count occured
	2. Unliking a message
	3. Submitting a new idea 
	4. Checking if the liked messages show up in the saved messages section 
	
# Debt and Backlog:
* Our current Techincal Debt is as follows:
	1. Nothing as of now
* Backlog:
	1. POST method in our Web code
	2. POST method in our Mobile Code
	3. PUT method to update likes in Web Code
	
## Javadoc documentation
* AdminCli [here](artifacts/admincli javadocs)
* Web [here](artifacts/webdocs javadocs)
* Mobile [here](artifacts/mobile dartdocs javadocs)
* Backend [here](artifacts/backend docs javadocs)
