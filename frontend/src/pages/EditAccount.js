import React, { useState, useEffect, useContext } from "react";
import { useHistory } from "react-router-dom";
import {
  Typography,
  Button,
  TextField,
  FormGroup,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@material-ui/core";
import axios from "axios";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";

function EditAccount() {
  const { accountState, getAccountInfo } = useContext(Context);

  // All possible account info categories for 3 types of users
  const {
    acc_id,
    branch_id,
    employee_id,
    email,
    username,
    first_name,
    last_name,
    hp,
    mgr_id,
    role_id,
    store_id,
    type_id,
  } = accountState;

  const [usernameState, setUsernameState] = useState(username);
  const [firstnameState, setFirstnameState] = useState(first_name);
  const [lastnameState, setLastnameState] = useState(last_name);
  const [emailState, setEmailState] = useState(email);
  const [hpState, setHpState] = useState(hp);
  const [alertState, setAlertState] = useState(false);
  const history = useHistory();

  // Prevent over re-render by using function to modify states, instead of changing states directly
  function openAlert() {
    setAlertState(true);
  }

  function closeAlert() {
    setAlertState(false);
  }

  function submitAccountUpdate(newPassword, confirmedNewPassword) {
    postAccountChange(
      usernameState,
      firstnameState,
      lastnameState,
      emailState,
      hpState
    );
    openAlert();
  }

  function continueRedirect() {
    console.log("Redirecting...");
    closeAlert();
    history.push("/");
  }

  useEffect(() => {
    getAccountInfo();
  }, []);

  const API_URL = "http://localhost:8080";
  const styles = useStyles();
  const disabledInfo = [
    "Role",
    "Account ID",
    "Employee ID",
    "Branch ID",
    "Mgr ID",
    "Store ID",
    "Store Type",
  ];

  const EditAccountInfo = (props) => {
    if (props.info == null) {
      return null;
    } else if (disabledInfo.includes(props.category)) {
      return (
        <TextField
          className={styles.big_textfield}
          label={props.category}
          defaultValue={props.info}
          disabled={true}
        />
      );
    } else {
      console.log("You shouldn't use this function.");
    }
  };

  function postAccountChange(
    newUsername,
    newFirstName,
    newLastName,
    newEmail,
    newHp
  ) {
    console.log("This is posting account changes");
    let payload = {
      username: newUsername,
      first_name: newFirstName,
      last_name: newLastName,
      email: newEmail,
      hp: newHp,
    };
    console.log(payload);
    let FormData = require("form-data");
    let formdata = new FormData();
    formdata.append("changes", JSON.stringify(payload));

    return axios
      .post(`${API_URL}/account/postProfileUpdate`, formdata, {
        headers: {
          "Content-Type": `multipart/form-data; boundary=${formdata._boundary}`,
        },
      })
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed account change submission");
      });
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align="center">
        Edit Account
      </Typography>
      <FormGroup column="true">
        <TextField
          className={styles.big_textfield}
          label="Username"
          onChange={(e) => setUsernameState(e.target.value)}
        />
        <TextField
          className={styles.big_textfield}
          label="First Name"
          onChange={(e) => setFirstnameState(e.target.value)}
        />
        <TextField
          className={styles.big_textfield}
          label="Last Name"
          onChange={(e) => setLastnameState(e.target.value)}
        />
        <TextField
          className={styles.big_textfield}
          label="Email"
          onChange={(e) => setEmailState(e.target.value)}
        />
        <TextField
          className={styles.big_textfield}
          label="Contact Number"
          onChange={(e) => setHpState(e.target.value)}
        />
        <br />
        <EditAccountInfo category="Role" info={role_id} />
        <EditAccountInfo category="Account ID" info={acc_id} />
        <EditAccountInfo category="Branch ID" info={branch_id} />
        <EditAccountInfo category="Employee ID" info={employee_id} />
        <EditAccountInfo category="Mgr ID" info={mgr_id} />
        <EditAccountInfo category="Store ID" info={store_id} />
        <EditAccountInfo category="Store Type" info={type_id} />
        <br />
      </FormGroup>
      <Button
        className={styles.big_buttons}
        align="center"
        variant="outlined"
        color="primary"
        fullWidth
        onClick={submitAccountUpdate}
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
            Account Information successfully updated.
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
  );
}

export default EditAccount;
