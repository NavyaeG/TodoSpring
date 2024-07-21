import { apiClient } from './ApiClient'

export function retriveAllTodosForUsernameApi(username){
    return apiClient.get(`/users/${username}/todos`)
}

export function deleteTodoApi(username,id){
    return apiClient.delete(`/users/${username}/todos/${id}`)
}

export function retrieveTodoApi(username,id){
    return apiClient.get(`/users/${username}/todos/${id}`)
}

export function updateTodoApi(username,id,todo){
    return apiClient.put(`/users/${username}/todos/${id}`,todo)
}

export const createTodoApi = (username,  todo) => apiClient.post(`/users/${username}/todos`, todo) //same thing as above but in arrow function