import {combineReducers} from 'redux';

import user from './user';
import students from "../../pages/students/reducers/student";
import student from "../../pages/student-profile/reducers/student-profile";

export default combineReducers({
    user,
    students,
    student
});
