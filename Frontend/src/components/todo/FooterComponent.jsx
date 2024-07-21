import { AuthContext,useAuth } from "./security/AuthContext"
import { useContext } from "react"

export default function FooterComponent(){

    //const authContext=useContext(AuthContext)
    const authContext=useAuth()

    return (
        <footer className="footer">
            <div className="container">
                footer
            </div>
        </footer>
    )
}