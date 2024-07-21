import './Counter.css'
import { useState } from 'react'
import CounterButton from './CounterButton'

//Parent component method
export default function Counter(){

    const [count,setCount]=useState(0)

    function incrementCounterParentFunction(val){
        setCount(count+val)
    }

    function decrementCounterParentFunction(val){
        setCount(count-val)
    }

    function resetCounter(val){
        setCount(0)
    }
    return (
        <>
            <span className="totalCount">{count}</span>
            <CounterButton val={1} 
                incrementMethod={incrementCounterParentFunction} 
                decrementMethod={decrementCounterParentFunction}>
            </CounterButton>
            <CounterButton val={5} 
                incrementMethod={incrementCounterParentFunction} 
                decrementMethod={decrementCounterParentFunction}>
            </CounterButton>
            <CounterButton val={10} 
                incrementMethod={incrementCounterParentFunction} 
                decrementMethod={decrementCounterParentFunction}>
            </CounterButton>
            <button
                className="resetButton" 
                onClick={resetCounter}
            >Reset
            </button>
        </>
    )
}