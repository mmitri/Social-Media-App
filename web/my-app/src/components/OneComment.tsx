import Homepage from "../pages/HomePage/HomePage";
import Postcard from "../components/PostCard/PostCard";
import { Link } from 'react-router-dom';

type OneCommentProps = {
    post_id : number;
    message : string;
}

 const OneComment = (props: OneCommentProps) =>{
    return(
        <>
        <div className="container mx-auto bg-gray-200 rounded-xl shadow border p-8 m-8">
            <div className='messageContent'>
                <p> {props.post_id}</p>
                <p>{props.message}</p>
                {/* <p>{props.user_id}</p> */}
                {/* <p>{props.valid}</p> */}
            </div>  
        </div>
        </>
    )}

    export default OneComment;
