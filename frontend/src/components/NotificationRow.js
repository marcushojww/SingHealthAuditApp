import React, { useState, useContext } from "react";
import { Link } from "react-router-dom";
import { Button, TextField, FormControl, InputLabel, Select, Typography, Grid } from "@material-ui/core";
import { DialogActions, DialogContent, DialogTitle, Dialog, DialogContentText } from "@material-ui/core";

import useStyles from "../styles";
import { Context } from "../Context"
import { toValidFormat } from "../components/utils";

// props.notification
const NotificationRow = (props) => {

  const notification = props.notification;
  const styles = useStyles();
  const { 
    postModifyNotification,
    deleteNotification,
  } = useContext(Context);

  // States for modifying announcement
  const [titleState, setTitleState] = useState(notification.title);
  const [messageState, setMessageState] = useState(notification.message);
  const [receiptDateState, setReceiptDateState] = useState(toValidFormat(notification.receipt_date)); // Default today
  const [endDateState, setEndDateState] = useState(toValidFormat(notification.end_date)); // Default one month later
  const [receiversState, setReceiversState] = useState(notification.to_role_ids); // Default all users

  // States for dialogs
  const [modifyDialogState, setModifyDialogState] = useState(false);
  const [deleteDialogState, setDeleteDialogState] = useState(false);
  const [tipDialogState, setTipDialogState] = useState(false);
  const [successDialogState, setSuccessDialogState] = useState(false);

  // Functions for dialogs
  function openModifyDialog() {setModifyDialogState(true);}
  function closeModifyDialog() {setModifyDialogState(false);}
  function openDeleteDialog() {setDeleteDialogState(true);}
  function closeDeleteDialog() {setDeleteDialogState(false);}
  function openTipDialog() {setTipDialogState(true);}
  function closeTipDialog() {setTipDialogState(false);}
  function openSuccessDialog() {setSuccessDialogState(true);}
  function closeSuccessDialog() {setSuccessDialogState(false);}

  function handleTitleChange(input_title) {setTitleState(input_title);}
  function handleMessageChange(input_message) {setMessageState(input_message);}
  function handleReceiptDateChange(input_date) {setReceiptDateState(input_date);}
  function handleEndDateChange(input_date) {setEndDateState(input_date);}
  function handleReceiverChange(input_receiver) {setReceiversState(parseInt(input_receiver));}

  function modifyPastAnnouncement(notification_id) {
    console.log("Modifying announcement...");
    console.log(notification_id);
    console.log(titleState);
    console.log(messageState);
    console.log(receiptDateState);
    console.log(endDateState);
    console.log(receiversState);
    postModifyNotification(notification_id, titleState, messageState, receiptDateState, endDateState, receiversState);
    openSuccessDialog();
  }

  function deleteAnnouncement(notification_id) {
    console.log("Deleting announcement...");
    deleteNotification(notification_id);
  }

  // Handle button clicks
  function handleModifyAnnouncementClick() {
    console.log("Modifying existing announcement...");
    openModifyDialog();
  }
  function handleDeleteAnnouncementClick() {
    console.log("Deleting existing announcement...");
    openDeleteDialog();
  }
  function handleTipClick() {
    console.log("Openning accouncement tips...");
    openTipDialog();
  }
  function handleSuccessClick() {
    console.log("Success dialog click...");
    closeSuccessDialog();
    closeModifyDialog();
    closeDeleteDialog();
  }

  return (
    <React.Fragment>
      <div className={styles.announcement_bubble}>
        <Grid item xs={12} sm container>
          <Grid item xs container direction="column" spacing={2}>
            <Grid item xs>
              <Typography variant="subtitle2" color="textSecondary">{notification.title}</Typography>
              <Typography variant="body1">{notification.message}</Typography>
            </Grid>
          </Grid>
          <Grid item>
            <Typography variant="body2" color="textSecondary">Announcement ID: {notification.notification_id}</Typography>
            <Typography variant="body2" color="textSecondary">Posted by {notification.creator_id} on {notification.receipt_date}</Typography>
            <Typography variant="body2" color="textSecondary">Valid period: {notification.receipt_date} to {notification.end_date}</Typography>
            <Button className={styles.buttons} variant="outlined" color="primary" onClick={handleModifyAnnouncementClick}>Modify</Button>
            <Button className={styles.buttons} variant="outlined" color="secondary" onClick={handleDeleteAnnouncementClick}>Delete</Button>
          </Grid>
        </Grid>

        {/* Dialog used to modify announcement */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={modifyDialogState}
          onClose={closeModifyDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"Modify Announcement"}</DialogTitle>
          <DialogContent>
            <TextField className={styles.new_announcement_input} value={titleState} label="Title" variant="outlined" onChange={(e) => handleTitleChange(e.target.value)}/>
            <TextField className={styles.new_announcement_input} value={messageState} label="Message" variant="outlined" onChange={(e) => handleMessageChange(e.target.value)}/>
            <TextField className={styles.new_announcement_input} value={receiptDateState} label="Receipt Date" variant="outlined" onChange={(e) => handleReceiptDateChange(e.target.value)}/>
            <TextField className={styles.new_announcement_input} value={endDateState} label="End Date" variant="outlined" onChange={(e) => handleEndDateChange(e.target.value)}/>
            <FormControl variant="outlined" className={styles.dialog_selector}>
              <InputLabel>Receivers</InputLabel>
              <Select native label="Receivers" value={receiversState} onChange={(e) => handleReceiverChange(e.target.value)}>
                <option value={7}>All users</option>
                <option value={5}>Manager and Tenant</option>
                <option value={6}>Auditor and Tenant</option>
                <option value={2}>Only Auditor</option>
                <option value={4}>Only Tenant</option>
              </Select>
            </FormControl>
          </DialogContent>
          <DialogContent>
            <Button className={styles.dialog_link} onClick={handleTipClick}>Tips</Button>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeModifyDialog} color="secondary">Cancel</Button>
            <Button onClick={() => modifyPastAnnouncement(notification.notification_id)} color="primary">Continue</Button>
          </DialogActions>
        </Dialog>

        {/* Dialog used to delete announcement */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={deleteDialogState}
          onClose={closeDeleteDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"Alert"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">Confirm to delete this announcement?</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeDeleteDialog} color="primary">Cancel</Button>
            <Button onClick={() => deleteAnnouncement(notification.notification_id)} color="secondary">Confirm</Button>
          </DialogActions>
        </Dialog>

        {/* Dialog used to show tips */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={tipDialogState}
          onClose={closeTipDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"Tips"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">How to create new announcements?</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "Title" and "Message" is where you should post your announcement information</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "Receipt Date" is the day when the receipients should start to see the announcement</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "End Date" is the day when the recipients should stop getting the notification</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "Receivers" indicates the range of receipients who can see the announcement.</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeTipDialog} color="primary">Ok</Button>
          </DialogActions>
        </Dialog>

        {/* Dialog used to proceed post/delete upon success */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={successDialogState}
          onClose={closeSuccessDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"Changes updated!"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">Your changes have been updated</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleSuccessClick} color="primary">Ok</Button>
          </DialogActions>
        </Dialog>

      </div>
    </React.Fragment>
  );
}

export default NotificationRow;