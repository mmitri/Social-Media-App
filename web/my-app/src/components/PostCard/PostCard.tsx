import LikeButton from '../LikeButton/LikeButton';
import DislikeButton from '../DislikeButton/DislikeButton';
import ProfileButton from '../ProfileButton/ProfileButton';
import CommentButton from '../CommentButton/CommentButton';
import "./PostCard.css"
import { Link } from 'react-router-dom';
import { text } from 'stream/consumers';
import { CommentStyle } from 'typedoc';
import FileButton from '../AddFileButton/FileButton';
//import { PromptProps } from 'react-router-dom';

interface Comment {
    comment_id: number,
    post_id: number, 
    user_id: number, 
    message: string
}


type PostCardProps = {
    post_id: number, 
    title: string;
    message: string;
    user_id: number;
    poster_name: string;
    valid: boolean;
    likes_number: number;
    dislikes_number: number;
    comments: Comment[];
    file: string;
};

//use prop to create post card type,
//what each post is framed with
/**
 * 
 * @param props for post card
 * @returns format and content of card
 */
const PostCard = (props: PostCardProps, props2: Comment) =>{
    const commentList = JSON.stringify(props.comments);
    return(
        <>
        <div className="container mx-auto bg-gray-200 rounded-xl shadow border p-8 m-8">
            <div className='messageContent'>
                <h4>{props.title}</h4>
                <Link to="/add-post">
                        <button type="button" className="text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700">
                        {props.poster_name}</button>
                </Link>
                {/*<p> {props.poster_name}</p>*/}
                <p>{props.message}</p>
                {/* <p>{props.user_id}</p> */}
                {/* <p>{props.valid}</p> */}
                <LikeButton  post_id={props.post_id} likeCount={props.likes_number}></LikeButton>
                <DislikeButton  post_id={props.post_id} likeCount={props.likes_number}></DislikeButton>
                <Link to="/edit-user-profile">
                <ProfileButton></ProfileButton>
                </Link>
                <Link to="/comment">
                <CommentButton post_id={props.post_id}/>
                </Link>
                <a href="https://www.youtube.com/watch?v=xvFZjo5PgG0" target="_blank">Visit my website!</a>
                <Link to="/add-file"><FileButton></FileButton></Link>
            </div>  
        </div>
        </>
    )
}

export default PostCard;

