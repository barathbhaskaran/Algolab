import React, { useEffect, useState } from 'react';
import './feedback.module.css';
import Header from "../../components/Header";
import styles from "../Feedback/feedback.module.css";
import {
    TextField,
    Box,
    Button,
    InputLabel,
    Select,
    MenuItem,
    Alert,
    Typography,
    Accordion,
    AccordionDetails, AccordionSummary
} from "@mui/material";
import backendCall from "../../utils/network";
import Snackbar from "@mui/material/Snackbar";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

export default function Feedback({ }) {
    const [token, setToken] = useState('');
    const [role, setRole] = useState('');

    const [instructorUsername, setInstructorUsername] = useState('');
    const [courseId, setCourseId] = useState('');
    const [feedback, setFeedback] = useState('');
    const [allFeedback, setAllFeedback] = useState([]);
    const [feedbackByInstructor, setFeedbackByInstructor] = useState([]);
    const [submitted, setSubmitted] = useState(false);
    const [allCourses, setAllCourses] = useState([]);
    const [message, setMessage] = useState('');
    const [isSnackbarOpen, setIsSnackbarOpen] = useState(false);
    const [snackType, setSnackType] = useState('success');

    useEffect(() => {
        let role = window.localStorage.getItem('role');
        let token = window.localStorage.getItem('token');
        setToken(token);

        if (token == null || token == '') {
            window.localStorage.removeItem('token');
            window.localStorage.removeItem('role');
            window.location = '/login';
        }
        setRole(role);

        if (role === 'ADMIN') {
            getAllFeedback(token);
        }

        if (role === 'INSTRUCTOR') {
            getUserDetails(token);
        }
        getAllCourses(token);
    }, []);

    const getAllFeedback = async (token) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.get('/api/v1/feedback/viewAll', config)
            .then((res) => {
                let allFeedback = res.data;
                setAllFeedback(allFeedback);
            }).catch((err) => {
                console.log('login error : ', err);
                if (err.response && err.response.data && err.response.data.error) {
                    console.log('err : ', err);
                    setMessage(err.response.data.error);
                    setSnackType('error');
                    setIsSnackbarOpen(true);
                }
            });
    }

    const getUserDetails = async (token) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.get('/api/v1/userDetails', config).then((res) => {
            let userData = res.data;
            setInstructorUsername(userData.username);
            getFeedbackByInstructor(token, userData.username);
        }).catch((err) => {
            if (err.response && err.response.data && err.response.data.error) {
                console.log('err : ', err);
                setMessage(err.response.data.error);
                setSnackType('error');
                setIsSnackbarOpen(true);
            }
        });
    }

    const getFeedbackByInstructor = async (token, username) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };
        await backendCall.get('/api/v1/feedback/viewCourseFeedbackByInstructor/' + username, config)
            .then((res) => {
                let feedbackByInstructor = res.data;
                setFeedbackByInstructor(feedbackByInstructor);
            }).catch((err) => {
                if (err.response && err.response.data && err.response.data.error) {
                    setMessage(err.response.data.error);
                    setSnackType('error');
                    setIsSnackbarOpen(true);
                }
            });
    }

    const getAllCourses = async (token) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.get('/api/v1/allCourses', config).then((res) => {
            let coursesData = res.data;
            setAllCourses(coursesData);
        }).catch((err) => {
            if (err.response && err.response.data && err.response.data.error) {
                console.log('err : ', err);
                setMessage(err.response.data.error);
                setSnackType('error');
                setIsSnackbarOpen(true);
            }
        });
    }

    const onCourseIdChange = (event) => {
        setCourseId(event.target.value);
    }

    const onFeedbackChange = (e) => {
        setFeedback(e.target.value);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        submitFeedback(token).then(() => {
            setSubmitted(true);
            setFeedback('');
            setMessage("Feedback successfully submitted");
            setSnackType('success');
            setIsSnackbarOpen(true);
        });
    };

    const submitFeedback = async (token) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.post('/api/v1/feedback/add/' + courseId, {
            content: feedback
        }, config)
            .then((res) => {
            }).catch((err) => {
                if (err.response && err.response.data && err.response.data.error) {
                    console.log('err : ', err);
                    setMessage(err.response.data.error);
                    setSnackType('error');
                    setIsSnackbarOpen(true);
                }
            });
    }

    const handleSnackbarClose = () => {
        setIsSnackbarOpen(false);
    }

    return (
        <>
            <Header />
            {role === 'STUDENT' && (
                <Box className={styles.bodyContainer}>
                    <Box className={styles.formContainer}>
                        {role === 'STUDENT' && (
                            <>
                                <InputLabel id="course-label" style={{ textAlign: 'left' }}>
                                    Course
                                </InputLabel>
                                <Select
                                    labelId="course-label"
                                    value={courseId}
                                    sx={{ width: '400px' }}
                                    onChange={onCourseIdChange}
                                    placeholder="Course"
                                >
                                    {allCourses && allCourses.map((course) => (
                                        <MenuItem key={course.id} value={course.id}>
                                            {course.title}
                                        </MenuItem>
                                    ))}
                                </Select>
                                <br />
                                <br />
                                <TextField
                                    id="standard-basic"
                                    multiline
                                    value={feedback}
                                    onChange={onFeedbackChange}
                                    sx={{ width: '400px' }}
                                    variant="outlined"
                                    placeholder="Feedback"
                                    rows={10}
                                    cols={20}
                                />
                                <br />
                                <br />
                                <Button
                                    className="button_style"
                                    variant="contained"
                                    color="primary"
                                    size="small"
                                    onClick={handleSubmit}
                                    sx={{ padding: '10px 20px' }}
                                >
                                    Submit
                                </Button>
                            </>
                        )}
                    </Box>
                </Box>
            )}

            {role === 'INSTRUCTOR' && (
                <>
                    <Box>
                        <Box>
                            <Typography variant="h5" gutterBottom>
                                Feedbacks
                            </Typography>
                            {feedbackByInstructor.map((feedbackItem) => (
                                <Accordion key={feedbackItem.id}>
                                    <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                        <Typography>{feedbackItem.course.title}</Typography>
                                    </AccordionSummary>
                                    <AccordionDetails>
                                        <Typography>{feedbackItem.content}</Typography>
                                    </AccordionDetails>
                                </Accordion>
                            ))}
                        </Box>
                    </Box>
                </>
            )}

            {role === 'ADMIN' && (
                <>
                    <Box>
                        <Box>
                            <Typography variant="h5" gutterBottom>
                                Feedbacks
                            </Typography>
                            {allFeedback.map((feedbackItem) => (
                                <Accordion key={feedbackItem.id}>
                                    <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                        <Typography>{feedbackItem.course.title}</Typography>
                                    </AccordionSummary>
                                    <AccordionDetails>
                                        <Typography>{feedbackItem.content}</Typography>
                                    </AccordionDetails>
                                </Accordion>
                            ))}
                        </Box>
                    </Box>
                </>
            )}
            <Snackbar open={isSnackbarOpen} autoHideDuration={4000} onClose={handleSnackbarClose}>
                <Alert severity={snackType} onClose={handleSnackbarClose}>
                    {message}
                </Alert>
            </Snackbar>
        </>
    );
}
