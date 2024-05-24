import {CLEAR_FILTER_PARAMS, RECEIVE_STUDENTS, REQUEST_STUDENTS, SET_FILTER_PARAMS} from "../constants/actionTypes";
import storage from "misc/storage";

const initialState = {
    isLoading: false,
    students: [],
    totalPages: 0,
    page: 0,
    size: 5,
    username: null,
    firstName: null,
    lastName: null,
    courseId: null,
};

export default (state = initialState, action) => {
    switch (action.type) {
        case REQUEST_STUDENTS: {
            const filter = storage.getItem('filter')
                ? JSON.parse(storage.getItem('filter'))
                : {
                    size: state.size,
                    page: state.page,
                    username: state.username,
                    firstName: state.firstName,
                    lastName: state.lastName,
                    courseId: state.courseId
                }

            return {
                ...state,
                ...filter,
                isLoading: true,
            };
        }
        case RECEIVE_STUDENTS: {
            const {
                students,
                totalPages
            } = action.students;

            return {
                ...state,
                isLoading: false,
                students,
                totalPages
            };
        }
        case SET_FILTER_PARAMS: {
            storage.setItem('filter', JSON.stringify(action.filter))
            return {
                ...state,
                ...action.filter
            };
        }
        case CLEAR_FILTER_PARAMS: {
            storage.removeItem('filter')
            return {
                ...initialState
            };
        }
        default:
            return state;
    }
};