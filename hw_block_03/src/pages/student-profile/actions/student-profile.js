import {GET_STUDENT, RECEIVE_STUDENT} from "../constants/actionTypes";
import config from "../../../config";
import axios from "../../../misc/requests";

const getStudent = () => ({type: GET_STUDENT});
const receiveStudent = (student) => ({student, type: RECEIVE_STUDENT});

const getStudentById = (id) => (dispatch) => {
    dispatch(getStudent());
    return axios.get(`${config.USERS_SERVICE}/students/${id}`)
        .then(student => dispatch(receiveStudent(student)))
};

const createStudent = async (student) => {
    return await axios.post(
        `${config.USERS_SERVICE}/students`,
        student,
        {
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                "Access-Control-Allow-Origin": "*",
            }
        }
    )
}

const updateStudent = async (id, student) => {
    return await axios.put(
        `${config.USERS_SERVICE}/students/${id}`,
        student,
        {
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                "Access-Control-Allow-Origin": "*",
            }
        }
    )
}

const create = (student) => (dispatch) => {
    return createStudent(student).then(students => dispatch(receiveStudent(students)))
};

const update = (id, student) => (dispatch) => {
    return updateStudent(id, student).then(students => dispatch(receiveStudent(students)))
};

export default {
    getStudentById,
    create,
    update
}



