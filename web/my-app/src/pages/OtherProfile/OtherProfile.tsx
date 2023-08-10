import React, { useState } from "react";
import {Link } from "react-router-dom";
import LoginPage from "../LoginPage/LoginPage";



function OtherProfile() {
    return (
        <div className = "login">
            <h1>User Profile Page</h1>
            <Link to="/"> Back to home</Link>
            
            <h3>Name: Justine De Fries </h3>
            <h3>Email: jdf123@lehigh.edu </h3>
            <h3>Sexual Orientation: Heterosexual </h3>
            <h3>Gender Identity: Woman </h3>
            <h3>Note: I love pizza! </h3>
        </div>
    );
}
export default OtherProfile;