import React, { useEffect, useState } from "react";
import {Link, useParams } from "react-router-dom";
import LoginPage from "../LoginPage/LoginPage";
import OneComment from "../../components/OneComment";
//import "./Comment.css"; 

type CommentProps = {
    post_id : number;
    message : string;
}

const Comment = (props: CommentProps) => {
    const[result, setResult] = useState<CommentProps[]>([]);
    useEffect(() =>{
        const api = async () => {
            const data = await fetch(`https://gentle-bastion-60274.herokuapp.com/allComments/${props.post_id}`, { //how to specify for unique id?
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
        <div className = "login">
            <p className="text-4xl text-gray-700 font-bold mb-5">
                Leave a Comment
            </p>
            <Link to="/">
            <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
            Home</button>
            </Link>

        </div>
        <p>
        {result.slice(0).reverse().map((value)=>(
                    <OneComment
                message={value.message} post_id={props.post_id}                        />
                ))}
        </p>
        </>
    );
}
export default Comment;