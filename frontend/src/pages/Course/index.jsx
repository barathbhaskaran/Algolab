
import React, { useEffect, useState } from 'react';
import { Alert, Box, Button, Card, CardContent, CardMedia, Dialog, DialogActions, DialogContent, DialogTitle, Grid, Input, InputLabel, MenuItem, Select, Snackbar, Stack, TextField, ToggleButtonGroup, Typography, styled, } from "@mui/material";
import styles from './course.module.css';
import Header from '../../components/Header';
import AddIcon from '@mui/icons-material/Add';
import MuiToggleButton from "@mui/material/ToggleButton";
import backendCall from '../../utils/network';
import CircleNotificationsIcon from "@mui/icons-material/CircleNotifications";
import CloudUploadIcon from '@mui/icons-material/CloudUpload';



const s3Prefix = process.env.REACT_APP_S3_PREFIX;

export default function Course() {

    const CourseTile = ({ course, isDelete }) => {

        const [imageUrl, setImageUrl] = useState('');
        useEffect(() => {
            setImageUrl(s3Prefix + '/course-images/' + course.id);
        }, []);

        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        const handleDeleteCourse =(event) => {
            backendCall.delete('/api/v1/deleteCourse?courseId=' + course.id, config).then((res) => {
                getUserCourses(token);
                getAllCourses(token);
                
            }).catch((err) => {

            });
            event.stopPropagation();
        }

        const handleEditCourse = async () => {
            window.location = `/lesson/${course.id}`;
        }

        const handleAnnouncementOnClick = (event) => {
            window.location = `/announcement/${course.id}`;
            event.stopPropagation();
        };

        return (
            <Card onClick={handleEditCourse}>
                <CardMedia
                    component="img"
                    style={{width: '100%', height: '100%', objectFit: 'contain'}}
                    image={imageUrl}
                    alt={course.title}
                />
                <CardContent>
                    <Typography variant="h6">{course.title}</Typography>
                    <Typography variant="body2" color="textSecondary">{course.description}</Typography>
                    <Typography variant="body2" color="textSecondary">Difficulty: {course.difficulty}</Typography>
                    {isDelete && <Button color='error' onClick={handleDeleteCourse}>Delete</Button>}
                    {isDelete && <Button onClick={handleEditCourse}>Edit</Button>}
                    <Button
                        onClick={handleAnnouncementOnClick}
                    >
                        <CircleNotificationsIcon /> Announcements
                    </Button>
                </CardContent>
            </Card>
        );
    };

    const ToggleButton = styled(MuiToggleButton)({
        "&.Mui-selected, &.Mui-selected:hover": {
            color: "white",
            backgroundColor: '#295bcc',
        }
    });

    const [message, setMessage] = useState('');
    const [isSnackbarOpen, setIsSnackbarOpen] = useState(false);
    const [snackType, setSnackType] = useState(false);

    const [role, setRole] = useState('');
    const [token, setToken] = useState('');
    const [subMenu, setSubMenu] = useState('allCourse');


    const [open, setOpen] = useState(false);
    const [title, setTitle] = useState('');
    const [difficulty, setDifficulty] = useState('');
    const [description, setDescription] = useState('');
    const [image, setImage] = useState(null);

    const [userCourses, setUserCourses] = useState([]);
    const [allCourses, setAllCourses] = useState([]);

    const handleImageChange = (e) => {
        setImage(e.target.files[0]);
    };

    const handleSubmit = async () => {

        if (image && image.size > 1024 * 1024) {
            setMessage('The image size is large. Try other image');
            setSnackType('error');
            setIsSnackbarOpen(true);
            return;
        }

        const formData = new FormData();
        formData.append('image', image);
        formData.append('title', title);
        formData.append('difficulty', difficulty);
        formData.append('description', description);

        try {
            const response = await backendCall.post('/api/v1/createCourse', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': 'Bearer ' + token, // Replace with your actual access token
                },
            });
            getAllCourses(token);
            getUserCourses(token);
        } catch (error) {
            console.error(error);
        }

        handleClose();
    };

    const handleClickOpen = () => {
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
        if (role == 'INSTRUCTOR') {
            getUserCourses(token);
        }
        getAllCourses(token);
    }, []);

    const handleSubMenuChange = (event) => {
        setSubMenu(event.target.value);
    }

    const getUserCourses = async (token) => {

        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.get('/api/v1/userCourses', config).then((res) => {
            let userData = res.data;
            setUserCourses(userData);
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
            let userData = res.data;
            setAllCourses(userData);
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

    return (
        <>
            <Header />
            <Stack className={styles.bodyContainer}>

                <Dialog open={open} onClose={handleClose}>
                    <DialogTitle>Create Course</DialogTitle>
                    <DialogContent>
                        <TextField label="Title" value={title} onChange={(e) => setTitle(e.target.value)} fullWidth />
                        <>
                            <br></br>
                            <br></br>
                        </>
                        <InputLabel id="dif-label">Difficulty</InputLabel>
                        <Select
                            labelId="dif-label"
                            value={difficulty}
                            sx={{ width: "400px" }}
                            onChange={(e) => setDifficulty(e.target.value)}
                            placeholder='Difficulty'
                        >
                            <MenuItem value="">Select Difficulty</MenuItem>
                            <MenuItem value='EASY'>EASY</MenuItem>
                            <MenuItem value='AVERAGE'>AVERAGE</MenuItem>
                            <MenuItem value='HARD'>HARD</MenuItem>
                        </Select>
                        <>
                            <br></br>
                            <br></br>
                            <br></br>
                        </>
                        <TextField label="Description" value={description} onChange={(e) => setDescription(e.target.value)} fullWidth />
                        <>
                            <br></br>
                            <br></br>
                            <br></br>
                        </>
                        <Box>
                            <Input
                                type="file"
                                onChange={handleImageChange}
                                style={{ display: 'none' }} // Hide the default file input
                                id="fileInput"
                            />
                            <label htmlFor="fileInput">
                                <Button
                                    variant="outlined"
                                    component="span"
                                    startIcon={<CloudUploadIcon />}
                                >
                                    Upload Image
                                </Button>
                            </label>
                        </Box>
                        {/* <input type="file" onChange={handleImageChange} /> */}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleClose} color="secondary">
                            Cancel
                        </Button>
                        <Button onClick={handleSubmit} color="primary">
                            Submit
                        </Button>
                    </DialogActions>
                </Dialog>
                {
                    (role == 'INSTRUCTOR' || role == 'ADMIN') &&
                    <Stack flexDirection="row" justifyContent="right" sx={{ padding: '20px' }}>
                        <Button variant='contained' onClick={handleClickOpen}>
                            <AddIcon /> Course
                        </Button>
                    </Stack>
                }


                {
                    role == 'INSTRUCTOR' &&
                    <Stack flexDirection="row" justifyContent="center" sx={{ padding: '20px' }}>
                        <ToggleButtonGroup
                            value={subMenu}
                            exclusive
                            onChange={handleSubMenuChange}
                        >
                            <ToggleButton value="userCourse">
                                Your Course
                            </ToggleButton>
                            <ToggleButton value="allCourse">
                                All course
                            </ToggleButton>
                        </ToggleButtonGroup>
                    </Stack>
                }

                {
                    role == 'INSTRUCTOR' && subMenu == 'userCourse' &&
                    <Grid container spacing={2} className={styles.courseGridContainer}>
                        {userCourses.map((course) => (
                            <Grid item xs={12} sm={6} md={4} lg={3} key={course.id}>
                                <CourseTile course={course} isDelete={true} />

                            </Grid>
                        ))}
                    </Grid>
                }
                {
                    role == 'ADMIN' && subMenu == 'allCourse' &&
                    <Grid container spacing={2} className={styles.courseGridContainer}>
                        {allCourses.map((course) => (
                            <Grid item xs={12} sm={6} md={4} lg={3} key={course.id}>
                                <CourseTile course={course} isDelete={true} />
                            </Grid>
                        ))}
                    </Grid>
                }
                {
                    subMenu == 'allCourse' && role != 'ADMIN' &&
                    <Grid container spacing={2} className={styles.courseGridContainer}>
                        {allCourses.map((course) => (
                            <Grid item xs={12} sm={6} md={4} lg={3} key={course.id}>
                                <CourseTile course={course} />
                            </Grid>
                        ))}
                    </Grid>
                }
            </Stack>

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