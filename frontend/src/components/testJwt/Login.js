import React, { Component, useState } from "react";
import {
  Grid,
  Link,
  Paper,
  CssBaseline,
  Box,
  Typography,
  Avatar,
  Button,
  TextField,
  FormControlLabel,
  Checkbox,
} from "@material-ui/core";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import singhealthBackground from "../../images/singhealth_building.png";

import AuthenticationService from "../../AuthenticationService";
import useStyles from "../../../src/styles";
import { useHistory } from "react-router";

function Login() {
  const [usernameState, setUsernameState] = useState("");
  const [passwordState, setPasswordState] = useState("");
  const [hasLoginFailedState, setHasLoginFailedState] = useState(false);
  const [showSuccessMessageState, setShowSuccessMessageState] = useState(false);

  const styles = useStyles();
  const history = useHistory();

  const DisplayMessage = () => {
    if (hasLoginFailedState) {
      return (
        <Typography align="center" color="secondary">
          Invalid Credentials
        </Typography>
      );
    } else if (showSuccessMessageState) {
      return <Typography>Login Successful</Typography>;
    } else {
      return null;
    }
  };

  function loginClicked() {
    console.log(usernameState);
    console.log(passwordState);
    AuthenticationService.executeJwtAuthenticationService(
      usernameState,
      passwordState
    )
      .then((response) => {
        console.log(response);
        console.log(response.data);
        AuthenticationService.registerSuccessfulLoginForJwt(
          usernameState,
          response.data.token
        );

        if (response.data.accountType === "Auditor") {
          history.push(`/home/a`);
        } else if (response.data.accountType === "Tenant") {
          history.push(`/home/t`);
        } else if (response.data.accountType === "Manager") {
          history.push(`/home/m`);
        }
      })
      .catch(() => {
        setShowSuccessMessageState(false);
        setHasLoginFailedState(true);
      });
  }

  return (
    <Grid container component="main" className={styles.root}>
      <CssBaseline />
      <Grid item xs={false} sm={4} md={7} className={styles.image} />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <div className={styles.paper}>
          <Avatar className={styles.avatar}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <form className={styles.form} noValidate>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
              value={usernameState}
              onChange={(e) => setUsernameState(e.target.value)}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              value={passwordState}
              onChange={(e) => setPasswordState(e.target.value)}
            />
            <DisplayMessage />
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              className={styles.submit}
              onClick={() => loginClicked()}
            >
              Sign In
            </Button>
            {/* <Grid container>
              <Grid item xs>
                <Link href="#" variant="body2">
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link href="#" variant="body2">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid> */}
            {/* <Box mt={5}>
              <Copyright />
            </Box> */}
          </form>
        </div>
      </Grid>
    </Grid>
  );
}

export default Login;
