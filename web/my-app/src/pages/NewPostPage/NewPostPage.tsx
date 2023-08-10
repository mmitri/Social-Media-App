import React, { useState } from "react";
import {Link } from "react-router-dom";

/**
 * 
 * @returns page for new post, including forms and buttons
 */
const NewPostPage = () =>{
    const backendUrl = 'https://gentle-bastion-60274.herokuapp.com'

    const [titleValue, setTitleValue] = useState("");
    const [messageValue, setMessageValue] = useState("");
    const[userID, setUserIDValue] = useState('');

    //reset field after add or cancel button is hit
    // Input Field handler
    const handleTitleInput = (e: { target: { value: React.SetStateAction<string>; }; }) => {
      setTitleValue(e.target.value);
    };
    const handleMessageInput = (e: { target: { value: React.SetStateAction<string>; }; }) => {
        setMessageValue(e.target.value);
    }
    const handleIDInput = (e: { target: { value: React.SetStateAction<string>; }; }) => {
        setUserIDValue(e.target.value);
    }
    
      type newMessageProps = {
        // mID: number,
        mTitle: String,
        mContent: String,
        mID: String;
      }
      /**
       * handle click by getting user input and using that to make POST
       */
    const handeClick = async () =>{
        //get and store user input
        const titleInput = document.getElementById("newTitle") as HTMLInputElement | null;
        const titleVal = titleInput?.value;
        console.log(titleVal)

        const messageInput = document.getElementById("newMessage") as HTMLInputElement | null;
        const messageVal = messageInput?.value;
        console.log(messageVal)

        const IDInput = document.getElementById("newID") as HTMLInputElement | null;
        const IDVal = IDInput?.value;
        console.log(IDVal)
        
        //post
        /**
         * Make Post for new message
         */
        const doAjax = async () => {
            await fetch(`${backendUrl}/post`, {
                method: 'POST',
                body: JSON.stringify(
                {
                    mID: 40,
                    mTitle: titleVal,
                    //mContent: messageVal,
                    
                    mCreated: new Date(), 
    
                }),
                headers: {
                    // 'Accept': 'application/json',
                    'Content-type': 'application/json'
                },
            
            }).then( (response) => {
                // If we get an "ok" message, return the json
                if (response.ok) {
                    return Promise.resolve( response.json() );
                }
                // Otherwise, handle server errors with a detailed popup message
                else{
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then( (data) => {
                //newEntryForm.onSubmitResponse(data);
                console.log(data);
            }).catch( (error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);


        setTitleValue("");
        setMessageValue("");
        setUserIDValue("");
    }
    /**
     * HTML to allow user to make a new post
     */
    return(
        <>
        <p className="text-4xl text-gray-700 font-bold mb-5">
            Add a New Post
        </p>
        <Link to="/">
            <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
            Home</button>
        </Link>
        <div id="addElement">
            <label>Title</label>
            <input type="text" value={titleValue} onChange={handleTitleInput} id="newTitle" />
            <label>Message</label> 
            <input type="text" value={messageValue} onChange={handleMessageInput} id="newMessage"></input>
            <Link to="/">
            <button id="addButton" onClick={handeClick}>Add</button>
            <button id="addCancel">Cancel</button>
            </Link>
        </div>
        </>
    )
}


export default NewPostPage;