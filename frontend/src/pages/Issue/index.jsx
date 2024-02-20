import React, { useEffect, useState } from 'react';
import {
    Button,
    Box,
    Typography,
    AccordionSummary, AccordionDetails, Accordion
} from '@mui/material';

import MuiAlert from '@mui/material/Alert';
import styles from './issue.module.css';
import Snackbar from '@mui/material/Snackbar';
import backendCall from '../../utils/network';
import Header from '../../components/Header';
import DeleteIcon from "@mui/icons-material/Delete";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function Issue() {
    const [token, setToken] = useState('');
    const [role, setRole] = useState('');

    const [issues, setIssues] = useState([]);

    const [message, setMessage] = useState('');
    const [isSnackbarOpen, setIsSnackbarOpen] = useState(false);
    const [snackType, setSnackType] = useState(false);

    const handleSnackbarClose = () => {
        setIsSnackbarOpen(false);
    }

    useEffect(() => {
        let role = window.localStorage.getItem('role');
        let token = window.localStorage.getItem('token');
        console.log('token : ', token)
        setToken(token);
        if (token == null || token == '') {
            window.localStorage.removeItem('token');
            window.localStorage.removeItem('role');
            window.location = '/login';
        }
        if (role !== 'ADMIN') {
            window.location = '/course';
        }
        setRole(role);
        getIssues(token);
    }, []);

    const getIssues = async (token) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };

        await backendCall.get('/api/v1/issues', config).then((res) => {
            console.log('getIssues response : ', res);
            setIssues(res.data);
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

    const deleteIssue = (issueId) => {
        let config = {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        };
        backendCall.delete('/api/v1/issues/' + issueId, config).then((res) => {
            getIssues(token);
        }).catch((err) => {
            console.log('err : ', err);
            setMessage(err.response.data.error);
            setSnackType('error');
            setIsSnackbarOpen(true);
        });
    }

    return (
        <>
            <Header />
            <Box className={styles.bodyContainer}>
                {issues.map((issue) => (
                    <Accordion key={issue.id} style={{paddingBottom: '16px'}}>
                        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography style={{fontWeight: 'bold'}}>{issue.name}</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <Typography variant="h5">User: {issue.user.username}</Typography>
                            <Typography variant="h5">Title: {issue.name}</Typography>
                            <Typography variant="body1">Description: {issue.description}</Typography>
                            <Typography variant="body1">Severity: {issue.severity}</Typography>
                        </AccordionDetails>
                        {(role === 'ADMIN' || role === 'INSTRUCTOR') && (
                            <Button
                                onClick={() => {deleteIssue(issue.id)}}
                                variant="outlined"
                                color="error"
                                startIcon={<DeleteIcon />}
                            >
                                Delete
                            </Button>
                        )}
                    </Accordion>
                ))}
            </Box>
            <Snackbar
                open={isSnackbarOpen}
                autoHideDuration={4000}
                onClose={handleSnackbarClose}
                disableWindowBlurListener={true}
            >
                <Box>
                    {
                        message !== '' &&
                        <>
                            <Alert onClose={handleSnackbarClose} severity={snackType}>
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
