import * as React from 'react';
import {useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import {Box, Button, Grid, IconButton, Link, Snackbar, TextField, Typography} from "@mui/material";
import {useDispatch, useSelector} from "react-redux";
import actionProfile from "../actions/student-profile";
import Paper from "@mui/material/Paper";
import pageURLs from "../../../constants/pagesURLs";
import * as pages from "../../../constants/pages";
import {Edit} from "@mui/icons-material";

function StudentProfile(props) {
    let {id, mode} = useParams();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    let student = useSelector(({student}) => student.student);
    const [currentMode, setCurrentMode] = useState(mode);
    const [successSaveMessage, setSuccessSaveMessage] = useState(false);
    const [errors, setErrors] = useState({});

    const [formData, setFormData] = useState({
        username: null,
        firstName: null,
        lastName: null,
        courseId: null,
    });

    useEffect(() => {
        if (id) {
            dispatch(actionProfile.getStudentById(id))
        }
    }, [dispatch, id])

    useEffect(() => {
        if (student) {
            setFormData({
                username: student.username,
                firstName: student.firstName,
                lastName: student.lastName,
                courseId: student.courseId,
            })
        }
    }, [student])

    const getMode = () => (currentMode === 'view') ? {readOnly: true} : {readOnly: false};
    const changeMode = () => setCurrentMode(() => currentMode === 'view' ? 'edit' : 'view');

    const changeValue = (e) => {
        const {name, value} = e.target;

        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const showSuccessSaveMessage = () => {
        setSuccessSaveMessage(() => true)
    }

    const closeSuccessSaveMessage = () => {
        setSuccessSaveMessage(() => false)
    }

    const cancelEdit = () => {
        if (id) {
            setFormData({
                username: student.username,
                firstName: student.firstName,
                lastName: student.lastName,
                courseId: student.courseId,
            })
            changeMode()
        } else {
            navigate(`${pageURLs[pages.studentsPage]}`)
        }
    }

    const validate = () => {
        const newErrors = {};
        if (!formData.username) {
            newErrors.username = 'Username is required';
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const saveForm = (e) => {
        e.preventDefault();
        if (!validate()) {
            return;
        }
        if (id) {
            dispatch(actionProfile.update(id, formData));
            showSuccessSaveMessage();
        } else {
            dispatch(actionProfile.create(formData));
        }
    }

    return (
        <>
            <Grid
                container
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                my={2}
            >
                <Grid item xs={6}>
                    <Typography my={2} variant="h3">Profile</Typography>
                </Grid>
                <Typography align='right'>
                    <Grid container spacing={2}>
                        <Grid item xs={2}>
                            <IconButton color="secondary" onClick={changeMode}>
                                <Edit/>
                            </IconButton>
                        </Grid>
                        <Grid item xs={10}>
                            <Link href={`${pageURLs[pages.studentsPage]}`}>
                                Go back to table
                            </Link>
                        </Grid>
                    </Grid>
                </Typography>
            </Grid>

            <Paper elevation={1} sx={{
                p: 2
            }}>
                <Box my={2}>
                    <TextField
                        value={formData.username}
                        onChange={changeValue}
                        label="Username"
                        name="username"
                        variant="filled"
                        required
                        error={!!errors.username}
                        helperText={errors.username}
                        InputProps={getMode()}
                        fullWidth
                    />
                </Box>
                <Box my={2}>
                    <TextField
                        value={formData.firstName}
                        onChange={changeValue}
                        label="First Name"
                        name="firstName"
                        variant="filled"
                        InputProps={getMode()}
                        fullWidth
                    />
                </Box>
                <Box my={2}>
                    <TextField
                        value={formData.lastName}
                        onChange={changeValue}
                        label="Last Name"
                        name="lastName"
                        variant="filled"
                        InputProps={getMode()}
                        fullWidth
                    />
                </Box>
                <Box my={2}>
                    <TextField
                        value={formData.courseId}
                        onChange={changeValue}
                        label="Course Id"
                        name="courseId"
                        type="number"
                        variant="filled"
                        InputProps={getMode()}
                        fullWidth
                    />
                </Box>
                {
                    currentMode === 'edit' &&
                    <Box my={2}>
                        <Button variant="contained" onClick={saveForm}>
                            Save
                        </Button>
                        <Button variant="text" onClick={cancelEdit}>
                            Cancel
                        </Button>
                    </Box>
                }
                <Box sx={{width: 150}}>
                    <Snackbar
                        anchorOrigin={{
                            vertical: "top",
                            horizontal: "right"
                        }}
                        autoHideDuration={successSaveMessage}
                        open={successSaveMessage}
                        onClose={closeSuccessSaveMessage}
                        message="Student was successfully saved"
                    />
                </Box>
            </Paper>
        </>
    )
}

export default StudentProfile;