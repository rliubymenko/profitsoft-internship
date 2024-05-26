import {GET_STUDENT, RECEIVE_STUDENT} from "../constants/actionTypes";

const initialState = {
    isLoading: false,
    id: null,
    username: null,
    firstName: null,
    lastName: null,
    courseId: null,
    student: {}
};

export default (state = initialState, action) => {
    switch (action.type) {
        case GET_STUDENT: {
            return {
                ...state,
                isLoading: true,
            };
        }
        case  RECEIVE_STUDENT: {
            const {
                student
            } = action;

            return {
                ...state,
                isLoading: false,
                student: student,
            };
        }
        default:
            return state;
    }
};