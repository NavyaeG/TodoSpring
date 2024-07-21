import { useState } from 'react'
import {useNavigate} from 'react-router-dom'
import { useAuth } from './security/AuthContext'

function LoginComponent(){
    const [username,setUsername]=useState ('nav')
    const [password,setPassword]=useState ("hello")
    const [showErrorMessage, setShowErrorMessage]=useState(false)
    const navigate=useNavigate()//useNavigate hook lets navigate to a different page
    const authContext=useAuth()

    function handleUsernameChange(event){
        setUsername(event.target.value)
    }

    function handlePasswordChange(event){
        setPassword(event.target.value)
    }

    async function handleSubmit(){
        if(await authContext.login(username,password)){
            navigate(`/welcome/${username}`)
        }
        else{
            setShowErrorMessage(true)
        }
    }
    
    return (
        <div className="Login">
            <div className="LoginForm">
                <h1>Time to Login</h1>
                {showErrorMessage && <div className='errorMessage'>Authenticated Failed. Please check your credentials</div>}{/*showing element only if showErrorMessage value is true*/}
                <div>
                    <label>User Name</label>
                    <input type="text" name="username" value={username} onChange={handleUsernameChange}/>
                </div>
                <div>
                    <label>Password</label>
                    <input type="text" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div>
                    <button type="button" name="login" onClick={handleSubmit}>login</button>
                </div>
            </div>
        </div>
    )
}

export default LoginComponent