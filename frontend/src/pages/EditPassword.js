import React, { useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import { Typography, Button, TextField, FormGroup, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@material-ui/core";
import axios from "axios";

import { Context } from "../Context";
import useStyles from "../styles";
import Navbar from "../Navbar";

function EditPassword() {
  const { accountState, getAccountInfo } = useContext(Context);
  const API_URL = "http://localhost:8080";
  const [passwordState, setPasswordState] = useState("");
  const [confirmPasswordState, setConfirmPasswordState] = useState("");
  const [alertState, setAlertState] = useState(false);
  const styles = useStyles();
  const history = useHistory();

  // Prevent over re-render by using function to modify states, instead of changing states directly
  function openAlert() {
    setAlertState(true);
  }

  function closeAlert() {
    setAlertState(false);
  }

  function submitNewPassword(newPassword, confirmedNewPassword) {
    if (newPassword != confirmedNewPassword) {
      console.log("Two passwords are not the same.")
    } else {
      postPasswordChange(newPassword);
      console.log("Password change updated.")
    }
    openAlert();
  }

  function continueRedirect() {
    console.log("Redirecting...");
    closeAlert();
    history.push("/");
  }

  function postPasswordChange(newPassword) {
    console.log("This is posting password change");
    let FormData = require("form-data");
    let formdata = new FormData();
    formdata.append("new_password", newPassword);
    return axios
      .post(
        `${API_URL}/account/postPasswordUpdate`,
        formdata,
        {
          headers: {
            "Content-Type": `multipart/form-data; boundary=${formdata._boundary}`,
          },
        }
      )
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed password change submission");
      })
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align="center">Edit Password</Typography>
      <FormGroup column="true">
        <TextField className={styles.big_textfield} label="New Password" onChange={(e) => setPasswordState(e.target.value)}/>
        <TextField className={styles.big_textfield} label="Confirm New Password" onChange={(e) => setConfirmPasswordState(e.target.value)}/>
      </FormGroup>
      <Button 
        align="center"
        variant="outlined"
        color="primary"
        className={styles.big_buttons}
        fullWidth
        onClick={() => submitNewPassword(passwordState, confirmPasswordState)}
      >
        Submit
      </Button>
      <Dialog
        open={alertState}
        onClose={closeAlert}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Message:"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Password successfully updated.
          </DialogContentText>
          <DialogContentText id="alert-dialog-description">
            Redirecting to login page...
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={continueRedirect} color="primary">
            Continue
          </Button>
        </DialogActions>
      </Dialog>
    </main>
  )
}

export default EditPassword;