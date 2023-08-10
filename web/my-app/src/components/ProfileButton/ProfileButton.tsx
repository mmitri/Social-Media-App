
import { type } from '@testing-library/user-event/dist/type';
import React, { useState } from 'react';
import "./ProfileButton.css"
/**
 * like button prop
 */
type ProfileButtonProps = {

}
/**
 * 
 * @param props LikeButton proprs containing ID and likecount
 * @returns 
 */
const ProfileButton = (props: ProfileButtonProps) => {

  const backendUrl = "https://gentle-bastion-60274.herokuapp.com"
  
  var [likes, setLikes] = useState();
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
  };
  return (
    <div className='likeButton'>
      <div className={ `like-button ${isClicked && 'liked'}` }>
      <button id="like-bttn" className={isClicked ? 'blue' : 'gray'} onClick={ handleClick }>
        <span className="likes-counter">{ `Profile` }</span>
      </button>
      </div>
    </div>
    
  );
};

export default ProfileButton;