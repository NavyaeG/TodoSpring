import  FirstComponent  from './FirstComponent';
import { SecondComponent } from './SecondComponent';
import LearningJavaScript from './LearningJavaScript';

export default function LearningComponent() {
    return (
      <div className="App">
        <FirstComponent></FirstComponent>
        <SecondComponent></SecondComponent>
        <LearningJavaScript></LearningJavaScript>
      </div>
    );
  }

