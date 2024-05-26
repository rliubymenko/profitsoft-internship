import * as React from 'react';
import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import actionStudents from "../actions/students"
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Fab,
    Fade,
    Grid,
    IconButton,
    Link,
    Popper,
    Snackbar,
    TableFooter,
    TablePagination,
    TextField,
    Typography,
} from "@mui/material";
import {ArrowDropDown, ArrowDropUp, Delete} from '@mui/icons-material/';

import TablePaginationActions from "@mui/material/TablePagination/TablePaginationActions";
import TableContainer from "@mui/material/TableContainer";
import pageURLs from "../../../constants/pagesURLs";
import * as pages from "../../../constants/pages";
import {useNavigate} from "react-router-dom";

const getStudentHeaders = () => {
    return ['username', 'firstName', 'lastName']
}

function Students(props) {
    const successDeleteMessageTimeout = 3000;
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const students = useSelector(({students}) => students);
    const [showFilters, setShowFilters] = useState(false);
    const [deleteBin, setDeleteBin] = useState(null);
    const [binAnchor, setBinAnchor] = useState(null);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
    const [deleteErrorMessage, setDeleteErrorMessage] = useState('');
    const [successDeleteMessage, setSuccessDeleteMessage] = useState(false);

    useEffect(() => {
        dispatch(actionStudents.fetchStudents(students))
    }, [students.size, students.page, students.username, students.firstName, students.lastName, students.courseId]);

    const changeShowFilters = () => setShowFilters(isShown => !isShown)
    const setFilterParams = filter => dispatch(actionStudents.setFilterParams(filter))
    const changeUsername = username => setFilterParams({
        username: username || null,
        size: students.size,
        page: students.page
    })
    const changeFirstName = firstName => setFilterParams({
        firstName: firstName || null,
        size: students.size,
        page: students.page
    });
    const changeLastName = lastName => setFilterParams({
        lastName: lastName || null,
        size: students.size,
        page: students.page
    });
    const changeCourseId = courseId => setFilterParams({
        courseId: courseId || null,
        size: students.size,
        page: students.page
    });
    const clearFilters = () => {

        dispatch(actionStudents.clearFilterParams())
        dispatch(actionStudents.fetchStudents({
            page: 0,
            size: 5
        }))
    };
    const handleChangePage = (event, newPage) => {
        const filter = {size: students.size, page: newPage};
        setFilterParams(filter)
        dispatch(actionStudents.fetchStudents(filter));
    };
    const handleChangeRowsPerPage = (event) => {
        const filter = {size: parseInt(event.target.value, 10), page: 1};
        setFilterParams(filter)
        dispatch(actionStudents.fetchStudents(filter));
    };
    const navigateToProfileEdit = (id) => navigate(`${pageURLs[pages.studentProfilePage]}/${id}/view`)
    const showBinPopper = (id, event) => {
        setDeleteBin(id);
        setBinAnchor(event.currentTarget);
    }

    const removeBinAnchor = () => {
        setBinAnchor(null)
    }

    const handleClickOpenDeleteDialog = () => {
        setOpenDeleteDialog(true)
    };
    const handleClickCloseDeleteDialog = () => {
        setOpenDeleteDialog(false)
        setDeleteErrorMessage(() => '')
    };

    const showSuccessDeleteMessage = () => {
        setSuccessDeleteMessage(() => true)
    }

    const closeSuccessDeleteMessage = () => {
        setSuccessDeleteMessage(() => false)
    }

    const deleteStudent = () => {
        actionStudents.deleteStudentById(deleteBin)
            .then(() => {
                showSuccessDeleteMessage()
                dispatch(actionStudents.fetchStudents({
                    page: 0,
                    size: 5
                }))
                handleClickCloseDeleteDialog()
            })
            .catch(() => {
                setDeleteErrorMessage(() => 'Error while deleting student')
            })
    };

    return (
        students.isLoading ? <CircularProgress/> :
            (
                <Paper>
                    <Grid
                        container
                        direction="row"
                        justifyContent="space-between"
                        alignItems="center"
                        my={2}
                    >
                        <Grid item xs={6}>
                            <Button
                                onClick={changeShowFilters}
                                variant="contained"
                                disableElevation
                                endIcon={showFilters ? <ArrowDropUp/> : <ArrowDropDown/>}
                            >
                                {showFilters ? "Hide filters" : "Filter"}
                            </Button>
                        </Grid>
                        <Typography align='right'>
                            <Grid item xs={6}>
                                <Link href={`${pageURLs[pages.studentProfilePage]}/edit`}>
                                    Add Student
                                </Link>
                            </Grid>
                        </Typography>
                    </Grid>
                    {
                        showFilters && (
                            <Box py={1}>
                                <Card variant="outlined">
                                    <CardHeader
                                        titleTypographyProps={{variant: "h6"}}
                                        title="Filter Options"
                                    />
                                    <CardContent>
                                        <Grid container spacing={4}>
                                            <Grid item spacing={1}>
                                                <Grid item xs={12}>
                                                    <TextField
                                                        value={students.username}
                                                        onChange={(e) => changeUsername(e.target.value)}
                                                        label="Username"
                                                        variant="filled"
                                                        fullWidth
                                                    />
                                                </Grid>
                                            </Grid>
                                            <Grid item spacing={1}>
                                                <Grid item xs={12}>
                                                    <TextField
                                                        value={students.firstName}
                                                        onChange={(e) => changeFirstName(e.target.value)}
                                                        label="First Name"
                                                        variant="filled"
                                                        fullWidth
                                                    />
                                                </Grid>
                                            </Grid>
                                        </Grid>
                                        <Grid my={1} container spacing={4}>
                                            <Grid item spacing={1}>
                                                <Grid item xs={12}>
                                                    <TextField
                                                        value={students.lastName}
                                                        onChange={(e) => changeLastName(e.target.value)}
                                                        label="Last Name"
                                                        variant="filled"
                                                        fullWidth
                                                    />
                                                </Grid>
                                            </Grid>
                                            <Grid item spacing={1}>
                                                <Grid item xs={12}>
                                                    <TextField
                                                        value={students.courseId}
                                                        onChange={(e) => changeCourseId(e.target.value)}
                                                        label="Course Id"
                                                        type="number"
                                                        variant="filled"
                                                        fullWidth
                                                    />
                                                </Grid>
                                            </Grid>
                                        </Grid>
                                    </CardContent>
                                    <CardActions>
                                        <Button onClick={clearFilters} size="small">
                                            Reset
                                        </Button>
                                    </CardActions>
                                </Card>
                            </Box>
                        )
                    }
                    <TableContainer component={Paper}>
                        <Table sx={{minWidth: 650}} aria-label="Basic Table">
                            <TableHead>
                                <TableRow>
                                    {
                                        getStudentHeaders().map((headerName) => (
                                            <TableCell>{headerName}</TableCell>
                                        ))
                                    }
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {students.students.map((student, index) => (
                                    <TableRow key={index}
                                              sx={{
                                                  '&:hover': {
                                                      cursor: 'pointer',
                                                      backgroundColor: 'rgba(0, 0, 0, 0.08)',
                                                  }
                                              }}
                                              onClick={() => navigateToProfileEdit(student.id)}
                                              onMouseEnter={(event) => showBinPopper(student.id, event)}
                                              onMouseLeave={removeBinAnchor}
                                    >
                                        <TableCell>
                                            {student.username}
                                        </TableCell>
                                        <TableCell>
                                            {student.firstName}
                                        </TableCell>
                                        <TableCell>
                                            {student.lastName}
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                            <TableFooter>
                                <TableRow>
                                    <TablePagination
                                        rowsPerPageOptions={[5, 10, 15]}
                                        colSpan={3}
                                        count={students.totalPages * students.size}
                                        rowsPerPage={students.size}
                                        page={students.page}
                                        slotProps={{
                                            select: {
                                                inputProps: {
                                                    'aria-label': 'rows per page',
                                                },
                                                native: true,
                                            },
                                        }}
                                        onPageChange={handleChangePage}
                                        onRowsPerPageChange={handleChangeRowsPerPage}
                                        ActionsComponent={TablePaginationActions}
                                    />
                                </TableRow>
                            </TableFooter>
                        </Table>
                    </TableContainer>
                    <Popper
                        open={deleteBin !== null}
                        anchorEl={binAnchor}
                        placement="right"
                        onClose={removeBinAnchor}
                        transition>
                        {({TransitionProps}) => (
                            <Fade {...TransitionProps} timeout={350}>
                                <Fab>
                                    <IconButton color="error"
                                                aria-label="delete"
                                                size="medium"
                                                onClick={handleClickOpenDeleteDialog}>
                                        <Delete/>
                                    </IconButton>

                                    <Dialog open={openDeleteDialog}
                                            onClose={handleClickCloseDeleteDialog}
                                    >
                                        <DialogTitle>Delete confirmation dialog</DialogTitle>
                                        <DialogContent>
                                            <DialogContentText>
                                                {deleteErrorMessage || 'Do you want ot delete this student?'}
                                            </DialogContentText>
                                        </DialogContent>
                                        <DialogActions>
                                            <Button onClick={deleteStudent} autoFocus>Delete</Button>
                                            <Button onClick={handleClickCloseDeleteDialog}>Cancel</Button>
                                        </DialogActions>
                                    </Dialog>
                                </Fab>
                            </Fade>
                        )}
                    </Popper>
                    <Box sx={{width: 150}}>
                        <Snackbar
                            anchorOrigin={{
                                vertical: "top",
                                horizontal: "right"
                            }}
                            autoHideDuration={successDeleteMessageTimeout}
                            open={successDeleteMessage}
                            onClose={closeSuccessDeleteMessage}
                            message="Student was successfully deleted"
                        />
                    </Box>
                </Paper>
            )
    )
}

export default Students;