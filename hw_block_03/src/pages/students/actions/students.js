import axios from "misc/requests";
import config from "config";
import {
    CLEAR_FILTER_PARAMS,
    ERROR_RECEIVE_STUDENTS,
    RECEIVE_STUDENTS,
    REQUEST_STUDENTS,
    SET_FILTER_PARAMS
} from "../constants/actionTypes";

const requestStudents = () => ({type: REQUEST_STUDENTS});
const receiveStudents = students => ({students, type: RECEIVE_STUDENTS});
const setFilterParams = filter => ({filter, type: SET_FILTER_PARAMS});
const clearFilterParams = () => ({type: CLEAR_FILTER_PARAMS});
const errorReceiveStudents = () => ({type: ERROR_RECEIVE_STUDENTS});

const getStudents = async ({username, firstName, lastName, courseId, page, size}) => {
    const {USERS_SERVICE} = config;

    return await axios.post(
        `${USERS_SERVICE}/students/_list`,
        {username, firstName, lastName, courseId, page, size},
        {
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                "Access-Control-Allow-Origin": "*",
            }
        }
    )
};

const deleteStudentById = async (id) => {
    return await axios.delete(`${config.USERS_SERVICE}/students/${id}`)
};

const fetchStudents = (filterParams) => (dispatch) => {
    dispatch(requestStudents());
    return getStudents(filterParams)
        .then(students => dispatch(receiveStudents(students)))
        .catch(() => dispatch(errorReceiveStudents()));
};

export default {
    fetchStudents,
    setFilterParams,
    clearFilterParams,
    deleteStudentById
};
