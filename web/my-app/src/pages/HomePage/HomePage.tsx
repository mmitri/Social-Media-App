import LikeButton from "../../components/LikeButton/LikeButton";
import PostCard from "../../components/PostCard/PostCard";
import "./HomePage.css"
import {Link } from "react-router-dom";
import React, { useEffect, useState } from "react";
import Navbar from "../../components/Navbar";
import { validateHeaderValue } from "http";

/*
type apiProps = {
    post_id: number, 
    title: string;
    message: string;
    user_id: number;
    valid: boolean;
} */

interface Comment {
    comment_id: number,
    post_id: number, 
    user_id: number, 
    message: string
}

interface apiProps{ 
    post_id: number, 
    title: string;
    message: string;
    user_id: number;
    valid: boolean;
    poster_name: string;
    likes_number: number;
    dislike_number: number;
    comments: Comment[];
    file: string;
  }
/** 
 * homepage includes title and map of post cards,
*make api call t get list of props type
*/
const HomePage = () => {

    const db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n"
    const heroku_url = "https://gentle-bastion-60274.herokuapp.com/";
    // const posts = [
    //     {
    //         message_id: 1,
    //         title: "Matt Silverman",
    //         message: "THis is a a test",
    //         likes: 5,
    //     },
    //     {
    //         message_id: 1,
    //         title: "Matt Silverman",
    //         message: "THis is a a test",
    //         likes: 0,
    //     },
    //     {
    //         message_id: 1,
    //         title: "Matt Silverman",
    //         message: "THis is a a test",
    //         likes: 3,
    //     },
    //     {
    //         message_id: 1,
    //         title: "Matt Silverman",
    //         message: "THis is a a test",
    //         likes: 4,
    //     },
    //     {
    //         message_id: 1,
    //         title: "Matt Silverman",
    //         message: "THis is a a test",
    //         likes: 1,
    //     },
    //     {
    //         message_id: 1,
    //         title: "Matt Silverman",
    //         message: "THis is a a test",
    //         likes: 6,
    //     },
    //     {
    //         message_id: 1,
    //         title: "Matt Silverman",
    //         message: "THis is a a test",
    //         likes: 5,
    //     }
    // ]


    /**
     * perform get to populate the home page with user posts
     */
    const[result, setResult] = useState<apiProps[]>([]);
    useEffect(() =>{
        const api = async () => {
            const data = await fetch("https://gentle-bastion-60274.herokuapp.com/allPosts", {
                method: "GET"
            });
            const jsonData = await data.json();
           setResult(jsonData.mData);
            console.log(result);
    
        }
        api();

    }, [])

    const itemList = ["A", "B", "C"];
    // Generate JSX code for Display each item
    const renderList = itemList.map((item, index) => 
    <div key={index}>{item}</div>
    );

    const commentList = result;
    console.log(commentList)
    /**
     * include link to add a post as well
     */
    //
    return  (
        <>
        <div>
        <Navbar />
        </div>
        <div className="homepage">
            <p className="text-7xl text-gray-700 font-bold mb-5">
            The Buzz
            </p>
            <div className="addButton">
                <div className="relative h-0 w-5 ...">
                    <div className="top-0 left-0 h-8 w-40 ..."><Link to="/edit-profile">
                        <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
                            Edit Profile</button>
                    </Link></div>
                </div>


                <Link to="/add-post">
                        <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
                        Add A Post</button>
                </Link>

                <div className=" h-0 ...">
                    <div className="absolute top-20 right-5 h-20 w-50 ..."><Link to="/login">
                        <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
                            Log Out</button>
                    </Link></div>
                </div>                
            </div>
            <p>
                {result.slice(0).reverse().map((value)=>(
                    <>
                    <PostCard 
                            post_id={value.post_id}
                            user_id={value.user_id}
                            title={value.title}
                            message={value.message}
                            valid={value.valid}
                            likes_number={value.likes_number} 
                            poster_name={value.poster_name} 
                            dislikes_number={value.dislike_number}  
                            comments = {value.comments}
                            file = {value.file}                                   
                    />
                    </>
                ))}
                
            </p>
        </div>

        <div className="app">
            <div>The List contains:</div>
            {renderList}
        </div>
    
    </>
    )
}

export default HomePage;