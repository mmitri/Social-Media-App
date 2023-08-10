import React from 'react';
import logo from './logo.svg';
import '../../App.css';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { useEffect, useState } from 'react';
import jwt_decode from "jwt-decode";
import { getModeForUsageLocation } from 'typescript';
import HomePage from '../HomePage/HomePage';
import {Link} from "react-router-dom";

 
function LoginPage(props) {
  /**
   * two different pages, home page and page where you add a post
   */
 
  interface userI {
    name: string | null;
    email?: string;
    iat?: number;
    iss?: string;
    picture?: string;
  }
 
  const [user, setUser] = useState<userI>({name : null}); //use this line of code in App.tsx
  const email = user.email;
  console.log(email)
 
  function handleCallbackResponse(response) {
    console.log("Encoded JWT ID Token: " + response.credential); //response.credential = jwt or json web token
    //var userObject = jwt_decode(response.credential);
    const decodedToken = jwt_decode(response.credential);
    console.log(decodedToken);
    setUser(decodedToken as userI);
    document.getElementById("signInDiv").hidden = true;
 
  }

  //create function to handle child transferring data to parent
 
  function handleSignOut(event) {
    setUser({name: null});
    document.getElementById("signInDiv").hidden = false;
  }

  function handleHomePage(event) {
    return (
    <Link to="/"> "Go Home" </Link>
    );
//document.getElementById("signInDiv").hidden = false;
  }
 
  //google oauth
  useEffect(() => {
    /* global google */
    //@ts-ignore
    google.accounts.id.initialize({
      client_id: "965631641964-k1ajivm4526cer2tfe8pqcbtlo4hq7at.apps.googleusercontent.com",
      callback: handleCallbackResponse,
      auto_select: false,
    });
 
    /* global google */
    //@ts-ignore
    google.accounts.id.renderButton(
      document.getElementById("signInDiv"),
      { theme: "outline", size: "large"}
    )

    /* global google */
    //@ts-ignore
    google.accounts.id.prompt();
  }, [])
  //If we have no user, we want to show sign in button
  //If we have a user, show log out button

 
  return (
    <div className = "login">
        <div id = "signInDiv"></div>
        

        {user &&
        <>
        <div> 
            <h3>{user.name}</h3>
            <h3>{user.email}</h3>
            <img src={user.picture}></img>
            <Link to="/">
                        <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
                        Home </button>
                </Link>
            
        </div>

        <div className="container mx-auto bg-gray-200 rounded-xl shadow border p-8 m-10">
        <p className="text-3xl text-gray-700 font-bold mb-5">
          The Buzz
          <h3>{user.name}</h3>
          <h3>{user.email}</h3>
          
        </p>
        </div>


        </>
        }
    </div>
  );
}

export default LoginPage;