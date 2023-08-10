import { Link } from 'react-router-dom';
//import { PromptProps } from 'react-router-dom';

type ProfileProps = {
    id: number,
    name: string,
    email: string,
    gender: string,
    sexual_orientation: string,
    note: string,
    valid: boolean;
};


//use prop to create post card type,
//what each post is framed with
/**
 * 
 * @param props for post card
 * @returns format and content of card
 */
const ProfileCard = (props: ProfileProps) =>{
    return(
        <div className="container mx-auto bg-gray-200 rounded-xl shadow border p-8 m-8">
            <div className='messageContent'>
                <p> {props.name}</p>
                <p>{props.email}</p>
                <p>{props.note}</p>
                {/* <p>{props.user_id}</p> */}
                {/* <p>{props.valid}</p> */}
            </div>  
        </div>

    )
}

export default ProfileCard;

