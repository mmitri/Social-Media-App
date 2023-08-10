import { validateHeaderValue } from "http";
import React, { useEffect, useState } from "react";
import {Link } from "react-router-dom";
import ProfileCard from "../../components/ProfileCard/ProfileCard";
import LoginPage from "../LoginPage/LoginPage";
import "./ProfilePage";
import { EditText, EditTextarea } from 'react-edit-text';
import 'react-edit-text/dist/index.css';



const ProfilePage = (props) => {

    interface profileProps{
        string: string;
        boolean: boolean; 
        id: number,
        name: string,
        email: string,
        gender: string,
        sexual_orientation: string,
        note: string,
        valid: boolean;
      }

    const db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n"
    const heroku_url = "https://gentle-bastion-60274.herokuapp.com/";

    const [input, setInput] = useState('hi');

    const handleChange = e => {
        setInput(e.target.value)
    }

    const handleSubmit = e => {
        e.preventDefault();

        props.onSubmit({
            id: Math.floor(Math.random() * 10000), 
            name: input
        });

       // setInput('')
    }



     /**
     * perform get to populate the profile page with all user profiles
     */

      const[result, setResult] = useState<profileProps[]>([]);
      useEffect(() =>{
          const api = async () => {
              const data = await fetch("https://gentle-bastion-60274.herokuapp.com/allProfiles", {
                  method: "GET"
              });
              const jsonData = await data.json();
             setResult(jsonData.mData);
              console.log(result);
          }
          api();
  
      }, [])

    return (
        <>
        <div className = "profile">
            <p className="text-4xl text-gray-700 font-bold mb-5">
            Profile Page
            </p>

            <div className="relative h-0 w-5 ...">
                    <div className="top-0 left-0 h-8 w-40 ..."><Link to="/">
                        <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
                            Home </button>
                    </Link></div>
            </div>

            Name: 
            <div>
                <EditText showEditButton />
            </div>

            Email: 
            <div>
                <EditText showEditButton />
            </div>


            Sexual Orientation: 
            <div>
                <EditText showEditButton />
            </div>

            Gender Identity: 
            <div>
                <EditText showEditButton />
            </div>

            Note: 
            <div>
                <EditText showEditButton />
            </div>

            <p>
                {result.slice(0).reverse().map((value)=>(
                    <>
                    <ProfileCard
                            id={value.id}
                            name = {value.name}
                            email = {value.email}
                            gender = {value.string}
                            sexual_orientation = {value.string}
                            note = {value.string}
                            valid = {value.boolean}                                    
                    />
                    </>
                ))}
                
            </p>
        </div>

    </>
    );
}
export default ProfilePage;