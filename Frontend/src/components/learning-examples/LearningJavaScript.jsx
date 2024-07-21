const person={
    name:"Nav",
    address:{
        line1:"123 Baker Street",
        city:"london",
        country:"UK"
    },
    profiles: ['instagram','twitter'],
    printProfile: ()=>{
        for(let i=0; i<person.profiles.length;i++){
            console.log(person.profiles[i])
        }
    }
}


export default function LearningJavaScript(){
    return(
        <>
            <div>{person.name}</div>
            <div>{person.address.line1} {person.address.city}</div>
            <div>{person.printProfile()}</div>
        </>
    )
}