import './App.css';
import TodoApp from "./components/todo/TodoApp.jsx"

// import  FirstComponent  from './components/learning-examples/FirstComponent';// if not using {} while importing, you need to use export default while exporting from that file
// //if not using {} while importing we can use any name and then use that name

// import { SecondComponent } from './components/learning-examples/SecondComponent';// ifusing {} while importing, you need to use export while exporting from that file
// //if not {} while importing we have to use the specific name of function or class

function App() {
  return (
    <div className="App">
      {/* <Counter></Counter> */}
      <TodoApp></TodoApp>
    </div>
  );
}

//State Built in react object used to contain datat or info about component

//earlier versions of react only had state for class components

//recent versions even function components have state-> need to use Hooks for them
//useState hook allows adding state to Function Components
//function components always recommended over class components


//Whats happening in the background with React?
//HTML page is represented by DOM -> each element in page is a node in DOM -> need to update DOM to update element
//Virtual DOM: representation of UI(kept in memory)

//1. React creates virtual DOM v1 on load of a page
//2. When user performs an action
//3. React creates Virtual DOM v2 of the page as a result of action
//4. React performs diff between v1 and v2
//5. React synchrnoizes changes


//props
// you can pass props object to a react component
//used for things that remain constant during lifetime of a component

export default App;