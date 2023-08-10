
import { type } from '@testing-library/user-event/dist/type';
import React, { useState, useEffect } from 'react';
import "./CommentButton.css"
/**
 * like button prop
 */
type CommentButtonProps = {
    post_id : number;
}

type CommentProps = {
  message : string;
}
/**
 * 
 * @param props LikeButton proprs containing ID and likecount
 * @returns 
 */
const CommentButton = (props: CommentButtonProps) => {

  const backendUrl = "https://gentle-bastion-60274.herokuapp.com"
  
  var [likes, setLikes] = useState();
  const [isClicked, setIsClicked] = useState(false);

  const btn = document.getElementById("like-bttn")!;

  const handleClick = () => {

  
    setIsClicked(!isClicked);

    /**
     * put for updating like info
     */
  };

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
    <div className='likeButton'>
      <div className={ `like-button ${isClicked && 'liked'}` }>
      <button id="like-bttn" className={isClicked ? 'blue' : 'gray'} onClick={ handleClick }>
        <span className="likes-counter">{ `Comment` }</span>
      </button>
      </div>
    </div>
    <div>{props.post_id}</div>
    </>
  );
};

export default CommentButton;