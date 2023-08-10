
import { type } from '@testing-library/user-event/dist/type';
import React, { useState } from 'react';
import "./DislikeButton.css"
/**
 * like button prop
 */
type DislikeButtonProps = {
    post_id: number,
    likeCount: number,
}
/**
 * 
 * @param props LikeButton proprs containing ID and likecount
 * @returns 
 */
const DislikeButton = (props: DislikeButtonProps) => {

  const backendUrl = "https://gentle-bastion-60274.herokuapp.com"
  
  var [likes, setLikes] = useState(props.likeCount);
  const [isClicked, setIsClicked] = useState(false);

  const btn = document.getElementById("like-bttn")!;

  const handleClick = () => {
    // console.log("likes before:" + likes);
    // if (isClicked) {
    //     setLikes(likes - 1);
    //     likes = likes -1;
    // } else {
    //     setLikes(likes + 1);
    //     likes = likes +1;
    // }
    setIsClicked(!isClicked);
    /**
     * put for updating like info
     */
    const doAjax = async () => {
      console.log("id number is:" + props.post_id);
      console.log("dislikes during ajax: " + likes);
      await fetch(`${backendUrl}/like`, {
          method: 'POST',
          body: JSON.stringify({
              like: "FALSE", //make false for dislike
            post_id: props.post_id,
            user_id: 6,
          }),
          headers: {
            'Content-type': 'application/json; charset=UTF-8'
        }
      }).then( (response) => {
          // If we get an "ok" message, return the json
          if (response.ok) {
              // return response.json();
              return Promise.resolve( response.json() );
          }
          // Otherwise, handle server errors with a detailed popup message
          else{
              window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
          }
          // return response;
          return Promise.reject(response);
      }).then( (data) => {
  
          console.log(data);
      }).catch( (error) => {
          console.warn('Something went wrong.', error);
          window.alert("Unspecified error");
      });
  }
  doAjax().then(console.log).catch(console.log);

  window.location.reload();

  };
  return (
    <div className='likeButton'>
      <div className={ `like-button ${isClicked && 'liked'}` }>
      <button id="like-bttn" className={isClicked ? 'blue' : 'gray'} onClick={ handleClick }>
        <span className="likes-counter">{ `Dislike | ${likes}` }</span>
      </button>
      </div>
    </div>
    
  );
};

export default DislikeButton;