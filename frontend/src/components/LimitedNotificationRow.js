import React, { useState, useContext } from "react";
import { Button, TextField, FormControl, InputLabel, Select, Typography, Grid } from "@material-ui/core";
import { DialogActions, DialogContent, DialogTitle, Dialog, DialogContentText } from "@material-ui/core";

import useStyles from "../styles";
import { Context } from "../Context"
import { toValidFormat } from "../components/utils";

const LimitedNotificationRow = (props) => {

  const styles = useStyles();

  return (
    <React.Fragment>
      <div className={styles.announcement_bubble}>
        <Grid item xs={12} sm container>
          <Grid item xs container direction="column" spacing={2}>
            <Grid item xs>
              <Typography variant="subtitle2" color="textSecondary">{props.notification.title}</Typography>
              <Typography variant="body1">{props.notification.message}</Typography>
            </Grid>
          </Grid>
          <Grid item>
            <Typography variant="body2" color="textSecondary">Announcement ID: {props.notification.notification_id}</Typography>
            <Typography variant="body2" color="textSecondary">Posted by {props.notification.creator_id} on {props.notification.receipt_date}</Typography>
            <Typography variant="body2" color="textSecondary">Valid period: {props.notification.receipt_date} to {props.notification.end_date}</Typography>
          </Grid>
        </Grid>
      </div>
    </React.Fragment>
  )
}

export default LimitedNotificationRow;