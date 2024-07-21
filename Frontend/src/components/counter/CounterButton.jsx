import {PropTypes} from 'prop-types'

export default function CounterButton({val, incrementMethod, decrementMethod}){
    // function incrementCounterFunction(){
    //     incrementMethod(val)
    // }

    function decrementCounterFunction(){
        decrementMethod(val)
    }

    return (
        <div className="Counter">
            <div>
                <button className="counterButton" 
                        onClick={()=>incrementMethod(val)}//passing method directly and passing argument using arrow function
                >+{val}</button>
                <button className="counterButton" 
                        onClick={decrementCounterFunction}//passing function here that is defined above
                >-{val}</button>
            </div>
        </div>
    )
}

CounterButton.propTypes={
    val:PropTypes.number
}

CounterButton.defaultProps={
    val: 1
}