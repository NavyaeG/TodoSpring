import { useEffect, useState } from "react"
import { retriveAllTodosForUsernameApi,deleteTodoApi } from "./api/TodoApiService copy"
import { useAuth } from "./security/AuthContext"
import { useNavigate } from "react-router-dom"
 
 export default function ListTodosComponent(){
    const today = new Date()
    const authContext=useAuth()
    const username=authContext.username
    const targetDate= new Date(today.getFullYear()+12, today.getMonth(),today.getDay())
    const [todos,setTodos] =useState([])
    const [message,setMessage] =useState(null)
    const navigate=useNavigate()

    useEffect(
        ()=>refreshTodos(),[]
    )

    function refreshTodos(){
        retriveAllTodosForUsernameApi(username)
        .then((response)=>{
            setTodos(response.data)
        })
        .catch((error)=>console.log(error))
    }

    function deleteTodo(id){
        deleteTodoApi(username,id)
        .then((response)=>{
            setMessage(`Deleted of todo with id: ${id}`)
            refreshTodos()
        })
        .catch((error)=>console.log(error))
    }

    function updateTodo(id){
        navigate(`/todo/${id}`)
        console.log(id)
    }
   
    function addNewTodo() {
        navigate(`/todo/-1`)
    }

    return (
        <div className="container">
            <h1>Things you want to do!</h1>
            {message && <div className="alert alert-warning">{message}</div>}
            <div>
                <table className="table">
                    <thead>
                        <tr>
                            <th>description</th>
                            <th>done</th>
                            <th>target date</th>
                            <th>Delete</th>
                            <th>Update</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            todos.map(
                                todo =>(
                                    <tr key={todo.id}>
                                        <td>{todo.description}</td>
                                        <td>{todo.done.toString()}</td>
                                        <td>{todo.targetDate.toString()}</td>
                                        <td><button className="btn btn-warning" onClick={()=>deleteTodo(todo.id)}>Delete</button></td>
                                        <td><button className="btn btn-success" onClick={()=>updateTodo(todo.id)}>Update</button></td>
                                    </tr>
                                )
                            )
                        }
                    </tbody>
                </table>
            </div>
            <div className="btn btn-success m-5" onClick={addNewTodo}>Add New Todo</div>
        </div>
    )
}