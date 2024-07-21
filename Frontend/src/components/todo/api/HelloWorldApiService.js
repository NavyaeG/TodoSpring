import { apiClient } from './ApiClient'

export function retriveHelloWorldBean(){
    return apiClient.get('/hello-world-bean')
}

export const retriveHelloWorldPathVariable = (username,token) =>  apiClient.get(`/hello-world/path-variable/${username}`)
