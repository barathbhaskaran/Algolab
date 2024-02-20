
import React, { useEffect, useState } from 'react';
import { Alert, Box, Button, Collapse, Dialog, DialogActions, DialogContent, DialogTitle, Grid, IconButton, InputLabel, List, ListItem, ListItemSecondaryAction, ListItemText, MenuItem, Paper, Select, Snackbar, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, ToggleButtonGroup, Typography, makeStyles, styled, } from "@mui/material";
import styles from './lesson.module.css';
import Header from '../../components/Header';
import AddIcon from '@mui/icons-material/Add';
import MuiToggleButton from "@mui/material/ToggleButton";
import backendCall from '../../utils/network';
import { useParams } from 'react-router-dom';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';


export default function Lesson() {


    const LessonDialog = ({ open, onClose, onError, isUpdate }) => {

        const [title, setTitle] = useState();
        const [lessonContent, setLessonContent] = useState('');
        const [mediaLink, setMediaLink] = useState('');
        const [practiceQuestions, setPracticeQuestions] = useState([]);


        const handleClose = () => {
            onClose();
        };

        const handleAddQuestion = () => {
            setPracticeQuestions((prevQuestions) => [
                ...prevQuestions,
                { questionName: '', questionDifficulty: '', answerContent: '', questionLink: '' },
            ]);
        };

        const handleRemoveQuestion = (index) => {
            setPracticeQuestions((prevQuestions) => {
                const updatedQuestions = [...prevQuestions];
                updatedQuestions.splice(index, 1);
                return updatedQuestions;
            });
        };

        const handleQuestionChange = (index, fieldName, value) => {
            setPracticeQuestions((prevQuestions) => {
                const updatedQuestions = [...prevQuestions];
                updatedQuestions[index] = {
                    ...updatedQuestions[index],
                    [fieldName]: value,
                };
                return updatedQuestions;
            });
        };

        useEffect(() => {
            if (lessonData !== null && isUpdate) {
                setTitle(lessonData.title);
                let content = lessonData.contents[0];
                if (content !== null) {
                    setPracticeQuestions(content.practiceQuestions);
                    setLessonContent(content.data);
                    setMediaLink(content.mediaLink);
                }

            }
        }, []);

        const isValid = () => {
            // Validate title length
            if (title.length > 100) {
                onError('Title should be less than 100 characters');
                return false;
            }

            // Validate media link format (simple check for URL format)
            // const mediaLinkPattern = /^(http|https):\/\/[\w-]+(\.[\w-]+)+([\w.,@?^=%&:/~+#-]*[\w@?^=%&/~+#-])?$/;
            // if (!mediaLinkPattern.test(mediaLink)) {
            //     onError('Invalid media link format');
            //     return false;
            // }

            // Validate content length
            if (lessonContent.split(' ').length > 500) {
                onError('Content should not be more than 500 words');
                return false;
            }

            // Validate practice questions
            for (const question of practiceQuestions) {
                if (
                    question.questionName.trim() === '' ||
                    question.questionDifficulty.trim() === '' ||
                    question.answerContent.trim() === '' ||
                    question.questionLink.trim() === ''
                ) {
                    onError('All practice question fields should be filled');
                    return false;
                }
            }

            // All validations passed
            return true;
        };

        const handleSubmit = async () => {
            if (isValid()) {
                let data = {
                    courseId: course.id,
                    title: title,
                    contents: [{
                        data: lessonContent,
                        mediaLink: mediaLink,
                        practiceQuestions: practiceQuestions
                    }]
                }
                const response = await backendCall.post('/api/v1/createLesson', data, {
                    headers: {
                        'Authorization': 'Bearer ' + token, // Replace with your actual access token
                    },
                });
                getAllLessons(token);
                onClose()
            }
        }

        const handleUpdate = async () => {
            if (isValid()) {
                let data = {
                    ...lessonData,
                    courseId: course.id,
                    title: title,
                    contents: [{
                        ...lessonData.contents[0],
                        data: lessonContent,
                        mediaLink: mediaLink,
                        practiceQuestions: practiceQuestions
                    }]
                }
                const response = await backendCall.put('/api/v1/updateLesson', data, {
                    headers: {
                        'Authorization': 'Bearer ' + token, // Replace with your actual access token
                    },
                });
                getAllLessons(token);
                getLesson(token, lessonData.id);
                onClose()
            }
        }

        return (
            <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
                <DialogTitle>Create Lesson</DialogTitle>
                <DialogContent>
                    <Stack flexDirection="column" justifyItems="center" alignItems="center">
                        <TextField label="Title" className={styles.mainLessonInput} value={title} onChange={(e) => setTitle(e.target.value)} />
                        <>
                            <br></br>
                            <br></br>
                        </>
                        <TextField label="Youtube Video ID" className={styles.mainLessonInput} value={mediaLink} onChange={(e) => setMediaLink(e.target.value)} />
                        <>
                            <br></br>
                            <br></br>
                        </>
                        <TextField label="Content" className={styles.mainLessonInput} multiline value={lessonContent} onChange={(e) => setLessonContent(e.target.value)} maxRows={25} />
                        <>
                            <br></br>
                            <br></br>
                        </>
                        <Typography variant="h5">Practice Questions</Typography>
                        {practiceQuestions.map((question, index) => (
                            <div key={index} className={styles.questionContainer}>
                                <TextField
                                    label="Question Name"
                                    fullWidth
                                    margin="normal"
                                    value={question.questionName}
                                    onChange={(e) => handleQuestionChange(index, 'questionName', e.target.value)}
                                />
                                <>
                                    <br></br>
                                </>

                                <TextField
                                    label="Difficulty Level"
                                    fullWidth
                                    margin="normal"
                                    value={question.questionDifficulty}
                                    onChange={(e) => handleQuestionChange(index, 'questionDifficulty', e.target.value)}
                                />
                                <>
                                    <br></br>
                                </>
                                <TextField
                                    label="Answer"
                                    fullWidth
                                    margin="normal"
                                    value={question.answerContent}
                                    onChange={(e) => handleQuestionChange(index, 'answerContent', e.target.value)}
                                />
                                <>
                                    <br></br>
                                </>
                                <TextField
                                    label="Link"
                                    fullWidth
                                    margin="normal"
                                    value={question.questionLink}
                                    onChange={(e) => handleQuestionChange(index, 'questionLink', e.target.value)}
                                />
                                <>
                                    <br></br>
                                </>
                                <Button onClick={() => handleRemoveQuestion(index)} color="secondary">
                                    Remove Question
                                </Button>
                            </div>
                        ))}
                        <Button onClick={handleAddQuestion} color="primary">
                            Add Question
                        </Button>

                    </Stack>
                </DialogContent>
                <DialogActions>
                    {
                        isUpdate &&
                        <Button onClick={handleUpdate} color="primary">
                            Update
                        </Button>
                    }
                    {
                        !isUpdate &&
                        <Button onClick={handleSubmit} color="primary">
                            Submit
                        </Button>
                    }

                    <Button onClick={handleClose} color="secondary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
        );
    };

    const { courseId } = useParams();


    const ToggleButton = styled(MuiToggleButton)({
        "&.Mui-selected, &.Mui-selected:hover": {
            color: "white",
            backgroundColor: '#295bcc',
        }
    });

    const [message, setMessage] = useState('');
    const [isSnackbarOpen, setIsSnackbarOpen] = useState(false);
    const [snackType, setSnackType] = useState(false);

    const [course, setCourse] = useState(null);
    const [lessons, setLessons] = useState([]);
    const [editLessonContent, setEditLessonContent] = useState(null);
    const [lessonData, setLessonData] = useState(null);
    const [isEditable, setIsEditable] = useState(false);
    const [isUpdate, setIsUpdate] = useState(false);
    const [role, setRole] = useState('');
    const [token, setToken] = useState('');

    const [open, setOpen] = useState(false);
    const [title, setTitle] = useState('');
    const [difficulty, setDifficulty] = useState('');
    const [description, setDescription] = useState('');
    const [image, setImage] = useState(null);

    const [userCourses, setUserCourses] = useState([]);
    const [allCourses, setAllCourses] = useState([]);
    const [openAnswer, setOpenAnswer] = useState({});

    const handleToggleAnswer = (questionId) => {
        setOpenAnswer((prevOpenAnswer) => ({
            ...prevOpenAnswer,
            [questionId]: !prevOpenAnswer[questionId],
        }));
    };


    const handleImageChange = (e) => {
        setImage(e.target.files[0]);
    };

    const handleSubmit = async () => {
        handleClose();
    };

    const handleClickOpen = () => {
        setIsUpdate(false);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

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
        checkCourseInstructor(token).then((isOwner) => {
            if (role == 'ADMIN' || isOwner) {
                setIsEditable(true);
            }
        });

        getCourse(token);
        getAllLessons(token);


    }, []);

    const getCourse = (token) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };
        let isOwner = false;
        backendCall.get('/api/v1/course?courseId=' + courseId, config).then((res) => {
            setCourse(res.data);
        }).catch((err) => {
            if (err.response && err.response.data && err.response.data.error) {
                console.log('err : ', err);
                setMessage(err.response.data.error);
                setSnackType('error');
                setIsSnackbarOpen(true);
            }
        });
    }

    const checkCourseInstructor = async (token) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };
        let isOwner = false;
        await backendCall.get('/api/v1/checkCourse?courseId=' + courseId, config).then((res) => {
            isOwner = res.data;
        }).catch((err) => {
            if (err.response && err.response.data && err.response.data.error) {
                console.log('err : ', err);
                setMessage(err.response.data.error);
                setSnackType('error');
                setIsSnackbarOpen(true);
            }
        });
        return isOwner;
    }

    const showErrorToast = (message) => {
        setMessage(message);
        setSnackType('error');
        setIsSnackbarOpen(true);
    }


    const getLesson = async (token, lessonId) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.get('/api/v1/getLesson?lessonId=' + lessonId, config).then((res) => {
            setLessonData(res.data);
        }).catch((err) => {
            if (err.response && err.response.data && err.response.data.error) {
                console.log('err : ', err);
                showErrorToast(err.response.data.error)
            }
        });
    }

    const getAllLessons = async (token) => {

        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.get('/api/v1/getLessonPages/' + courseId, config).then((res) => {
            setLessons(res.data);
        }).catch((err) => {
            if (err.response && err.response.data && err.response.data.error) {
                console.log('err : ', err);
                setMessage(err.response.data.error);
                setSnackType('error');
                setIsSnackbarOpen(true);
            }
        });
    }

    const hanldeSnackbarClose = () => {
        setIsSnackbarOpen(false);
    }

    const handleLessonClick = (lessonId) => {
        // setLessonData(lesson);
    };

    const editLesson = async (lessonId) => {
        await getLesson(token, lessonId);
        setIsUpdate(true);
        setOpen(true);
    }
    const deleteLesson = async (lessonId) => {

        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };
        await backendCall.delete('/api/v1/deleteLesson?lessonId=' + lessonId, config).then((res) => {
            getAllLessons(token);
            setLessonData(null);
        }).catch((err) => {

        });
    }


    const StyledListItem = styled(ListItem)({
        cursor: 'pointer',
        '&:hover': {
            backgroundColor: theme => theme.palette.action.hover,
        },
    });

    const LessonTitle = styled(ListItemText)({
        marginRight: theme => theme.spacing(2),
    });

    const StyledIconButton = styled(IconButton)({
        '&:hover': {
            backgroundColor: theme => theme.palette.action.hover,
        },
    });




    return (
        <>
            <Header />
            <LessonDialog open={open} onClose={handleClose} onError={showErrorToast} isUpdate={isUpdate} />
            {
                course !== null &&
                <h1>{course.title}</h1>
            }

            {
                (isEditable) &&
                <Stack flexDirection="row" justifyContent="right" sx={{ padding: '20px' }}>
                    <Button variant='contained' onClick={handleClickOpen}>
                        <AddIcon /> Lesson
                    </Button>
                </Stack>
            }
            <Grid container spacing={2}>
                <Grid item xs={3}>
                    <Paper elevation={3} style={{ height: '100%', overflow: 'auto' }}>
                        <List>
                            {lessons != '' && lessons.map((lesson) => (
                                <StyledListItem key={lesson.id} selected={(lessonData!=null && lessonData.title==lesson.title)} onClick={() => handleLessonClick(lesson.id)}>
                                    <LessonTitle primary={lesson.title} onClick={() => getLesson(token, lesson.id)} />
                                    {isEditable && (
                                        <ListItemSecondaryAction>
                                            <StyledIconButton onClick={() => editLesson(lesson.id)} size="small">
                                                <EditIcon />
                                            </StyledIconButton>
                                            <StyledIconButton onClick={() => deleteLesson(lesson.id)} size="small">
                                                <DeleteIcon />
                                            </StyledIconButton>
                                        </ListItemSecondaryAction>
                                    )}
                                </StyledListItem>

                            ))}
                        </List>
                    </Paper>
                </Grid>

                <Grid item xs={9}>
                    {
                        lessonData == null &&
                        <Paper elevation={3} style={{ padding: '20px' }} className={styles.lessonContentContainer}>
                            <Typography > Select a Lesson to view its content</Typography>
                        </Paper>

                    }
                    {
                        lessonData != null &&
                        <Paper elevation={3} style={{ padding: '20px' }} className={styles.lessonContentContainer}>
                            <Typography variant='h4'>{lessonData.title}</Typography>

                            {
                                lessonData.contents.map((content) => {
                                    return (
                                        <>
                                            <iframe className={styles.lessonVideo} src={'https://www.youtube.com/embed/' + content.mediaLink}></iframe>
                                            <hr></hr>
                                            <Box>{content.data}</Box>
                                            <hr></hr>
                                            <TableContainer component={Paper}>
                                                <Table>
                                                    <TableHead>
                                                        <TableRow>
                                                            <TableCell>Question Name</TableCell>
                                                            <TableCell>Difficulty</TableCell>
                                                            <TableCell>Link</TableCell>
                                                            <TableCell>Action</TableCell>
                                                        </TableRow>
                                                    </TableHead>

                                                    <TableBody>
                                                        {content.practiceQuestions.map((question) => (
                                                            <>
                                                                <TableRow key={question.id}>
                                                                    <TableCell>{question.questionName}</TableCell>
                                                                    <TableCell>{question.questionDifficulty}</TableCell>
                                                                    <TableCell>{question.questionLink}</TableCell>
                                                                    <TableCell>
                                                                        <Button onClick={() => handleToggleAnswer(question.id)}>
                                                                            {
                                                                                openAnswer[question.id] ? "Hide" : "Show"
                                                                            } Answer
                                                                        </Button>
                                                                    </TableCell>
                                                                </TableRow>
                                                                <TableRow key={question.id + 'ans'}>
                                                                    <TableCell colSpan={4} align='center'>
                                                                        <Collapse in={openAnswer[question.id]}>
                                                                            <Typography>{question.answerContent}</Typography>
                                                                        </Collapse>
                                                                    </TableCell>
                                                                </TableRow>

                                                            </>
                                                        ))}
                                                    </TableBody>
                                                </Table>
                                            </TableContainer>
                                        </>

                                    )
                                })
                            }

                        </Paper>
                    }

                </Grid>
            </Grid>

            <Snackbar
                open={isSnackbarOpen}
                autoHideDuration={4000}
                onClose={hanldeSnackbarClose}
                disableWindowBlurListener={true}
            >
                <Box>
                    {
                        message != '' &&
                        <>
                            <Alert onClose={hanldeSnackbarClose} severity={snackType}>
                                {message}
                            </Alert>
                            <br></br>
                        </>
                    }
                </Box>

            </Snackbar>
        </>
    );
}