import React, { useEffect, useContext } from "react";
import { Link } from 'react-router-dom';
import { Typography, Button } from "@material-ui/core";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import { FormGroup } from "@material-ui/core";

function Account() {
  const {
    accountState,
    getAccountInfo
  } = useContext(Context);

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

  useEffect(() => {
    getAccountInfo();
  }, []);

  const styles = useStyles();
  const API_URL = "http://localhost:8080";

  // Used to generate one category of account info
  // Filter out those empty categories
  const AccountInfo = (props) => {
    if (props.info != null) {
      return <Typography align="center">{props.category}: {props.info}</Typography>;
    } else {
      return null;
    };
  };

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align='center'>Account</Typography>
      <br />
      <AccountInfo category="Username" info={username}/>
      <AccountInfo category="First Name" info={first_name}/>
      <AccountInfo category="Last Name" info={last_name}/>
      <br />
      <AccountInfo category="Email" info={email}/>
      <AccountInfo category="Contact Number" info={hp}/>
      <AccountInfo category="Role" info={role_id}/>
      <br />
      <AccountInfo category="Account ID" info={acc_id}/>
      <AccountInfo category="Branch ID" info={branch_id}/>
      <AccountInfo category="Employee ID" info={employee_id} />
      <AccountInfo category="Mgr ID" info={mgr_id}/>
      <AccountInfo category="Store ID" info={store_id}/>
      <AccountInfo category="Store Type" info={type_id}/>
      <br />
      <FormGroup column="true">
        <Link to="/edit_account">
          <Button
            className={styles.big_buttons}
            align="center"
            variant="outlined"
            color="primary"
          >
            Edit Account
          </Button>
        </Link>
        <Link to="/edit_password">
          <Button
            className={styles.big_buttons}
            align="center"
            variant="outlined"
            color="secondary"
          >
            Edit Password
          </Button>
        </Link>

      </FormGroup>

    </main>
  );
}

export default Account;
