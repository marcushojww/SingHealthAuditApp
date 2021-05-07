import React, { useState, useContext, useEffect } from "react";
import { Link } from "react-router-dom";
import {
  Button,
  IconButton,
  TextField,
  FormControl,
  InputLabel,
  Select,
  Typography,
  Grid,
} from "@material-ui/core";
import {
  InputAdornment,
  DialogActions,
  DialogContent,
  DialogTitle,
  Dialog,
  DialogContentText,
} from "@material-ui/core";
import SearchIcon from "@material-ui/icons/Search";

import Navbar from "../Navbar";
import useStyles from "../styles";
import { Context } from "../Context";
import NotificationRow from "../components/NotificationRow";

function ManagerHome() {
  const styles = useStyles();
  const {
    accountState,
    getAllAvailableNotifications,
    getNotificationByNotificationId,
    chatSubmitState,
    postNewNotification,
    postModifyNotification,
    deleteNotification,
    getNotificationsByCreatorId,
  } = useContext(Context);

  const [
    displayedNotificationsState,
    setDisplayedNotificationsState,
  ] = useState([]);
  const [rangeState, setRangeState] = useState("all");
  const [searchBarInputState, setSearchBarInputState] = useState("");

  // States for inputs
  const [titleState, setTitleState] = useState("");
  const [messageState, setMessageState] = useState("");
  const [receiptDateDisplayed, setReceiptDateDisplayed] = useState(new Date());
  const [receiptDateState, setReceiptDateState] = useState(""); // Default today
  const [endDateDisplayed, setEndDateDisplayed] = useState(new Date());
  const [endDateState, setEndDateState] = useState(""); // Default one month later
  const [receiversState, setReceiversState] = useState(7); // Default all users

  // States for dialogs
  const [postNewDialogState, setPostNewDialogState] = useState(false);
  const [tipDialogState, setTipDialogState] = useState(false);
  const [successDialogState, setSuccessDialogState] = useState(false);

  const { role_id } = accountState;

  function handleTitleChange(input_title) {
    setTitleState(input_title);
  }
  function handleMessageChange(input_message) {
    setMessageState(input_message);
  }
  function handleReceiptDateChange(input_date) {
    setReceiptDateState(input_date); // String passed to backend
    setReceiptDateDisplayed(input_date); // Date object to display
  }
  function handleEndDateChange(input_date) {
    setEndDateState(input_date); // String passed to backend
    setEndDateDisplayed(input_date); // Date object to display
  }
  function handleReceiverChange(input_receiver) {
    setReceiversState(parseInt(input_receiver));
  }

  function openPostNewDialog() {
    setPostNewDialogState(true);
  }
  function closePostNewDialog() {
    setPostNewDialogState(false);
  }
  function openTipDialog() {
    setTipDialogState(true);
  }
  function closeTipDialog() {
    setTipDialogState(false);
  }
  function openSuccessDialog() {
    setSuccessDialogState(true);
  }
  function closeSuccessDialog() {
    setSuccessDialogState(false);
  }

  function resetStates() {
    setTitleState("");
    setMessageState("");
    setReceiptDateState("");
    setEndDateState("");
    setReceiversState(7);
  }

  function submitNewAccouncement() {
    console.log("Submitting new announcement...");
    postNewNotification(
      titleState,
      messageState,
      receiptDateState,
      endDateState,
      receiversState
    );
    openSuccessDialog();
    resetStates();
  }

  function handleSearchBarChange(search_input) {
    setSearchBarInputState(parseInt(search_input)); // String => Integer
  }

  function handleSearchButtonClick() {
    console.log("Submitting search bar input: " + searchBarInputState);
    console.log(typeof searchBarInputState);
    if (searchBarInputState < 1000 && searchBarInputState > 0) {
      setRangeState("by_notification_id");
      console.log("Setting range to By Institution ID");
    } else if (searchBarInputState >= 1000) {
      setRangeState("by_manager_id");
      console.log("Setting range to By Manager ID");
    } else {
      setRangeState("all");
      console.log("Setting range to All");
    }

    console.log("Searchbar input: " + searchBarInputState);
    console.log("Current range: " + rangeState);
  }

  function handleNewAnnouncementClick() {
    console.log("Posting new announcement...");
    openPostNewDialog();
  }
  function handleTipClick() {
    console.log("Openning accouncement tips...");
    openTipDialog();
  }
  function handleSuccessClick() {
    console.log("Success dialog click...");
    closeSuccessDialog();
    closePostNewDialog();
  }

  useEffect(() => {
    async function getResponse() {
      try {
        await getNotificationsByCreatorId().then((response) => {
          console.log("All available notifications: " + response.data);
          setDisplayedNotificationsState(response.data);
        });
      } catch {
        console.log("Failed to retrive allAvailableNotifications");
      }
    }
    getResponse();
  }, [chatSubmitState]);

  return (
    <main className={styles.main}>
      <Navbar />
      <div className={styles.body}>
        <TextField
          className={styles.search_bar}
          label="Search Notification ID/Creator ID"
          variant="outlined"
          InputProps={{
            endAdornment: (
              <InputAdornment>
                <IconButton onClick={handleSearchButtonClick}>
                  <SearchIcon />
                </IconButton>
              </InputAdornment>
            ),
          }}
          onChange={(e) => handleSearchBarChange(e.target.value)}
        />
        <div className={styles.annoucement_title_div}>
          <Typography variant="h6" className={styles.annoucement_title}>
            Announcements
          </Typography>
        </div>
        <div className={styles.announcement_list}>
          {displayedNotificationsState.map((notification, index) => {
            // return (
            //   <NotificationRow notification={notification} key={index}/>
            // )
            if (rangeState === "all") {
              return (
                <NotificationRow notification={notification} key={index} />
              );
            } else if (
              rangeState === "by_notification_id" &&
              notification.notification_id === searchBarInputState
            ) {
              return (
                <NotificationRow notification={notification} key={index} />
              );
            } else if (
              rangeState === "by_manager_id" &&
              notification.creator_id === searchBarInputState
            ) {
              return (
                <NotificationRow notification={notification} key={index} />
              );
            } else {
              return null;
            }
          })}
        </div>
        <div className={styles.post_new_accouncement_div}>
          <Button
            className={styles.big_bottom_buttons}
            align="center"
            variant="outlined"
            color="primary"
            onClick={handleNewAnnouncementClick}
          >
            Post New Announcement
          </Button>
        </div>

        {/* Dialog used for posting new announcement */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={postNewDialogState}
          onClose={closePostNewDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">
            {"New Announcement"}
          </DialogTitle>
          <DialogContent>
            <TextField
              className={styles.new_announcement_input}
              value={titleState}
              label="Title"
              variant="outlined"
              onChange={(e) => handleTitleChange(e.target.value)}
            />
            <TextField
              className={styles.new_announcement_input}
              value={messageState}
              label="Message"
              variant="outlined"
              onChange={(e) => handleMessageChange(e.target.value)}
            />
            <TextField
              className={styles.new_announcement_input}
              value={receiptDateState}
              label="Receipt Date (DD/MM/YYYY)"
              variant="outlined"
              onChange={(e) => handleReceiptDateChange(e.target.value)}
            />
            <TextField
              className={styles.new_announcement_input}
              value={endDateState}
              label="End Date (DD/MM/YYYY)"
              variant="outlined"
              onChange={(e) => handleEndDateChange(e.target.value)}
            />
            {/* <MuiPickersUtilsProvider utils={DateFnsUtils}>
              <Grid container justify="space-around">
                <KeyboardDatePicker
                  className={styles.dialog_date_picker}
                  margin="normal"
                  label="Receipt Date"
                  format="dd/MM/yyyy"
                  value={receiptDateState}
                  onChange={(e) => handleReceiptDateChange(e.target.value)}
                  KeyboardButtonProps={{
                    'aria-label': 'change date',
                  }}
                />
                <KeyboardDatePicker
                  className={styles.dialog_date_picker}
                  label="End Date"
                  format="dd/MM/yyyy"
                  value={endDateState}
                  onChange={(e) => handleEndDateChange(e.target.value)}
                  KeyboardButtonProps={{
                    'aria-label': 'change date',
                  }}
                />
              </Grid>
            </MuiPickersUtilsProvider> */}
            <FormControl variant="outlined" className={styles.dialog_selector}>
              <InputLabel>Receivers</InputLabel>
              <Select
                native
                label="Receivers"
                value={receiversState}
                onChange={(e) => handleReceiverChange(e.target.value)}
              >
                <option value={7}>All users</option>
                <option value={5}>Manager and Tenant</option>
                <option value={6}>Auditor and Tenant</option>
                <option value={2}>Only Auditor</option>
                <option value={4}>Only Tenant</option>
              </Select>
            </FormControl>
          </DialogContent>
          <DialogContent>
            <Button className={styles.dialog_link} onClick={handleTipClick}>
              Tips
            </Button>
          </DialogContent>
          <DialogActions>
            <Button onClick={closePostNewDialog} color="secondary">
              Cancel
            </Button>
            <Button onClick={submitNewAccouncement} color="primary">
              Continue
            </Button>
          </DialogActions>
        </Dialog>

        {/* Dialog used for announcement tips */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={tipDialogState}
          onClose={closeTipDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"Tips"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">
              How to create new announcements?
            </DialogContentText>
            <DialogContentText id="alert-dialog-description">
              * "Title" and "Message" is where you should post your announcement
              information
            </DialogContentText>
            <DialogContentText id="alert-dialog-description">
              * "Receipt Date" is the day when the receipients should start to
              see the announcement
            </DialogContentText>
            <DialogContentText id="alert-dialog-description">
              * "End Date" is the day when the recipients should stop getting
              the notification
            </DialogContentText>
            <DialogContentText id="alert-dialog-description">
              * "Receivers" indicates the range of receipients who can see the
              announcement.
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeTipDialog} color="primary">
              Ok
            </Button>
          </DialogActions>
        </Dialog>

        {/* Dialog used for handling dialog states upon successful submission */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={successDialogState}
          onClose={closeSuccessDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">
            {"Changes updated!"}
          </DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">
              Your changes have been updated
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleSuccessClick} color="primary">
              Ok
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    </main>
  );
}

export default ManagerHome;
