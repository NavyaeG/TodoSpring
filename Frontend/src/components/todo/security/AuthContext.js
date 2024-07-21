import { createContext, useState, useContext } from "react";
import { executeBasicAuthenticationService ,executeJwtAuthenticationService} from "../api/AuthenticateApiService";
import { apiClient } from "../api/ApiClient";

//1. Create a context
//React Context is a way to manage state globally. 
//It can be used together with the useState Hook to share state between deeply nested components more easily than with useState alone.
export const AuthContext=createContext()

export const useAuth = () => useContext(AuthContext)//creating a hook that can be used by other components to use useAuth


//2. Sharing the created context(authprovider) with other components
export default function AuthProvider({children}){

    //3. Put some state in the context
    const [isAuthenticated,setAuthenticated]=useState(false)
    const [username,setUsername]=useState(null)
    const [token,setToken]=useState(null)
    
    // async function login(username,password){
        
    //     const baToken="Basic "+ window.btoa(username+":"+password)
    //     try{
    //         const response=await executeBasicAuthenticationService(baToken)//waiting for this call to return a response and then using that response to validate

    //         if(response.status==200){
    //             setAuthenticated(true)
    //             setUsername(username)
    //             setToken(baToken)

    //             //all api calls are being intercepted by this and it is adding authorization header
    //             apiClient.interceptors.request.use(
    //                 (config)=>{
    //                     console.log('intercepting and adding a token')
    //                     config.headers.Authorization= baToken
    //                     return config
    //                 }
    //             )

    //             return true
    //         }
    //         else{
    //             logout()
    //             return false
    //         }
    //     } catch(error){
    //         logout()
    //         return false
    //     }
    // }

    async function login(username,password){
        
        try{
            const response=await executeJwtAuthenticationService(username,password)//waiting for this call to return a response and then using that response to validate

            if(response.status==200){
                const jwtToken='Bearer '+response.data.token
                console.log(response)
                setAuthenticated(true)
                setUsername(username)
                setToken(jwtToken)

                //all api calls are being intercepted by this and it is adding authorization header
                apiClient.interceptors.request.use(
                    (config)=>{
                        console.log('intercepting and adding a token')
                        config.headers.Authorization= jwtToken
                        return config
                    }
                )

                return true
            }
            else{
                logout()
                return false
            }
        } catch(error){
            logout()
            return false
        }
    }

    function logout(){
        setAuthenticated(false)
        setUsername(null)
        setToken(null)
    }

    // const valueToBeShatred={number, isAuthenticated, setAuthenticated}//object with all the values to be shared, but in this case we are passing all of them directly

    return (
        <AuthContext.Provider value={ {isAuthenticated, login, logout, username, token} }>
            {children}
        </AuthContext.Provider>
    )
}